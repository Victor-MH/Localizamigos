package com.example.locafriends

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.locafriends.modelo.Usuario
import kotlinx.android.synthetic.main.activity_registro.*
import org.json.JSONObject

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Manejamos el evento del botón para pedir los valores de los
        // componentes de nuestro formulario
        registrarse.setOnClickListener {
            var usuario = Usuario()
            usuario.email = txtEmail.text.toString()
            usuario.nickname = txtNickname.text.toString()
            usuario.nombre = txtNombre.text.toString()

            //rapidclimate@gmail.com
            // El siguiente paso es este objeto que acabamos de crear(Usuario)
            // Lo tenemos que enviar a un servidor externo para que pueda ser
            // guardado y registrado, así como se registran en cualquier red
            // social. Para este paso necesitamos enviar esta info a un servidor
            //


            // muy particular que se denomina Arquitectura estilo REST

            //Punto 3: Generar el objeto JSON de los datos de punto 2
            var usuariojson = JSONObject()
                usuariojson.put("nombre", usuario.nombre)
                usuariojson.put("nickname", usuario.nickname)
                usuariojson.put("email", usuario.email)

            //Punto 4 Generar el objeto de tipo Request donde se enviara al backend
            var url="https://benesuela.herokuapp.com/api/usuario"
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, usuariojson,
                    Response.Listener { response ->
                        //En este cuarto parametro del objeto JsonObjectRequest se recibe la NOTIFICACION  de la clase Estatus

                        //Aquí guardamos el objeto sharedpreferences

                        //Aquí usamos progra funcional con lambdas y contemplamos el null poiner una vez
                        val preferencias = applicationContext?.getSharedPreferences("AMIGOS", Context.MODE_PRIVATE)?:return@Listener
                        with(preferencias.edit()) {
                            putString("nickname", usuario.nickname).commit()
                        }

                        //Esta opción tiene que contemplar 3 veces el null pointer exeption
                        //Este usa objetos como siempre
                        //preferencias?.edit()?.putString("nickname", usuario.nickname)?.commit()

                        //Ponemos la notificacion del backend en un objeto tipo toast
                        Toast.makeText(this, response.get("mensaje").toString(), Toast.LENGTH_LONG).show()
                    },
                    Response.ErrorListener { error ->
                        // TODO: Handle error
                        Toast.makeText(this, "Hubo un error, ${error}", Toast.LENGTH_LONG).show()
                    }
            )

            // Acceso al request por medio de una clase Singleton
            MiSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }
    }
}

public class MiSingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: MiSingleton? = null
        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: MiSingleton(context).also {
                        INSTANCE = it
                    }
                }
    }

    //Para el caso d cargar un objeto como una imagen.
    val imageLoader: ImageLoader by lazy {
        ImageLoader(requestQueue,
                object : ImageLoader.ImageCache {
                    private val cache = LruCache<String, Bitmap>(20)
                    override fun getBitmap(url: String): Bitmap {
                        return cache.get(url)
                    }
                    override fun putBitmap(url: String, bitmap: Bitmap) {
                        cache.put(url, bitmap)
                    }
                })
    }
    val requestQueue: RequestQueue by lazy {
        // applicationContext es para evitar fuga de mmoria

        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}