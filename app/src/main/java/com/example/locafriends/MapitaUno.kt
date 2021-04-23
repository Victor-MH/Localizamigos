package com.example.locafriends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.locafriends.modelo.ServicioUsuario
import com.example.locafriends.modelo.Usuario
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_mapita_todos.*
import kotlinx.android.synthetic.main.activity_mapita_uno.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
//
class MapitaUno() : AppCompatActivity() {
    var    mapboxMap: MapboxMap?=null
    var    amigo_email: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.miToken))
        setContentView(R.layout.activity_mapita_uno)

        amigo_email = intent?.extras?.getString("amigo_email").toString()

        mapUnoView?.onCreate(savedInstanceState)
        mapUnoView?.getMapAsync { mapboxMap ->
            this.mapboxMap=mapboxMap
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                mapboxMap.animateCamera(CameraUpdateFactory.zoomTo(8.0))

                uno()
            }
        }
    }

    fun uno() {
        var usuarios=ArrayList<Usuario>()

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

                usuarios.forEach {
                    if(it.email.equals(amigo_email)) {
                        mapboxMap?.addMarker(
                                MarkerOptions()
                                        .position(LatLng(it.localizacion?.lat!!, it.localizacion?.lon!!))
                                        .title(it.email)
                                        .snippet(" Fecha:" + it.localizacion?.fecha!!)
                        )
                    }
                }

            }
        }

    }



    // Add the mapView lifecycle to the activity's lifecycle methods
    public override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    public override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}