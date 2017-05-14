package com.example.ivonneortega.the_news_project.recyclerViewAdapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.data.Article;
import com.example.ivonneortega.the_news_project.data.Category;

import java.util.List;

/**
 * Created by ivonneortega on 4/29/17.
 */

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.CategoriesViewHolder>  {

    private List<Category> mList;

    /**
     * Recycler view constructor
     * @param categoriesList
     */
    public CategoriesRecyclerAdapter(List<Category> categoriesList) {
        mList = categoriesList;
    }

    /**
     * Inflating the recycler view adapter with the XML file
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CategoriesViewHolder(inflater.inflate(R.layout.custom_recyclerview_all_stories,parent,false));
    }

    /**
     * Setting each view
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, int position) {

        //Title of the Category
        holder.mTitleOfCategory.setText(mList.get(position).getCategoryName());

        //Setting Layout Manager for Recycler View
        //This is the main recycler view that scroll vertically and has all the categories
        // Or at least the important ones
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.mTitleOfCategory.getContext(),LinearLayoutManager.HORIZONTAL,false);
        holder.mRecyclerView.setLayoutManager(linearLayoutManager);

        ArticleRecyclerAdapter adapter = new ArticleRecyclerAdapter(mList.get(position).getList());
        holder.mRecyclerView.setAdapter(adapter);
    }

    /**
     * @return the list size
     */
    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * Swiping the current list with a new list
     * @param newList is going to be the new list for the recycler view adapter
     */
    public void swapData(List<Category> newList) {
        mList = newList;
        notifyDataSetChanged();
    }

    /**
     * Custom view holder
     */
    public class CategoriesViewHolder extends RecyclerView.ViewHolder {

        //Setting Views including Recycler View
        TextView mTitleOfCategory;
        RecyclerView mRecyclerView;

        /**
         * Custom view holder constructor
         * @param itemView
         */
        public CategoriesViewHolder(View itemView) {
            super(itemView);
            mTitleOfCategory = (TextView) itemView.findViewById(R.id.title_category_main_activity);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView_each_category);
        }
    }


}
