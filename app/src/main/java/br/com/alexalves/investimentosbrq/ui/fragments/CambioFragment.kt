package br.com.alexalves.investimentosbrq.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.alexalves.investimentosbrq.R

class CambioFragment : Fragment() {

    lateinit var inflatedView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflatedView = inflater.inflate(R.layout.fragment_cambio, container, false)
        return inflatedView
    }

}
