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

import android.os.CountDownTimer
import android.view.View
import android.view.View.OnClickListener
import com.kinvey.sample.kitchensink.App
import com.kinvey.sample.kitchensink.R
import com.kinvey.sample.kitchensink.UseCaseFragment
import kotlinx.android.synthetic.main.feature_push.*

/**
 * @author mjsalinger
 * @since 2.0
 */
class PushFragment : UseCaseFragment(), OnClickListener {

    override fun initViews(v: View) {
        pushRegisterBtn.setOnClickListener(this)
        val pushEnabledStr = "$isPushEnabled"
        pushStatusText.text = pushEnabledStr
    }

    override val viewID = R.layout.feature_push

    override val title = "Push!"

    private val isPushEnabled: Boolean
        get() = client?.push(FCMService::class.java)?.isPushEnabled ?: false

    private fun initPush() {
        client?.push(FCMService::class.java)?.initialize(App.instance)
    }

    override fun onClick(v: View) {
        if (v === pushRegisterBtn) {
            registerPush()
        }
    }

    private fun registerPush() {
        initPush()
        //Not going to hook up intents for this sample, so just wait for a while before redrawing
        object : CountDownTimer(10000, 1000) {
            override fun onTick(miliseconds: Long) {}
            override fun onFinish() {
                //after 5 seconds update the status
                val pushEnabledStr = "$isPushEnabled"
                pushStatusText.text = pushEnabledStr
            }
        }.start()
    }
}