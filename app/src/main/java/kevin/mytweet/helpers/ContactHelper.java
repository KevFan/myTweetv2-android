package kevin.mytweet.helpers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.content.ContentResolver;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Contact Helper methods
 * Created by kevin on 19/10/2017.
 */

public class ContactHelper {
  /**
   * Gets the contact email
   *
   * @param context Context of where method was called
   * @param data    Data passes in through intent
   * @return String on contact email
   */
  public static String getEmail(Context context, Intent data) {
    String email = "no email";

    Uri contactUri = data.getData();
    ContentResolver cr = context.getContentResolver();

    Cursor cur = cr.query(contactUri, null, null, null, null);

    if (cur.getCount() > 0) {
      try {
        cur.moveToFirst();
        String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
        Cursor emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
        emails.moveToFirst();
        email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        emails.close();
      } catch (Exception e) {
        info(e.toString());
      }
    }
    return email;
  }

  /**
   * Gets the contact name
   *
   * @param context Context of where method was called
   * @param data    Data passes in through intent
   * @return String on contact name
   */
  public static String getContact(Context context, Intent data) {
    String contact = "unable to find contact";
    Uri contactUri = data.getData();
    String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
    Cursor c = context.getContentResolver().query(contactUri, queryFields, null, null, null);
    if (c.getCount() == 0) {
      c.close();
      return contact;
    }
    c.moveToFirst();
    contact = c.getString(0);
    c.close();

    return contact;
  }

  /**
   * Calls intent to send email, passing email, subject and email body
   *
   * @param context Context of whether the method was called from
   * @param email   email to send to
   * @param subject subject of email
   * @param body    body of email
   */
  public static void sendEmail(Context context, String email, String subject, String body) {
    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
    emailIntent.putExtra(Intent.EXTRA_TEXT, body);
    context.startActivity(Intent.createChooser(emailIntent, "Sending Email"));
  }
}
