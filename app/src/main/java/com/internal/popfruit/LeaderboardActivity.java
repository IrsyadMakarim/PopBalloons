package com.internal.popfruit;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView rvList;
    DatabaseHelper myDb;
    private ArrayList<Items> items = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_leaderboard);

      getSupportActionBar().setTitle("Leaderboard");
      rvList = findViewById(R.id.scoreRV);
      myDb = new DatabaseHelper(this);

      getDataItems();

      rvList.setLayoutManager(new LinearLayoutManager(this));
      rvList.setAdapter(new ListAdapter(items));
    }

    private void getDataItems(){
        Cursor cursor = myDb.getAllData();

        while (cursor.moveToNext()){
            Items item = new Items();
            item.setId(cursor.getInt(0));
            item.setScore(cursor.getString(1));

            items.add(item);
        }
    }
}