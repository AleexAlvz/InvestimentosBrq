package br.com.alexalves.investimentosbrq.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.utils.CurrencyUtils

class MoedasAdapter(
    private val currencies: List<Currency>,
    private val context: Context?,
    private val onItemClick: (currency: Currency) -> Unit
) : RecyclerView.Adapter<MoedasAdapter.MoedasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoedasViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_moeda, parent, false)
        return MoedasViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MoedasViewHolder, position: Int) {
        vinculaCampos(holder, currencies[position])
    }

    fun vinculaCampos(holder: MoedasViewHolder, currency: Currency) {
        holder.itemView.setOnClickListener { onItemClick(currency) }
        holder.nomeMoeda.setText(currency.abbreviation)
        holder.variacaoMoeda.text = CurrencyUtils().getVariacaoFormatada(currency.variation)
        val variationColor = CurrencyUtils().getCurrencyColor(currency.variation, context!!)
        holder.variacaoMoeda.setTextColor(variationColor)
    }

    override fun getItemCount(): Int { return currencies.size }

    class MoedasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeMoeda = itemView.findViewById<TextView>(R.id.item_moeda_text_moeda)
        val variacaoMoeda = itemView.findViewById<TextView>(R.id.item_moeda_text_variacao)
    }
}
