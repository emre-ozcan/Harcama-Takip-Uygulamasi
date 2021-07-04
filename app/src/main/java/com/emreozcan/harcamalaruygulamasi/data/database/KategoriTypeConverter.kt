package com.emreozcan.harcamalaruygulamasi.data.database

import androidx.room.TypeConverter
import com.emreozcan.harcamalaruygulamasi.model.ExpenseTypes

class KategoriTypeConverter {

    @TypeConverter
    fun fromKategori(expenseTypes: ExpenseTypes): String{
        return expenseTypes.name
    }

    @TypeConverter
    fun toKategori(str: String): ExpenseTypes{
        return ExpenseTypes.valueOf(str)
    }
}