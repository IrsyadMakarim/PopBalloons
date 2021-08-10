package com.example.popballoons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class LeaderboardActivity extends BaseAdapter {

  private Context mContext;
  private LayoutInflater inflater;
  private List<Items> itemsItems;


  public LeaderboardActivity(Context context, List<Items> itemsItems) {
    this.mContext = context;
    this.itemsItems = itemsItems;

  }

  @Override
  public int getCount() {
    return itemsItems.size();
  }

  @Override
  public Object getItem(int location) {
    return itemsItems.get(location);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View scoreView, ViewGroup parent) {
    ViewHolder holder;
    if (inflater == null) {
      inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    if (scoreView == null) {

      scoreView = inflater.inflate(R.layout.list_row, parent, false);
      holder = new ViewHolder();
      holder.name = (TextView) scoreView.findViewById(R.id.leaderboardName);
      holder.score = (TextView) scoreView.findViewById(R.id.leaderboardScore);

      scoreView.setTag(holder);

    } else {
      holder = (ViewHolder) scoreView.getTag();
    }

    final Items m = itemsItems.get(position);
    holder.name.setText(m.getName());
    holder.score.setText(m.getScore());

    return scoreView;
  }

  static class ViewHolder {

    TextView name;
    TextView score;

  }
}