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
package com.kinvey.sample.kitchensink.custom

import android.view.View
import android.view.View.OnClickListener
import com.google.api.client.json.GenericJson
import com.kinvey.android.callback.KinveyListCallback
import com.kinvey.sample.kitchensink.R
import com.kinvey.sample.kitchensink.UseCaseFragment
import kotlinx.android.synthetic.main.feature_endpoint_basic.*

/**
 * @author edwardf
 * @since 2.0
 */
class EndpointFragment : UseCaseFragment(), OnClickListener {

    override fun onClick(v: View) {
        if (v === tryItBtn) { hitTheEndpoint() }
    }

    override val viewID = R.layout.feature_endpoint_basic

    override val title = "Endpoints"

    override fun bindViews(v: View) {
        tryItBtn?.setOnClickListener(this)
    }

    private fun hitTheEndpoint() {
        val endpoints = client?.customEndpoints<GenericJson, GenericJson>(GenericJson::class.java)
        endpoints?.callEndpoint("doit", GenericJson(),
        object : KinveyListCallback<GenericJson> {
            override fun onSuccess(result: List<GenericJson>) {
                if (result == null) {
                    endpointResultText?.text = "nope, got null back!"
                } else {
                    endpointResultText?.text = result[0].toString()
                }
            }
            override fun onFailure(error: Throwable) {
                endpointResultText?.text = "Uh oh -> $error"
            }
        })
    }
}