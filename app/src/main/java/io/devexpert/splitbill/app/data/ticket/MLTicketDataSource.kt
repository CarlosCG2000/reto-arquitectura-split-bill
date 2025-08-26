package io.devexpert.splitbill.app.data.ticket

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import io.devexpert.splitbill.data.ticket.TickectDataSource
import io.devexpert.splitbill.domain.TicketData
import kotlinx.serialization.json.Json

class MLTicketDataSource: TickectDataSource {
    private var json = Json { ignoreUnknownKeys = true }

    override suspend fun processTicket(image: ByteArray): TicketData {
        Log.d("TicketProcessor", "Iniciando procesamiento de imagen...")

        // Definir el schema para la respuesta JSON (sin requiredProperties)
        val jsonSchema = Schema.obj(
            mapOf(
                "items" to Schema.array(
                    Schema.obj(
                        mapOf(
                            "name" to Schema.string(),
                            "count" to Schema.integer(),
                            "price_per_unit" to Schema.double()
                        )
                    )
                ),
                "total" to Schema.double()
            )
        )

        val prompt = """
                    Analiza esta imagen de un ticket de restaurante y extrae:
                    1. Lista de items con nombre, cantidad y precio individual
                    2. Total de la cuenta
                    Si no puedes leer algún precio, ponlo como 0.0
                """.trimIndent()

        val inputContent = content {
            inlineData(image, "image/jpeg") // image(image)
            text(prompt)
        }

        val model = Firebase.ai.generativeModel(
            modelName = "gemini-2.5-flash-lite-preview-06-17",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = jsonSchema
            }
        )

        val response = model.generateContent(inputContent)
        val responseText = response.text ?: throw Exception("No se recibió respuesta de la IA")
        Log.d("TicketProcessor", "Respuesta de IA: $responseText")

        // Parsear el JSON usando kotlinx.serialization
        val ticketData = json.decodeFromString<TicketData>(responseText)
        return ticketData
    }
}