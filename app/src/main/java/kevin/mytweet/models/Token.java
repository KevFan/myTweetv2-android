package kevin.mytweet.models;

/**
 * Created by kevin on 24/12/2017.
 */

public class Token {
  public boolean success;
  public String token;
  public User user;

  public Token(boolean success, String token) {
    this.success = success;
    this.token = token;
  }
}