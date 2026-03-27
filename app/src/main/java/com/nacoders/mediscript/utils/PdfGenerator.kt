package com.nacoders.mediscript.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class PdfGenerator(private val context: Context) {

    fun generateAndSavePdf(patient: PatientEntity, prescription: PrescriptionEntity?) {
        val pdfDocument = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842 // A4 Height
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        val boldTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val regularTypeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = Color.parseColor("#2C3E50")
        paint.typeface = boldTypeface
        paint.textSize = 28f
        canvas.drawText("MEDISCRIPT", 40f, 60f, paint)

        paint.textSize = 12f
        paint.typeface = regularTypeface
        paint.color = Color.GRAY
        canvas.drawText("Digital Medical Prescription", 40f, 80f, paint)

        paint.color = Color.BLACK
        paint.strokeWidth = 2f
        canvas.drawLine(40f, 100f, 555f, 100f, paint)

        var yPos = 135f
        paint.typeface = boldTypeface
        paint.textSize = 14f
        paint.color = Color.BLACK
        canvas.drawText("PATIENT DETAILS", 40f, yPos, paint)

        yPos += 25f
        paint.typeface = regularTypeface
        paint.textSize = 12f
        canvas.drawText("Name: ${patient.name}", 40f, yPos, paint)
        canvas.drawText("Phone: ${patient.phone}", 350f, yPos, paint) // Side-by-side layout

        yPos += 20f
        canvas.drawText("Age/Gender: ${patient.age} / ${patient.gender}", 40f, yPos, paint)

        yPos += 30f
        canvas.drawLine(40f, yPos, 555f, yPos, paint) // Section Divider


        yPos += 40f
        paint.typeface = boldTypeface
        paint.textSize = 18f
        canvas.drawText("Medicines", 40f, yPos, paint) // Traditional Prescription Symbol

        yPos += 30f
        paint.textSize = 12f

        if (prescription?.medicineList.isNullOrEmpty()) {
            paint.typeface = regularTypeface
            canvas.drawText("No medicines prescribed.", 60f, yPos, paint)
        } else {
            prescription?.medicineList?.forEach { medicine ->
                paint.typeface = boldTypeface
                canvas.drawText("• ${medicine.name}", 60f, yPos, paint)

                paint.typeface = regularTypeface
                paint.color = Color.DKGRAY
                canvas.drawText("  Dosage: ${medicine.dosage} | Duration: ${medicine.duration}", 60f, yPos + 18f, paint)

                paint.color = Color.BLACK
                yPos += 45f
            }
        }

        yPos += 20f
        if (!prescription?.notes.isNullOrBlank()) {
            paint.typeface = boldTypeface
            paint.textSize = 14f
            canvas.drawText("Notes & Advice:", 40f, yPos, paint)

            yPos += 25f
            paint.typeface = regularTypeface
            paint.textSize = 12f
            val notes = prescription?.notes ?: ""
            canvas.drawText(notes, 40f, yPos, paint)
        }

        paint.textSize = 10f
        paint.color = Color.GRAY
        canvas.drawText("Generated via Mediscript App", 220f, 810f, paint)

        pdfDocument.finishPage(page)
        saveFile(patient.name, pdfDocument)
    }

    private fun saveFile(patientName: String, pdfDocument: PdfDocument) {
        val fileName = "Prescription_${patientName.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/Mediscript")
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let { fileUri ->
                try {
                    val outputStream: OutputStream? = resolver.openOutputStream(fileUri)
                    outputStream?.use { pdfDocument.writeTo(it) }
                    Toast.makeText(context, "PDF saved to Downloads/Mediscript", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Failed to save PDF: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    pdfDocument.close()
                }
            }
        } else {
            try {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val appDir = File(downloadsDir, "Mediscript")
                if (!appDir.exists()) appDir.mkdirs()

                val file = File(appDir, fileName)
                FileOutputStream(file).use { pdfDocument.writeTo(it) }
                Toast.makeText(context, "PDF saved to Downloads/Mediscript", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                pdfDocument.close()
            }
        }
    }
}