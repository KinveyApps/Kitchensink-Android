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

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.kinvey.android.callback.KinveyReadCallback
import com.kinvey.android.store.DataStore
import com.kinvey.java.core.KinveyClientCallback
import com.kinvey.java.model.KinveyReadResponse
import com.kinvey.java.store.StoreType
import com.kinvey.sample.kitchensink.*
import kotlinx.android.synthetic.main.feature_appdata_get.*

/**
 * @author edwardf
 * @since 2.0
 */
class GetFragment : UseCaseFragment(), OnClickListener {

    private var entityStore: DataStore<MyEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entityStore = DataStore.collection(Constants.COLLECTION_NAME, MyEntity::class.java, StoreType.AUTO, client)
    }

    override fun onResume() {
        super.onResume()
        count()
    }

    override val viewID = R.layout.feature_appdata_get

    override val title = "Get"

    override fun initViews(v: View) {
        getBtn?.setOnClickListener(this)
        getIdSpinner?.adapter = ArrayAdapter(activity!!, android.R.layout.simple_spinner_dropdown_item, arrayOf("--"))
        getIdSpinner?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (getIdSpinner?.selectedItem.toString() == "--") {
                    return
                }
                findSelectedItem(getIdSpinner?.selectedItem.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        }
    }

    private fun getIt() {
        count()
        if (getIdSpinner?.selectedItem == null) {
            return
        }
        findSelectedItem(getIdSpinner?.selectedItem.toString())
    }

    private fun findSelectedItem(id: String) {
        entityStore?.find(id, object : KinveyClientCallback<MyEntity> {
            override fun onSuccess(result: MyEntity) {
                getNameValueText?.text = result.name
                getIdValueText?.text = result.id
            }
            override fun onFailure(error: Throwable) {
                AndroidUtil.toast(this@GetFragment, "something went wrong on getEntity -> ${error.message}")
            }
        })
    }

    override fun onClick(v: View) {
        if (v === getBtn) { getIt() }
    }

    private fun updateSpinner(result: List<MyEntity>?) {
        if (activity == null) { return }
        val ids = result?.map { item -> item.id }?.toTypedArray() ?: arrayOf()
        getIdSpinner?.adapter = ArrayAdapter<String>(context as Context, android.R.layout.simple_spinner_dropdown_item, ids)
    }

    private fun count() {
        entityStore?.find(object : KinveyReadCallback<MyEntity> {
            override fun onSuccess(result: KinveyReadResponse<MyEntity>?) {
                getCurrentCountText?.text = result?.result?.size.toString()
                updateSpinner(result?.result)
            }
            override fun onFailure(error: Throwable) {
                AndroidUtil.toast(activity, "something went wrong on get-> ${error.message}")
            }
        })
    }
}