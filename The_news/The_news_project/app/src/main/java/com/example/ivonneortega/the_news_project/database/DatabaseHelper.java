package com.example.ivonneortega.the_news_project.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ivonneortega.the_news_project.data.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Helper class. Used to store articles locally on the device.
 * Created by WilliamAlford on 4/30/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    //Database version and name
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "news.db";

    //Articles table
    public static final String TABLE_ARTICLES = "articles";

    //Articles table columns
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_BODY = "body";
    public static final String COL_DATE = "date";
    public static final String COL_SOURCE = "source";
    public static final String COL_IS_SAVED = "isSaved";
    public static final String COL_CATEGORY = "category";
    public static final String COL_IMAGE = "image";
    public static final String COL_IS_TOP_STORY = "isTopStory";
    public static final String COL_URL = "url";

    //Articles table creation string
    private static final String CREATE_TABLE_ARTICLES = "CREATE TABLE " + TABLE_ARTICLES + " (" +
            COL_ID + " INTEGER PRIMARY KEY, " +
            COL_TITLE + " TEXT, " +
            COL_BODY + " TEXT, " +
            COL_DATE + " TEXT, " +
            COL_SOURCE + " TEXT, " +
            COL_IS_SAVED + " INTEGER, " +
            COL_CATEGORY + " TEXT, " +
            COL_IMAGE + " TEXT, " +
            COL_IS_TOP_STORY + " INTEGER, " +
            COL_URL + " TEXT" + ")";

    //Singleton instance variable
    private static DatabaseHelper sInstance;

    /**
     * Private constructor. Can only be called from within the singleton.
     * @param context Per the getInstance method, this is always the application context.
     */
    private DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * The public method by which other activities, services, and classes can call and access the database.
     * @param context the context in which the helper is called.
     * @return the instance of the Database Helper singleton
     */
    public static DatabaseHelper getInstance(Context context){
        if(sInstance == null){
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ARTICLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);

        onCreate(db);
    }

    /**
     * Retrieve an article from the database by its ID.
     * @param id The ID of the article.
     * @return The article with the given ID, or null if nothing was found.
     */
    public Article getArticlesById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_ARTICLES,
                null,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        Article articles = null;

        if (cursor.moveToFirst()) {

            articles = new Article(
                    cursor.getLong(cursor.getColumnIndex(COL_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(COL_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COL_CATEGORY)),
                    cursor.getString(cursor.getColumnIndex(COL_DATE)),
                    cursor.getString(cursor.getColumnIndex(COL_BODY)),
                    cursor.getString(cursor.getColumnIndex(COL_SOURCE)),
                    cursor.getInt(cursor.getColumnIndex(COL_IS_SAVED)),
                    cursor.getInt(cursor.getColumnIndex(COL_IS_TOP_STORY)),
                    cursor.getString(cursor.getColumnIndex(COL_URL))

                    );
            cursor.close();
            return articles;
        }
        else
        {
            cursor.close();
            return null;
        }
    }

    /**
     * Insert an article into the database from raw attributes
     * @param image The URL of the article's image.
     * @param title The article's headline.
     * @param category The category of the article.
     * @param date The publication date of the article.
     * @param body The first paragraph of the article.
     * @param source The source of the article. In the current scope of this project, it is always "New York Times".
     * @param isSaved A value that determines whether the user has saved the article.
     * @param isTopStory A value that determines whether the article came from the New York Times' top stories feed.
     * @param url The source URL of the article.
     * @return The ID of the newly inserted article.
     */
    public long insertArticleIntoDatabase(String image, String title, String category, String date,
                                          String body, String source, int isSaved, int isTopStory, String url) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_CATEGORY, category);
        values.put(COL_DATE, date);
        values.put(COL_BODY, body);
        values.put(COL_SOURCE, source);
        values.put(COL_IS_SAVED, isSaved);
        values.put(COL_IMAGE, image);
        values.put(COL_IS_TOP_STORY, isTopStory);
        values.put(COL_URL, url);

        return  db.insert(TABLE_ARTICLES, null, values);
    }

    /**
     * Delete an individual article from the database based on its ID.
     * @param id The ID of the article to delete.
     */
    public void deleteIndividualArticlesFromDatabase(long id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ARTICLES, COL_ID+" = ?", new String[]{String.valueOf(id)});
    }

    /**
     * Method to return whether there is anything in the database.
     * @return True if there are any items in the database. Otherwise false.
     */
    public boolean isDatabaseEmpty()
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_ARTICLES,null,null,null,null,null,null);
        boolean hasSomething = false;

        if(cursor.moveToFirst())
        {
            hasSomething = true;
        }
        cursor.close();
        return hasSomething;
    }

    /**
     * Update an article to mark it as saved by the user.
     * @param id The ID of the article to mark as saved.
     */
    public void saveArticle(long id){
        SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_IS_SAVED,Article.TRUE);
            db.update(TABLE_ARTICLES,
                    values,
                    COL_ID + " = ?",
                    new String[]{String.valueOf(id)}
            );
    }

    /**
     * Update an article to mark it as no longer saved by the user.
     * @param id The ID of the article to mark as no longer saved.
     */
    public void unSaveArticle(long id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_IS_SAVED,Article.FALSE);
        db.update(TABLE_ARTICLES,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    /**
     * TODO find out what this is actually supposed to do
     */
    public void deleteAllSavedArticles(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_ARTICLES,null,null,null,null,null,null);

        if(cursor.moveToFirst())
        {
            while(!cursor.isAfterLast()) {
                deleteIndividualArticlesFromDatabase(cursor.getLong(cursor.getColumnIndex(COL_ID)));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    /**
     * Retrieve articles from the database based on title and category.
     * @param query The string by which to search the database.
     * @return A list of articles matching the query in the title or category.
     */
    public List<Article> searchArticles(String query){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_ARTICLES, // a. table
                null, // b. column names
                COL_TITLE + " LIKE ? OR " + COL_CATEGORY + " LIKE ?", // c. selections
                new String[]{"%" + query + "%", "%" + query + "%"}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        List<Article> articles = new ArrayList<>();

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                articles.add( new Article(
                        cursor.getLong(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(COL_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COL_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(COL_DATE)),
                        cursor.getString(cursor.getColumnIndex(COL_BODY)),
                        cursor.getString(cursor.getColumnIndex(COL_SOURCE)),
                        cursor.getInt(cursor.getColumnIndex(COL_IS_SAVED)),
                        cursor.getInt(cursor.getColumnIndex(COL_IS_TOP_STORY)),
                        cursor.getString(cursor.getColumnIndex(COL_URL)))
                );

                cursor.moveToNext();
            }
        }
        cursor.close();
        return articles;
    }

    /**
     * Retrieve a full list of all items in the database designated as Top Stories.
     * @return A list containing articles that are marked as top stories.
     */
    public List<Article> getTopStoryArticles() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ARTICLES, // a. table
                null, // b. column names
                COL_IS_TOP_STORY + " = ?", // c. selections
                new String[]{String.valueOf(Article.TRUE)}, // d. selections args
                null, // e. group by
                null, // f. having
                COL_ID + " DESC", // g. order by
                null); // h. limit

        List<Article> articles = new ArrayList<>();

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                articles.add( new Article(
                        cursor.getLong(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(COL_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COL_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(COL_DATE)),
                        cursor.getString(cursor.getColumnIndex(COL_BODY)),
                        cursor.getString(cursor.getColumnIndex(COL_SOURCE)),
                        cursor.getInt(cursor.getColumnIndex(COL_IS_SAVED)),
                        cursor.getInt(cursor.getColumnIndex(COL_IS_TOP_STORY)),
                        cursor.getString(cursor.getColumnIndex(COL_URL)))

                );

                cursor.moveToNext();
            }
        }
        cursor.close();
        return articles;
    }

    /**
     * Retrieve a list of all items in the database marked as saved by the user.
     * @return A list containing articles marked as saved by the user.
     */
    public List<Article> getSavedArticles() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ARTICLES, // a. table
                null, // b. column names
                COL_IS_SAVED + " = ?", // c. selections
                new String[]{String.valueOf(Article.TRUE)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        List<Article> articles = new ArrayList<>();

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                articles.add( new Article(
                        cursor.getLong(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(COL_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COL_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(COL_DATE)),
                        cursor.getString(cursor.getColumnIndex(COL_BODY)),
                        cursor.getString(cursor.getColumnIndex(COL_SOURCE)),
                        cursor.getInt(cursor.getColumnIndex(COL_IS_SAVED)),
                        cursor.getInt(cursor.getColumnIndex(COL_IS_TOP_STORY)),
                        cursor.getString(cursor.getColumnIndex(COL_URL)))

                );

                cursor.moveToNext();
            }
        }
        cursor.close();
        return articles;
    }

    /**
     * Retrieve a list of articles that match a given category.
     * @param query The category, as a string, that should be searched for.
     * @return A list of articles in the given category.
     */
    public List<Article> getArticlesByCategory(String query) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ARTICLES, // a. table
                null, // b. column names
                COL_CATEGORY + " LIKE ?", // c. selections
                new String[]{"%" + query + "%"}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        List<Article> articles = new ArrayList<>();

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                articles.add( new Article(
                        cursor.getLong(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(COL_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COL_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(COL_DATE)),
                        cursor.getString(cursor.getColumnIndex(COL_BODY)),
                        cursor.getString(cursor.getColumnIndex(COL_SOURCE)),
                        cursor.getInt(cursor.getColumnIndex(COL_IS_SAVED)),
                        cursor.getInt(cursor.getColumnIndex(COL_IS_TOP_STORY)),
                        cursor.getString(cursor.getColumnIndex(COL_URL)))
                );

                cursor.moveToNext();
            }
        }
        cursor.close();
        return articles;
    }

    /**
     * Check to see whether a given article has a value for its body text.
     * @param id The ID of the article to check.
     * @return The body text if it exists, otherwise null.
     */
    public String isThereAParagraph(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_ARTICLES, // a. table
                null, // b. column names
                COL_ID + " = ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        String aux = null;
        if(cursor.moveToFirst())
        {
            if(cursor.getString(cursor.getColumnIndex(COL_BODY)) == null)
            {
                return null;
            }
             aux = cursor.getString(cursor.getColumnIndex(COL_BODY));
        }

        Log.d(TAG, "isThereAParagraph: " + aux);
        cursor.close();
        return aux;
    }

    /**
     * Adds a body paragraph to an article in the database.
     * @param id The ID of the article for which a paragraph is to be added.
     * @param paragraph A string which will be added to the article.
     */
    public void addParagraph(long id, String paragraph) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BODY,paragraph);
        db.update(TABLE_ARTICLES,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    /**
     * Retrieve an article from the database based on a given URL.
     * @param url The URL of the article to be searched for.
     * @return An article that has a URL matching the input.
     */
    public Article getArticleByUrl(String url) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_ARTICLES,
                null,
                COL_URL + " = ?",
                new String[]{url},
                null, null, null);

        Article article = null;

        if (cursor.moveToFirst()) {
            article = new Article(cursor.getLong(cursor.getColumnIndex(COL_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(COL_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COL_CATEGORY)),
                    cursor.getString(cursor.getColumnIndex(COL_DATE)),
                    cursor.getString(cursor.getColumnIndex(COL_BODY)),
                    cursor.getString(cursor.getColumnIndex(COL_SOURCE)),
                    cursor.getInt(cursor.getColumnIndex(COL_IS_SAVED)),
                    cursor.getInt(cursor.getColumnIndex(COL_IS_TOP_STORY)),
                    cursor.getString(cursor.getColumnIndex(COL_URL)));
        }

        cursor.close();

        return article;
    }

    /**
     * Checks the size of the database and trims it by removing the oldest items.
     */
    public void checkSizeAndRemoveOldest() {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_ARTICLES, null, null, null, null, null, COL_DATE + " DESC");
        boolean oldestRemoved = false;

        if (cursor.moveToFirst() && cursor.getCount() > 1000) {
            while (!oldestRemoved && !cursor.isAfterLast() && cursor.getCount() > 1000) {
                if (cursor.getInt(cursor.getColumnIndex(COL_IS_SAVED)) == Article.FALSE) {
                    long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
                    Log.d(TAG, "checkSizeAndRemoveOldest: id - " + id);
                    Log.d(TAG, "checkSizeAndRemoveOldest: count - " + cursor.getCount());
                    deleteIndividualArticlesFromDatabase(id);

                    oldestRemoved = true;
                }
                cursor.moveToNext();
            }
        }

        cursor.close();
    }
}
