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

import com.kinvey.sample.kitchensink.appData.AppDataActivity
import com.kinvey.sample.kitchensink.custom.CustomEndpointActivity
import com.kinvey.sample.kitchensink.file.FileActivity
import com.kinvey.sample.kitchensink.push.PushActivity
import com.kinvey.sample.kitchensink.user.UserActivity
import java.util.*

/**
 * @author edwardf
 * @since 2.0
 */
object Loader {

    val featureList: List<Feature>
        get() {
            val featureList = ArrayList<Feature>()
            val appData = Feature("App Data", "App Data can be used to store and retrieve objects using Kinvey's BaaS", AppDataActivity::class.java)
            featureList.add(appData)
            val files = Feature("File", "Store large file, images or video", FileActivity::class.java)
            featureList.add(files)
            val push = Feature("Push", "Enable Push notifications using GCM", PushActivity::class.java)
            featureList.add(push)
            val customEndpoint = Feature("Custom Endpoints", "Define behavoir on you backend and run it from the client.", CustomEndpointActivity::class.java)
            featureList.add(customEndpoint)
            val user = Feature("User", "Store data associated with a user and perform user lookup operations", UserActivity::class.java)
            featureList.add(user)
            return featureList
        }

    //--------------class declaration of features
    class Feature(var name: String, var blurb: String, var activity: Class<*>)
}