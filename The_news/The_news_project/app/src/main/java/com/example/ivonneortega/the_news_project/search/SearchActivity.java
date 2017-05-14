package com.example.ivonneortega.the_news_project.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.recyclerViewAdapters.ArticlesVerticalRecyclerAdapter;
import com.example.ivonneortega.the_news_project.settings.SettingsActivity;
import com.example.ivonneortega.the_news_project.data.Article;
import com.example.ivonneortega.the_news_project.database.DatabaseHelper;

import java.util.List;

/**
 * Search Activity
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    SearchView mSearchView;
    ImageButton mBackButton;
    ArticlesVerticalRecyclerAdapter mAdapter;
    boolean mStartActivity;
    private boolean theme_changed;

    /**
     * On create activity method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting the theme color
        setTheme();

        //Setting all the views
        settingUpTheViews();

        //Search query listener
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //When the query has been summited hide the keyboard
            @Override
            public boolean onQueryTextSubmit(String query) {
                InputMethodManager imm = (InputMethodManager)mSearchView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
                return true;
            }

            //do a search for everytime the text in the searchview changes
            @Override
            public boolean onQueryTextChange(String newText) {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mSearchView.getContext(),LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(linearLayoutManager);
                List<Article> articleList = DatabaseHelper.getInstance(mSearchView.getContext()).searchArticles(newText);
                mAdapter = new ArticlesVerticalRecyclerAdapter(articleList,false);
                recyclerView.setAdapter(mAdapter);

                return true;
            }
        });
    }

    /**
     * Onclick for the backbutton in the toolbar
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_toolbar:
                finish();
                break;
        }
    }

    /**
     * Setting all the views
     */
    public void settingUpTheViews()
    {
        mSearchView = (SearchView) findViewById(R.id.search_editText);
        mSearchView.onActionViewExpanded();
        mBackButton = (ImageButton) findViewById(R.id.back_toolbar);
        mBackButton.setClickable(true);
        mBackButton.setOnClickListener(this);
    }

    /**
     * On resume the activity
     * Gets if the theme has been changed, if it has been changed then relaunches the activity
     */
    @Override
    protected void onResume() {
        super.onResume();
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
     * Setting the theme
     */
    public void setTheme()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.ivonneortega.the_news_project.Settings", Context.MODE_PRIVATE);
        String str = sharedPreferences.getString(SettingsActivity.THEME,"DEFAULT"); //Initial value of the String is "Hello"
        Log.d("weqweqweqwe", "setTheme: "+str);
        if(str.equals("dark"))
        {
            Log.d("sdsdfsdfsdfsdf", "setTheme: qweqwdqqwdqwdqwdwd");
            setTheme(R.style.DarkTheme);
            setContentView(R.layout.activity_search);
            findViewById(R.id.root_toolbar).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkTheme));
        }
        else
        {
            setContentView(R.layout.activity_search);
        }
        mStartActivity=true;
    }
}
