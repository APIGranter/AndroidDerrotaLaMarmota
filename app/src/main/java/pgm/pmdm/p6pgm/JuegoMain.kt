@file:Suppress("SpellCheckingInspection")

package pgm.pmdm.p6pgm

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

private const val CIRCULO = 0
private const val TRIANGULO = 1
private const val CUADRADO = 2
private const val EQUIS = 3

// Variablessss
var secuenciaMarmota = mutableListOf<Int>()
var secuenciaJugador = mutableListOf<Int>()
var vidasJugador = 3
var numRonda = 1
// IMPORTANTEEEEE
var rondasParaGanar = 3

// V


class JuegoMain : AppCompatActivity() {

    lateinit var muestraSecuenciaFunc: () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_juego_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.contenedor_info, Informacion())
                .commit()
        }
        supportFragmentManager.executePendingTransactions()

        val oscurecedor = findViewById<View>(R.id.oscurecedor)
        val textoError = findViewById<TextView>(R.id.errorView)
        val textoVidas = findViewById<TextView>(R.id.viewVidas)
        val pantalla = findViewById<ImageView>(R.id.muestraPatron)
        val botonCirculo = findViewById<ImageButton>(R.id.boton_circulo)
        val botonTriangulo = findViewById<ImageButton>(R.id.boton_triangulo)
        val botonCuadrado = findViewById<ImageButton>(R.id.boton_cuadrado)
        val botonEquix = findViewById<ImageButton>(R.id.boton_equis)
        val reintentar = findViewById<Button>(R.id.reintentar)
        val rendirse = findViewById<Button>(R.id.rendirse)

        val listaBotones = intArrayOf(0, 1, 2, 3)
        listaBotones[CIRCULO] = R.drawable.circulo
        listaBotones[TRIANGULO] = R.drawable.triangulo
        listaBotones[CUADRADO] = R.drawable.cuadrado
        listaBotones[EQUIS] = R.drawable.equis


        var imgPorRonda = 3 + numRonda




        fun deshabilitarBotones() {
            botonCirculo.isEnabled = false
            botonTriangulo.isEnabled = false
            botonEquix.isEnabled = false
            botonCuadrado.isEnabled = false

            // cambia la transparencia
            botonCirculo.alpha = 0.3f
            botonTriangulo.alpha = 0.3f
            botonEquix.alpha = 0.3f
            botonCuadrado.alpha = 0.3f
        }

        fun habilitarBotones() {
            botonCirculo.isEnabled = true
            botonTriangulo.isEnabled = true
            botonEquix.isEnabled = true
            botonCuadrado.isEnabled = true

            botonCirculo.alpha = 1.0f
            botonTriangulo.alpha = 1.0f
            botonEquix.alpha = 1.0f
            botonCuadrado.alpha = 1.0f
        }

        fun mostrarSecuencia() {
            deshabilitarBotones()

            val handler = android.os.Handler(android.os.Looper.getMainLooper())
            secuenciaMarmota.forEachIndexed { index, imageId ->
                val tiempoAparecer = 1000L * (index + 1)
                val tiempoDesaparecer = tiempoAparecer + 500L
                handler.postDelayed({
                    pantalla.setImageResource(listaBotones[imageId])
                }, tiempoAparecer)
                handler.postDelayed({
                    pantalla.setImageResource(android.R.color.transparent)
                }, tiempoDesaparecer)
            }

            if (secuenciaMarmota.isNotEmpty()) {
                val totalDuration = 1000L * (secuenciaMarmota.size) + 500L
                handler.postDelayed({
                    habilitarBotones()
                }, totalDuration)
            }

        }

        // PARTE DEL APAÑO
        muestraSecuenciaFunc = ::mostrarSecuencia


        fun feedbackError(esGameOver: Boolean) {
            // Lo del vibrator es full copiado
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    android.os.VibrationEffect.createOneShot(
                        300,
                        android.os.VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(300)
            }


            val handler =
                android.os.Handler(android.os.Looper.getMainLooper())
            oscurecedor.setBackgroundColor(0x99DC143C.toInt())
            textoError.setTextColor(android.graphics.Color.WHITE)
            textoVidas.setTextColor(android.graphics.Color.WHITE)
            deshabilitarBotones()
            textoError.visibility = View.VISIBLE
            textoVidas.visibility = View.VISIBLE
            if (vidasJugador == 1) {
                textoVidas.setText("Te queda ${vidasJugador} sola vida.")
            } else if (vidasJugador == 0){
                textoError.setText("¡Perdedor!")
                textoVidas.setText("")
            } else {
                textoVidas.setText("Te quedan ${vidasJugador} vidas.")
            }


            if (!esGameOver) {
                handler.postDelayed({
                    textoError.visibility = View.GONE
                    textoVidas.visibility = View.GONE
                    oscurecedor.setBackgroundColor(0x80000000.toInt())
                    mostrarSecuencia()
                }, 1200L)
            }
        }



        fun rondaMarmota() {
            actualizarInfoFragment()
            secuenciaMarmota.clear()
            secuenciaJugador.clear()
            imgPorRonda = 2 + numRonda

            if (numRonda == rondasParaGanar){
                // Gana
                val intent = Intent(this, ResultadoGame::class.java)
                startActivity(intent)
                finish()

            }

            while (secuenciaMarmota.size < imgPorRonda) {
                secuenciaMarmota.add(getRandomNumber(0, 3))
            }
            mostrarSecuencia()
        }


        fun gameOver(){
            deshabilitarBotones();
            feedbackError(true)
            reintentar.visibility = View.VISIBLE
            rendirse.visibility = View.VISIBLE
            val anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.bombeo_animacion)
            textoError.startAnimation(anim)
        }

        fun rondaJugador() {
            val i: Int = secuenciaJugador.size - 1

            if (secuenciaJugador[i] != secuenciaMarmota[i]) {
                vidasJugador--
                actualizarInfoFragment()
                textoError.setText(getString(R.string.equivocado))
                if (vidasJugador <= 0) {gameOver()
                    return
                }

                feedbackError(false)
                // Limpio la secuencia de imputs del jugador
                secuenciaJugador.clear()
                // Vuelvo a mostrarle la secuencia correcta
                actualizarInfoFragment()
                return
            }

            // exito
            if (secuenciaJugador.size == secuenciaMarmota.size) {

                deshabilitarBotones()
                val handler = android.os.Handler(android.os.Looper.getMainLooper())
                oscurecedor.setBackgroundColor(0x9926A69A.toInt()) // color tila
                textoError.setText("¡Correcto!")
                textoError.visibility = View.VISIBLE


                handler.postDelayed({
                    oscurecedor.setBackgroundColor(0x80000000.toInt())
                    textoError.visibility = View.GONE

                    numRonda++
                    actualizarInfoFragment()
                    rondaMarmota()
                }, 800L) //

            }
        }


        botonCirculo.setOnClickListener {
            val handler = android.os.Handler(android.os.Looper.getMainLooper())
            // Flash de brillo
            botonCirculo.colorFilter = PorterDuffColorFilter(
                0x80FFFFFF.toInt(),
                PorterDuff.Mode.SRC_ATOP
            )
            // quitarlo tras un momento
            handler.postDelayed({
                botonCirculo.clearColorFilter()
            }, 100L)
            secuenciaJugador.add(CIRCULO)
            rondaJugador()
        }
        botonTriangulo.setOnClickListener {
            val handler = android.os.Handler(android.os.Looper.getMainLooper())
            botonTriangulo.colorFilter = PorterDuffColorFilter(
                0x80FFFFFF.toInt(),
                PorterDuff.Mode.SRC_ATOP
            )
            handler.postDelayed({
                botonTriangulo.clearColorFilter()
            }, 100L)
            secuenciaJugador.add(TRIANGULO)
            rondaJugador()
        }
        botonCuadrado.setOnClickListener {
            val handler = android.os.Handler(android.os.Looper.getMainLooper())
            botonCuadrado.colorFilter = PorterDuffColorFilter(
                0x80FFFFFF.toInt(),
                PorterDuff.Mode.SRC_ATOP
            )
            handler.postDelayed({
                botonCuadrado.clearColorFilter()
            }, 100L)
            secuenciaJugador.add(CUADRADO)
            rondaJugador()
        }
        botonEquix.setOnClickListener {
            val handler = android.os.Handler(android.os.Looper.getMainLooper())
            botonEquix.colorFilter = PorterDuffColorFilter(
                0x80FFFFFF.toInt(),
                PorterDuff.Mode.SRC_ATOP
            )
            handler.postDelayed({
                botonEquix.clearColorFilter()
            }, 100L)
            secuenciaJugador.add(EQUIS)
            rondaJugador()
        }



        rondaMarmota()



        reintentar.setOnClickListener {
            vidasJugador = 3
            numRonda = 1
            rondasParaGanar = 3
            secuenciaMarmota.clear()
            secuenciaJugador.clear()
            actualizarInfoFragment()


            reintentar.visibility = View.GONE
            rendirse.visibility = View.GONE
            textoError.clearAnimation()
            textoError.visibility = View.GONE
            textoVidas.visibility = View.GONE
            oscurecedor.setBackgroundColor(0x80000000.toInt())

            rondaMarmota()
        }

        rendirse.setOnClickListener {
            finish()
        }





    }

    // APAÑO por la encapsulacion de funciones
    fun repetirSecuencia() {
        secuenciaJugador.clear()
        rondasParaGanar++

        actualizarInfoFragment()
        if(::muestraSecuenciaFunc.isInitialized) {
            muestraSecuenciaFunc()
        }
    }

    fun actualizarInfoFragment() {
        val fragment = supportFragmentManager.findFragmentById(R.id.contenedor_info) as? Informacion
        fragment?.actualizarInfo(numRonda, vidasJugador, rondasParaGanar)
    }
    fun getRandomNumber(min: Int, max: Int): Int {
        return Random.nextInt(min, max + 1)
    }


}