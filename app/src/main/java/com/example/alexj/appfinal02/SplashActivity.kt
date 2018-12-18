package com.example.alexj.appfinal02

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import java.lang.Exception

/**
 * Clase para pantalla de introduccion, Splash
 */
class SplashActivity : Activity() {

    lateinit var  hilo: Runnable
    var mLottie: LottieAnimationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // lanzamos la animacion de Lottie. Añadida por xml en SplashActivity
        mLottie?.playAnimation()


        /**
         * Creamos un hilo, pasándole un objeto de tipo hilo(Thread)
         */
        val hilo = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(5000)

                    /**
                    creamos un nuevo Itent para pasar de esta actividad a la que queramos después de los 5 segundos
                    establecidos en Thread.sleep
                    */
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        // lanzamos el hilo
        hilo.start()
    }

}
