package com.example.ivonneortega.the_news_project.categoryView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.recyclerViewAdapters.ArticlesVerticalRecyclerAdapter;
import com.example.ivonneortega.the_news_project.search.SearchActivity;
import com.example.ivonneortega.the_news_project.settings.SettingsActivity;
import com.example.ivonneortega.the_news_project.data.Article;
import com.example.ivonneortega.the_news_project.database.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Category View Activity
 * Showing all the articles within one category
 */

public class CategoryViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    ImageButton mSearch, mOptions;
    RecyclerView mRecyclerView;
    ArticlesVerticalRecyclerAdapter mAdapter;
    String mCategory;
    List<Article> mList;
    boolean mStartActivity;
    View hView;
    ImageView nav_user;
    NavigationView navigationView;
    ProgressBar mProgressBar;
    private boolean theme_changed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme();
        settingUpTheViews();
        mList = new ArrayList<>();
    }

    /**
     * change the adapter everytime an item from the navigation drawer is clicked
     * if mStartActivity is true it means the activity just started
     * if mStartActivity is false it means the activity already started so it checks the theme from
     * shared preferences to see if the theme has changed, if the theme changed then the activity needs to be relaunched
     */
    @Override
    protected void onResume() {
        super.onResume();

        if(mAdapter!=null)
        {
            mAdapter.notifyDataSetChanged();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.ivonneortega.the_news_project.Settings", Context.MODE_PRIVATE);
        theme_changed = sharedPreferences.getBoolean(SettingsActivity.THEME_HAS_CHANGED,false);

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
     * sets the theme, taking the extra from shared preferences and then setting the content views
     */
    public void setTheme()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.ivonneortega.the_news_project.Settings", Context.MODE_PRIVATE);
        String str = sharedPreferences.getString(SettingsActivity.THEME,"DEFAULT"); //Initial value of the String is "Hello"
        if(str.equals("dark"))
        {
            setTheme(R.style.DarkTheme);
            setContentView(R.layout.activity_category_view);
            findViewById(R.id.root_toolbar).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTheme));
        }
        else
        {
            setContentView(R.layout.activity_category_view);

        }
        mStartActivity=true;
    }

    /**
     * On Back Pressed method from navigation drawer
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Navigation Drawer
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String category = null;
        switch (id) {
            case R.id.nav_world:
                category = "World";
                break;
            case R.id.nav_politics:
                category = "Politics";
                break;
            case R.id.nav_business:
                category = "Business Day";
                break;
            case R.id.nav_technology:
                category = "Technology";
                break;
            case R.id.nav_science:
                category = "Science";
                break;
            case R.id.nav_sports:
                category = "Sports";
                break;
            case R.id.nav_movies:
                category = "Movies";
                break;
            case R.id.nav_fashion:
                category = "Fashion";
                break;
            case R.id.nav_food:
                category = "Food";
                break;
            case R.id.nav_health:
                category = "Health";
                break;
            case R.id.nav_miscellaneous:
                category = "Miscellaneous";
                break;
            }

        moveToCategoryViewActivity(category);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Launch Category Activity passing the category as an intent
     * @param category
     */
    public void moveToCategoryViewActivity(String category)
    {
        Intent intent = new Intent(this, CategoryViewActivity.class);
        intent.putExtra(DatabaseHelper.COL_CATEGORY,category);
        startActivity(intent);
        finish();
    }

    /**
     * Setting up all the views
     * including the navigation drawer, setting the toolbar title to reference the category
     * and the recycler view adapter
     */
    public void settingUpTheViews()
    {

        Toolbar toolbar = (Toolbar) findViewById(R.id.root_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mCategory = intent.getStringExtra(DatabaseHelper.COL_CATEGORY);
        getSupportActionBar().setTitle(mCategory);



        //NAVIGATION DRAWER SET UP
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Setting up views and click listener
        mSearch = (ImageButton) findViewById(R.id.search_toolbar);
        mOptions = (ImageButton) findViewById(R.id.options_toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.category_progress);
        mSearch.setClickable(true);
        mOptions.setClickable(true);
        mSearch.setOnClickListener(this);
        mOptions.setOnClickListener(this);


        //Setting up recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.category_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        List<Article> categoryIndividualItems = new ArrayList<>();

        mAdapter = new ArticlesVerticalRecyclerAdapter(categoryIndividualItems,false);
        mRecyclerView.setAdapter(mAdapter);

        getList(mCategory);

    }

    /**
     * Handle the click for both icons in the tool bar
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.search_toolbar:
                moveToSearchActivity();
                break;
            case R.id.options_toolbar:
                moveToSettingsActivity();
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
     * Launch Settings Activity
     */
    public void moveToSettingsActivity()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    /**
     * Sets a list of all the sub categories within an specific category
     * @param category is the category that's currently displaying
     */
    public void getList(String category)
    {
        List<String> categories = new ArrayList<>();
        switch (category) {
            case "World":
                categories.add("World");
                break;
            case "Politics":
            case "U.S.":
                categories.add("u.s");
                categories.add("Politics");
                break;
            case "Business Day":
                categories.add("Business Day");
                break;
            case "Technology":
                categories.add("Technology");
                break;
            case "Science":
                categories.add("Science");
                break;
            case "Sports":
                categories.add("Sports");
                break;
            case "Movies":
                categories.add("Movies");
                categories.add("Teather");
                break;
            case "Fashion":
            case "Fashion & Style":
                categories.add("Fashion");
                categories.add("Style");
                break;
            case "Food":
                categories.add("food");
                break;
            case "Health":
                categories.add("Health");
                categories.add("Well");
                break;
            case "Miscellaneous":
            case "Climate":
            case "Real":
            case "Arts":
                categories.add("Climate");
                categories.add("Real");
                categories.add("Arts");
                categories.add("The Upshot");
                categories.add("Opinion");
                categories.add("Times");
                categories.add("Technology");
                categories.add("Magazine");
                categories.add("N.Y./Region");
                categories.add("T Magazine Travel");
                break;
        }
        Log.d("THE CATEGORY IS", "getList: "+category);
        getListWithArticlesByCategory(categories);
    }


    /**
     * From a list of categories make a database call for each category and stores them in a list
     * @param categories is the list of categories
     */
    public void getListWithArticlesByCategory(final List<String> categories)
    {
        final DatabaseHelper db = DatabaseHelper.getInstance(this);

        AsyncTask<List<String>,Void,List<Article>> asyncTask = new AsyncTask<List<String>, Void, List<Article>>() {
            @Override
            protected List<Article> doInBackground(List<String>... params) {
                List<Article> articles = new ArrayList<>();
                List<Article> aux;
                for(int i=0;i<categories.size();i++)
                {
                    aux = db.getArticlesByCategory(categories.get(i));
                    articles = copyOneListIntoAnother(articles,aux);
                }
                return articles;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressBar.setVisibility(View.VISIBLE);


            }

            @Override
            protected void onPostExecute(List<Article> list) {
                super.onPostExecute(list);
                mProgressBar.setVisibility(View.GONE);
                mAdapter.swapData(list);
                hView =  navigationView.getHeaderView(0);
                nav_user = (ImageView) hView.findViewById(R.id.navigation_image);
                Picasso.with(hView.getContext())
                        .load(list.get(list.size()/2).getImage())
                        .fit()
                        .into(nav_user);
            }
        }.execute(categories);

    }


    /**
     * Copy one list into another
     * @param list1 is the result
     * @param list2 is the list with are going to paste into list1
     * @return
     */
    public List<Article> copyOneListIntoAnother(List<Article> list1, List<Article> list2)
    {
        for(int i=0;i<list2.size();i++)
        {
            list1.add(list2.get(i));
        }
        return list1;
    }

}
