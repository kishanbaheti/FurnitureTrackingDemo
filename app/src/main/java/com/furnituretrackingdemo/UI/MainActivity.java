package com.furnituretrackingdemo.UI;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.furnituretrackingdemo.R;
import com.furnituretrackingdemo.adapter.DBAdapter;
import com.furnituretrackingdemo.adapter.furnitureItemAdapter;
import com.furnituretrackingdemo.model.FeedItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<FeedItem> feedsList = null;
    private RecyclerView mRecyclerView;
    private furnitureItemAdapter adapter ;

    private DBAdapter dbObj = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,detailScreenActivity.class).putExtra("action","new"));
            }
        });

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        loadItemFromDatabase();

    }

    private void loadItemFromDatabase()
    {
        dbObj.open();

        Cursor item_cursor = dbObj.getItem();

        int total_row = item_cursor.getCount();

        if(total_row>0)
        {
            feedsList = new ArrayList<FeedItem>();

            for(int i=0;i<total_row;i++)
            {
                item_cursor.moveToPosition(i);

                int _id = item_cursor.getInt(item_cursor.getColumnIndex(DBAdapter.KEY_ROWID));
                String name = item_cursor.getString(item_cursor.getColumnIndex(DBAdapter.KEY_NAME));
                String description = item_cursor.getString(item_cursor.getColumnIndex(DBAdapter.KEY_DESC));
                String image_path = item_cursor.getString(item_cursor.getColumnIndex(DBAdapter.KEY_IMAGE));
                String location = item_cursor.getString(item_cursor.getColumnIndex(DBAdapter.KEY_LOCATION));
                int cost = item_cursor.getInt(item_cursor.getColumnIndex(DBAdapter.KEY_COST));

                FeedItem itemObj = new FeedItem();
                itemObj.setId(_id);
                itemObj.setName(name);
                itemObj.setDescription(description);
                itemObj.setImg_path(image_path);
                itemObj.setLocation(location);
                itemObj.setCost(cost);

                feedsList.add(itemObj);
            }
        }

        item_cursor.close();

        dbObj.close();



        adapter = new furnitureItemAdapter(MainActivity.this, feedsList);
        mRecyclerView.setAdapter(adapter);

    }

}
