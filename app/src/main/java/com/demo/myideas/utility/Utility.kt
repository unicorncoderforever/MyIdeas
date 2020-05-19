package com.demo.myideas.utility

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.demo.myideas.R
import retrofit2.HttpException
import java.util.regex.Matcher
import java.util.regex.Pattern

class Utility {
    companion object {
        fun getSpannableString(normalString: String, navigate: () -> Unit): SpannableString {
            val ss = SpannableString(normalString);
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    navigate()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }
            ss.setSpan(
                clickableSpan,
                ss.indexOf("?") + 1,
                ss.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return ss
        }

        fun showToast(context: Context, resourceId: Int) {
            Toast.makeText(context, resourceId, Toast.LENGTH_SHORT).show()
        }

        fun showToast(context: Context, resourceId: String) {
            Toast.makeText(context, resourceId, Toast.LENGTH_SHORT).show()
        }

        fun getResponseCode(e: Throwable): Int {
            if (e is HttpException) {
                return e.response()!!.code()
            }
            return 507;
        }

        fun CharSequence?.isValidEmail() =
            !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

        fun isValidPassword(password: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val PASSWORD_PATTERN =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"
            pattern = Pattern.compile(PASSWORD_PATTERN)
            matcher = pattern.matcher(password)
            return matcher.matches()
        }

        fun showDialogue(positionClick: () -> Unit, activity: Activity, title: Int, content: Int) {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton(
                        R.string.ok,
                        DialogInterface.OnClickListener { dialog, id ->
                            positionClick()
                        })
                    setNegativeButton(R.string.cancel,
                        DialogInterface.OnClickListener { dialog, id ->
                            dialog.dismiss()
                        })
                }
                builder.setTitle(title)
                builder.setMessage(content)
                builder.create()
                builder.show()
            }

        }
    }


}

