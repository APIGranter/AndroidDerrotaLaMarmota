package pgm.pmdm.p6pgm

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Introduccion : AppCompatActivity() {
    @SuppressLint("UseKtx")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val jugar = findViewById<Button>(R.id.boton_luchar)
        jugar.setOnClickListener {
            val intent = Intent(this, JuegoMain::class.java)
            startActivity(intent)

        }

        val botonInfo = findViewById<Button>(R.id.boton_informacion)
        botonInfo.setOnClickListener {
            val url = "https://apigranter.github.io/AndroidDerrotaLaMarmota/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }


    }
}