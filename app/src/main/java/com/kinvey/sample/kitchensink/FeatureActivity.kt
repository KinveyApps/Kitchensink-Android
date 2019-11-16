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
package com.kinvey.sample.kitchensink

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_feature.*

/**
 * @author edwardf
 * @since 2.0
 */
abstract class FeatureActivity : BaseAppCompatActivity() {

    private var mFragments: List<UseCaseFragment>? = arrayListOf()
    private var mAdapter: FeatureAdapter? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature)
    }

    public override fun onResume() {
        super.onResume()
        populateViews()
    }

    private fun populateViews() {
        mFragments = fragments
        mAdapter = FeatureAdapter(mFragments ?: arrayListOf(), supportFragmentManager)
        featurePager?.adapter = mAdapter
        featureIndicator?.setViewPager(featurePager)
        featurePager?.offscreenPageLimit = 0
    }

    override fun getApplicationContext(): App? {
        return App.instance
    }

    abstract val fragments: List<UseCaseFragment>?

    private class FeatureAdapter(val mFragments: List<UseCaseFragment>, fm: FragmentManager)
        : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount() = mFragments.size

        override fun getPageTitle(position: Int): String? {
            return mFragments[position].title
        }
    }
}