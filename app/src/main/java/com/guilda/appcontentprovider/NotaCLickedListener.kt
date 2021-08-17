package com.guilda.appcontentprovider

import android.database.Cursor

interface NotaCLickedListener {
    fun noteClickedItem(cursor: Cursor?)
    fun noteRemoveItem(cursor: Cursor?)
}