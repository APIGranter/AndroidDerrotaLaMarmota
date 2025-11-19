package pgm.pmdm.p6pgm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.graphics.alpha


class Informacion : Fragment() {

    private lateinit var tvRonda: TextView
    private lateinit var tvVidas: TextView
    private lateinit var botonRei: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_informacion, container, false)
        view.findViewById<TextView>(R.id.valor_rondas)?.text = numRonda.toString()
        view.findViewById<TextView>(R.id.valor_vidas)?.text = vidasJugador.toString()
        view.findViewById<TextView>(R.id.valor_meta)?.text = rondasParaGanar.toString()

        var botonRepetir = view.findViewById<ImageButton>(R.id.boton_repetirSecuencia)
        botonRepetir?.setOnClickListener {
            (activity as? JuegoMain)?.repetirSecuencia()
        }

        return view
    }

    fun actualizarInfo(ronda: Int, vidas: Int, meta: Int) {
        view?.apply {
            findViewById<TextView>(R.id.valor_rondas)?.text = ronda.toString()
            findViewById<TextView>(R.id.valor_vidas)?.text = vidas.toString()
            findViewById<TextView>(R.id.valor_meta)?.text = meta.toString()
        }
    }






}