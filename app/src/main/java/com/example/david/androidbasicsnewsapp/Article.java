package com.example.david.androidbasicsnewsapp;

import java.util.Date;

public class Article {

    private String title;
    private String sectionName;
    private String authorName;
    private String webUrl;
    private Date webPublicationDate;

    public Article(String title, String sectionName, String authorName, String webUrl, Date webPublicationDate) {
        this.title = title;
        this.sectionName = sectionName;
        this.authorName = authorName;
        this.webUrl = webUrl;
        this.webPublicationDate = webPublicationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Date getWebPublicationDate() {
        return webPublicationDate;
    }

    public void setWebPublicationDate(Date webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }
}
