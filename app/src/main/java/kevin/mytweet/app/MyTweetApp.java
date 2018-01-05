package kevin.mytweet.app;

import android.app.Application;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import kevin.mytweet.activities.HomeActivity;
import kevin.mytweet.models.Follow;
import kevin.mytweet.models.Token;
import kevin.mytweet.models.Tweet;
import kevin.mytweet.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;
import static kevin.mytweet.helpers.SaveLoadHelper.saveToken;

/**
 * MyTweetApp - main application
 * Created by kevin on 12/10/2017.
 */

public class MyTweetApp extends Application implements Callback<Token> {
  public MyTweetService tweetService;
  public MyTweetServiceOpen tweetServiceOpen;
  public boolean tweetServiceAvailable = false;
  public List<User> users = new ArrayList<>();
  public List<Follow> followers = new ArrayList<>();
  public List<Follow> followings = new ArrayList<>();
  public List<Tweet> timeLine = new ArrayList<>();
  public User currentUser = null;
  protected static MyTweetApp app;


  /* Client used to interact with Google APIs. */
  public GoogleApiClient mGoogleApiClient;
  public Location mCurrentLocation;

  /**
   * Called when application is first created
   * If the last previous logged in user, haven't logged out, their details are still in shared
   * preferences, try to log the user in instead of starting the welcome activity
   */
  @Override
  public void onCreate() {
    super.onCreate();
    info("MyTweet App Started");
    app = this;
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .build();
    mGoogleApiClient.connect();
    sendBroadcast(new Intent("kevin.mytweet.receivers.SEND_BROADCAST"));
    tweetServiceOpen = RetrofitServiceFactory.createService(MyTweetServiceOpen.class);
  }

  /**
   * Static method to return the application object
   *
   * @return Application object
   */
  public static MyTweetApp getApp() {
    return app;
  }


//
//  /**
//   * Sets shared preference values to current user
//   */
//  public void setPreferenceSettings() {
//    info("MyTweetApp - setting shared preference to current user");
//    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//    SharedPreferences.Editor editor = prefs.edit();
//    editor.putString("firstName", currentUser.firstName);
//    editor.putString("lastName", currentUser.lastName);
//    editor.putString("email", currentUser.email);
//    editor.putString("password", currentUser.password);
//    editor.apply();
//  }

  public void addTweet(Tweet tweet) {
    timeLine.add(tweet);
  }

  /**
   * Get tweet from ArrayList of tweets
   *
   * @param id Id of the tweet
   * @return Tweet matching with ID
   */
  public Tweet getTweet(String id) {
    for (Tweet tweet : timeLine) {
      if (tweet._id.equals(id)) {
        return tweet;
      }
    }

    return null;
  }

  /**
   * Delete tweet from the ArrayList of tweets
   *
   * @param tweet Tweet to remove
   */
  public void deleteTweet(Tweet tweet) {
    timeLine.remove(tweet);
  }

  public void validUser(String email, String password) {
    User user = new User("", "", email, password);
    info(user.email + " " + user.password);
    Call<Token> call = (Call<Token>) tweetServiceOpen.authenticate(user);
    call.enqueue(this);
  }

  @Override
  public void onResponse(Call<Token> call, Response<Token> response) {
    Token auth = response.body();
    if (auth.user != null) {
      saveToken(this, auth); // Save the Token
      currentUser = auth.user;
      tweetService = RetrofitServiceFactory.createService(MyTweetService.class, auth.token);
      info("Authenticated " + currentUser.firstName + ' ' + currentUser.lastName);
      startActivity(new Intent(this, HomeActivity.class));
    } else {
      info("Unauthenticated - invalid credentials");
      toastMessage(this, "Email/password invalid :(");
    }
  }

  @Override
  public void onFailure(Call<Token> call, Throwable t) {
    toastMessage(this, "Unable to authenticate with Tweet Service");
    info("Failed to Authenticated!");
  }
}
