package br.com.alexalves.utils.extensions

import android.widget.TextView

object TextViewExtensions {

    fun TextView.setAccessibleText(text: String, descriptionBeforeText: String?=null, descriptionAfterText: String?=null){
        this.text=text
        var description = String()
        descriptionBeforeText?.let { description += it+" " }
        description += text+" "
        descriptionAfterText?.let { description += descriptionAfterText+" " }
        this.contentDescription = description
    }
}