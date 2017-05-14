package com.example.ivonneortega.the_news_project.mainActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ivonneortega.the_news_project.detailView.CollectionDemoActivity;
import com.example.ivonneortega.the_news_project.settings.SettingsActivity;
import com.example.ivonneortega.the_news_project.data.Article;
import com.example.ivonneortega.the_news_project.data.NYTApiData;
import com.example.ivonneortega.the_news_project.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.ivonneortega.the_news_project.data.NYTApiData.JSON;

public class ArticleRefreshService extends JobService {
    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = "ArticleRefreshService";

    private List<String> mNewsWireList = new ArrayList<>();
    private List<String> mTopStoriesList = new ArrayList<>();
    private DatabaseHelper mDb;

    /**
     * Set up lists of topics in onCreate so the lists can be used throughout the service.
     * Also make sure the database instance is set up here.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mDb = DatabaseHelper.getInstance(this);

        if (mNewsWireList.isEmpty()) {
            mNewsWireList.add("World");
            mNewsWireList.add("u.s.");
            mNewsWireList.add("Business+Day");
            mNewsWireList.add("technology");
            mNewsWireList.add("science");
            mNewsWireList.add("Sports");
            mNewsWireList.add("Movies");
            mNewsWireList.add("fashion+&+style");
            mNewsWireList.add("Food");
            mNewsWireList.add("Health");
        }

        if (mTopStoriesList.isEmpty()) {
            mTopStoriesList.add("world");
            mTopStoriesList.add("politics");
            mTopStoriesList.add("business");
            mTopStoriesList.add("technology");
            mTopStoriesList.add("science");
            mTopStoriesList.add("sports");
            mTopStoriesList.add("movies");
            mTopStoriesList.add("fashion");
            mTopStoriesList.add("food");
            mTopStoriesList.add("health");
        }
    }

    /**
     * Method to handle what happens when the job starts. First the connectivity state is checked.
     * If there is no network connectivity, nothing happens. Otherwise, two loops are run. One loop
     * runs a series of AsyncTasks to query the top stories api and the other queries the newswire API
     * in order. The top stories tasks are run in the background because they need to be delayed a
     * small amount to prevent too many API calls per second, but not pause the main thread of the
     * application.
     * @param params
     * @return
     */
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: REFRESH JOB STARTED");

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            for (String topic : mTopStoriesList) {
                AsyncTask<String, Void, Void> topTask = new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        queryTopStories(params[0]);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
                topTask.execute(topic);
            }

            for (String topic : mNewsWireList) {
                queryNewsWire(topic);
            }

            jobFinished(params, true);
        }

        return true;
    }

    /**
     * Method to query the top stories API endpoint. Each retrieved article is sent to
     * the addArticleToDatabase method to be added to the local database.
     * @param query The topic for which to find articles.
     */
    private void queryTopStories(final String query) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                NYTApiData.URL_TOP_STORY + query + JSON + "?api-key=" + NYTApiData.API_KEY, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            JSONArray results = response.getJSONArray("results");
                            for(int i = 0; i < results.length(); i++)
                            {
                                JSONObject article = results.getJSONObject(i);
                                addArticleToDatabase(article, Article.TRUE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error retrieving top stories for " + query, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onErrorResponse: " + error);
            }
        });

        queue.add(jsonObjectRequest);
    }

    /**
     * Method to query the newswire API endpoint. Each retrieved article is sent to the
     * addArticleToDatabase method to be added to the local database.
     * @param query the topic for which to find articles.
     */
    private void queryNewsWire(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                NYTApiData.URL_NEWS_WIRE + query + JSON + "?api-key=" + NYTApiData.API_KEY, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            JSONArray results = response.getJSONArray("results");

                            for(int i = 0; i < results.length(); i++)
                            {
                                JSONObject article = results.getJSONObject(i);
                                addArticleToDatabase(article, Article.FALSE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onErrorResponse: "+error);
            }
        });
        queue.add(jsonObjectRequest);
    }

    /**
     * Method to add an article to the database. Creates an article from a JSONObject, then checks to
     * make sure the article is not already present in the database before adding. Also makes sure the
     * database has not grown too large. If so, old items are removed before adding new ones.
     * Generates a notification for the user if the article is a top story.
     * @param object The raw JSON data of the article to be added.
     * @param fromTopStories An int that tells the database whether the article is a top story
     */
    public void addArticleToDatabase(JSONObject object, final int fromTopStories) {
        AsyncTask<JSONObject, Void, Void> dbTask = new AsyncTask<JSONObject, Void, Void>() {
            @Override
            protected Void doInBackground(JSONObject... params) {
                JSONObject object = params[0];

                String url = null;
                String title = null;
                String date = null;
                String category = null;
                String image = null;
                boolean hasImage = false;
                try {
                    url = object.getString("url");
                    title = object.getString("title");
                    date = object.getString("published_date");
                    category = object.getString("section");
                    if (!object.getString("multimedia").equals("")) {
                        JSONArray multimedia = object.getJSONArray("multimedia");
                        for (int i = 0; i < multimedia.length(); i++) {
                            JSONObject pic = multimedia.getJSONObject(i);
                            if (pic.getString("format").equals("Normal") && pic.getString("type").equals("image")) {
                                image = pic.getString("url");
                                hasImage = true;
                            }
                        }
                    }
                } catch (JSONException e) {
                    hasImage = false;
                    e.printStackTrace();
                }
                String source = "New York Times";
                int isSaved = Article.FALSE;

                //Only add the article to the database if it has an image
                if (mDb.getArticleByUrl(url) == null && hasImage) {
                    mDb.checkSizeAndRemoveOldest();
                    Log.d(TAG, "doInBackground: " + title);
                    long id = mDb.insertArticleIntoDatabase(image, title, category, date.substring(0, date.indexOf('T')), null, source, isSaved, fromTopStories, url);

                    if (fromTopStories == Article.TRUE) {
                        generateNotification(title, id);
                    }
                }

                return null;
            }
        };
        dbTask.execute(object);
    }

    /**
     * A notification that is shown when a new top story is added to the database in the background.
     * Tapping the notification opens the detail view of the article. The notification also includes
     * a button that can be used to add the article to the read later list.
     * @param title The title of the article, to be displayed in the notification.
     * @param id The ID of the article, so it can be retrieved and displayed in the app.
     */
    private void generateNotification(String title, long id) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.ivonneortega.the_news_project.Settings", Context.MODE_PRIVATE);
        int notification = sharedPreferences.getInt(SettingsActivity.NOTIFICATION,SettingsActivity.TRUE);
        if(notification==SettingsActivity.TRUE) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());

            Intent openArticleIntent = new Intent(this, CollectionDemoActivity.class);
            openArticleIntent.putExtra(DatabaseHelper.COL_ID, id);
            openArticleIntent.putExtra(CollectionDemoActivity.TYPE_OF_INTENT,"allStories");
            PendingIntent openDetails = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), openArticleIntent, 0);

            Intent saveArticleIntent = new Intent(this, SaveFromNotificationService.class);
            saveArticleIntent.putExtra(DatabaseHelper.COL_ID, id);
            PendingIntent saveArticle = PendingIntent.getService(this, (int) System.currentTimeMillis(), saveArticleIntent, 0);

            notificationBuilder.setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setAutoCancel(true)
                    .setContentTitle("New top news:")
                    .setContentText(title)
                    .setContentIntent(openDetails)
                    .setOngoing(false)
                    .addAction(android.R.drawable.ic_input_add, "Save", saveArticle);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob: JOB STOPPED");
        return false;
    }
}

