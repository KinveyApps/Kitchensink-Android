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
package com.kinvey.sample.kitchensink

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.multidex.MultiDex
import com.google.firebase.FirebaseApp
import com.kinvey.android.Client
import com.kinvey.android.Client.Builder
import com.kinvey.android.callback.KinveyUserCallback
import com.kinvey.android.model.User
import com.kinvey.sample.kitchensink.account.LoginActivity
import timber.log.Timber

/**
 * @author mjsalinger
 * @since 2.0
 */
class App : Application() {

    var client: Client<User>? = null

    // NOTE: When configuring push notifications you have to change the android package name of this app

    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseApp.initializeApp(this)
        client = Builder<User>(this.applicationContext)
            .setRetrieveUserCallback(object : KinveyUserCallback<User> {
                override fun onSuccess(result: User) {
                    Timber.i("ok, got success")
                    if (instance?.client == null) { return }
                    if (instance?.client?.isUserLoggedIn == false) {
                        val login = Intent(applicationContext, LoginActivity::class.java)
                        login.flags = login.flags or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(login)
                    }
                }
                override fun onFailure(error: Throwable) {
                    Timber.i("ok, got failure")
                    if (instance?.client == null) { return }
                    if (instance?.client?.isUserLoggedIn == false) {
                        val login = Intent(applicationContext, LoginActivity::class.java)
                        login.flags = login.flags or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(login)
                    }
                }
            }).build()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    companion object {
        var instance: App? = null
            private set
    }
}