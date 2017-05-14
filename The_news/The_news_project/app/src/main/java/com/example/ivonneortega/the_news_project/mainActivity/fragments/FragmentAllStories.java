package com.example.ivonneortega.the_news_project.mainActivity.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivonneortega.the_news_project.mainActivity.ArticleRefreshService;
import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.recyclerViewAdapters.CategoriesRecyclerAdapter;
import com.example.ivonneortega.the_news_project.data.Article;
import com.example.ivonneortega.the_news_project.data.Category;
import com.example.ivonneortega.the_news_project.data.NYTApiData;
import com.example.ivonneortega.the_news_project.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.ivonneortega.the_news_project.data.NYTApiData.JSON;


/**
 * Fragment to show all the stories
 */
public class FragmentAllStories extends Fragment {

    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout mAllRefresh;
    private AsyncTask<String, Void, Boolean> mTask;
    private static final String TAG = "FragmentAllStories";
    private CategoriesRecyclerAdapter mAdapter;
    private List<String> mSources;
    private DatabaseHelper db;

    /**
     * Fragment constructor
     */
    public FragmentAllStories() {
        // Required empty public constructor
    }

    /**
     * Instanciate the fragment
     * @return the fragment
     */
    public static FragmentAllStories newInstance() {
        FragmentAllStories fragment = new FragmentAllStories();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    /**
     * Inflating the fragment with the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view we are inflating
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setSources();
        return inflater.inflate(R.layout.fragment_fragment_all_stories,container,false);
    }

    /**
     * Sources of the second API call
     */
    public void setSources()
    {
        mSources = new ArrayList<>();
        mSources.add("associated-press");
        mSources.add("bbc-news");
        mSources.add("business-insider");
        mSources.add("buzzfeed");
        mSources.add("cnn");
        mSources.add("espn");
    }

    /**
     * Setting the recycler view
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setting Recycler View
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_all_stories);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Creating a list to test recycler view
        List<Article> categoryIndividualItems;

        //Secondary Test
        List<Category> allStories = new ArrayList<>();


        db = DatabaseHelper.getInstance(view.getContext());
        //Setting Adapter With lists
        mAdapter = new CategoriesRecyclerAdapter(allStories);
        recyclerView.setAdapter(mAdapter);
        getAllStoriesArticles();

        mAllRefresh = (SwipeRefreshLayout) view.findViewById(R.id.all_swipe_refresh);
        mAllRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAllStories();
            }
        });
    }

    /**
     * Database call that gets all the articles for each category
     */
    private void getAllStoriesArticles()
    {
        AsyncTask<Void,Void,List<Category>> asyncTask = new AsyncTask<Void, Void, List<Category>>() {
            @Override
            protected List<Category> doInBackground(Void... params) {
                //Creating a list to test recycler view
                List<Article> categoryIndividualItems;

                //Secondary Test
                List<Category> allStories = new ArrayList<>();

                categoryIndividualItems = db.getArticlesByCategory("Business");
                allStories.add(new Category("Business",categoryIndividualItems));
                categoryIndividualItems = db.getArticlesByCategory("Tech");
                allStories.add(new Category("Tech",categoryIndividualItems));
                categoryIndividualItems = db.getArticlesByCategory("World");
                allStories.add(new Category("World",categoryIndividualItems));
                categoryIndividualItems = db.getArticlesByCategory("Health");
                allStories.add(new Category("Health", categoryIndividualItems));
                categoryIndividualItems = db.getArticlesByCategory("u.s.");
                allStories.add(new Category("National", categoryIndividualItems));
                categoryIndividualItems = db.getArticlesByCategory("science");
                allStories.add(new Category("Science", categoryIndividualItems));
                categoryIndividualItems = db.getArticlesByCategory("sports");
                allStories.add(new Category("Sports", categoryIndividualItems));
                categoryIndividualItems = db.getArticlesByCategory("movies");
                allStories.add(new Category("Movies", categoryIndividualItems));
                categoryIndividualItems = db.getArticlesByCategory("fashion");
                allStories.add(new Category("Fashion", categoryIndividualItems));
                categoryIndividualItems = db.getArticlesByCategory("food");
                allStories.add(new Category("Food", categoryIndividualItems));

                //Miscellaneous list
                List<Article> aux = db.getArticlesByCategory("Climate");
                categoryIndividualItems = aux;
                aux = db.getArticlesByCategory("Real");
                copyOneListIntoAnother(categoryIndividualItems,aux);
                aux = db.getArticlesByCategory("Arts");
                copyOneListIntoAnother(categoryIndividualItems,aux);
                allStories.add(new Category("Miscellaneous", categoryIndividualItems));

                return allStories;
            }

            @Override
            protected void onPostExecute(List<Category> list) {
                super.onPostExecute(list);
                mAdapter.swapData(list);
            }
        }.execute();
    }

    /**
     * Copying one list into another
     * @param list1 is the returning list
     * @param list2 is the list we are copying into list1
     * @return the final list
     */
    public List<Article> copyOneListIntoAnother(List<Article> list1, List<Article> list2)
    {
        for(int i=0;i<list2.size();i++)
        {
            list1.add(list2.get(i));
        }
        return list1;
    }

    /**
     * Start an async task to go through each NewsWire topic and refresh the article list for each one.
     * Stops the SwipeRefreshLayout for refreshing in onPostExecute.
     */
    private void refreshAllStories() {
        mTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                boolean updated = false;
                long sum = 0;

                for (String topic : params) {
                    JSONArray articles = getArticles(topic);
                    sum += addArticlesToDatabase(articles);
                }

                if (sum > 0) {
                    updated = true;
                }

                return updated;
            }

            @Override
            protected void onPostExecute(Boolean dbChanged) {
                super.onPostExecute(dbChanged);
                mAllRefresh.setRefreshing(false);
                if (dbChanged) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        mTask.execute("World", "u.s.", "Business Day", "technology", "science", "Sports", "Movies", "fashion+&+style", "Food", "Health");
    }

    /**
     * Method that gets the articles for the API call.
     * @param topic the topic to search for.
     * @return An array of articles in JSON format.
     */
    private JSONArray getArticles(String topic) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(NYTApiData.URL_NEWS_WIRE + topic + JSON + "?api-key=" + NYTApiData.API_KEY)
                .build();

        JSONArray articles = null;

        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonReply = new JSONObject(response.body().string());
            articles = jsonReply.getJSONArray("results");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return articles;
    }

