package com.nacoders.mediscript.util

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.nacoders.mediscript.data.local.entity.PatientEntity
import com.nacoders.mediscript.data.local.entity.PrescriptionEntity
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PdfGenerator(private val context: Context) {

    // Theme Colors from Color.kt
    private val medicalBlue = Color.parseColor("#2A7FFF")
    private val textPrimary = Color.parseColor("#1A1A1A")
    private val textSecondary = Color.parseColor("#6B7280")
    private val dividerColor = Color.parseColor("#E5E7EB")

    fun generateAndSavePdf(patient: PatientEntity, prescription: PrescriptionEntity?): File? {
        val pdfDocument = PdfDocument()
        val pageWidth = 595 // A4 Width
        val pageHeight = 842 // A4 Height
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        // --- 1. Header Section ---
        paint.color = medicalBlue
        canvas.drawRect(0f, 0f, pageWidth.toFloat(), 120f, paint)

        paint.color = Color.WHITE
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 32f
        canvas.drawText("MEDISCRIPT", 40f, 65f, paint)

        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Digital Healthcare Solutions • Smart E-Prescription", 40f, 90f, paint)

        // The "Rx" Symbol
        paint.textSize = 60f
        paint.alpha = 80 // Subtle watermark effect
        canvas.drawText("Rx", 480f, 90f, paint)
        paint.alpha = 255

        // --- 2. Date & Reference ---
        var yPos = 160f
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        paint.color = textSecondary
        paint.textSize = 10f
        canvas.drawText("Date: $currentDateTime", 40f, yPos, paint)
        canvas.drawText("Ref: ${prescription?.prescriptionNumber ?: "N/A"}", 400f, yPos, paint)

        // --- 3. Patient Details Box ---
        yPos += 30f
        paint.color = Color.parseColor("#F7F9FC") // SurfaceWhite background
        canvas.drawRoundRect(40f, yPos, 555f, yPos + 80f, 15f, 15f, paint)

        yPos += 30f
        paint.color = medicalBlue
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 11f
        canvas.drawText("PATIENT INFORMATION", 60f, yPos, paint)

        yPos += 25f
        paint.color = textPrimary
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 14f
        canvas.drawText(patient.name.uppercase(), 60f, yPos, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.textSize = 12f
        paint.color = textSecondary
        canvas.drawText("${patient.age} Yrs • ${patient.gender} • ${patient.phone}", 60f, yPos + 18f, paint)

        // --- 4. Medications Section ---
        yPos += 80f
        paint.color = medicalBlue
        paint.strokeWidth = 2f
        canvas.drawLine(40f, yPos, 80f, yPos, paint) // Accent line

        yPos += 25f
        paint.textSize = 18f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = textPrimary
        canvas.drawText("Medications", 40f, yPos, paint)

        yPos += 35f
        if (prescription?.medicineList.isNullOrEmpty()) {
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            canvas.drawText("No medications prescribed.", 60f, yPos, paint)
        } else {
            prescription?.medicineList?.forEach { medicine ->
                // Bullet point
                paint.color = medicalBlue
                canvas.drawCircle(50f, yPos - 5f, 4f, paint)

                paint.color = textPrimary
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textSize = 13f
                canvas.drawText(medicine.name, 65f, yPos, paint)

                yPos += 18f
                paint.color = textSecondary
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textSize = 11f

                val schedule = mutableListOf<String>()
                if (medicine.isMorning) schedule.add("Morning")
                if (medicine.isAfternoon) schedule.add("Afternoon")
                if (medicine.isNight) schedule.add("Night")
                val freqText = if (schedule.isEmpty()) "As needed" else schedule.joinToString(" + ")

                canvas.drawText("Dosage: ${medicine.dosage}  |  Freq: $freqText  |  Duration: ${medicine.duration}", 65f, yPos, paint)

                yPos += 40f // Spacing between medicines
            }
        }

        // --- 5. Notes Section ---
        if (!prescription?.notes.isNullOrBlank()) {
            yPos += 10f
            paint.color = Color.parseColor("#FFF9C4") // Light yellow "sticky note" color
            canvas.drawRect(40f, yPos, 555f, yPos + 60f, paint)

            yPos += 25f
            paint.color = textPrimary
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 12f
            canvas.drawText("Advice & Clinical Notes:", 55f, yPos, paint)

            yPos += 20f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(prescription.notes ?: "", 55f, yPos, paint)
        }

        // --- 6. Footer & Signature ---
        paint.color = dividerColor
        canvas.drawLine(40f, 750f, 555f, 750f, paint)

        paint.color = textSecondary
        paint.textSize = 10f
        canvas.drawText("Generated via Mediscript App", 40f, 810f, paint)

        pdfDocument.finishPage(page)
        return savePdfToFile(patient.name, pdfDocument)
    }

    private fun savePdfToFile(patientName: String, pdfDocument: PdfDocument): File? {
        val fileName = "Prescription_${patientName.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(directory, fileName)

        return try {
            FileOutputStream(file).use { pdfDocument.writeTo(it) }
            file
        } catch (e: Exception) {
            null
        } finally {
            pdfDocument.close()
        }
    }
}