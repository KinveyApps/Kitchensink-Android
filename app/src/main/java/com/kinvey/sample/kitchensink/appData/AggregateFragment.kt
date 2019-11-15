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
import com.kinvey.android.store.DataStore
import com.kinvey.java.Query
import com.kinvey.java.core.KinveyAggregateCallback
import com.kinvey.java.model.AggregateType
import com.kinvey.java.model.Aggregation
import com.kinvey.java.store.StoreType
import com.kinvey.sample.kitchensink.*
import kotlinx.android.synthetic.main.feature_appdata_aggregate.*
import java.util.*

/**
 * @author edwardf
 * @since 2.0
 */
class AggregateFragment : UseCaseFragment(), OnClickListener {

    private var entityStore: DataStore<MyEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entityStore = DataStore.collection(Constants.COLLECTION_NAME, MyEntity::class.java, StoreType.AUTO, client)
    }

    override val viewID = R.layout.feature_appdata_aggregate

    override fun bindViews(v: View) {
        aggCountBtn?.setOnClickListener(this)
        aggSumBtn?.setOnClickListener(this)
        aggMinBtn.setOnClickListener(this)
        aggMaxBtn?.setOnClickListener(this)
        aggAverageBtn?.setOnClickListener(this)
    }

    override val title: String
        get() = "Aggregates"

    private fun performCount(fields: ArrayList<String>, q: Query) {
        entityStore?.group(AggregateType.COUNT, fields, null, q, object : KinveyAggregateCallback() {
            override fun onFailure(error: Throwable) {
                Toast.makeText(activity, "something went wrong -> " + error.message, Toast.LENGTH_SHORT).show()
            }
            override fun onSuccess(res: Aggregation) {
                Toast.makeText(activity, "got: " + res.res!!.size, Toast.LENGTH_SHORT).show()
            }
        }, null)
    }

    private fun performSum(fields: ArrayList<String>, q: Query) {
        entityStore?.group(AggregateType.SUM, fields, "aggregateField", q, object : KinveyAggregateCallback() {
            override fun onFailure(error: Throwable) {
                Toast.makeText(activity, "something went wrong -> " + error.message, Toast.LENGTH_SHORT).show()
            }
            override fun onSuccess(res: Aggregation) {
                Toast.makeText(activity, "got: " + res.res!!.size, Toast.LENGTH_SHORT).show()
            }
        }, null)
    }

    private fun performMin(fields: ArrayList<String>, q: Query) {
        entityStore?.group(AggregateType.MIN, fields, "aggregateField", q, object : KinveyAggregateCallback() {
            override fun onFailure(error: Throwable) {
                Toast.makeText(activity, "something went wrong -> " + error.message, Toast.LENGTH_SHORT).show()
            }
            override fun onSuccess(res: Aggregation) {
                Toast.makeText(activity, "got: " + res.res!!.size, Toast.LENGTH_SHORT).show()
            }
        }, null)
    }

    private fun performMax(fields: ArrayList<String>, q: Query) {
        entityStore?.group(AggregateType.MAX, fields, "aggregateField", q, object : KinveyAggregateCallback() {
            override fun onFailure(error: Throwable) {
                Toast.makeText(activity, "something went wrong -> " + error.message, Toast.LENGTH_SHORT).show()
            }
            override fun onSuccess(res: Aggregation) {
                Toast.makeText(activity, "got: " + res.res!!.size, Toast.LENGTH_SHORT).show()
            }
        }, null)
    }

    private fun performAverage(fields: ArrayList<String>, q: Query) {
        entityStore?.group(AggregateType.AVERAGE, fields, "aggregateField", q, object : KinveyAggregateCallback() {
            override fun onFailure(error: Throwable) {
                Toast.makeText(activity, "something went wrong -> " + error.message, Toast.LENGTH_SHORT).show()
            }
            override fun onSuccess(res: Aggregation) {
                Toast.makeText(activity, "got: " + res.res!!.size, Toast.LENGTH_SHORT).show()
            }
        }, null)
    }

    override fun onClick(v: View) {
        val fields = ArrayList<String>()
        fields.add("_acl.creator")
        val q = Query()
        when (v.id) {
            R.id.aggCountBtn -> performCount(fields, q)
            R.id.aggSumBtn -> performSum(fields, q)
            R.id.aggMinBtn -> performMin(fields, q)
            R.id.aggMaxBtn -> performMax(fields, q)
            R.id.aggAverageBtn -> performAverage(fields, q)
        }
    }
}