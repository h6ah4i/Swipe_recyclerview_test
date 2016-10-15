package com.avenger.avengerapp

import android.app.Application
import com.avenger.avengerapp.domain.models.OrderItem
import timber.log.Timber
import java.util.*

/**
 * Created by RobGThai on 10/7/16.
 */
class QsrApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
