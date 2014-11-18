Kitchensink-Android
===================

This application provides a compilation of various use-cases for Kinvey's Android library.

Try features such as:

* Allow users to sign up and log in.
* See AppData functionality in action, by saving and retrieving entities.
* Upload and Download text files through our File API.
* Try out Push notifications.
* Set up a Custom Endpoint and use it
* Perform User lookup and modify the current User


## Set up Kitchensink Project

1. Download the [Kitchensink](https://github.com/KinveyApps/StatusShare-Android/archive/master.zip) project.
2. Download the [ActionBar Sherlock Library](http://actionbarsherlock.com/)
3. Download the [ViewPager Indicator Library](http://viewpagerindicator.com/) 
4. In Eclipse, go to __File &rarr; Import…__
5. Click __Android &rarr; Existing Android Code into Workspace__
6. __Browse…__ to set __Root Directory__ to the extracted zip from step 1
7. Repeat steps 4 - 7 for the zip from step 2 and 3 as well.
7. In the __Projects__ box, make sure the __HomeActivity__ project check box, the __library__ project from Action Bar Sherlock, and the __library__ project from ViewPagerIndicator are selected. Then click __Finish__.
8. Specify your app key and secret in `assets/kinvey.properties` constant variables
`app.key` and `app.secret`

##Set up a Custom Endpoint

One of the features used in this application is a Custom Endpoint, which must be created on your backend.  The Custom Endpoint used in this sample application just returns a hard-coded String, however [take a look at the business logic guide](http://devcenter.kinvey.com/android/guides/business-logic) for more information about what can be accomplished with Custom Endpoints.

1.  Visit [the Kinvey console, and login to your application](http://console.kinvey.com) and go to the Custom Endpoint add-on

    ![Custom Endpoint add-on](https://raw.github.com/KinveyApps/Kitchensink-Android/master/assets/android-oauth1-tutorial-custom.png)

2.  Create a new Custom Endpoint, named `doit`.  

3.  Paste the following code into the javascript editor:

    ```
    function onRequest(request, response, modules){  
        response.body = [{"resp": "Hit a custom Endpoint!"}];
        response.complete(201);
    }
    ```
    
4.  The class `com.kinvey.sample.kitchensink.custom.EndpointFragment` consumes this Endpoint, so make sure the first parameter of `callEndpoint(…)` matches the name of the Custom Endpoint (in this case, `doit`).
 
 
##Configure Push Notifications
 
Push Notifications are tied to the application's package context, so you will have to change the package declaration to configure push notifications.


1.  Perform a global find and replace in your editor of choice replacing `com.kinvey.sample.kitchensink` with a unique package name.  Note this must be done in the Manifest, and in all classes.  If you use an automated refactor tool, ensure that all instances of the string have been replaced.

2.  [Take a look at our Push Configuration guide, and register for Push notifications](http://devcenter.kinvey.com/android/guides/push) to get started!
  
##License


Copyright (c) 2014 Kinvey Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License
is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
or implied. See the License for the specific language governing permissions and limitations under
the License.