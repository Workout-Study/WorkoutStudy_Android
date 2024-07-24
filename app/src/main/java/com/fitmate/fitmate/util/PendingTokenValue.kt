package com.fitmate.fitmate.util

import java.io.Serializable

enum class PendingTokenValue(val value:String, val code:Int): Serializable {
    CERTIFICATION("certification" ,1)
}
