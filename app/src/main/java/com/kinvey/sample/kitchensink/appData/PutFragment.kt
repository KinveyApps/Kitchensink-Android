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
import android.widget.ArrayAdapter
import com.kinvey.android.callback.KinveyDeleteCallback
import com.kinvey.android.callback.KinveyReadCallback
import com.kinvey.android.store.DataStore
import com.kinvey.java.Query
import com.kinvey.java.core.KinveyClientCallback
import com.kinvey.java.model.KinveyReadResponse
import com.kinvey.java.store.StoreType
import com.kinvey.sample.kitchensink.*
import kotlinx.android.synthetic.main.feature_appdata_put.*
import java.util.*

/**
 * @author edwardf
 * @since 2.0
 */
class PutFragment : UseCaseFragment(), OnClickListener {

    private var addCount = 0
    private var ids: Array<String> = arrayOf()
    private var entityStore: DataStore<MyEntity>? = null
    private val random = Random()
    private val RANDOM_NAME_MAX = 10000
    private val RANDOM_AGGREGATE_MAX = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entityStore = DataStore.collection(Constants.COLLECTION_NAME, MyEntity::class.java, StoreType.AUTO, client)
    }

    override val viewID = R.layout.feature_appdata_put

    override val title = "Put"

    override fun initViews(v: View) {
        createBtn?.setOnClickListener(this)
        deleteAllBtn?.setOnClickListener(this)
        putCountSpinner?.adapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_dropdown_item, arrayOf("1", "2", "3"))
        putCountSpinner?.setSelection(0)
        putTotalCountText?.text = "0"
        count()
    }

    private fun putIt(howMany: Int) {
        addCount = 0
        if (100 < howMany + Integer.valueOf(putTotalCountText?.text.toString())) {
            AndroidUtil.toast(this@PutFragment, "Try something besides just creating new entities!  Delete some first.")
            return
        }
        val entities = generateEntities(howMany)
        entityStore?.save(entities, object : KinveyClientCallback<List<MyEntity>> {
            override fun onSuccess(result: List<MyEntity>) {
                addCount += result.size
                putTotalCountText?.text = (Integer.valueOf(putTotalCountText?.text.toString()) + 1).toString()
                if (addCount == howMany) {
                    AndroidUtil.toast(this@PutFragment, "Successfully saved $addCount")
                    putCountSpinner?.setSelection(0)
                }
                count()
            }
            override fun onFailure(error: Throwable) {
                AndroidUtil.toast(this@PutFragment, "something went wrong on put -> ${error.message}")
            }
        })
    }

    private fun generateEntities(howMany: Int): List<MyEntity> {
        return 1.rangeTo(howMany).map {
            val ent = MyEntity()
            ent.name = "name " + random.nextInt(RANDOM_NAME_MAX)
            ent.access.setGloballyWritable(true)
            ent.access.setGloballyReadable(true)
            ent.aggField = Random().nextInt(RANDOM_AGGREGATE_MAX)
            ent
        }
    }

    private fun deleteAll() {
        val q = KinveyUtils.getDeleteAllQuery(client) ?: return
        entityStore?.delete(q, object : KinveyDeleteCallback {
            override fun onSuccess(integer: Int?) {
                AndroidUtil.toast(activity, "deleted $integer entities!")
                count()
            }
            override fun onFailure(error: Throwable) {
                AndroidUtil.toast(activity, "something went wrong -> ${error.message}")
            }
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.createBtn -> putIt(Integer.valueOf(putCountSpinner?.selectedItem.toString()))
            R.id.deleteAllBtn -> deleteAll()
        }
    }

    private fun updateItemsCount(list: List<MyEntity>) {
        putTotalCountText?.text = list.size.toString()
    }

    private fun count() {
        entityStore?.find(object : KinveyReadCallback<MyEntity> {
            override fun onSuccess(result: KinveyReadResponse<MyEntity>?) {
                val list = result?.result ?: arrayListOf()
                updateItemsCount(list)
                ids = list.mapNotNull { it.id }.toTypedArray()
            }
            override fun onFailure(error: Throwable) {
                AndroidUtil.toast(activity, "something went wrong -> ${error.message}")
            }
        })
    }
}