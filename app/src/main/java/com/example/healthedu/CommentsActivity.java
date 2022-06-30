package com.example.healthedu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.healthedu.Database.DBHelper;
import com.example.healthedu.Interface.OnSelectedClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity implements OnSelectedClickListener {

    private FloatingActionButton addCommentBtn;
    private RecyclerView commentsRecycleView;
    private List<String> selected = new ArrayList<>();

    private DBHelper db;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        addCommentBtn = findViewById(R.id.addCommentBtn);
        commentsRecycleView = findViewById(R.id.comment_recycleView);
//        tvEmpty = findViewById(R.id.tv_empty);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Comments");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = new DBHelper(this);

        //load records
        loadRecords();

        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentsActivity.this, AddUpdateCommentsActivity.class);
                intent.putExtra("isEditMode", false); //want to add new data, set false
                startActivity(intent);
            }
        });
    }

    private void loadRecords() {
        CommentAdapterRecord commentAdapterRecord = new CommentAdapterRecord(CommentsActivity.this,
                db.viewComments(DBHelper.COMMENT_COL_1 + " DESC"), this);
        commentsRecycleView.setAdapter(commentAdapterRecord);
    }

    private void searchRecords(String query) {
        CommentAdapterRecord commentAdapterRecord = new CommentAdapterRecord(CommentsActivity.this,
                db.searchComments(query), this);
        commentsRecycleView.setAdapter(commentAdapterRecord);
    }

    @Override
    protected void onResume() {
        loadRecords();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchRecords(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchRecords(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int cid = item.getItemId();
        if (cid == R.id.action_delete) {

            //TODO
            if(!selected.isEmpty()){
                for (String commentId : selected) {
                    //deleteByCommentID(commentId);
                    db.deleteComment(commentId);
                }
            }
            onResume();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setSelectedItems(String selectedItem, boolean isSelected) {
        if (isSelected) {
            selected.add(selectedItem);
        } else {
            selected.remove(selectedItem);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}