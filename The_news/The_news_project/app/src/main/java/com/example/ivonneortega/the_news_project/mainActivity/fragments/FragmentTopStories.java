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
import com.example.ivonneortega.the_news_project.recyclerViewAdapters.ArticlesVerticalRecyclerAdapter;
import com.example.ivonneortega.the_news_project.data.Article;
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
 * Fragment to show only the top stories
 */
public class FragmentTopStories extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArticlesVerticalRecyclerAdapter mAdapter;


    private SwipeRefreshLayout mTopRefresh;
    private static final String TAG = "FragmentTopStories";
    private AsyncTask<String, Void, Boolean> mTask;
    private DatabaseHelper db;

    private OnFragmentInteractionListener mListener;

    /**
     * Fragment constructor
     */
    public FragmentTopStories() {
        // Required empty public constructor
    }

    /**
     * Instanciating the fragment
     * @return the fragment
     */
    public static FragmentTopStories newInstance() {
        FragmentTopStories fragment = new FragmentTopStories();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * On create fragment method
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    /**
     * Inflating the view with the xml file
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_top_stories, container, false);
    }

    /**
     * Setting the recycler view and recycler view adapter with a list of top story articles
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setting Recycler View
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.top_stories_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Creating a list to test recycler view
        List<Article> categoryIndividualItems = new ArrayList<>();
        db = DatabaseHelper.getInstance(view.getContext());

        mAdapter = new ArticlesVerticalRecyclerAdapter(categoryIndividualItems,false);
        recyclerView.setAdapter(mAdapter);
        getTopStoriesArticles();

        mTopRefresh = (SwipeRefreshLayout) view.findViewById(R.id.top_swipe_refresh);
        mTopRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTopStories();
            }
        });
    }

    /**
     * Database call to get all the articles in the database that are top stories
     */
    private void getTopStoriesArticles()
    {
        AsyncTask<Void,Void,List<Article>> asyncTask = new AsyncTask<Void, Void, List<Article>>() {
            @Override
            protected List<Article> doInBackground(Void... params) {
                List<Article> list = db.getTopStoryArticles();
                return list;
            }

            @Override
            protected void onPostExecute(List<Article> list) {
                super.onPostExecute(list);
                mAdapter.swapData(list);
            }
        }.execute();
    }

    /**
     * Handle the refresh top story fragment when user scroll down
     */
    private void refreshTopStories() {
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
                mTopRefresh.setRefreshing(false);
                if (dbChanged) {
                    List<Article> newArticles = DatabaseHelper.getInstance(getContext()).getTopStoryArticles();
                    mAdapter.swapData(newArticles);
                }
            }
        };
        mTask.execute("world", "politics", "business", "technology", "science", "sports", "movies", "fashion", "food", "health");
    }

    /**
     * Getting the articles that are top story for New York Times API
     * @param topic
     * @return
     */
    private JSONArray getArticles(String topic) {
        OkHttpClient client = new OkHttpClient();

        String url = NYTApiData.URL_TOP_STORY + topic + JSON + "?api-key=" + NYTApiData.API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        JSONArray articles = null;

        try {
            Response response = client.newCall(request).execute();
            String reply = response.body().string();
            JSONObject jsonReply = new JSONObject(reply);
            if (jsonReply.has("results")) {
                articles = jsonReply.getJSONArray("results");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return articles;
    }

    /**
     * Adding each article to the Database
     * @param articles is the article we are adding to the Database
     * @return the id of the new article in the database
     */
    private long addArticlesToDatabase(JSONArray articles) {
        DatabaseHelper db = DatabaseHelper.getInstance(getContext());
        long added = 0;

        if (articles != null) {
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
                        //Log.d(TAG, "doInBackground: " + title);
                        added += db.insertArticleIntoDatabase(image, title, category, date.substring(0, date.indexOf('T')), null, source, isSaved, Article.TRUE, url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
