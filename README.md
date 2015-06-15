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
4. Download the latest Kinvey library (zip) and extract the downloaded zip file, from: http://devcenter.kinvey.com/android/downloads



###Eclipse
1. In Eclipse, go to __File &rarr; Import…__
2. Click __Android &rarr; Existing Android Code into Workspace__
3. __Browse…__ to set __Root Directory__ to the extracted zip from step 1
4. Repeat steps 4 - 7 for the zip from step 2 and 3 as well.
5. In the __Projects__ box, make sure the __HomeActivity__ project check box, the __library__ project from Action Bar Sherlock, and the __library__ project from ViewPagerIndicator are selected. Then click __Finish__.
6. Copy all jars in the **libs/** folder of the Kinvey Android library zip to TestDrive's **libs/** folder on the file system

###Android Studio
1. In Android Studio, go to **File $rarr; New %rarr; Import Project**
2. **Browse** to the extracted zip from step 1, and click **OK**
3. Click **Next** and **Finish**.
4. Repeat Steps 1-3 for ActionBarSherlock, as well as ViewPagerIndicator.
4. **Browse** to the location of your project, and create a new folder called **lib** inside the **app** directory
5. Copy all jars in the **libs/** folder of the Kinvey Android library zip to the **lib/** folder you just created
6. Expand **Gradle Scripts** in the **Project** Window, and select `build.gradle(Module:app)`.
7. Modify `dependencies` section, leaving any existing dependencies in place and replacing the `x.x.x` with the correct version number

```java
dependencies {    
    compile files('lib/google-http-client-1.19.0.jar')
    compile files('lib/google-http-client-android-1.19.0.jar')
    compile files('lib/google-http-client-gson-1.19.0.jar')
    compile files('lib/google-http-client-jackson2-1.19.0.jar')
    compile files('lib/gson-2.1.jar')
    compile files('lib/guava-18.0.jar')
    compile files('lib/jackson-core-2.1.3.jar')
    compile files('lib/kinvey-android-lib-x.x.x.jar')
    compile files('lib/kinvey-java-x.x.x.jar')
}
```
    

8.  Click the **play** button to start a build, if you still see compilation errors ensure the versions are correctly defined in the dependencies list

###Finally, for all IDEs
7. Specify your app key and secret in `assets/kinvey.properties` constant variables
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