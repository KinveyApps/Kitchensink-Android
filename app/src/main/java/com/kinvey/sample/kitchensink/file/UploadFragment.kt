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

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager

import android.widget.Toast
import com.kinvey.android.callback.AsyncUploaderProgressListener
import com.kinvey.android.store.FileStore
import com.kinvey.java.core.MediaHttpUploader
import com.kinvey.java.model.FileMetaData
import com.kinvey.java.store.StoreType
import com.kinvey.sample.kitchensink.Constants
import com.kinvey.sample.kitchensink.R
import com.kinvey.sample.kitchensink.UseCaseFragment
import kotlinx.android.synthetic.main.feature_file_upload.*
import timber.log.Timber
import java.io.*

/**
 * @author m0rganic
 * @since 2.0
 */
class UploadFragment : UseCaseFragment(), OnClickListener {

    private var fileStore: FileStore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileStore = client?.getFileStore(StoreType.AUTO)
    }

    override val viewID = R.layout.feature_file_upload

    override val title = "Upload"

    override fun bindViews(v: View) {
        fileUploadBtn?.setOnClickListener(this)
    }

    override fun populateViews() {
        LoadTask().execute(target)
    }

    override fun onClick(v: View) {
        if (v === fileUploadBtn) {
            fileUploadProgressText?.text = null
            SaveTask(fileUploadContentsEdit?.text.toString(), target).execute()
            hideKeyboard(v)
        }
    }

    private val target: File
        private get() = (File(activity?.filesDir, Constants.FILENAME))

    private fun showException(e: Exception?) {
        Toast.makeText(activity, e?.message, Toast.LENGTH_SHORT).show()
        Timber.e(e)
    }

    @Throws(IOException::class)
    private fun save(text: String, target: File) {
        val fos = FileOutputStream(target)
        val out = OutputStreamWriter(fos)
        out.write(text)
        out.flush()
        fos.fd.sync()
        out.close()
    }

    private fun hideKeyboard(v: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(fileUploadContentsEdit?.windowToken, 0)
    }

    @Throws(IOException::class)
    private fun load(target: File): String {
        var result = ""
        try {
            val inStr: InputStream = FileInputStream(target)
            val tmp = InputStreamReader(inStr)
            val reader = BufferedReader(tmp)
            var str: String
            val buf = StringBuilder()
            while ((reader.readLine().also { str = it }) != null) {
                buf.append(str + "\n")
            }
            inStr.close()
            result = buf.toString()
        } catch (e: FileNotFoundException) {
            // that's OK, we probably haven't created it yet
            Timber.e(e)
        }
        return (result)
    }

    private inner class LoadTask : AsyncTask<File, Void?, String>() {

        private var e: Exception? = null

        override fun doInBackground(vararg args: File): String {
            var result = ""
            try {
                result = load(args[0])
            } catch (e: Exception) {
                this.e = e
            }
            return (result)
        }

        override fun onPostExecute(text: String) {
            if (e == null) {
                fileUploadContentsEdit?.setText(text)
            } else {
                showException(e)
            }
        }
    }

    private inner class SaveTask internal constructor(private val text: String, private val target: File)
        : AsyncTask<Void, Void, Void?>() {

        private val saveError: Exception? = null

        override fun doInBackground(vararg args: Void): Void? {
            try {
                save(text, target)
                //Call the kinvey specific task to perform upload
                val meta = FileMetaData(target.name)
                meta.id = target.name
                fileStore?.upload(target, meta, object : AsyncUploaderProgressListener<FileMetaData?> {
                    override fun onSuccess(fileMetaData: FileMetaData?) {
                        Timber.i("successfully upload: ${target.length()} byte file.")
                        Toast.makeText(activity, "Upload finished.", Toast.LENGTH_SHORT).show()
                    }
                    override fun onCancelled() {}
                    override val isCancelled = false
                    override fun onFailure(error: Throwable) {
                        Timber.e("failed to upload: ${target.length()} byte file. Error: $error")
                        showException(error as Exception)
                    }
                    @Throws(IOException::class)
                    override fun progressChanged(uploader: MediaHttpUploader) {
                        Timber.i("upload progress: ${uploader.uploadState}")
                        val state = uploader.uploadState.toString()
                        activity?.runOnUiThread {
                            fileUploadProgressText?.text =
                                if ((fileUploadProgressText?.text == null)) state
                                else fileUploadProgressText?.text.toString() + "\n" + state
                        }
                    }
                })
            } catch (e: Exception) {
                showException(e)
            }
            return (null)
        }
    }
}