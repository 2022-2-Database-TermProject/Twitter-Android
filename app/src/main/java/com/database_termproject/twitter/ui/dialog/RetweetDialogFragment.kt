package com.database_termproject.twitter.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.database_termproject.twitter.databinding.FragmentRetweetDialogBinding
import com.database_termproject.twitter.utils.DialogFragmentUtils

class RetweetDialogFragment: DialogFragment() {
    private lateinit var binding: FragmentRetweetDialogBinding
    private lateinit var myDialogCallback: MyDialogCallback

    interface MyDialogCallback {
        fun confirm(content: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRetweetDialogBinding.inflate(inflater, container, false)

        //다이얼로그 프래그먼트 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        setMyClickListener()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //전체 프래그먼트 크기 설정
        DialogFragmentUtils.dialogFragmentResize(
            requireContext(),
            this,
            0.9f,
            0.5f
        )
    }

    fun setMyDialogCallback(myDialogCallback: MyDialogCallback) {
        this.myDialogCallback = myDialogCallback
    }

    private fun setMyClickListener(){
        binding.retweetDialogCancelTv.setOnClickListener {
            dismiss()
        }

        binding.retweetDialogSaveTv.setOnClickListener {
            // Validation 후, 저장
            val text = binding.retweetDialogContentEt.text.toString()
            if(text.isEmpty()) return@setOnClickListener;

            myDialogCallback.confirm(text);
            dismiss()
        }
    }

}