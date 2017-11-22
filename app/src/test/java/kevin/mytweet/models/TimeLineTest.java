package kevin.mytweet.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by kevin on 01/11/2017.
 */
public class TimeLineTest {
  private TimeLine timeLine1, timeLine2;
  private Tweet tweet1, tweet2;
  @Before
  public void setUp() throws Exception {
    tweet1 = new Tweet("Test1", new Date());
    tweet2 = new Tweet("Test2", new Date());
    timeLine1 = new TimeLine();
    timeLine2 = new TimeLine();
    timeLine2.tweets.add(tweet1);
    timeLine2.tweets.add(tweet2);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testConstructor() {
    assertNotNull(timeLine1);
    assertNotNull(timeLine1.tweets);
    assertEquals(0, timeLine1.tweets.size());

    assertNotNull(timeLine2);
    assertNotNull(timeLine2.tweets);
    assertEquals(2, timeLine2.tweets.size());
    assertEquals("Test1", timeLine2.tweets.get(0).tweetMessage);
    assertEquals(tweet1.tweetDate, timeLine2.tweets.get(0).tweetDate);
    assertEquals(tweet1.id, timeLine2.tweets.get(0).id);
    assertEquals("Test2", timeLine2.tweets.get(1).tweetMessage);
    assertEquals(tweet2.tweetDate, timeLine2.tweets.get(1).tweetDate);
    assertEquals(tweet2.id, timeLine2.tweets.get(1).id);
  }

  @Test
  public void addTweet() throws Exception {
    assertEquals(0, timeLine1.tweets.size());
    Tweet test1 = new Tweet("Testing add tweet 1", new Date());
    timeLine1.addTweet(test1);
    assertEquals(1, timeLine1.tweets.size());
    assertEquals("Testing add tweet 1", timeLine1.tweets.get(0).tweetMessage);
    assertEquals(test1.tweetDate, timeLine1.tweets.get(0).tweetDate);
    assertEquals(test1.id, timeLine1.tweets.get(0).id);

    assertEquals(2, timeLine2.tweets.size());
    Tweet test2 = new Tweet("Testing add tweet 2", new Date());
    timeLine2.addTweet(test2);
    assertEquals(3, timeLine2.tweets.size());
    assertEquals("Testing add tweet 2", timeLine2.tweets.get(2).tweetMessage);
    assertEquals(test2.tweetDate, timeLine2.tweets.get(2).tweetDate);
    assertEquals(test2.id, timeLine2.tweets.get(2).id);
  }

  @Test
  public void getTweet() throws Exception {
    assertEquals(tweet1, timeLine2.getTweet(tweet1.id));
    assertEquals(tweet1.id, timeLine2.getTweet(tweet1.id).id);
    assertEquals(tweet1.tweetMessage, timeLine2.getTweet(tweet1.id).tweetMessage);
    assertEquals(tweet1.tweetDate, timeLine2.getTweet(tweet1.id).tweetDate);
    assertSame(tweet1, timeLine2.getTweet(tweet1.id));

    assertEquals(tweet2.id, timeLine2.getTweet(tweet2.id).id);
    assertEquals(tweet2, timeLine2.getTweet(tweet2.id));
    assertEquals(tweet2.tweetMessage, timeLine2.getTweet(tweet2.id).tweetMessage);
    assertEquals(tweet2.tweetDate, timeLine2.getTweet(tweet2.id).tweetDate);
    assertSame(tweet2, timeLine2.getTweet(tweet2.id));
  }

  @Test
  public void deleteTweet() throws Exception {
    assertEquals(2, timeLine2.tweets.size());
    assertEquals("Test1", timeLine2.tweets.get(0).tweetMessage);
    assertEquals(tweet1.tweetDate, timeLine2.tweets.get(0).tweetDate);
    assertEquals(tweet1.id, timeLine2.tweets.get(0).id);
    timeLine2.deleteTweet(tweet1);
    assertEquals(1, timeLine2.tweets.size());
    assertNull(timeLine2.getTweet(tweet1.id));

    assertNotEquals("Test1", timeLine2.tweets.get(0).tweetMessage);
    // Date could be equal but not same object
    assertNotSame(tweet1.tweetDate, timeLine2.tweets.get(0).tweetDate);
    assertNotEquals(tweet1.id, timeLine2.tweets.get(0).id);
    assertEquals("Test2", timeLine2.tweets.get(0).tweetMessage);
    assertEquals(tweet2.tweetDate, timeLine2.tweets.get(0).tweetDate);
    assertEquals(tweet2.id, timeLine2.tweets.get(0).id);

    timeLine2.deleteTweet(tweet2);
    assertEquals(0, timeLine2.tweets.size());
    assertNull(timeLine2.getTweet(tweet2.id));
  }

}