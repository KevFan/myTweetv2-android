package kevin.mytweet.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

import kevin.mytweet.activities.TimeLineActivity;
import kevin.mytweet.models.User;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * MyTweetApp - main application
 * Created by kevin on 12/10/2017.
 */

public class MyTweetApp extends Application {
  public List<User> users = new ArrayList<>();
  public User currentUser = null;
  protected static MyTweetApp app;

  private static final String FILENAME = "myTweetData.json";

  /**
   * Called when application is first created
   * If the last previous logged in user, haven't logged out, their details are still in shared
   * preferences, try to log the user in instead of starting the welcome activity
   */
  @Override
  public void onCreate() {
    super.onCreate();
    info("MyTweet App Started");
    users = load();
    app = this;
    // If current user is still logged in, log them in instead of starting welcome activity
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    if (successLogin(prefs.getString("email", null), prefs.getString("password", null))) {
      info("Logging in previous user: " + prefs.getString("email", null));
      startActivity(new Intent(this, TimeLineActivity.class));
    } else {
      info("No logged in user detected - starting welcome activity");
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
   * Add new user to stored list of user, save, set current user to new user and set preference
   * setting to new user details
   *
   * @param user New user to add
   */
  public void newUser(User user) {
    users.add(user);
    save();
    currentUser = user;
    setPreferenceSettings();
  }

  /**
   * Validates the email and password to stored list of users. If credentials are correct, sets
   * current user to matching user and set preference setting to matching user
   *
   * @param email    email of user
   * @param password password of user
   * @return Boolean of whether credentials are valid and successfully logged in user
   */
  public boolean successLogin(String email, String password) {
    for (User user : users) {
      if (user.email.equals(email) && user.password.equals(password)) {
        currentUser = user;
        setPreferenceSettings();
        info("Logged in: " + user.toString());
        return true;
      }
    }
    return false;
  }

  /**
   * Uses GSon and output stream to write the current list of users to a json file
   */
  public void save() {
    Gson gson = new GsonBuilder().create();
    Writer writer;
    try {
      OutputStream out = this.openFileOutput(FILENAME, Context.MODE_PRIVATE);
      writer = new OutputStreamWriter(out);
      writer.write(gson.toJson(users));
      writer.close();
      info("Saved by gson!!");
    } catch (Exception e) {
      info(e.toString());
    }
  }

  /**
   * Using GSon and input stream, load a list of users from a json file
   *
   * @return List of users
   */
  public List<User> load() {
    List<User> users = new ArrayList<User>();
    Gson gson = new Gson();
    Type modelType = new TypeToken<List<User>>() {
    }.getType();
    BufferedReader reader;
    try {
      // open and read the file into a StringBuilder
      InputStream in = this.openFileInput(FILENAME);
      reader = new BufferedReader(new InputStreamReader(in));
      StringBuilder jsonString = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        // line breaks are omitted and irrelevant
        jsonString.append(line);
      }
      reader.close();
      users = gson.fromJson(jsonString.toString(), modelType);
      info("Loaded by GSon!!");
    } catch (Exception e) {
      info(e.toString());
    }

    return users;
  }

  /**
   * Sets shared preference values to current user
   */
  public void setPreferenceSettings() {
    info("MyTweetApp - setting shared preference to current user");
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString("firstName", currentUser.firstName);
    editor.putString("lastName", currentUser.lastName);
    editor.putString("email", currentUser.email);
    editor.putString("password", currentUser.password);
    editor.apply();
  }
}
