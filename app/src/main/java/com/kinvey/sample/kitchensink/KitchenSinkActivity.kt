/**
 * Copyright (c) 2014 Kinvey Inc.
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
package com.kinvey.sample.kitchensink

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.api.client.http.HttpTransport
import com.kinvey.android.store.UserStore
import com.kinvey.java.AbstractClient
import com.kinvey.java.core.KinveyClientCallback
import com.kinvey.java.dto.BaseUser
import com.kinvey.sample.kitchensink.Loader.Feature
import com.kinvey.sample.kitchensink.account.LoginActivity
import kotlinx.android.synthetic.main.activity_kitchen_sink.*
import timber.log.Timber
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author edwardf
 * @since 2.0
 */
class KitchenSinkActivity : AppCompatActivity(), OnItemClickListener {

    private var mAdapter: FeatureAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kitchen_sink)
        bindViews()
        // run the following comamnd to turn on verbose logging:
        // adb shell setprop log.tag.HttpTransport DEBUG
        Logger.getLogger(HttpTransport::class.java.name).level = LOGGING_LEVEL
        val myClient = (application as App).client
        if (myClient?.isUserLoggedIn == false) {
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
            finish()
        }
    }

    private fun bindViews() {
        val featureList = Loader.featureList
        mAdapter = FeatureAdapter(this, featureList,
                (getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater))
        ksList?.adapter = mAdapter
        ksList?.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        val feature = Intent(this, mAdapter?.getItem(position)?.activity)
        startActivity(feature)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // depending on which option is tapped, act accordingly
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_login -> {
                val client = (applicationContext as App).client
                UserStore.logout(client as AbstractClient<BaseUser>, object : KinveyClientCallback<Void?> {
                    override fun onSuccess(result: Void?) {
                        login()
                    }
                    override fun onFailure(error: Throwable) {
                        Timber.e(error)
                    }
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun login() {
        val login = Intent(this, LoginActivity::class.java)
        startActivity(login)
    }

    /**
     *
     * This Adapter is used to maintain data and push_legacy individual row views to
     * the ListView object, note it constructs the Views used by each row and
     * uses the ViewHolder pattern.
     *
     */
    private inner class FeatureAdapter // NOTE: I pass an arbitrary textViewResourceID to the super
    // constructor-- Below I override
    // getView(...), which causes the underlying adapter to ignore this
    // field anyways, it is just needed in the constructor.
    (context: Context, objects: List<Feature?>, private val mInflater: LayoutInflater)
        : ArrayAdapter<Feature?>(context, R.layout.activity_feature, objects) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            var holder: FeatureViewHolder? = null
            var name: TextView? = null
            var blurb: TextView? = null
            val rowData = getItem(position)
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.row_feature_list, null)
                holder = FeatureViewHolder(convertView)
                convertView.tag = holder
            }
            holder = convertView?.tag as FeatureViewHolder
            name = holder?.name
            name?.text = rowData?.name
            blurb = holder.blurb
            blurb?.text = rowData?.blurb
            return convertView
        }

        /**
         * This pattern is used as an optimization for Android ListViews.
         *
         * Since every row uses the same layout, the View object itself can be
         * recycled, only the data/content of the row has to be updated.
         *
         * This allows for Android to only inflate enough Row Views to fit on
         * screen, and then they are recycled. This allows us to avoid creating
         * a new view for every single row, which can have a negative effect on
         * performance (especially with large lists on large screen devices).
         *
         */
        private inner class FeatureViewHolder(private val mRow: View) {
            private var tvName: TextView? = null
            private var tvBlurb: TextView? = null
            val name: TextView?
                get() {
                    if (null == tvName) {
                        tvName = mRow.findViewById<View>(R.id.row_feature_name) as TextView
                    }
                    return tvName
                }
            val blurb: TextView?
                get() {
                    if (null == tvBlurb) {
                        tvBlurb = mRow.findViewById<View>(R.id.row_feature_blurb) as TextView
                    }
                    return tvBlurb
                }
        }
    }

    companion object {
        private val LOGGING_LEVEL: Level? = Level.FINEST
        const val TAG = "KitchenSinkActivity"
    }
}