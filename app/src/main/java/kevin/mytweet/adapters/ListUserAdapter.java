package kevin.mytweet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kevin.mytweet.R;
import kevin.mytweet.models.Follow;
import kevin.mytweet.models.User;

/**
 * Custom adaptor for the timeline fragment to list tweets
 */
public class ListUserAdapter extends ArrayAdapter<User> {
  private Context context;
  public List<User> users;

  /**
   * TimeLineAdapter constructor
   *
   * @param context   Context of where the adapter is constructed
   * @param users ArrayList of users
   */
  public ListUserAdapter(Context context, List<User> users) {
    super(context, 0, users);
    this.users = users;
    this.context = context;
  }

  /**
   * Call list_item_tweet for each tweet in ArrayList to display tweet data at specific position
   *
   * @param position    position of tweet item
   * @param convertView View to reuse
   * @param parent      View parent of where convert view will be attached
   * @return View with tweet data at specific position
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.list_item_user, null);
    }

    User user = users.get(position);

    TextView userName = (TextView) convertView.findViewById(R.id.list_user_UserName);
    ImageView userImage = (ImageView) convertView.findViewById(R.id.list_user_userImage);
    userName.setText(user.firstName + " " + user.lastName);
    if (!user.image.equals("")) {
      Picasso.with(context).load(user.image).into(userImage);
    } else {
      userImage.setImageResource(R.mipmap.ic_launcher_round);
    }

    return convertView;
  }

  @Override
  public int getCount() {
    return users.size();
  }
}
