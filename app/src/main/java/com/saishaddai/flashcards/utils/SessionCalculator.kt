package com.saishaddai.flashcards.utils

import kotlin.math.min

class SessionCalculator {
    fun calculateProgress(
        flashcardsVistas: Int,
        totalFlashcardsTema: Int,
        rachaDias: Int,
        progresoAcumulado: Double
    ): SessionResult {
        // 1. Progreso base
        val base = (100.0 / totalFlashcardsTema) * flashcardsVistas

        // 2. Multiplicador de constancia
        val multiplicador = 1 + (0.03 * rachaDias)

        // 3. Bonus mensual & Titulo
        var bonus = 0.0
        val titulo = when {
            rachaDias >= 30 -> {
                bonus = 10.0
                "Master"
            }
            progresoAcumulado < 20 -> "Novato"
            progresoAcumulado < 50 -> "Intermedio"
            progresoAcumulado < 80 -> "Avanzado"
            else -> "Experto"
        }

        // 4. Avance de la sesión
        val avanceSesion = (base * multiplicador) + bonus

        // 5. Nuevo progreso acumulado (máx. 100)
        val nuevoProgreso = min(progresoAcumulado + avanceSesion, 100.0)

        return SessionResult(avanceSesion, nuevoProgreso, titulo)
    }
}

data class SessionResult(
    val avanceSesion: Double,
    val nuevoProgreso: Double,
    val titulo: String
)
