package com.nacoders.mediscript.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import java.io.File
import java.io.FileOutputStream

class PdfGenerator(private val context: Context) {

    fun generateAndSavePdf(patient: PatientEntity, prescription: PrescriptionEntity?): File? {
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
        canvas.drawText("Phone: ${patient.phone}", 350f, yPos, paint)

        yPos += 20f
        canvas.drawText("Age/Gender: ${patient.age} / ${patient.gender}", 40f, yPos, paint)

        yPos += 30f
        canvas.drawLine(40f, yPos, 555f, yPos, paint)

        // --- Medicines Section ---
        yPos += 40f
        paint.typeface = boldTypeface
        paint.textSize = 18f
        canvas.drawText("Medicines", 40f, yPos, paint)

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

        // --- Notes Section ---
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

        // --- Footer ---
        paint.textSize = 10f
        paint.color = Color.GRAY
        canvas.drawText("Generated via Mediscript App", 220f, 810f, paint)

        pdfDocument.finishPage(page)

        // Save to file and return it
        return savePdfToFile(patient.name, pdfDocument)
    }

    private fun savePdfToFile(patientName: String, pdfDocument: PdfDocument): File? {
        val fileName = "Prescription_${patientName.replace(" ", "_")}.pdf"

        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

        if (directory != null && !directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, fileName)

        return try {
            FileOutputStream(file).use { pdfDocument.writeTo(it) }
            Toast.makeText(context, "Prescription Prepared", Toast.LENGTH_SHORT).show()
            file // Success: Return the file
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            null // Failure: Return null
        } finally {
            pdfDocument.close()
        }
    }
}