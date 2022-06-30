package com.example.healthedu;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthedu.Database.DBHelper;
import com.example.healthedu.Interface.OnSelectedClickListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CommentAdapterRecord extends RecyclerView.Adapter<CommentAdapterRecord.HolderRecord> {

    private Context context;
    private ArrayList<ModelCommentRecord> commentList;
    public List<String> selected = new ArrayList<>();
    OnSelectedClickListener onSelectedClickListener;

    DBHelper db;

    public CommentAdapterRecord() {
    }

    public CommentAdapterRecord(Context context, ArrayList<ModelCommentRecord> commentList, OnSelectedClickListener onSelectedClickListener) {
        this.context = context;
        this.commentList = commentList;

        db = new DBHelper(context);
        this.onSelectedClickListener = onSelectedClickListener;

    }

    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments_list, parent, false);
        return new HolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecord holder, final int position) {
        ModelCommentRecord model = commentList.get(position);
        String cid = model.getId();
        final String comment = model.getComment();
        final String cimage = model.getImage();
        String blogID = model.getBlogID();

        holder.txtComment.setText(comment);
        holder.imageComment.setImageURI(Uri.parse(cimage));

        holder.linear1s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("sdasd");
                model.setSelected(!model.isSelected());

                onSelectedClickListener.setSelectedItems(cid, model.isSelected());

                holder.relativeLays.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        holder.commentMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(
                        "" + position,
                        "" + cid,
                        "" + comment,
                        "" + cimage
                );
            }
        });

        holder.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile(Uri.parse(cimage));
                shareImage();
            }
        });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile(Uri.parse(cimage));
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "", null));
                emailIntent.putExtra(Intent.EXTRA_TEXT, comment);
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///storage/emulated/0/tempemail.jpg"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text");
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.setPackage("com.google.android.gm");
                context.startActivity(Intent.createChooser(emailIntent, null));
            }
        });
    }

    private void showMoreDialog(String position, final String cid, final String comment, final String cimage) {
        //options to display in dialog
        String[] options = {"Edit", "delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item clicks
                if (which == 0) {
                    //edit is clicked
                    Intent intent = new Intent(context, AddUpdateCommentsActivity.class);
                    intent.putExtra("CID", cid);
                    intent.putExtra("COMMENT", comment);
                    intent.putExtra("CIMAGE", cimage);
                    intent.putExtra("isEditMode", true);
                    context.startActivity(intent);
                } else if (which == 1) {
                    //delete is clicked
                    db.deleteComment(cid);
                    //refresh the record by calling activitie's onResume method
                    ((CommentsActivity) context).onResume();
                }
            }
        });
        //show dialog
        builder.create().show();
    }


    public void shareImage() {
        try {
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test Mail");
            Uri uriForFile = FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()),
                    BuildConfig.APPLICATION_ID + ".provider", new File("storage/emulated/0/tempemail.jpg"));
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriForFile);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "<---MY TEXT--->.");
            context.startActivity(Intent.createChooser(shareIntent, "Share Deal"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error while sharing Image on Facebook", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFile(Uri sourceuri) {
        String sourceFilename = sourceuri.getPath();
        String destinationFilename = android.os.Environment.getExternalStorageDirectory().getPath() + File.separatorChar + "tempemail.jpg";
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public List<String> getSelected() {
        return selected;
    }

    class HolderRecord extends RecyclerView.ViewHolder {
        public TextView txtComment;
        public ImageButton commentMoreBtn;
        private ImageView imageComment;
        private Button shareBtn;
        private Button uploadBtn;
        private LinearLayout linear1s;
        private RelativeLayout relativeLays;

        public HolderRecord(@NonNull View itemView) {
            super(itemView);

            txtComment = itemView.findViewById(R.id.comment);
            imageComment = itemView.findViewById(R.id.view_commentImage);
            commentMoreBtn = itemView.findViewById(R.id.comment_moreBtn);
            shareBtn = itemView.findViewById(R.id.comment_shareBtn);
            uploadBtn = itemView.findViewById(R.id.comment_uploadBtn);
            linear1s = itemView.findViewById(R.id.linear1);
            relativeLays = itemView.findViewById(R.id.relativeLay);

        }
    }

}

