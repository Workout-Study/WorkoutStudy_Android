package com.fitmate.fitmate.util

import android.app.Application
import android.content.Context
import com.fitmate.fitmate.R
import dagger.hilt.android.qualifiers.ApplicationContext

//( 1: 등산, 2: 생활 체육, 3: 웨이트, 4: 수영, 5: 축구, 6: 농구, 7: 야구, 8: 바이킹, 9: 클라이밍, 10: 기타 )
enum class GroupCategory(val code:Int ) {

    HIKING(1),
    LIFE_SPORTS(2),
    WEIGHT_TRAINING(3),
    SWIMMING(4),
    SOCCER(5),
    BASKETBALL(6),
    BASEBALL(7),
    BIKING(8),
    CLIMBING(9);


    fun getCategoryName(context:Context): String {
        return when(this){
            HIKING -> context.getString(R.string.category_scr_1)
            LIFE_SPORTS -> context.getString(R.string.category_scr_2)
            WEIGHT_TRAINING -> context.getString(R.string.category_scr_3)
            SWIMMING -> context.getString(R.string.category_scr_4)
            SOCCER -> context.getString(R.string.category_scr_5)
            BASKETBALL -> context.getString(R.string.category_scr_6)
            BASEBALL -> context.getString(R.string.category_scr_7)
            BIKING -> context.getString(R.string.category_scr_8)
            CLIMBING -> context.getString(R.string.category_scr_9)
        }
}


}