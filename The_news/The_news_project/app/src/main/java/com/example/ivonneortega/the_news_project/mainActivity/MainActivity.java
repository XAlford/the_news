package com.example.ivonneortega.the_news_project.mainActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.ivonneortega.the_news_project.categoryView.CategoryViewActivity;
import com.example.ivonneortega.the_news_project.mainActivity.fragments.FragmentAdapterMainActivity;
import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.search.SearchActivity;
import com.example.ivonneortega.the_news_project.settings.SettingsActivity;
import com.example.ivonneortega.the_news_project.data.Article;
import com.example.ivonneortega.the_news_project.data.Category;
import com.example.ivonneortega.the_news_project.database.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    FragmentAdapterMainActivity mAdapter;
    public static final String URL = "https://newsapi.org/v1/articles?source=";
    public static final String API_KEY = "b9742f05aeab45e097c3c57a30ccb224";
    List<String> mSourcesByTop, mSourcesByLatest;
    boolean mStartActivity;
    private boolean theme_changed;
    List<Category> categories_by_top;

    public static final int ARTICLE_REFRESH_JOB = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();

        setSources();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new FragmentAdapterMainActivity(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        categories_by_top = new ArrayList<>();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.root_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Article article = DatabaseHelper.getInstance(this).getArticlesById(1);
        if(article!=null)
        {
            View hView =  navigationView.getHeaderView(0);
            ImageView nav_user = (ImageView) hView.findViewById(R.id.navigation_image);
            Picasso.with(this)
                    .load(article.getImage())
                    .fit()
                    .into(nav_user);
        }

        ImageButton optionsToolbar = (ImageButton) findViewById(R.id.options_toolbar);
        optionsToolbar.setClickable(true);
        ImageButton searchToolbar = (ImageButton) findViewById(R.id.search_toolbar);
        searchToolbar.setClickable(true);
        optionsToolbar.setOnClickListener(this);
        searchToolbar.setOnClickListener(this);

        setupJob();

        //TODO FOR NEWS API CALL
//        for(int i = 0; i< mSourcesByTop.size(); i++)
//        {
//            searchArticlesByTop(mSourcesByTop.get(i));
//        }
//        for(int i=0;i<mSourcesByLatest.size();i++)
//        {
//            searchArticlesByLatest(mSourcesByLatest.get(i));
//        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==2)
                    mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * Set up a scheduled job to automatically check for new articles in the background.
     * Only runs if the device is plugged in and on a network
     */
    private void setupJob() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(this, ArticleRefreshService.class);

        JobInfo refreshJob = new JobInfo.Builder(ARTICLE_REFRESH_JOB, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresCharging(true)
                .build();

        jobScheduler.schedule(refreshJob);
    }

    /**
     * Checks if the theme has changed, if so, update the views
     */
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.ivonneortega.the_news_project.Settings", Context.MODE_PRIVATE);
        theme_changed = sharedPreferences.getBoolean(SettingsActivity.THEME_HAS_CHANGED,false);
        if(mAdapter!=null)
        {
            mAdapter.notifyDataSetChanged();
        }

        if(mStartActivity){
            mStartActivity = false;

        } else if (theme_changed){
            SharedPreferences sharedPreferences1 = getSharedPreferences("com.example.ivonneortega.the_news_project.Settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putBoolean(SettingsActivity.THEME_HAS_CHANGED,false);
            editor.apply();
            finish();
            startActivity(getIntent());
        }
    }

    /**
     * On back button pressed
     * Navigation Drawer method
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    /**
     * Handles the clicks on each item on the navigation drawer and launches Category View
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        List<String> categories = new ArrayList<>();

        if (id == R.id.nav_world) {
            moveToCategoryViewActivity("World");
        } else if (id == R.id.nav_politics) {
            moveToCategoryViewActivity("Politics");
        } else if (id == R.id.nav_business) {
            moveToCategoryViewActivity("Business Day");
        } else if (id == R.id.nav_technology) {
            moveToCategoryViewActivity("Technology");
        }
        else if (id == R.id.nav_science) {
        moveToCategoryViewActivity("Science");
        }
        else if (id == R.id.nav_sports) {
            moveToCategoryViewActivity("Sports");
        }
        else if (id == R.id.nav_movies) {
            moveToCategoryViewActivity("Movies");
        }
        else if (id == R.id.nav_fashion) {
            moveToCategoryViewActivity("Fashion");
        }
        else if (id == R.id.nav_food) {
            moveToCategoryViewActivity("Food");
        }
        else if (id == R.id.nav_health) {
            moveToCategoryViewActivity("Health");
        }
        else if (id == R.id.nav_miscellaneous) {
            moveToCategoryViewActivity("Miscellaneous");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * On click for each item in the toolbar
     * @param v is the view that has been clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.options_toolbar:
                moveToSettingsActivity();
                break;
            case R.id.search_toolbar:
                moveToSearchActivity();
                break;
        }

    }

    /**
     * Launch Search Activity
     */
    public void moveToSearchActivity()
    {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    /**
     * Launch Category Activity
     * @param category is the category we are going to display in Category Activity
     */
    public void moveToCategoryViewActivity(String category)
    {
        Intent intent = new Intent(this, CategoryViewActivity.class);
        intent.putExtra(DatabaseHelper.COL_CATEGORY,category);
        startActivity(intent);
    }

    /**
     * Launch Settings Activity
     */
    public void moveToSettingsActivity()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Sets the theme that gets from a Shared preference
     * sets mStartActivity to true so that onResume can verify is the app was already started or not
     */
    public void setTheme()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.ivonneortega.the_news_project.Settings", Context.MODE_PRIVATE);
        String str = sharedPreferences.getString(SettingsActivity.THEME,"DEFAULT"); //Initial value of the String is "Hello"
        if(str.equals("dark"))
        {
            setTheme(R.style.DarkTheme);
            setContentView(R.layout.activity_main);
            findViewById(R.id.root_toolbar).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTheme));
        }
        else
        {
            setContentView(R.layout.activity_main);

        }
        mStartActivity=true;

    }

    /**
     * METHOD FOR SECOND API CALL
     */
    //TODO DON'T DELETE THIS