    /**
     * Adds the article to the database
     * @param articles is the article we are adding to the Database
     * @return the id of the new article in the database
     */
    private long addArticlesToDatabase(JSONArray articles) {
        DatabaseHelper db = DatabaseHelper.getInstance(getContext());
        long added = 0;

        for (int i = 0; i < articles.length(); i++) {
            String url;
            String title;
            String date;
            String category;
            String image = null;
            boolean hasImage = false;
            try {
                JSONObject article = articles.getJSONObject(i);
                url = article.getString("url");
                title = article.getString("title");
                date = article.getString("published_date");
                category = article.getString("section");
                if (!article.getString("multimedia").equals("")) {
                    JSONArray multimedia = article.getJSONArray("multimedia");
                    for (int j = 0; j < multimedia.length(); j++) {
                        JSONObject pic = multimedia.getJSONObject(j);
                        if (pic.getString("format").equals("Normal") && pic.getString("type").equals("image")) {
                            image = pic.getString("url");
                            hasImage = true;
                        }
                    }
                }
                String source = "New York Times";
                int isSaved = Article.FALSE;

                if (db.getArticleByUrl(url) == null && hasImage) {
                    db.checkSizeAndRemoveOldest();
                    Log.d(TAG, "doInBackground: " + title);
                    added += db.insertArticleIntoDatabase(image, title, category, date.substring(0, date.indexOf('T')), null, source, isSaved, Article.FALSE, url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return added;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
