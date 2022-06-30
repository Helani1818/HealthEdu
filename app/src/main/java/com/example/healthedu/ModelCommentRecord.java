package com.example.healthedu;

public class ModelCommentRecord
{
    String id, comment, image, blogID;
    private boolean isSelected = false;

    public ModelCommentRecord(String id, String comment, String image, String blogID) {
        this.id = id;
        this.comment = comment;
        this.image = image;
        this.blogID = blogID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBlogID() {
        return blogID;
    }

    public void setBlogID(String blogID) {
        this.blogID = blogID;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}
