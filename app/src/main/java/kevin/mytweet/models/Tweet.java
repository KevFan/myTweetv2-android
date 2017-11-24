package kevin.mytweet.models;

import java.util.Date;
import java.util.Random;

/**
 * Tweet model
 * Created by kevin on 13/10/2017.
 */

public class Tweet {
  public String tweetMessage;
  public Date tweetDate;
  public String _id;

  /**
   * Tweet constructor
   *
   * @param tweetMessage Tweet message
   * @param tweetDate    Tweet date
   */
  public Tweet(String tweetMessage, Date tweetDate) {
    this.tweetMessage = tweetMessage;
    this.tweetDate = tweetDate;
  }

  /**
   * Generate string report composed ot the tweet message and tweet date
   *
   * @return String of report
   */
  public String getTweetReport() {
    return tweetMessage + "\n\n" + tweetDate.toString();
  }
}
