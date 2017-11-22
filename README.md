# MyTweet Android
![Welcome Activity][welcome] ![TimeLine Activity][timeline] ![Welcome Activity][addtweet]

MyTweet android is a twitter/microblog app that allows users to write tweets with a max length of 140 characters. Each tweet are saved to the specific user timeline where the user has options to delete tweets by multi-long press select or all tweets through the clear option in the overflow menu. The user can select a contact and email the tweet content through a third party app such as Gmail.

## Project Approach
### Activity & CRUD
* BaseActivity
  * used to store a common codebase among the other activities, so they can extend from base activity to reduce code duplication, such as the navigate up functionality.
* WelcomeActivity
  * first activity shown on application launched if the last user has logged out. User can then go to the sign in actiivity or log in activity
* SignUpActivty
  * signs up a new user from the field texts, add the new user to the user list in the main app, saves the user details and sets the current user to the new user
* LoginActivity
  * validates the login credentials to the existing user list in the app and sets the current user to the user found. These activities bring the user to the TimeLineActivity.
* TimeLineActivity
  * pairs with the TimeLineFragment, listing the user tweets if any in a list view using a custom adapter. The fragment has a multichoice listener to delete the selected tweets. There is also a floating add tweet button that starts the add tweet/fragment pair to add a tweet. The timeline also has an overflow menu with the options to clear the timetime, deleting all tweets, settings to start the Settings activity/fragment pair to update user details and a logout option.
* DetailTweetPagerActivity
  * Pairs with the DetailTweetFragment and view page to detail the saved tweets from the timeline and allow user to swipe left and right between tweets
* AddTweetActivity
  * Pairs with the AddTweetFragemnt to add a new tweet
* SettingsActivity
  * Pairs with the SettingsFragment to use shared preference to update/maintain the current logged in user details

The MyTweetApp loads a list of users if myTweetData.json if present, and keeos track of the current user of during the application.

### Model & Persistence
There are 3 models used in this project:

* User - firstName, lastName, email, password, a timeline
* TimeLine - an array list of tweets
* Tweet - tweet message, tweet date

The database model is relatively simple with a user having a timeline which holds an arraylist of tweets.

Persistence was achieved by maintaining a locally stored lists of users achieved using GSon. User details are also persisted through shared preference so that they persist after the application is killed. This is used to automatically log in the last logged in user to if they haven't logged out so that the user does not need to enter their credentials at the login activity each time on starting the application.

### Settings/Permission
The app provides a settings activity/fragment pair using shared preferences that is used to update the details of the current signed in user.

The only permission this app requests/uses is the read contacts permission that is used in the detailTweet/addTweet activity/fragment pair to get the phone contact name/email to send the tweet content.

### UX/DX
The UX is designed from a minimal approach with a simple layout that is easy to navigate. The UI is currently only designed for a mobile view and in portrait mode. As such several activities are not made into fragments such as the login and signup actvity. Other activities pairs with a fragment such as the TimeLineFramgent. By this approach, the fragments can be easily re-used if additional functionality is added such a search functionality or if there is a UI change such as the addition of a nagivation drawer. Error checks are included within activities to prevent un-desired user error/scenarios such as having the same email used to sign up by multiple users. Dialog boxes are presented to users to confirms action that would lose data such as exiting the add tweet activity without saving.

The DX is designed to have miminal code duplication between activities and between fragments. The BaseActivity hold the navigate up functionality so activities that need this functionality can extend the BaseActivity. The BaseTweetFragment holds the functionality common between the AddTweetFragment and DetailTweetFragment which is the selectContact/emailTweet functionality to reduce code duplication. All code is commented and referenced where necessary. Tests for the models are included, however activity/fragments have no automated tests included.

## List of Software + Technologies Used
* [Android Studio](https://developer.android.com/studio/index.html) - IDE
* [Gson](https://github.com/google/gson) - Persistence by Object to Json serialisation
* [SonarLint](https://www.sonarlint.org/intellij/) - Java code analyser

## References
* Based heavily on [MyRent labs](https://wit-ictskills-2017.github.io/mobile-app-dev/labwall.html) from Android Development Module
* [Gson Serialisation](https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt)
* [Dialog Box](https://stackoverflow.com/questions/6413700/android-proper-way-to-use-onbackpressed-with-toast)
* [Round Button](https://stackoverflow.com/questions/7690416/android-border-for-button)

## Authors:
Kevin Fan ([KevFan](https://github.com/KevFan)) - 20077688

## Version/Date:
5th November 2017

[welcome]: ./readme_resources/welcome.jpg
[timeline]: ./readme_resources/timeline.jpg
[addtweet]: ./readme_resources/addtweet.jpg
