package kevin.mytweet.models;

import java.util.Date;
import java.util.Random;

/**
 * Tweet model
 * Created by kevin on 13/10/2017.
 */

public class Tweet {
  public String tweetText;
  public Date tweetDate;
  public String _id;
  public User tweetUser;
  public String tweetImage;
  public Marker marker = new Marker();

  /**
   * Tweet constructor
   *
   * @param tweetText Tweet message
   * @param tweetDate Tweet date
   */
  public Tweet(String tweetText, Date tweetDate) {
    this.tweetText = tweetText;
    this.tweetDate = tweetDate;
  }

  /**
   * Generate string report composed ot the tweet message and tweet date
   *
   * @return String of report
   */
  public String getTweetReport() {
    return tweetText + "\n\n" + tweetDate.toString();
  }
}
