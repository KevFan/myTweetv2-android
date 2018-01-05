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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kevin.mytweet.models.Token;
import kevin.mytweet.models.Tweet;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Created by kevin on 04/01/2018.
 */

public class SaveLoadHelper {
  private static final String FILE_TOKEN = "token.json";
  private static final String FILE_TIMELINE = "timeline.json";

  /**
   * Uses GSon and output stream to write the current list of users to a json file
   */
  public static void saveToken(Context context, Token token) {
    Gson gson = new GsonBuilder().create();
    Writer writer;
    try {
      OutputStream out = context.openFileOutput(FILE_TOKEN, Context.MODE_PRIVATE);
      writer = new OutputStreamWriter(out);
      writer.write(gson.toJson(token));
      writer.close();
      info("Token Saved by gson!!");
    } catch (Exception e) {
      info(e.toString());
    }
  }

  /**
   * Using GSon and input stream, load a list of users from a json file
   *
   * @return List of users
   */
  public static Token loadToken(Context context) {
    Token token;
    Gson gson = new Gson();
    Type modelType = new TypeToken<Token>() {}.getType();
    BufferedReader reader;
    try {
      // open and read the file into a StringBuilder
      InputStream in = context.openFileInput(FILE_TOKEN);
      reader = new BufferedReader(new InputStreamReader(in));
      StringBuilder jsonString = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        // line breaks are omitted and irrelevant
        jsonString.append(line);
      }
      reader.close();
      token = gson.fromJson(jsonString.toString(), modelType);
      info("Token Loaded by GSon!!");
    } catch (Exception e) {
      token = null;
      info("Something went wrong loading tokens");
      info(e.toString());
    }

    return token;
  }

  /**
   * Uses GSon and output stream to write the current list of users to a json file
   */
  public static void saveTweets(Context context, List<Tweet> tweetList) {
    Gson gson = new GsonBuilder().create();
    Writer writer;
    try {
      OutputStream out = context.openFileOutput(FILE_TIMELINE, Context.MODE_PRIVATE);
      writer = new OutputStreamWriter(out);
      writer.write(gson.toJson(tweetList));
      writer.close();
      info("tweets Saved by gson!!");
    } catch (Exception e) {
      info(e.toString());
    }
  }

  public static List<Tweet> loadTimeLine(Context context) {
    List<Tweet> tweetList;
    Gson gson = new Gson();
    Type modelType = new TypeToken<List<Tweet>>() {}.getType();
    BufferedReader reader;
    try {
      // open and read the file into a StringBuilder
      InputStream in = context.openFileInput(FILE_TIMELINE);
      reader = new BufferedReader(new InputStreamReader(in));
      StringBuilder jsonString = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        // line breaks are omitted and irrelevant
        jsonString.append(line);
      }
      reader.close();
      tweetList = gson.fromJson(jsonString.toString(), modelType);
      info("Tweets Loaded by GSon!!");
    } catch (Exception e) {
      tweetList = new ArrayList<>();
      info("Something went wrong loading Tweets");
      info(e.toString());
    }

    return tweetList;
  }
}
