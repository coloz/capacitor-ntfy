package com.example.capacitor.ntfy

import com.getcapacitor.Logger

class Ntfy {

    fun echo(value: String): String {
        Logger.info("Echo", value)

        return value
    }
}