//    public void searchArticlesByTop(final String source) {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        final DatabaseHelper db = DatabaseHelper.getInstance(this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//                URL +source+ "&apiKey=" + API_KEY, null,
//                new com.android.volley.Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONObject root = response;
//                            JSONArray articles = root.getJSONArray("articles");
//                            for(int i=0;i<articles.length();i++)
//                            {
//                                JSONObject article = articles.getJSONObject(i);
//                                String articleString = article.getString("title");
//                                String description = article.getString("description");
//                                String url = article.getString("url");
//                                String image = article.getString("urlToImage");
//                                String date = article.getString("publishedAt");
//                                if(date.length()>5)
//                                    date.substring(0,5);
//                               long insert = db.insertArticleIntoDatabase(
//                                        image,articleString,source,date,description,source,Article.FALSE,
//                                        Article.FALSE,url);
//
//                                Log.d("THIS", "onResponse: "+insert);
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        queue.add(jsonObjectRequest);
//    }
//     TODO DON'T DELETE THIS
//    public void searchArticlesByLatest(final String source) {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        final DatabaseHelper db = DatabaseHelper.getInstance(this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//                URL +source+ "&apiKey=" + API_KEY+"&orderBy=latest", null,
//                new com.android.volley.Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONObject root = response;
//                            JSONArray articles = root.getJSONArray("articles");
//                            for(int i=0;i<articles.length();i++)
//                            {
//                                JSONObject article = articles.getJSONObject(i);
//                                String articleString = article.getString("title");
//                                String description = article.getString("description");
//                                String url = article.getString("url");
//                                String image = article.getString("urlToImage");
//                                String date = article.getString("publishedAt");
//                                if(date.length()>5)
//                                    date.substring(0,5);
//                                long insert = db.insertArticleIntoDatabase(
//                                        image,articleString,source,date,description,source,Article.FALSE,
//                                        Article.TRUE,url);
//
//                                Log.d("THIS", "onResponse: "+insert);
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        queue.add(jsonObjectRequest);
//    }

    //TODO DON'T DELETE THIS
    public void setSources()
    {
        mSourcesByTop = new ArrayList<>();
        mSourcesByTop.add("associated-press");
        mSourcesByTop.add("bbc-news");
        mSourcesByTop.add("business-insider");
        mSourcesByTop.add("buzzfeed");
        mSourcesByTop.add("cnn");
        mSourcesByTop.add("espn");

        mSourcesByLatest = new ArrayList<>();
        mSourcesByLatest.add("buzzfeed");
    }
}
