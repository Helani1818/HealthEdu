package com.example.healthedu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.healthedu.Database.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addBlogBtn;
    private RecyclerView blogsRecycleView;

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBlogBtn = findViewById(R.id.addBlogBtn);
        blogsRecycleView = findViewById(R.id.blog_recycleView);

        db = new DBHelper(this);
        
        //load records
        loadRecords();

        addBlogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddUpdateBlogsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadRecords() {
        BlogAdapterRecord blogAdapterRecord = new BlogAdapterRecord(MainActivity.this,
                db.viewBlogs(DBHelper.BLOG_COL_1 + " DESC"));
        blogsRecycleView.setAdapter(blogAdapterRecord);
    }

    private void searchRecords(String query) {
        BlogAdapterRecord blogAdapterRecord = new BlogAdapterRecord(MainActivity.this, db.searchBlogs(query));
        blogsRecycleView.setAdapter(blogAdapterRecord);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecords();
    }

}