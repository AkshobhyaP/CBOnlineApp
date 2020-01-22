package com.codingblocks.cbonlineapp.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.codingblocks.cbonlineapp.util.extensions.save

class PreferenceHelper private constructor() {

    var SP_ACCESS_TOKEN_KEY: String
        get() = prefs?.getString(ACCESS_TOKEN, ACCESS_TOKEN) ?: ""
        set(value) {
            prefs?.save(ACCESS_TOKEN, value)
        }

    var SP_JWT_TOKEN_KEY: String
        get() = prefs?.getString(JWT_TOKEN, JWT_TOKEN) ?: ""
        set(value) {
            prefs?.save(JWT_TOKEN, value)
        }

    var SP_JWT_REFRESH_TOKEN: String
        get() = prefs?.getString(REFRESH_TOKEN, REFRESH_TOKEN) ?: ""
        set(value) {
            prefs?.save(REFRESH_TOKEN, value)
        }
    var SP_USER_IMAGE: String
        get() = prefs?.getString(USER_IMAGE, USER_IMAGE) ?: "Empty"
        set(value) {
            prefs?.save(USER_IMAGE, value)
        }
    var SP_ONEAUTH_ID: String
        get() = prefs?.getString(ONEAUTH_ID, ONEAUTH_ID) ?: ""
        set(value) {
            prefs?.save(ONEAUTH_ID, value)
        }

    var SP_USER_ID: String
        get() = prefs?.getString(USER_ID, USER_ID) ?: ""
        set(value) {
            prefs?.save(USER_ID, value)
        }

    var SP_USER_NAME: String
        get() = prefs?.getString(USER_NAME, USER_NAME) ?: ""
        set(value) {
            prefs?.save(USER_NAME, value)
        }

    var SP_EMAIL_ID: String
        get() = prefs?.getString(EMAIL_ID, EMAIL_ID) ?: ""
        set(value) {
            prefs?.save(EMAIL_ID, value)
        }

    var SP_ROLE_ID: Int
        get() = prefs?.getInt(ROLE_ID, ROLE_ID_DEFAULT) ?: ROLE_ID_DEFAULT
        set(value) {
            prefs?.save(ROLE_ID, value)
        }

    var SP_WIFI: Boolean
        get() = prefs?.getBoolean(WIFI, WIFI_DEFAULT) ?: WIFI_DEFAULT
        set(value) {
            prefs?.save(WIFI, value)
        }
    var SP_DATA_LIMIT: Float
        get() = prefs?.getFloat(DATA_LIMIT, DATA_LIMIT_DEFAULT) ?: DATA_LIMIT_DEFAULT
        set(value) {
            prefs?.save(DATA_LIMIT, value)
        }
    var SP_PLAYBACK_SPEED: Float
        get() = prefs?.getFloat(PLAYBACK_SPEED, PLAYBACK_SPEED_DEFAULT) ?: PLAYBACK_SPEED_DEFAULT
        set(value) {
            prefs?.save(PLAYBACK_SPEED, value)
        }
    var SP_PIP: Boolean
        get() = prefs?.getBoolean(PIP, PIP_DEFAULT) ?: PIP_DEFAULT
        set(value) {
            prefs?.save(PIP, value)
        }

    var SP_COURSE_FILTER_TYPE: Int
        get() = prefs?.getInt("COURSE_FILTER_TYPE", COURSE_FILTER_TYPE) ?: COURSE_FILTER_TYPE
        set(value) {
            prefs?.save("COURSE_FILTER_TYPE", value)
        }

    companion object {
        const val PREFS_FILENAME = "com.codingblocks.cbonline.app.prefs"
        const val ACCESS_TOKEN = "access_token"
        const val JWT_TOKEN = "jwt_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val USER_IMAGE = "user_image"
        const val ONEAUTH_ID = "oneauth_id"
        const val USER_ID = "user_id"
        const val USER_NAME = "user_name"
        const val ROLE_ID = "role_id"
        const val EMAIL_ID = "email_id"
        const val ROLE_ID_DEFAULT = 0
        const val COURSE_FILTER_TYPE = 0

        const val WIFI = "wifi"
        const val WIFI_DEFAULT = false
        const val DATA_LIMIT = "data_limit"
        const val DATA_LIMIT_DEFAULT = 1.0f
        const val PLAYBACK_SPEED = "playback_speed"
        const val PLAYBACK_SPEED_DEFAULT = 1f

        const val PIP = "pip"
        const val PIP_DEFAULT = false
        private var prefs: SharedPreferences? = null
        private var instance: PreferenceHelper = PreferenceHelper()

        fun getPrefs(context: Context): PreferenceHelper {
            if (prefs == null) {
                prefs = context.getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
            }
            return instance
        }
    }
}
