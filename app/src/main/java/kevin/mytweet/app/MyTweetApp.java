package kevin.mytweet.app;

import android.app.Application;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
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
import static kevin.mytweet.helpers.SaveLoadHelper.loadFollowers;
import static kevin.mytweet.helpers.SaveLoadHelper.loadFollowings;
import static kevin.mytweet.helpers.SaveLoadHelper.loadTimeLine;
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
    // Set google api client to use location services
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .build();
    mGoogleApiClient.connect();

    tweetServiceOpen = RetrofitServiceFactory.createService(MyTweetServiceOpen.class);

    // If no internet connectivity - load timeline, followers, followings from saved jsons
    if (!isOnline()) {
      timeLine = loadTimeLine(this);
      followers = loadFollowers(this);
      followings = loadFollowings(this);
    } else {
      // Start broadcast for services to updating listings
      sendBroadcast(new Intent("kevin.mytweet.receivers.SEND_BROADCAST"));
    }
  }

  /**
   * Static method to return the application object
   *
   * @return Application object
   */
  public static MyTweetApp getApp() {
    return app;
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
   * Calls api to authenticate user based on email and password entered
   *
   * @param email    email of user
   * @param password password of user
   */
  public void validUser(String email, String password) {
    User user = new User("", "", email, password);
    info(user.email + " " + user.password);
    Call<Token> call = (Call<Token>) tweetServiceOpen.authenticate(user);
    call.enqueue(this);
  }

  /**
   * On success response - save the token and create the jwt services using token result and
   * set current user to response
   *
   * @param call     Token call
   * @param response Token response
   */
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

  /**
   * On failure of api call to authenticate user
   *
   * @param call Token call
   * @param t    Error
   */
  @Override
  public void onFailure(Call<Token> call, Throwable t) {
    toastMessage(this, "Unable to authenticate with Tweet Service");
    info("Failed to Authenticated!");
  }

  /**
   * Ping check for checking is user has internet connectivity or not
   * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
   *
   * @return Boolean of whether
   */
  public boolean isOnline() {
    Runtime runtime = Runtime.getRuntime();
    try {
      Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
      int exitValue = ipProcess.waitFor();
      return (exitValue == 0);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return false;
  }

}
