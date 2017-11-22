package kevin.mytweet.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by kevin on 01/11/2017.
 */
public class TweetTest {
  private Tweet tweet1, tweet2, tweet3;

  @Before
  public void setUp() throws Exception {
    tweet1 = new Tweet("Hello from test 1", new Date());
    tweet2 = new Tweet("Hello from test 2", new Date());
    tweet3 = new Tweet("Hello from test 3", new Date());
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testConstructor() {
    assertEquals("Hello from test 1", tweet1.tweetMessage);
    assertEquals("Hello from test 2", tweet2.tweetMessage);
    assertEquals("Hello from test 3", tweet3.tweetMessage);
    assertNotNull(tweet1.tweetDate);
    assertNotNull(tweet2.tweetDate);
    assertNotNull(tweet3.tweetDate);
  }

  @Test
  public void getTweetReport() throws Exception {
    assertEquals("Hello from test 1" + "\n\n" + tweet1.tweetDate.toString(),
        tweet1.getTweetReport());
    assertEquals("Hello from test 2" + "\n\n" + tweet2.tweetDate.toString(),
        tweet2.getTweetReport());
    assertEquals("Hello from test 3" + "\n\n" + tweet3.tweetDate.toString(),
        tweet3.getTweetReport());
  }

}