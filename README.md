# MyTweet 2 Android
![Nav Drawer][nav] ![TimeLine][timeline] ![Maps][map]

MyTweet 2 android is a continutation of the first Android assignment which provides addtional functionality and communicates with a web application equivalent.

## Addiational Functionality
* Following/Follower
  * Allow user to follow / unfollow another user
  * Tweets from following users are merged into user timeline view
* Images
  * User can upload profile image
  * User can delete profile image
  * User can also add image to tweet
* Maps / Location - Google Map API
  * Tweets now include location of device
  * User tweets with markers can be view and plotted into map
* Services - Depending on shared preference setting value
  * Refresh user tweet listing 
  * Refresh user following listing

## Web App Integration
The Android application aims to provide complete intregation with the web application equivalent. All actions completed by the user is also reflected on the web application. This is achieved by having the android communicate with the API of the web server application. 

The web server application github can be found at: https://github.com/KevFan/myTweet2-ent-web
with deployed server application at: http://34.242.209.100/

A client single page application for the web server can also be found at github: https://github.com/KevFan/mytweet2-aurelia-client-ts 
with the deployed application at http://kevfans3.s3-website-eu-west-1.amazonaws.com/#/

## Persistance Approach
Persistance achieved in the android application is largely achieved by saving the data to the web application mongo database if connected online. The user would then pull the latest data from the server. 

However, if the user if offline, the user token, tweet list, follower list and following list is also persisted offline onto the device using Gson. The application would load from these saved files to provide some offline access to the application.

## Git Approach
A git branching model was used in this project. The project would contain two main branches, the master branch would contain the latest stable release, and a development branch for the development of new features. For the developement of new features, an appropriate feature branch would branch from the development branch and merged back on completion. When enough features are completed, the development branch is merged back into the master branch, which will always signal a new release.

The github for the project can be found at the following link: 
https://github.com/KevFan/myTweetv2-android

## UX/DX
The UX adopts an approach to use mainly the navigation drawer for navigation and available user actions. A profile fragment with a pager view is provided for listing of the user tweets, followers and followings, which also provide a profile activty for viewing of other user profiles. The UX aims to provide a common color scheme throughout the application and provide user interactions that are intuitive.

The DX approach adopted aims to provide as few code duplications as possible. Fragments were used where possible to allow the fragments be reused where possible. The code itself is all commented for documentation and seperated into appropriate packages. 

## Building Instructions
If building your own apk of the application, API keys are not provided so you should generate and provide you own google-services.json into the `app` directory for use of google services, and also put in your Google Maps API key into your manfiest file of the application or as a string resource. If these are not provided, the google maps functionality would not work as intended.

## Authors:
Kevin Fan ([KevFan](https://github.com/KevFan)) - 20077688

## Version/Date:
7th January 2018

[nav]: ./readme_resources/navdrawer.jpg
[timeline]: ./readme_resources/timeline.jpg
[map]: ./readme_resources/maps.jpg
