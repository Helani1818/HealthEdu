package com.example.healthedu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BlogAdapterRecord extends RecyclerView.Adapter<BlogAdapterRecord.HolderRecord> {

    private Context context;
    private ArrayList<ModelBlogRecord> blogList;

    public BlogAdapterRecord(Context context, ArrayList<ModelBlogRecord> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blogs_list_layout, parent, false);
        return new HolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecord holder, int position) {
        ModelBlogRecord model = blogList.get(position);
        String id = model.getId();
        String title = model.getTitle();
        String description = model.getDescription();
        String image = model.getImage();

        holder.txtBlogTitle.setText(title);
        holder.BlogImage.setImageURI (Uri.parse (image));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BlogDetailsActivity.class);
                intent.putExtra("RECORD_ID", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    class HolderRecord extends RecyclerView.ViewHolder{
        public TextView txtBlogTitle;
        public ImageView  BlogImage;

        public HolderRecord(@NonNull View itemView) {
            super(itemView);

            txtBlogTitle = itemView.findViewById(R.id.blog_title);
            BlogImage = itemView.findViewById (R.id.view_blogImage);
        }
    }
}
