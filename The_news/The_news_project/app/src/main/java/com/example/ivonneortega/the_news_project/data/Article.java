package com.example.ivonneortega.the_news_project.data;

import java.util.List;

/**
 * Model of an Article object.
 * Created by ivonneortega on 4/29/17.
 */

public class Article {

    public static final int TRUE = 0;
    public static final int FALSE = 1;

    private String mImage;
    private String mTitle,mCategory,mDate, mBody, mSource, mUrl;
    private long mId;
    private boolean mIsSaved;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public boolean isTopStory() {
        return mIsTopStory;
    }

    public void setTopStory(boolean topStory) {
        mIsTopStory = topStory;
    }

    private boolean mIsTopStory;

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public Article(long id, String image, String title, String category, String date, String body, String source, int isSaved, int isTopStory, String url) {
        mSource = source;
        mId = id;
        mImage = image;
        mTitle = title;
        mCategory = category;
        mDate = date;
        if(isSaved==TRUE)
            mIsSaved = true;
        else
            mIsSaved = false;

        if(isTopStory==TRUE)
            mIsTopStory = true;
        else
            mIsTopStory = false;

        mUrl = url;
        mBody = body;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public boolean isSaved() {
        return mIsSaved;
    }

    public void setSaved(boolean saved) {
        mIsSaved = saved;
    }

    public static int getArticlePosition(long id, List<Article> list)
    {
        int i=0;
        while (i<list.size())
        {
            if(list.get(i).getId()==id)
            {
                return i;
            }
            else
                i++;
        }
        return -1;
    }
}
