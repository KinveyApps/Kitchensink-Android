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
package com.kinvey.sample.kitchensink.push;

import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.kinvey.android.push.KinveyFCMService;
import com.kinvey.sample.kitchensink.App;
import com.kinvey.sample.kitchensink.R;

import timber.log.Timber;

public class FCMService extends KinveyFCMService {
    @Override
    public void onMessage(String message) {
        Timber.i("GCM - onMessage: " + message);
        displayNotification(message);
    }

    private void displayNotification(String message) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(App.getInstance())
            .setSmallIcon(R.drawable.icon_small)
            .setContentTitle(App.getInstance().getResources().getString(R.string.app_name))
            .setContentText(message);
        NotificationManager mNotificationManager =
        (NotificationManager) App.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
}