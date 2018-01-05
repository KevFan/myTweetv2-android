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
    writeToFile(context, new GsonBuilder().create().toJson(token), FILE_TOKEN);
  }

  /**
   * Using GSon and input stream, load a list of users from a json file
   *
   * @return List of users
   */
  public static Token loadToken(Context context) {
    return new Gson().fromJson(readFromFile(context, FILE_TOKEN), new TypeToken<Token>() {}.getType());
  }

  /**
   * Uses GSon and output stream to write the current list of users to a json file
   */
  public static void saveTweets(Context context, List<Tweet> tweetList) {
    writeToFile(context, new GsonBuilder().create().toJson(tweetList), FILE_TIMELINE);
  }

  public static List<Tweet> loadTimeLine(Context context) {
    return new Gson().fromJson(readFromFile(context, FILE_TIMELINE), new TypeToken<List<Tweet>>() {}.getType());
  }

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
