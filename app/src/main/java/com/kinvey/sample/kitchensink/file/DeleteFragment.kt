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
package com.kinvey.sample.kitchensink.file

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.kinvey.android.callback.KinveyDeleteCallback
import com.kinvey.android.store.FileStore
import com.kinvey.java.model.FileMetaData
import com.kinvey.java.store.StoreType
import com.kinvey.sample.kitchensink.Constants
import com.kinvey.sample.kitchensink.R.layout
import com.kinvey.sample.kitchensink.UseCaseFragment
import kotlinx.android.synthetic.main.feature_file_delete.*
import timber.log.Timber
import java.io.File
import java.io.IOException

/**
 * @author m0rganic
 * @since 2.0
 */
class DeleteFragment : UseCaseFragment(), OnClickListener {

    private var fileStore: FileStore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileStore = client?.getFileStore(StoreType.AUTO)
    }

    override val viewID: Int
        get() = layout.feature_file_delete

    override fun initViews(v: View) {
        fileDeleteBtn?.setOnClickListener(this)
    }

    override val title = "Delete"

    private val target: File
        private get() = File(activity?.filesDir, Constants.FILENAME)

    private fun delete() {
        val meta = FileMetaData(Constants.FILENAME)
        meta.id = Constants.FILENAME
        try {
            fileStore?.remove(meta, object : KinveyDeleteCallback {
                override fun onSuccess(integer: Int?) {
                    val file = target
                    if (file.exists()) {
                        file.delete()
                    }
                    Timber.i("deleted " + file.name + " file.")
                    Toast.makeText(activity, "Delete finished.", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(error: Throwable) {
                    Timber.e(error)
                    Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
                }
            })
        } catch (e: IOException) {
            Timber.e(e)
        }
    }

    override fun onClick(v: View) {
        if (v === fileDeleteBtn) { delete() }
    }
}