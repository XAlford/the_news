package com.example.ivonneortega.the_news_project.data;

import java.util.List;

/**
 * Model of a Category object.
 * Created by ivonneortega on 4/29/17.
 */

public class Category {

    private String mCategoryName;
    private List<Article> mList;

    public Category(String categoryName, List<Article> list) {
        mCategoryName = categoryName;
        mList = list;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public List<Article> getList() {
        return mList;
    }

    public void setList(List<Article> list) {
        mList = list;
    }


}
