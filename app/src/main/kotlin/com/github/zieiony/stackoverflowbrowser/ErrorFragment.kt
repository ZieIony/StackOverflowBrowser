package com.github.zieiony.stackoverflowbrowser

import android.os.Bundle
import android.view.View
import com.github.zieiony.stackoverflowbrowser.navigation.NavigationFragment
import com.github.zieiony.stackoverflowbrowser.navigation.FragmentAnnotation
import com.github.zieiony.stackoverflowbrowser.navigation.NavigationStep
import kotlinx.android.synthetic.main.fragment_error.*
import java.io.Serializable

@FragmentAnnotation(layout = R.layout.fragment_error)
class ErrorFragment : NavigationFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        error_title.text = arguments!!.getString(ERROR_TITLE)
        error_message.text = arguments!!.getString(ERROR_MESSAGE)
        error_confirm.setOnClickListener {
            navigateBack()
        }
    }

    companion object {
        const val ERROR_TITLE = "title"
        const val ERROR_MESSAGE = "message"

        fun makeStep(title: String, message: String): NavigationStep {
            val arguments = HashMap<String, Serializable>()
            arguments.put(ERROR_TITLE, title)
            arguments.put(ERROR_MESSAGE, message)
            return NavigationStep(ErrorFragment::class.java, arguments)
        }
    }
}