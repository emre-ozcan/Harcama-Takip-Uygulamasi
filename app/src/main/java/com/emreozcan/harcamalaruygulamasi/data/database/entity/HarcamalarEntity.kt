package com.emreozcan.harcamalaruygulamasi.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emreozcan.harcamalaruygulamasi.model.ExpenseTypes
import com.emreozcan.harcamalaruygulamasi.util.Constants.Companion.EXPENSE_TABLE_NAME
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Room Databasede tutulacak olan verilerin sınıfıdır
 *
 * Parcelable ne demek? -> Recyclerda carda veya item a tıkladığımızda biz o veriyi bir sınıf olarak göndeririz
 * sınıf olarak göndermek içinde Parcelable sınıfından inherit edilmesi gereklidir yoksa sınıf olarak veri taşınamaz !
 *
 */
@Parcelize
@Entity(tableName = EXPENSE_TABLE_NAME)
data class HarcamalarEntity(
    @ColumnInfo(name = "kategori") val kategori: ExpenseTypes?,
    @ColumnInfo(name = "harcama") var harcama: Double?,
    @ColumnInfo(name = "aciklama") val aciklama: String?
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}