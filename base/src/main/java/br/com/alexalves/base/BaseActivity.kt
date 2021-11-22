package br.com.alexalves.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseActivity: AppCompatActivity() {

    fun replaceFragmentNoStack(container: View, fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(container.id, fragment)
            .commit()
    }

}