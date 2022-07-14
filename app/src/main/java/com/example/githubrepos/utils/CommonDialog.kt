package com.example.githubrepos.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDialog
import com.example.coremodule.utils.debouncedOnClick
import com.example.githubrepos.R

class CommonDialog private constructor() {

    companion object {

        /**
         * Applies multiple properties to the dialog and returns the instance.
         * Call show() to display the dialog
         */
        fun create(context: Context, init: Builder.() -> Unit) = Builder(context, init).build()

        /**
         * Applies multiple properties to the dialog and displays it.
         */
        fun show(context: Context, init: Builder.() -> Unit) {
            Builder(context, init).build().show()
        }

        class Builder private constructor(val context: Context) {

            constructor(context: Context, init: Builder.() -> Unit) : this(context) {
                init()
            }

            /**
             * title at top of dialog
             */
            var title: String? = null

            /**
             * text to be displayed below the title
             */
            var message: String? = null

            var cancellable: Boolean = true

            private var positiveButtonText: String? = null
            private var positiveButtonListener: () -> Unit? = {}


            private var onCancelListener: () -> Unit? = {}
            private var onDismissListener: () -> Unit? = {}

            private var listOfString: List<String>? = null
            private var listOnClick: ((Int, String) -> Unit)? = null

            /**
             * set title to the dialog
             * @param title a lambda function that returns title as string
             */
            fun title(title: Builder.() -> String) = apply {
                this.title = title()
            }

            /**
             * set title to the dialog
             * @param title a lambda function that returns title as string res
             */
            fun titleRes(titleRes: Builder.() -> Int) = apply {
                title = context.getString(titleRes())
            }

            /**
             * set the content body of the dialog
             * @param content a lambda function that returns content body as string
             */
            fun message(message: Builder.() -> String) = apply {
                this.message = message()
            }

            /**
             * set the content body of the dialog
             * @param content a lambda function that returns content body as string res
             */
            fun messageRes(res: Builder.() -> Int) = apply {
                this.message = context.getString(res())
            }

            /**
             * set the dialog as cancellable
             * @param cancellable a lambda function that returns a boolean stating if dialog is cancellable
             */
            fun cancellable(cancellable: Builder.() -> Boolean) = apply {
                this.cancellable = cancellable()
            }

            /**
             * set the first or topmost button on the dialog
             * @param text The literal string to display on button
             * @param res The string resource to display on button
             * @param onClick A listener to invoke when the button is pressed.
             */
            fun positiveButton(
                text: String? = null,
                @StringRes res: Int? = null,
                onClick: (() -> Unit)? = null
            ) = apply {
                positiveButtonText = text?.let { it } ?: res?.let { context.getString(it) }
                onClick?.let { positiveButtonListener = it }
            }


            /**
             * called when the dialog is cancelled
             * @param onClick A listener to invoke whe dialog is cancelled
             */
            fun onCancelListener(onClick: (() -> Unit)) = apply {
                onCancelListener = onClick
            }

            /**
             * called when the dialog is dismissed
             * @param onClick A listener to invoke whe dialog is dismissed
             */
            fun onDismissListener(onClick: (() -> Unit)) = apply {
                onDismissListener = onClick
            }

            fun list(items: List<String>, onItemSelected: (Int, String) -> Unit) = apply {
                listOfString = items
                listOnClick = onItemSelected
            }

            /**
             * creates the instance of CustomDimDialog and set the given attributes
             * @return instance of CustomDimDialog
             */
            fun build(): AppCompatDialog {
                val dialog = AppCompatDialog(context)
                val dialogView = LayoutInflater.from(context).inflate(R.layout.common_dialog,null)
                dialog.setContentView(dialogView)

                with(dialogView) {
                    this.findViewById<TextView>(R.id.dialogTitle).text = title
                    this.findViewById<TextView>(R.id.commonDialogContent).text = message

                    this.findViewById<Button>(R.id.customDialogButtonPositive).debouncedOnClick {
                        positiveButtonListener()
                        dialog.dismiss()
                    }
                }

                // set Cancelable
                dialog.setCancelable(cancellable)
                dialog.setCanceledOnTouchOutside(cancellable)
                dialog.setOnCancelListener { onCancelListener() }

                // set dismiss listener
                dialog.setOnDismissListener { onDismissListener.invoke() }

                return dialog
            }
        }
    }
}
