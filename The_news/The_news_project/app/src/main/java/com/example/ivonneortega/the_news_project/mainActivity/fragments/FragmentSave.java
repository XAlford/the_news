package com.example.ivonneortega.the_news_project.mainActivity.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.recyclerViewAdapters.ArticlesVerticalRecyclerAdapter;
import com.example.ivonneortega.the_news_project.swipe.SimpleItemTouchHelperCallback;
import com.example.ivonneortega.the_news_project.data.Article;
import com.example.ivonneortega.the_news_project.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.ivonneortega.the_news_project.detailView.CollectionDemoActivity.TAG;


/**
 * Fragment to show only the saved stories
 */
public class FragmentSave extends Fragment {

    private ArticlesVerticalRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private DatabaseHelper db;

    private OnFragmentInteractionListener mListener;

    /**
     * Fragment constructor
     */
    public FragmentSave() {
        // Required empty public constructor
    }

    /**
     * Sets the adapter so an item can be removed by swiping left
     */
    @Override
    public void onResume() {
        super.onResume();
        //Creating a list to test recycler view
        List<Article> categoryIndividualItems = new ArrayList<>();
        mAdapter = new ArticlesVerticalRecyclerAdapter(categoryIndividualItems,true);
        mRecyclerView.setAdapter(mAdapter);

        db = DatabaseHelper.getInstance(mRecyclerView.getContext());

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        getSaveArticles();
    }

    /**
     * Get all the articles that are saved by calling the database
     */
    private void getSaveArticles()
    {
        AsyncTask<Void,Void,List<Article>> asyncTask = new AsyncTask<Void, Void, List<Article>>() {
            @Override
            protected List<Article> doInBackground(Void... params) {
                List<Article> list = db.getSavedArticles();
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
     * Instanciating the fragment
     * @return a fragment
     */
    public static FragmentSave newInstance() {
        FragmentSave fragment = new FragmentSave();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creating the fragment
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    /**
     * Inflating the fragment with the xml file
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_save, container, false);
    }

    /**
     * Setting the recycler view
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.save_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
