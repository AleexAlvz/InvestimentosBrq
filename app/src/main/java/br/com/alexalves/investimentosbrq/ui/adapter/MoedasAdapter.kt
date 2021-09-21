package br.com.alexalves.investimentosbrq.ui.adapter

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.R

class MoedasAdapter(
    private val moedas: List<Moeda>,
    private val context: Context?,
    private val onItemClick: (moeda: Moeda) -> Unit
) : RecyclerView.Adapter<MoedasAdapter.MoedasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoedasViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_moeda, parent, false)
        return MoedasViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MoedasViewHolder, position: Int) {
        vinculaCampos(holder, moedas[position])
    }

    private fun vinculaCampos(holder: MoedasViewHolder, moeda: Moeda) {
        holder.itemView.setOnClickListener { onItemClick(moeda) }
        holder.nomeMoeda.setText(moeda.abreviacao)
        val variacaoFormatada = moeda.variation.toString()+"%"
        holder.variacaoMoeda.text = variacaoFormatada
        setColorVariacao(moeda, holder)
    }

    private fun setColorVariacao(
        moeda: Moeda,
        holder: MoedasViewHolder
    ) {
        val variacao = moeda.variation
        if (variacao > 0) {
            holder.variacaoMoeda.setTextColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.green_positive
                )
            )
        } else if (variacao < 0) {
            holder.variacaoMoeda.setTextColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.red_negative
                )
            )
        } else if (variacao.equals(0)) {
            holder.variacaoMoeda.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        }
    }

    override fun getItemCount(): Int { return moedas.size }

    class MoedasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeMoeda = itemView.findViewById<TextView>(R.id.item_moeda_text_moeda)
        val variacaoMoeda = itemView.findViewById<TextView>(R.id.item_moeda_text_variacao)
    }
}
