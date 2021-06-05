package com.example.freshair.ModelsBlog;

public class Post {

    private String firstName;
    private String lastName;
    private String titlePost;
    private String contentPost;
    private String createdAt;
    private String urlImage;

    public Post(String firstName, String lastName, String titlePost, String contentPost, String createdAt, String urlImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.titlePost = titlePost;
        this.contentPost = contentPost;
        this.createdAt = createdAt;
        this.urlImage = urlImage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitlePost() {
        return titlePost;
    }

    public void setTitlePost(String title) {
        this.titlePost = title;
    }

    public String getContentPost() {
        return contentPost;
    }

    public void setContentPost(String content) {
        this.contentPost = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
