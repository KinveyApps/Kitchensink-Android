/**
 * Copyright (c) 2019 Kinvey Inc.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.kinvey.sample.kitchensink.push

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat.Builder
import com.kinvey.android.push.KinveyFCMService
import com.kinvey.sample.kitchensink.App
import com.kinvey.sample.kitchensink.R.drawable
import com.kinvey.sample.kitchensink.R.string
import timber.log.Timber

class FCMService : KinveyFCMService() {

    private val channelStr = FCMService::class.java.simpleName

    override fun onMessage(message: String?) {
        Timber.i("GCM - onMessage: $message")
        displayNotification(message)
    }

    private fun displayNotification(message: String?) {
        val mBuilder: Builder = Builder(baseContext, channelStr)
            .setSmallIcon(drawable.icon_small)
            .setContentTitle(App.instance?.resources?.getString(string.app_name))
            .setContentText(message)
        val mNotificationManager = App.instance?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build())
    }
}