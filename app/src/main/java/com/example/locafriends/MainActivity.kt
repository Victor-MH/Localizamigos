package com.example.locafriends

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Esconde la barra de menu de arriba
        // Tambien valida que exista el supportActionBar, después de que existe lo esconde
        supportActionBar?.hide()

        // Carga el layout, R = /res
        setContentView(R.layout.activity_main)

        /*Si el usuario ya se registró debemos buscar las preferencias compartidas
        * pues ocultamos el botón de empezar ahora*/
        val preferencias = applicationContext?.getSharedPreferences("AMIGOS",Context.MODE_PRIVATE)?:return
        with(preferencias){
            val valorNick = getString("nickname", null)
            if(!valorNick.isNullOrEmpty()){
                //Si se cumple quiere decir que ya te registraste y ya tiene el nickname
                Toast.makeText(applicationContext, "Bienvenido de nuevo ${valorNick}", Toast.LENGTH_LONG).show()
                //Ocultamos el botón de registro
                empezar.visibility = View.INVISIBLE
            }
        }


        //Codigo para activar el vibrador
        // getSystemService() invoca un servicio y es lo que devuelve
        // ponerle "as" es como "castear"
        // service es una clase y Vibrator es una subclase
        val vibrador = getSystemService(VIBRATOR_SERVICE) as Vibrator

        // Invocamos al vibrador
        vibrador.vibrate(1000)

        // Empezamos a programar para ver la forma de implementar los
        // eventos de boton en android studio
        val botonEmpezar = findViewById<Button>(R.id.empezar)

        // Ahora invocamos el boton localizar con el plugin de extensiones
        localizar.setOnClickListener{
            startActivity(Intent(this, MapitaActivity::class.java))
        }

        // Manejar el evento
        botonEmpezar.setOnClickListener {
            // Antes de la navegacion hacia la activity registro
            // vamos a invocar un componente llamado Toast: mensajes
            // de corta duración
            Toast.makeText(applicationContext, "Vamos a registrarnos!!", Toast.LENGTH_LONG).show()

            // Ir a otra activity
            // Un intent sirve para movernos de una activity a otra
            // ::class.java es para referenciar a otra clase
            val intent = Intent(this, RegistroActivity::class.java)
            // La redireccionamos
            startActivity(intent)
        }

        //Declaracion de variables en kotlin
        /*var x = 4
        var y = "Hola Mundo"
        var z:Int = 5
        var w: String = "Nuevo Mundo"
        var v:Int*/

        //Imprimir en el logcat
        Log.i("MALO", "Primer mensaje con etiqueta info")

        // Para concatenar
        var mensajito:String = " vamos aconcatenar"
        Log.i("MALO", "En kotlin" + mensajito + "más fácil que en JAVA")
        // Mejor versión, interpolación de strings
        Log.i("MALO", "En kotlin $mensajito más fácil que en JAVA")
        // Además la interpolación reloaded
        Log.i("MALO", "Vamos a interpolar la expresión 5 + 3 cuyo resultado es ${5+3} que puede ser una operación")

        //Enkotlin las funciones son lo que en java son métodos y tienen también mucho más poder
        // porque pueden anidarse y son tratadas como OBJETOS. A esto se le denomina programación
        // FUNCIONAL: una funcion es tratada como cualquier tipo de variable (objeto)
        // Una funcion puede devolver otra funcion

        //Invocamos nuestra funcion
        saludar()

        // Aquí estamos dentro del ambito del metodo onCreate (es decir su cuerpo,
        // kotlin permine implementar una función aquí dentro, cosa que no es posible en JAVA

        fun mensajito() {
            Log.i("MALO", "Implementando una función dentro de otra")
            // Implementar una función de otra da seguridad, porque encapsula una función dentro de otra
        }

        mensajito()

        // Funciones con tipo de retorno
        fun sumar():Int {
            var x = 5 + 4
            return x
        }

        Log.i("MALO", "Invocando una función con tipo de retorno ${ sumar() }")

        //Otra modalida de una función
        fun saludar2(mensaje:String) {
            Log.i("MALO", mensaje)
        }

        saludar2("Función que recibe un argumento")

        // Kotlin es orientado a objetos, es funcional y también es reactivo
        fun saludar3(nombre:String, edad:Int) {
            Log.i("MALO", "Tu nombre es $nombre y tu edad es $edad")
        }

        saludar3("Victor", 20)
    }

    // Aquí declaramos una función
    fun saludar() {
        Log.i("MALO", "Implementando mi primer función en kotlin")
    }
}