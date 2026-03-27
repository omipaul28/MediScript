package com.nacoders.mediscript.utils

fun parsePrescriptionVoice(
    text: String,
    onNameFound: (String) -> Unit,
    onDosageFound: (String) -> Unit,
    onDurationFound: (String) -> Unit,
    onTimeFound: (morning: Boolean, afternoon: Boolean, night: Boolean) -> Unit
) {
    val input = text.lowercase()

    val durationRegex = Regex("(\\d+\\s*(days|day|weeks|week|month|months))")
    durationRegex.find(input)?.let { onDurationFound(it.value) }

    val dosageRegex = Regex("(\\d+\\s*(mg|g|ml|tablet|tab))")
    dosageRegex.find(input)?.let { onDosageFound(it.value) }

    val isMorning = input.contains("morning") || input.contains("daily")
    val isAfternoon = input.contains("afternoon")
    val isNight = input.contains("night") || input.contains("daily")
    onTimeFound(isMorning, isAfternoon, isNight)

    val namePart = input.split(Regex("(\\d+|at|for|daily)")).firstOrNull()?.trim()
    if (!namePart.isNullOrBlank()) {
        onNameFound(namePart)
    }
}