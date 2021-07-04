package com.emreozcan.harcamalaruygulamasi.util

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.emreozcan.harcamalaruygulamasi.R
import com.emreozcan.harcamalaruygulamasi.model.ExpenseTypes
import www.sanju.motiontoast.MotionToast

fun ImageView.loadImageFromKategori(expenseTypes: ExpenseTypes){
    when(expenseTypes){
        ExpenseTypes.FATURA -> this.setImageResource(R.drawable.ic_fatura)
        ExpenseTypes.DIGER -> this.setImageResource(R.drawable.ic_diger)
        ExpenseTypes.KIRA -> this.setImageResource(R.drawable.ic_kira)
    }
}

fun successUserToast(activity: Activity, context: Context){
    MotionToast.createToast(activity,
        "Kullanıcı Bilgilerİ",
        "Bilgileriniz başarılı bir şekilde değiştirildi !",
        MotionToast.TOAST_SUCCESS,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context,R.font.helvetica_regular))
}

fun successHarcamaToast(activity: Activity, context: Context){
    MotionToast.createToast(activity,
        "Harcama Ekleme",
        "Harcamanız başarılı bir şekilde eklendi!",
        MotionToast.TOAST_SUCCESS,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context,R.font.helvetica_regular))
}

fun warningToast(activity: Activity,context: Context){
    MotionToast.createToast(activity,
        "Boş Alan",
        "Lütfen boş alanları doldurunuz",
        MotionToast.TOAST_WARNING,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context,R.font.helvetica_regular))
}

fun deleteToast(activity: Activity,context: Context){
    MotionToast.createToast(activity,
        "Harcama Sİlİndİ",
        "Harcama başarılı bir şekilde silindi",
        MotionToast.TOAST_DELETE,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(context,R.font.helvetica_regular))
}

