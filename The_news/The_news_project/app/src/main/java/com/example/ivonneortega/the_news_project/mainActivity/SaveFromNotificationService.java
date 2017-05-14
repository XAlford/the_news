package com.example.ivonneortega.the_news_project.mainActivity;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.ivonneortega.the_news_project.database.DatabaseHelper;

/**
 * Allow new top articles to be added to the saved list directly from a notification
 * Created by Makalaster on 5/3/17.
 */

public class SaveFromNotificationService extends IntentService {

    public SaveFromNotificationService() {
        super("SaveFromNotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        long id = 0;
        if (intent != null) {
            id = intent.getLongExtra(DatabaseHelper.COL_ID, -1);
        }
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        db.saveArticle(id);
    }
}
