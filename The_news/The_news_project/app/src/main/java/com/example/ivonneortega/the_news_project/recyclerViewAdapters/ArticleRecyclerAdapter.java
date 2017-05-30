package com.example.ivonneortega.the_news_project.recyclerViewAdapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.categoryView.CategoryViewActivity;
import com.example.ivonneortega.the_news_project.data.Article;
import com.example.ivonneortega.the_news_project.database.DatabaseHelper;
import com.example.ivonneortega.the_news_project.detailView.CollectionDemoActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ivonneortega on 4/29/17.
 */

public class ArticleRecyclerAdapter extends RecyclerView.Adapter<ArticleRecyclerAdapter.CategoryIndividualItemViewHolder> {

    private static final int VIEW_TYPE_MORE = 1 ;
    private static final int VIEW_TYPE_ARTICLE = 2 ;
    private List<Article> mList;

    /**
     * Recycler view constructor
     * @param individualItems is the list that is going to be displayed by the recycler view adapter
     */
    public ArticleRecyclerAdapter(List<Article> individualItems) {
        mList = individualItems;
    }

    /**
     * Inflating the view of the recycler view
     * @param parent
     * @param viewType depending on the type of view the method can inflate two different views
     *                 one for the articles and one for the see more view
     * @return
     */
    @Override
    public CategoryIndividualItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //IF THE TYPE OF VIEW IS A TYPE ARTICLE INFLATE THE ARTICLES XML
        if(viewType == VIEW_TYPE_ARTICLE)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new CategoryIndividualItemViewHolder(inflater.inflate(R.layout.custom_recyclerview_all_stories_horizontal,parent,false));
        }
        //IF THE TYPE OF VIEW IS A TYPE SEE MORE INFLATE THE SEE MORE XML
        else
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new CategoryIndividualItemViewHolder(inflater.inflate(R.layout.see_more_custom,parent,false));
        }
    }

    /**
     * Setting each article view by their content
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ArticleRecyclerAdapter.CategoryIndividualItemViewHolder holder, int position) {
        //This is the recycler view that scrolls horizontally

        //If we are not at the last position
        if(position == 5)
        {
            holder.mSeeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToCategoryView(mList.get(holder.getAdapterPosition()).getCategory(),v);
                }
            });
        }
        //If we are at the last position of the list then show the "SEE MORE" TEXTVIEW
        else
        {
            Picasso.with(holder.mArticleImage.getContext())
                    .load(mList.get(holder.getAdapterPosition()).getImage())
                    .fit()
                    .centerCrop()
                    .into(holder.mArticleImage);
            if(mList.get(holder.getAdapterPosition()).isSaved())
                holder.mHeart.setVisibility(View.VISIBLE);
            else
                holder.mHeart.setVisibility(View.INVISIBLE);

            //This is the title of each article
            holder.mTitleOfArticle.setText(mList.get(position).getTitle());
            holder.mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 clickOnProduct(v,mList.get(holder.getAdapterPosition()).getId());
                }
            });
        }
    }

    /**
     * Launch Detail View when an article is clicked
     * @param v a view to be able to get a context
     * @param id is the id of the article
     */
    private void clickOnProduct(View v,long id)
    {
        Intent intent = new Intent(v.getContext().getApplicationContext(), CollectionDemoActivity.class);
        intent.putExtra(DatabaseHelper.COL_ID,id);
        intent.putExtra(CollectionDemoActivity.TYPE_OF_INTENT,"allStories");
        v.getContext().startActivity(intent);
    }

    /**
     * Launch Category View when clicking on see more
     * @param category is the category that is going to be displayed in category view
     * @param v is a view to be able to get a context
     */
    private void goToCategoryView(String category,View v)
    {
        Intent intent = new Intent(v.getContext().getApplicationContext(), CategoryViewActivity.class);
        intent.putExtra(DatabaseHelper.COL_CATEGORY,category);

        v.getContext().startActivity(intent);
    }

    /**
     * Number of items in the recycler view
     * @return
     */
    @Override
    public int getItemCount() {
        //if the list is empty return 0
        if(mList.size()<=0)
            return 0;
        //else return just 6 so we are always displaying just 6 articles
        return 6;
    }
    /**
     * Get the type by the position in the recycler view
     * if it's the last one then is going to be a see more button
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return (position == 5) ? VIEW_TYPE_MORE : VIEW_TYPE_ARTICLE;
    }

    /**
     * Custom view holder for the recycler view
     */
    public class CategoryIndividualItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleOfArticle;
        ImageView mArticleImage;
        View mSeeMore;
        View mRoot;
        ImageView mHeart;

        /**
         * View holder constructor
         * @param itemView
         */
        public CategoryIndividualItemViewHolder(View itemView) {
            super(itemView);
            mTitleOfArticle = (TextView) itemView.findViewById(R.id.all_stories_article_title);
            mArticleImage = (ImageView) itemView.findViewById(R.id.all_stories_article_images);
            mSeeMore = itemView.findViewById(R.id.see_more_layout);
            mRoot = itemView.findViewById(R.id.root_category_individual_item);
            mHeart = (ImageView) itemView.findViewById(R.id.horizontal_heart);


        }
    }


}
