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
import com.kinvey.android.callback.AsyncDownloaderProgressListener
import com.kinvey.android.store.FileStore
import com.kinvey.java.core.MediaHttpDownloader
import com.kinvey.java.model.FileMetaData
import com.kinvey.java.store.StoreType
import com.kinvey.sample.kitchensink.Constants
import com.kinvey.sample.kitchensink.R
import com.kinvey.sample.kitchensink.UseCaseFragment
import kotlinx.android.synthetic.main.feature_file_download.*
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author m0rganic
 * @since 2.0
 */
class DownloadFragment : UseCaseFragment(), OnClickListener {

    private var fileStore: FileStore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileStore = client?.getFileStore(StoreType.AUTO)
    }

    override val viewID = R.layout.feature_file_download

    override val title = "Download"

    override fun bindViews(v: View) {
        fileDownloadBtn?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v === fileDownloadBtn) {
            try {
                fileDownloadProgressText?.text = null
                download(target)
            } catch (e: IOException) {
                boom(e)
            }
        }
    }

    private fun boom(e: Exception) {
        Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show()
        Timber.e("Exception downloading file")
    }

    private val target: File
        private get() = (File(activity?.filesDir, Constants.FILENAME))

    @Throws(IOException::class)
    private fun download(target: File) {
        val fos = FileOutputStream(target)
        // call kinvey specific task to perform download
        val meta = FileMetaData(Constants.FILENAME)
        meta.id = Constants.FILENAME
        fileStore?.download(meta, fos, object : AsyncDownloaderProgressListener<FileMetaData> {
            override fun onSuccess(fileMetaData: FileMetaData) {
                Timber.i("successfully download: ${target.name} file.")
                Toast.makeText(activity, "Download finished.", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(error: Throwable) {
                Timber.e("failed to download: ${target.name} file.")
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
            }

            override fun onCancelled() {}
            override val isCancelled: Boolean
                get() = false

            @Throws(IOException::class)
            override fun progressChanged(downloader: MediaHttpDownloader) {
                Timber.i("progress updated: ${downloader.downloadState}")
                val state = downloader.downloadState.toString()
                activity?.runOnUiThread {
                    fileDownloadProgressText?.text =
                        if ((fileDownloadProgressText?.text == null)) state
                        else fileDownloadProgressText?.text.toString() + "\n" + state
                }
            }
        })
    }
}