package com.aipg.wifilogin.localData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addStockTable")
class dataClass(val usernameSt: String,
                  val passwordSt: String,
                  val autoDie: Int,
                  val date: String) {
    @PrimaryKey (autoGenerate = true) var id = 0
}