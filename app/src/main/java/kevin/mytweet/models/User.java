package kevin.mytweet.models;

/**
 * User model
 * Created by kevin on 12/10/2017.
 */

public class User {
  public String _id;
  public String firstName;
  public String lastName;
  public String email;
  public String password;
  public String image;

  /**
   * User constructor
   *
   * @param firstName First name of user
   * @param lastName  Last name of user
   * @param email     Email of user
   * @param password  Password of user
   */
  public User(String firstName, String lastName, String email, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }

  /**
   * Override equals method to compare by email
   * @param o Object to compare
   * @return Boolean of whether object is equal
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof User)) {
      return false;
    }
    User user = (User) o;
    return email.equals(user.email);
  }

  /**
   * Override hashcode to hash of user email
   * @return Hash code value
   */
  @Override
  public int hashCode() {
    return email.hashCode();
  }
}
