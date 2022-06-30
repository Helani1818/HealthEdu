package com.example.healthedu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthedu.Database.DBHelper;

import static com.example.healthedu.Database.DBHelper.BLOG_COL_1;
import static com.example.healthedu.Database.DBHelper.BLOG_COL_2;
import static com.example.healthedu.Database.DBHelper.BLOG_COL_3;
import static com.example.healthedu.Database.DBHelper.BLOG_COL_4;
import static com.example.healthedu.Database.DBHelper.TABLE_2;

public class BlogDetailsActivity extends AppCompatActivity {

    ImageView viewBlogImage;
    TextView viewBlogTitle, viewBlogDescription;
    Button showCommentsBtn;

    private String recordID;

    private DBHelper db;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Blog");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get record id from adapter through intent
        Intent intent = getIntent();
        recordID = intent.getStringExtra("RECORD_ID");

        viewBlogImage = findViewById(R.id.view_blogImage);
        viewBlogTitle = findViewById(R.id.view_blogTitle);
        viewBlogDescription = findViewById(R.id.view_blogDescription);
        showCommentsBtn = findViewById(R.id.showCommentsBtn);

        showCommentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BlogDetailsActivity.this, CommentsActivity.class);
                startActivity(intent);
            }
        });

        db = new DBHelper(this);

        showBlogDetails();
    }

    private void showBlogDetails() {
        //query to select record based on record id
        String selectQuery = "SELECT * FROM " + TABLE_2 + " WHERE " + BLOG_COL_1 +" =\"" + recordID+"\"";

        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        //keep checking in whole database for that record
        if (cursor.moveToFirst()){
            do {
                //get data
                String id = ""+ cursor.getInt(cursor.getColumnIndex(BLOG_COL_1));
                String title = ""+ cursor.getString(cursor.getColumnIndex(BLOG_COL_2));
                String description = ""+ cursor.getString(cursor.getColumnIndex(BLOG_COL_3));
                String image = ""+ cursor.getString(cursor.getColumnIndex(BLOG_COL_4));

                //set data
                if (image.equals("null")){
                    //no image in the record
                    viewBlogImage.setImageResource(R.drawable.ic_baseline_person_24);
                }
                else{
                    viewBlogImage.setImageURI(Uri.parse(image));
                }
                viewBlogTitle.setText(title);
                viewBlogDescription.setText(description);


            }while (cursor.moveToNext());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}