package kevin.mytweet.helpers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import static kevin.mytweet.helpers.MessageHelpers.info;

/**
 * Picture Helpers - contains helpers related to picture selection
 * Created by kevin on 27/12/2017.
 */

public class PictureHelper {
  public static final int PICK_IMAGE = 2;

  /**
   * Helps set up intent to get picture
   *
   * @return return intent to get picture
   */
  public static Intent setGetPictureIntent() {
    // https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    return intent;
  }

  /**
   * Helper to get the real path to a image file after image selection by intent for correct uploading
   * https://stackoverflow.com/questions/29646975/how-to-get-file-path-of-image-from-uri-in-android-lollipop
   *
   * @param context Context of image selection
   * @param uri     Uri containing image data
   * @return String to real path of image selected
   */
  public static String getRealPathFromURI_API19(Context context, Uri uri) {
    info(uri.getPath());
    String filePath = "";
    if (DocumentsContract.isDocumentUri(context, uri)) {
      String wholeID = DocumentsContract.getDocumentId(uri);
      info(wholeID);
      // Split at colon, use second item in the array
      String[] splits = wholeID.split(":");
      if (splits.length == 2) {
        String id = splits[1];

        String[] column = {MediaStore.Images.Media.DATA};
        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            column, sel, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
          filePath = cursor.getString(columnIndex);
        }
        cursor.close();
      }
    } else {
      filePath = uri.getPath();
    }
    return filePath;
  }
}
