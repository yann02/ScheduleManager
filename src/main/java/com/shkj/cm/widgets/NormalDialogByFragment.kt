package com.shkj.cm.widgets

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.shkj.cm.R


/**
 * @author Yan
 * A custom dialog by DialogFragment.
 * @since 2020/11/02
 */
const val TITLE = "title"
const val MESSAGE = "message"
const val NEGATIVE = "negative"
const val POSITIVE = "positive"

class NormalDialogByFragment : DialogFragment() {
    private var title: String? = ""
    private var message: String? = ""
    private var negative: String? = ""
    private var positive: String? = ""
    private var mPositiveListener: PositiveDialogListener? = null
    private var mNegativeListener: NegativeDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            title = it.getString(TITLE)
            message = it.getString(MESSAGE)
            negative = it.getString(NEGATIVE)
            positive = it.getString(POSITIVE)
        }
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth)
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mPositiveListener = context as PositiveDialogListener
            mNegativeListener = context as NegativeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
//            throw ClassCastException(("$context must implement NoticeDialogListener"))
        }
    }

    interface PositiveDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }

    interface NegativeDialogListener {
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onStart() {
        super.onStart()
        val mDialog = dialog
        if (null != mDialog) {
            val dm = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(dm)
            dialog!!.window!!.setLayout((dm.widthPixels * 0.89).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_normal, container, false)
        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val tvMessage = view.findViewById<TextView>(R.id.tv_message)
        val tvNegative = view.findViewById<TextView>(R.id.tv_negative)
        val tvPositive = view.findViewById<TextView>(R.id.tv_positive)
        title?.let {
            tvTitle.text = it
        }
        message?.let {
            tvMessage.text = it
        }
        negative?.let {
            tvNegative.text = it
        }
        positive?.let {
            tvPositive.text = it
        }
        tvNegative.setOnClickListener {
            mNegativeListener?.onDialogNegativeClick(this)
            dialog?.dismiss()
        }
        tvPositive.setOnClickListener {
            mPositiveListener?.onDialogPositiveClick(this)
            dialog?.dismiss()
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        /*设置背景透明，否则无法自定义边框圆角*/
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    companion object {
        fun getInstance(title: String?, message: String?, negative: String?, positive: String?): NormalDialogByFragment {
            return NormalDialogByFragment().apply {
                arguments = Bundle().also {
                    title?.let { mit ->
                        it.putString(TITLE, mit)
                    }
                    message?.let { mit ->
                        it.putString(MESSAGE, mit)
                    }
                    negative?.let { mit ->
                        it.putString(NEGATIVE, mit)
                    }
                    positive?.let { mit ->
                        it.putString(POSITIVE, mit)
                    }
                }
            }
        }
    }

}
