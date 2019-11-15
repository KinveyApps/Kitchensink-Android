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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kinvey.android.Client
import com.kinvey.android.model.User

/**
 * @author edwardf
 * @since 2.0
 */
abstract class UseCaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View? {
        return inflater.inflate(viewID, group, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    override fun onResume() {
        super.onResume()
        populateViews()
    }

    /**
     * @return the ID defined as  R.layout.* for this specific use case fragment.  If you are adding a new fragment, add a new layout.xml file and reference it here.
     */
    abstract open val viewID: Int

    /**
     * In this method establish all references to View widgets within the layout.
     *
     * For example:
     *
     * TextView mytext = (TextView) v.findViewById(R.id.mytextview);
     *
     * This is called once from onCreate.
     *
     * @param v  the View object inflated by the Fragment, this will be the parent of any View Widget within the fragment.
     */
    abstract fun initViews(v: View)

    /**
     * In this method populate the view objects.  This is called from onResume, to ensure that the data displayed is at least refreshed when the fragment is resumed.
     *
     * This method is optional.
     *
     * For example:
     *
     * mytext.setText("hello" + user.getName());
     */
    open fun populateViews() {}

    /**
     *
     * @return a String to display as the title of this fragment, used by viewpager indicator
     */
    abstract open val title: String?

    val applicationContext: App?
        get() = (activity as FeatureActivity?)?.applicationContext

    open val client: Client<User>?
        get() = (activity as FeatureActivity?)?.client
}