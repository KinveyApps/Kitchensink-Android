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
package com.kinvey.sample.kitchensink.appData

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.kinvey.android.callback.KinveyReadCallback
import com.kinvey.android.store.DataStore
import com.kinvey.java.Query
import com.kinvey.java.model.KinveyReadResponse
import com.kinvey.java.store.StoreType
import com.kinvey.sample.kitchensink.Constants
import com.kinvey.sample.kitchensink.MyEntity
import com.kinvey.sample.kitchensink.R
import com.kinvey.sample.kitchensink.R.layout
import com.kinvey.sample.kitchensink.UseCaseFragment
import kotlinx.android.synthetic.main.feature_appdata_query.*

/**
 * @author edwardf
 * @since 2.0
 */
class QueryFragment : UseCaseFragment(), OnClickListener {

    private var entityStore: DataStore<MyEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entityStore = DataStore.collection(Constants.COLLECTION_NAME, MyEntity::class.java, StoreType.AUTO, client)
    }

    override val title = "Query"

    override val viewID = layout.feature_appdata_query

    override fun bindViews(v: View) {
        queryCurrentUserBtn.setOnClickListener(this)
        queryNotCurrentUserBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.queryCurrentUserBtn -> queryForCurrent()
            R.id.queryNotCurrentUserBtn -> queryForNotCurrent()
        }
    }

    private fun queryForCurrent() {
        val q = Query()
        q.equals("_acl.creator", client?.activeUser?.id)
        executeQueryAndUpdateView(q)
    }

    private fun queryForNotCurrent() {
        val q = Query()
        q.notEqual("_acl.creator", client?.activeUser?.id)
        executeQueryAndUpdateView(q)
    }

    private fun executeQueryAndUpdateView(q: Query) {
        entityStore?.find(q, object : KinveyReadCallback<MyEntity> {
            override fun onSuccess(result: KinveyReadResponse<MyEntity>?) {
                Toast.makeText(activity, "got " + result?.result?.size.toString() + " results!", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(error: Throwable) {
                Toast.makeText(activity, "something went wrong -> " + error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}