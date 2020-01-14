package com.kinvey.sample.kitchensink

import com.kinvey.android.Client
import com.kinvey.android.model.User
import com.kinvey.java.Query

object KinveyUtils {

    fun getDeleteAllQuery(client: Client<User>?): Query? {
        val query = client?.query()
        return query?.notEqual("age", "100500")
    }
}