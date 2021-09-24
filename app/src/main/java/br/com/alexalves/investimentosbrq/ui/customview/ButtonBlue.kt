package br.com.alexalves.investimentosbrq.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import br.com.alexalves.investimentosbrq.R

class ButtonBlue @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var title: String? = null
    val viewButton = inflate(context, R.layout.custom_button_blue, this)
    var configuraClique: (()->Unit)? = null

    private var state: ButtonBlueState = ButtonBlueState.Enabled
        set(value) {
            field = value
            refreshState()
        }

    private fun refreshState() {
        isEnabled = state.isEnabled
        isClickable = state.isEnabled
        refreshDrawableState()

        viewButton.findViewById<TextView>(R.id.text_button_blue).run {
            this.setText(title)
            isEnabled = state.isEnabled
            isClickable = state.isEnabled
        }

        when(state){
            ButtonBlueState.Enabled -> viewButton
                .findViewById<TextView>(R.id.text_button_blue).run {
                    this.setTextColor(ContextCompat.getColor(context, R.color.white))
                }

            ButtonBlueState.Disabled -> viewButton
                .findViewById<TextView>(R.id.text_button_blue).run {
                    this.setTextColor(ContextCompat.getColor(context, R.color.grey))
                }
        }
    }

    init {
        setLayout(attrs)
        configuraOnClickListener()
        refreshState()
    }

    private fun configuraOnClickListener() {
        viewButton.setOnClickListener { configuraClique?.invoke() }
        viewButton.findViewById<TextView>(R.id.text_button_blue).setOnClickListener { configuraClique?.invoke() }
    }

    private fun setLayout(attrs: AttributeSet?) {
        attrs?.let { attributeSet ->
            val attributes = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.ButtonBlue
            )
            setBackgroundResource(R.drawable.button_blue_background)

            val titleResId = attributes.getResourceId(
                R.styleable.ButtonBlue_custom_button_title, 0
            )
            if (titleResId != 0) {
                title = context.getString(titleResId)
                Log.i("TESTE", title.toString())
            }

            attributes.recycle()
        }
    }

    sealed class ButtonBlueState(val isEnabled: Boolean) {
        object Enabled : ButtonBlueState(true)
        object Disabled : ButtonBlueState(false)
    }

    fun configuraTitulo(title: String){
        this.title = title
        refreshState()
    }

    fun configuraEstado(boolean: Boolean){
        if (boolean){
            state = ButtonBlueState.Enabled
        } else state = ButtonBlueState.Disabled
    }
}