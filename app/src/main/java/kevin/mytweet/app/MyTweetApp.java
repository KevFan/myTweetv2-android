package kevin.mytweet.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kevin.mytweet.activities.HomeActivity;
import kevin.mytweet.adapters.ListUserAdapter;
import kevin.mytweet.models.Follow;
import kevin.mytweet.models.Token;
import kevin.mytweet.models.Tweet;
import kevin.mytweet.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static kevin.mytweet.helpers.MessageHelpers.info;
import static kevin.mytweet.helpers.MessageHelpers.toastMessage;

/**
 * MyTweetApp - main application
 * Created by kevin on 12/10/2017.
 */

public class MyTweetApp extends Application implements Callback<Token> {
  public MyTweetService tweetService;
  public MyTweetServiceOpen tweetServiceOpen;
  public boolean tweetServiceAvailable = false;
//  public String          service_url  = "http://192.168.0.8:4000";   // Standard Emulator IP Address
//  public String          service_url  = "https://test-remote-myweet.herokuapp.com";   // Standard Emulator IP Address

  public List<User> users = new ArrayList<>();
  public List<Follow> followers = new ArrayList<>();
  public List<Follow> followings = new ArrayList<>();
  public List<Tweet> timeLine = new ArrayList<>();
  public User currentUser = null;
  protected static MyTweetApp app;

  private static final String FILENAME = "myTweetData.json";

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
//    users = load();
    app = this;
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .build();
    mGoogleApiClient.connect();
    // If current user is still logged in, log them in instead of starting welcome activity
//    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//    if (successLogin(prefs.getString("email", null), prefs.getString("password", null))) {
//      info("Logging in previous user: " + prefs.getString("email", null));
//      startActivity(new Intent(this, HomeActivity.class));
//    } else {
//      info("No logged in user detected - starting welcome activity");
//    }
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

//  /**
//   * Uses GSon and output stream to write the current list of users to a json file
//   */
//  public void save() {
//    Gson gson = new GsonBuilder().create();
//    Writer writer;
//    try {
//      OutputStream out = this.openFileOutput(FILENAME, Context.MODE_PRIVATE);
//      writer = new OutputStreamWriter(out);
//      writer.write(gson.toJson(users));
//      writer.close();
//      info("Saved by gson!!");
//    } catch (Exception e) {
//      info(e.toString());
//    }
//  }
//
//  /**
//   * Using GSon and input stream, load a list of users from a json file
//   *
//   * @return List of users
//   */
//  public List<User> load() {
//    List<User> users = new ArrayList<User>();
//    Gson gson = new Gson();
//    Type modelType = new TypeToken<List<User>>() {
//    }.getType();
//    BufferedReader reader;
//    try {
//      // open and read the file into a StringBuilder
//      InputStream in = this.openFileInput(FILENAME);
//      reader = new BufferedReader(new InputStreamReader(in));
//      StringBuilder jsonString = new StringBuilder();
//      String line;
//      while ((line = reader.readLine()) != null) {
//        // line breaks are omitted and irrelevant
//        jsonString.append(line);
//      }
//      reader.close();
//      users = gson.fromJson(jsonString.toString(), modelType);
//      info("Loaded by GSon!!");
//    } catch (Exception e) {
//      info(e.toString());
//    }
//
//    return users;
//  }
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

  //
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
