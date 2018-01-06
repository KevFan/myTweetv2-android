package kevin.mytweet.helpers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import kevin.mytweet.models.Follow;
import kevin.mytweet.models.Token;
import kevin.mytweet.models.Tweet;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Helper class related to save and loading of files
 * Created by kevin on 04/01/2018.
 */

public class SaveLoadHelper {
  private static final String FILE_TOKEN = "token.json";
  private static final String FILE_TIMELINE = "timeline.json";
  private static final String FILE_FOLLOWERS = "followers.json";
  private static final String FILE_FOLLOWINGS = "followings.json";

  /**
   * Uses GSon and output stream to write current token to a json file
   */
  public static void saveToken(Context context, Token token) {
    writeToFile(context, new GsonBuilder().create().toJson(token), FILE_TOKEN);
  }

  /**
   * Using GSon and input stream, load a token from a json file
   *
   * @return Token
   */
  public static Token loadToken(Context context) {
    return new Gson().fromJson(readFromFile(context, FILE_TOKEN), new TypeToken<Token>() {
    }.getType());
  }

  /**
   * Uses GSon and output stream to write the current list of tweets to a json file
   */
  public static void saveTweets(Context context, List<Tweet> tweetList) {
    writeToFile(context, new GsonBuilder().create().toJson(tweetList), FILE_TIMELINE);
  }

  /**
   * Using GSon and input stream, load a list of tweets from a json file
   *
   * @return List of tweets
   */
  public static List<Tweet> loadTimeLine(Context context) {
    return new Gson().fromJson(readFromFile(context, FILE_TIMELINE), new TypeToken<List<Tweet>>() {
    }.getType());
  }

  /**
   * Uses GSon and output stream to write the current list of followers to a json file
   */
  public static void saveFollowers(Context context, List<Follow> followers) {
    writeToFile(context, new GsonBuilder().create().toJson(followers), FILE_FOLLOWERS);
  }

  /**
   * Using GSon and input stream, load a list of users from a json file
   *
   * @return List of followers
   */
  public static List<Follow> loadFollowers(Context context) {
    return new Gson().fromJson(readFromFile(context, FILE_FOLLOWERS), new TypeToken<List<Follow>>() {
    }.getType());
  }

  /**
   * Uses GSon and output stream to write the current list of followings to a json file
   */
  public static void saveFollowings(Context context, List<Follow> followings) {
    writeToFile(context, new GsonBuilder().create().toJson(followings), FILE_FOLLOWINGS);
  }

  /**
   * Using GSon and input stream, load a list of followings from a json file
   *
   * @return List of users
   */
  public static List<Follow> loadFollowings(Context context) {
    return new Gson().fromJson(readFromFile(context, FILE_FOLLOWINGS), new TypeToken<List<Follow>>() {
    }.getType());
  }

  /**
   * Private helper to read from a file and build and return the read file as a string
   *
   * @param context  Context
   * @param fileName Filename to be read
   * @return String of file read
   */
  private static String readFromFile(Context context, String fileName) {
    StringBuilder jsonString = new StringBuilder();
    BufferedReader reader;
    try {
      // open and read the file into a StringBuilder
      InputStream in = context.openFileInput(fileName);
      reader = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = reader.readLine()) != null) {
        // line breaks are omitted and irrelevant
        jsonString.append(line);
      }
      reader.close();
      info(fileName + " successfully read");
    } catch (Exception e) {
      info("Something went reading " + fileName);
      info(e.toString());
    }

    return jsonString.toString();
  }

  /**
   * Private helper to wrote Json string to a file
   *
   * @param context  Context
   * @param json     Json in string format
   * @param fileName filename to save to
   */
  private static void writeToFile(Context context, String json, String fileName) {
    Writer writer;
    try {
      OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      writer = new OutputStreamWriter(out);
      writer.write(json);
      writer.close();
      info("saved to " + fileName);
    } catch (Exception e) {
      info(e.toString());
    }
  }
}
