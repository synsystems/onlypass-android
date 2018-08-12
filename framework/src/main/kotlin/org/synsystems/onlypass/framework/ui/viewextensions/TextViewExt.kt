package org.synsystems.onlypass.framework.ui.viewextensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

/**
 * Sets a listener to be called when the text in this view changes.
 */
inline fun TextView.addTextChangedListener(crossinline listener: (String) -> Unit) {
  addTextChangedListener(object : TextWatcher {
    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
      listener(text.toString())
    }

    override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(text: Editable) {}
  })
}