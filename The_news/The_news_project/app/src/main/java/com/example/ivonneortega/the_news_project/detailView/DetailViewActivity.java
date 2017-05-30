package com.example.ivonneortega.the_news_project.detailView;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivonneortega.the_news_project.categoryView.CategoryViewActivity;
import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.data.Article;
import com.example.ivonneortega.the_news_project.database.DatabaseHelper;

public class DetailViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, DetailViewFragment.OnFragmentInteractionListener {

    private long mId;
    private ImageView mImage;
    private TextView mTitle, mDate, mContent;

    //TODO I KNOW WE ARE NOT USING THIS BUT DON'T DELETE IT JUST YET

    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.root_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton optionsToolbar = (ImageButton) findViewById(R.id.options_toolbar);
        optionsToolbar.setClickable(true);
        ImageButton shareToolbar = (ImageButton) findViewById(R.id.share_toolbar);
        shareToolbar.setClickable(true);
        optionsToolbar.setOnClickListener(this);
        shareToolbar.setOnClickListener(this);
        ImageButton heartToolbar = (ImageButton) findViewById(R.id.heart_toolbar);
        heartToolbar.setClickable(true);
        heartToolbar.setOnClickListener(this);

        Intent intent = getIntent();
        mId = intent.getLongExtra(DatabaseHelper.COL_ID,-1);
        Article article = DatabaseHelper.getInstance(this).getArticlesById(mId);

//        DetailViewFragment detailFragment = DetailViewFragment.newInstance(mArticle);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container,detailFragment)
//                .commit();
        //creatingViews();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            moveToCategoryViewActivity();
        } else if (id == R.id.nav_gallery) {
            moveToCategoryViewActivity();
        } else if (id == R.id.nav_slideshow) {
            moveToCategoryViewActivity();
        } else if (id == R.id.nav_manage) {
            moveToCategoryViewActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void moveToCategoryViewActivity() {
        Intent intent = new Intent(this, CategoryViewActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.options_toolbar:
                Toast.makeText(this, "Click on options button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_toolbar:
                Toast.makeText(this, "Click on share button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.heart_toolbar:
                Toast.makeText(this, "Click on heart button", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }

    }

    public void creatingViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.root_toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton optionsToolbar = (ImageButton) findViewById(R.id.options_toolbar);
        optionsToolbar.setClickable(true);
        ImageButton shareToolbar = (ImageButton) findViewById(R.id.share_toolbar);
        shareToolbar.setClickable(true);
        optionsToolbar.setOnClickListener(this);
        shareToolbar.setOnClickListener(this);
        ImageButton heartToolbar = (ImageButton) findViewById(R.id.heart_toolbar);
        heartToolbar.setClickable(true);
        heartToolbar.setOnClickListener(this);

        mImage = (ImageView) findViewById(R.id.detail_image);
        mTitle = (TextView) findViewById(R.id.detail_title);
        mDate = (TextView) findViewById(R.id.detail_date);
        mContent = (TextView) findViewById(R.id.detail_content);
    }

    public void settingUpViews() {
        Intent intent = getIntent();
        mId = intent.getLongExtra(DatabaseHelper.COL_ID,-1);
        Article article = DatabaseHelper.getInstance(this).getArticlesById(mId);
    }

    @Override
    public void onFragmentInteraction() {

    }
}
