/*
 * Copyright (c) 2019 Kinvey Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.kinvey.sample.kitchensink.user

import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.kinvey.android.Client
import com.kinvey.android.callback.KinveyUserCallback
import com.kinvey.android.callback.KinveyUserListCallback
import com.kinvey.android.model.User
import com.kinvey.android.store.UserStore
import com.kinvey.sample.kitchensink.R
import com.kinvey.sample.kitchensink.UseCaseFragment
import kotlinx.android.synthetic.main.feature_user_lookup.*

/**
 * @author edwardf
 */
class LookupFragment : UseCaseFragment(), OnClickListener {

    override fun initViews(v: View) {
        userLookupBtn.setOnClickListener(this)
        userLookupUpdateBtn.setOnClickListener(this)
    }

    override val viewID = R.layout.feature_user_lookup

    override val title = "Lookup"

    private fun performUserLookup() {
        val users = applicationContext?.client?.userDiscovery()
        val criteria = users?.userLookup()
        criteria?.lastName = "Smith"
        val lookupCriteria = criteria ?: return
        users.lookup(lookupCriteria, object : KinveyUserListCallback {
            override fun onSuccess(result: Array<User>) {
                Toast.makeText(activity, "lookup returned: ${result.size} user(s)!", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(error: Throwable) {
                Toast.makeText(activity, "lookup failed -> ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun performRetrievalWithReferences() {
        UserStore.retrieve(arrayOf("ref"), client as Client<User>, object : KinveyUserCallback<User> {
            override fun onSuccess(result: User) {
                Toast.makeText(activity, "got user!", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(error: Throwable) {
                Toast.makeText(activity, "failed -> ${error.message}", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        })
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.userLookupBtn -> performUserLookup()
            R.id.userLookupUpdateBtn -> performRetrievalWithReferences()
        }
    }
}