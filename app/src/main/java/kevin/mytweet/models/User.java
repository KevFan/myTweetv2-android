package kevin.mytweet.models;

/**
 * User model
 * Created by kevin on 12/10/2017.
 */

public class User {
  public String firstName;
  public String lastName;
  public String email;
  public String password;
  public TimeLine timeLine;

  /**
   * User constructor
   *
   * @param firstName First name of user
   * @param lastName  Last name of user
   * @param email     Email of user
   * @param password  Password of user
   * @param timeLine  TimeLine of user
   */
  public User(String firstName, String lastName, String email, String password, TimeLine timeLine) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.timeLine = timeLine;
  }
}
