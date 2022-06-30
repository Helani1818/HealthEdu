package com.example.healthedu.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.healthedu.LoginActivity;
import com.example.healthedu.ModelBlogRecord;
import com.example.healthedu.ModelCommentRecord;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper
{
    public static final String TABLE_2 = "BLOG_TABLE";
    public static final String BLOG_COL_1 = "ID";
    public static final String BLOG_COL_2 = "TITLE";
    public static final String BLOG_COL_3 = "DESCRIPTION";
    public static final String BLOG_COL_4 = "IMAGE";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_2 + "("
            + BLOG_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BLOG_COL_2 + " TEXT,"
            + BLOG_COL_3 + " TEXT,"
            + BLOG_COL_4 + " TEXT" + ")";

    public static final String TABLE_3 = "COMMENTS_TABLE";
    public static final String COMMENT_COL_1 = "CID";
    public static final String COMMENT_COL_2 = "COMMENT";
    public static final String COMMENT_COL_3 = "CIMAGE";
    public static final String COMMENT_COL_4 = "BLOGID";
    public static final String CREATE_TABLE_1 = "CREATE TABLE " + TABLE_3 + "("
            + COMMENT_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COMMENT_COL_2 + " TEXT,"
            + COMMENT_COL_3 + " TEXT, "
            + COMMENT_COL_4 + " TEXT" + ")";


    public DBHelper(Context context) {
        super(context, "Healthedu.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table user(username TEXT primary key, name TEXT, email TEXT, password TEXT)");
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists user");
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_2);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_3);
        onCreate(db);
    }

    public Boolean insertUser(String username, String name, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("name",name);
        contentValues.put("email",email);
        contentValues.put("password",password);
        long result = db.insert("user", null,contentValues);
        if ((result==-1)) {
            return false;
        } else
        {
            return true;
        }
    }
    public  Boolean checkUsername(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where username = ?", new String[] {username});
        if(cursor.getCount()>0) {
            return true;
        } else
        {
            return false;
        }
    }

    public  Boolean checkUsernameAndPassword(String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0) {
            return true;
        } else
        {
            return false;
        }
    }

    public long insertRecord(String title, String description, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BLOG_COL_2, title);
        values.put(BLOG_COL_3, description);
        values.put(BLOG_COL_4, image);

        long id = db.insert(TABLE_2, null, values);
        return id;
    }

    public ArrayList<ModelBlogRecord> viewBlogs (String orderBy)
    {
        ArrayList<ModelBlogRecord> blogList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_2 + " ORDER BY " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do {
                ModelBlogRecord modelBlogRecord = new ModelBlogRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(BLOG_COL_1)),
                        ""+cursor.getString(cursor.getColumnIndex(BLOG_COL_2)),
                        ""+cursor.getString(cursor.getColumnIndex(BLOG_COL_3)),
                        ""+cursor.getString(cursor.getColumnIndex(BLOG_COL_4)));
                blogList.add(modelBlogRecord);
            }while (cursor.moveToNext());
        }
        return blogList;
    }

    public ArrayList<ModelBlogRecord> searchBlogs (String query)
    {
        ArrayList<ModelBlogRecord> blogList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_2 + " WHERE " + BLOG_COL_2 + " LIKE '% " + query +"%' ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do {
                ModelBlogRecord modelBlogRecord = new ModelBlogRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(BLOG_COL_1)),
                        ""+cursor.getString(cursor.getColumnIndex(BLOG_COL_2)),
                        ""+cursor.getString(cursor.getColumnIndex(BLOG_COL_3)),
                        ""+cursor.getString(cursor.getColumnIndex(BLOG_COL_4)));
                blogList.add(modelBlogRecord);
            }while (cursor.moveToNext());
        }
        return blogList;
    }

    public int getBlogsCount () {
        String countQuery = "SELECT * FROM " + TABLE_2;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public long insertComment(String comment, String cimage, String blogID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COMMENT_COL_2, comment);
        values.put(COMMENT_COL_3, cimage);
        values.put(COMMENT_COL_4, blogID);
        long cid = db.insert(TABLE_3, null, values);
        return cid;
    }

    public ArrayList<ModelCommentRecord> viewComments (String orderBy)
    {
        ArrayList<ModelCommentRecord> commentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_3 +  " ORDER BY " + orderBy;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ModelCommentRecord modelCommentRecord = new ModelCommentRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(COMMENT_COL_1)),
                        ""+cursor.getString(cursor.getColumnIndex(COMMENT_COL_2)),
                        ""+cursor.getString(cursor.getColumnIndex(COMMENT_COL_3)),
                        ""+cursor.getString (cursor.getColumnIndex (COMMENT_COL_4)));
                commentList.add(modelCommentRecord);
            }while (cursor.moveToNext());
        }return commentList;
    }

    public ArrayList<ModelCommentRecord> searchComments (String query)
    {
        ArrayList<ModelCommentRecord> commentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_3 + " WHERE " + COMMENT_COL_2 + " LIKE '%" + query +"%'";
        if(query.trim().equals("")){
             selectQuery = "SELECT * FROM " + TABLE_3;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ModelCommentRecord modelCommentRecord = new ModelCommentRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(COMMENT_COL_1)),
                        ""+cursor.getString(cursor.getColumnIndex(COMMENT_COL_2)),
                        ""+cursor.getString(cursor.getColumnIndex(COMMENT_COL_3)),
                        ""+cursor.getString(cursor.getColumnIndex(COMMENT_COL_4)));
                commentList.add(modelCommentRecord);
            }while (cursor.moveToNext());
        }return commentList;
    }

    public int getCommentsCount () {
        String countQuery = "SELECT * FROM " + TABLE_3;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public void updateComment(String cid, String comment, String cimage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COMMENT_COL_2, comment);
        values.put(COMMENT_COL_3, cimage);

        db.update(TABLE_3, values, COMMENT_COL_1 +" = ?", new String[]{cid});

    }

    //delete by id
    public void deleteComment(String cid)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_3, COMMENT_COL_1 + " =?", new String[]{cid});
    }

    //delete all
    public void deleteAllComments()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_3);
    }

    public  Boolean updateUser(String username, String name, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("email",email);
        contentValues.put("password",password);
        Cursor cursor = db.rawQuery("Select * from user where username =  ?", new String[] {username});
        if(cursor.getCount()>0)
        {
            long result = db.update("user",contentValues, "username=?", new String[] {username});
            if ((result==-1))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }
    public  Boolean deleteUser(String username, String name, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where username =  ?", new String[] {username});
        if(cursor.getCount()>0)
        {
            long result = db.delete("user", "username=?", new String[] {username});
            if ((result==-1))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public  Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from user ", null);
        return cursor;
    }

}
