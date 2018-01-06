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
import kevin.mytweet.app.MyTweetApp;
import kevin.mytweet.models.Tweet;

/**
 * Custom adaptor for the timeline fragment to list tweets
 */
public class TimeLineAdapter extends ArrayAdapter<Tweet> {
  private boolean isActionMode;
  private Context context;
  public List<Tweet> timeLine;

  /**
   * TimeLineAdapter constructor
   *
   * @param context Context of where the adapter is constructed
   * @param tweets  ArrayList of tweets
   */
  public TimeLineAdapter(Context context, List<Tweet> tweets) {
    super(context, 0, tweets);
    this.context = context;
    this.timeLine = tweets;
    this.isActionMode = false;
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
      convertView = inflater.inflate(R.layout.list_item_tweet, null);
    }

    Tweet tweet = timeLine.get(position);

    TextView tweetText = (TextView) convertView.findViewById(R.id.list_item_tweetText);
    tweetText.setText(tweet.tweetText);
    tweetText.setMaxLines(1);

    TextView tweetDate = (TextView) convertView.findViewById(R.id.list_item_tweetDate);
    tweetDate.setText(tweet.tweetDate.toString());

    TextView tweetUserName = (TextView) convertView.findViewById(R.id.list_item_tweetUserName);
    tweetUserName.setText(tweet.tweetUser.firstName + " " + tweet.tweetUser.lastName);

    ImageView tweetUserImage = (ImageView) convertView.findViewById(R.id.list_item_tweetUser_image);
    if (!tweet.tweetUser.image.equals("")) {
      Picasso.with(context).load(tweet.tweetUser.image).into(tweetUserImage);
    } else {
      tweetUserImage.setImageResource(R.mipmap.ic_launcher_round);
    }

    ImageView tweetImage = (ImageView) convertView.findViewById(R.id.list_item_tweetImage);
    if (!tweet.tweetImage.equals("")) {
      Picasso.with(context).load(tweet.tweetImage).into(tweetImage);
    }

    return convertView;
  }

  @Override
  public int getCount() {
    return timeLine.size();
  }

  //https://stackoverflow.com/questions/14446249/enable-disable-item-selection-at-listview-in-multiple-choice-mode
  @Override
  public boolean isEnabled(int position) {
    if (this.isActionMode) {
      Tweet tweet = this.getItem(position);
      if (tweet.tweetUser._id.equals(MyTweetApp.getApp().currentUser._id)) {
        //only enable items that are not inside the basket
        return true;
      }
      //all other items are disabled during actionmode
      return false;
    }
    //no actionmode = everything enabled
    return true;
  }

  public void setActionMode(boolean isActionMode) {
    this.isActionMode = isActionMode;
  }
}
