Kitchensink-Android
===================

This application provides an introduction to Version 2 of Kinvey's Android library.

Try features such as:

* Allow users to sign up and log in.
* See AppData functionality in action, by saving and retrieving entities.
* Upload and Download text files through our File API.
* Try out Push notifications.


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
![app.key and app.secret]()




##License


Copyright (c) 2013 Kinvey Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License
is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
or implied. See the License for the specific language governing permissions and limitations under
the License.