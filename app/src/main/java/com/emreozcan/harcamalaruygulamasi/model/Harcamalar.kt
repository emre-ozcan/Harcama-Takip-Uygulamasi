package com.emreozcan.harcamalaruygulamasi.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
/**
 * Room Databasede tutulacak olan verilerin sınıfıdır
 *
 * Parcelable ne demek? -> Recyclerda carda veya item a tıkladığımızda biz o veriyi bir sınıf olarak göndeririz
 * sınıf olarak göndermek içinde Parcelable sınıfından inherit edilmesi gereklidir yoksa sınıf olarak veri taşınamaz !
 *
 */
@Parcelize
@Entity(tableName = "harcamalar")
data class Harcamalar (
    @ColumnInfo(name= "kategori") val kategori :String?,
    @ColumnInfo(name = "harcama") var harcama:Double?,
    @ColumnInfo(name = "aciklama") val aciklama:String?
        ):Parcelable{
        @PrimaryKey (autoGenerate = true)
        var uid:Int=0
}