package com.example.locafriends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.locafriends.adapter.AmigoAdapter
import com.example.locafriends.modelo.ServicioUsuario
import com.example.locafriends.modelo.Usuario
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.android.synthetic.main.activity_amigos.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class AmigosActivity : AppCompatActivity() {

    var usuarios=ArrayList<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amigos)

        Log.i("recycler", "${usuarios.size}")

        todos()
    }

    fun todos() {
        GlobalScope.launch(Dispatchers.IO){

            val retrofit = Retrofit.Builder()
                    .baseUrl("https://benesuela.herokuapp.com/")
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build()

            //PASO 2: GENERAR UN OBJETO PARAHABILITAR TU SERVICIO DE RETROFIT USANDO EL OBJETO
            //DEL PUNTO ANTERIOR
            val servicioUsuario = retrofit.create(ServicioUsuario::class.java)

            //PASO 2
            val enviarUsuarios = servicioUsuario.buscarTodos()

            //SE ENVIA  AL BACK- END Y  EN ESTE MOMENTO SE OBTIENE LA RESPUESTA
            usuarios = enviarUsuarios.execute().body()!!

            //El siguiente se ejecuta en el Thread de la VIEW
            launch(Dispatchers.Main) {
                Toast.makeText(applicationContext, "${usuarios.size} usuarios conectados", Toast.LENGTH_LONG).show()

                val recyclerView = findViewById<RecyclerView>(R.id.amigos_recycler_view)
                recyclerView.adapter = AmigoAdapter(this@AmigosActivity, usuarios )
                recyclerView.setHasFixedSize(true)

            }
        }
    }

}