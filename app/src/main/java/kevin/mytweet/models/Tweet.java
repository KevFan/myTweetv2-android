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
  public Long id;

  /**
   * Tweet constructor
   *
   * @param tweetMessage Tweet message
   * @param tweetDate    Tweet date
   */
  public Tweet(String tweetMessage, Date tweetDate) {
    this.id = unsignedLong();
    this.tweetMessage = tweetMessage;
    this.tweetDate = tweetDate;
  }

  /**
   * Generate a long greater than zero
   *
   * @return Unsigned Long value greater than zero
   */
  private Long unsignedLong() {
    long rndVal = 0;
    do {
      rndVal = new Random().nextLong();
    } while (rndVal <= 0);
    return rndVal;
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
