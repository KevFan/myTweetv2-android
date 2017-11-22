package kevin.mytweet.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by kevin on 01/11/2017.
 */
public class UserTest {
  private User user1, user2, user3;
  private TimeLine timeLine1, timeLine2, timeLine3;

  @Before
  public void setUp() throws Exception {
    timeLine1 = new TimeLine();
    timeLine2 = new TimeLine();
    timeLine3 = new TimeLine();
    timeLine1.addTweet(new Tweet("Test 1", new Date()));
    timeLine2.addTweet(new Tweet("Test 2", new Date()));
    timeLine3.addTweet(new Tweet("Test 3", new Date()));
    user1 = new User("Homer", "Simpson", "homer@simpson.com",
        "secret", timeLine1);
    user2 = new User("Marge", "Simpson", "marge@simpson.com",
        "secret", timeLine2);
    user3 = new User("Bart", "Simpson", "bart@simpson.com",
        "secret", timeLine3);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testConstructor() {
    assertEquals("Homer", user1.firstName);
    assertEquals("Simpson", user1.lastName);
    assertEquals("homer@simpson.com", user1.email);
    assertEquals("secret", user1.password);
    assertNotNull(user1.timeLine);
    assertEquals(1, user1.timeLine.tweets.size());
    assertEquals("Test 1", user1.timeLine.tweets.get(0).tweetMessage);

    assertEquals("Marge", user2.firstName);
    assertEquals("Simpson", user2.lastName);
    assertEquals("marge@simpson.com", user2.email);
    assertEquals("secret", user2.password);
    assertNotNull(user2.timeLine);
    assertEquals(1, user2.timeLine.tweets.size());
    assertEquals("Test 2", user2.timeLine.tweets.get(0).tweetMessage);

    assertEquals("Bart", user3.firstName);
    assertEquals("Simpson", user3.lastName);
    assertEquals("bart@simpson.com", user3.email);
    assertEquals("secret", user3.password);
    assertNotNull(user3.timeLine);
    assertEquals(1, user3.timeLine.tweets.size());
    assertEquals("Test 3", user3.timeLine.tweets.get(0).tweetMessage);
  }
}