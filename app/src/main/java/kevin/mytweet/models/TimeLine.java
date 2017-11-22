package kevin.mytweet.models;

import java.util.ArrayList;
import java.util.List;

/**
 * TimeLine model - contains an ArrayList of tweets
 * Created by kevin on 15/10/2017.
 */

public class TimeLine {
  public List<Tweet> tweets;

  /**
   * TimeLine constructor
   */
  public TimeLine() {
    tweets = new ArrayList<Tweet>();
  }

  /**
   * Add Tweet to ArrayList of tweets
   *
   * @param tweet Tweet to add
   */
  public void addTweet(Tweet tweet) {
    tweets.add(tweet);
  }

  /**
   * Get tweet from ArrayList of tweets
   *
   * @param id Id of the tweet
   * @return Tweet matching with ID
   */
  public Tweet getTweet(Long id) {
    for (Tweet tweet : tweets) {
      if (tweet.id.equals(id)) {
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
    tweets.remove(tweet);
  }
}
