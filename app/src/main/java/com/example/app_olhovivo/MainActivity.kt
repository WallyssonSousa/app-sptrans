package com.example.app_olhovivo

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


class MainActivity : AppCompatActivity() {

    private lateinit var input: EditText
    private lateinit var button: Button
    private lateinit var output: TextView
    private lateinit var api: API
    
    private val token = "1cb78dcd4b11b611ef3a0dba44db760d25315d60eac0da383beb84abd26c6000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.inputLinha)
        button = findViewById(R.id.btnBuscar)
        output = findViewById(R.id.txtResultado)

        val client = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                private val cookieStore = mutableMapOf<String, List<Cookie>>()
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookieStore[url.host()] = cookies
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return cookieStore[url.host()] ?: emptyList()
                }
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.olhovivo.sptrans.com.br/v2.1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(API::class.java)

        // Autenticação
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.autenticar(token)
                if (response.isSuccessful && response.body() == true) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Autenticado com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Falha na autenticação", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Buscar linha
        button.setOnClickListener {
            val termo = input.text.toString()
            if (termo.isBlank()) {
                Toast.makeText(this, "Digite algo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val resposta = api.buscarLinha(termo)
                    val linhas = resposta.body()
                    val texto = if (!linhas.isNullOrEmpty()) {
                        linhas.joinToString("\n\n") {
                            """
                        Código: ${it.codigo}
                        Letreiro: ${it.letreiro}
                        Terminal Principal: ${it.terminalPrincipal}
                        Terminal Secundário: ${it.terminalSecundario}
                        """.trimIndent()
                        }
                    } else {
                        "Nenhuma linha encontrada"
                    }

                    withContext(Dispatchers.Main) {
                        output.text = texto
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        output.text = "Erro: ${e.message}"
                    }
                }
            }
        }
    }
    interface API {
        @POST("Login/Autenticar")
        suspend fun autenticar(@Query("token") token: String): Response<Boolean>

        @GET("Linha/Buscar")
        suspend fun buscarLinha(@Query("termosBusca") termo: String): Response<List<Linha>>
    }

    // Mapeamento correto dos campos
    data class Linha(
        @SerializedName("cl") val codigo: Int,
        @SerializedName("lt") val letreiro: String,
        @SerializedName("tp") val terminalPrincipal: String,
        @SerializedName("ts") val terminalSecundario: String
    )
}
