package com.example.weatherapp.ui.alert.view

import android.app.Dialog
import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.DiplayAlarmDialogBinding

class AlarmDialog(context: Context) : Dialog(context) {

    private val binding: DiplayAlarmDialogBinding
    private var ringtone: Ringtone


    init {
        binding= DiplayAlarmDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, alarmSound)

        binding.dismissButton.setOnClickListener {
            dismiss()
        }


        window?.let {
            it.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            it.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        }
    }

    fun setAlarmInfo(title: String, description: String) {
        binding.dialogTitle.text = title
        binding.dialogDescription.text = description
    }

    fun showAlarm() {
        ringtone.play()
        show()
    }

    override fun dismiss() {
        super.dismiss()
        ringtone.stop()
    }
    override fun setCancelable(cancelable: Boolean) {

    }
}
