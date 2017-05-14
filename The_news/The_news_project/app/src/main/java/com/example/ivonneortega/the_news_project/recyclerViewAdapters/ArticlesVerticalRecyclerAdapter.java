package com.example.ivonneortega.the_news_project.recyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivonneortega.the_news_project.detailView.CollectionDemoActivity;
import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.swipe.SaveSwipeLeft;
import com.example.ivonneortega.the_news_project.data.Article;
import com.example.ivonneortega.the_news_project.database.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by ivonneortega on 4/30/17.
 */

public class ArticlesVerticalRecyclerAdapter extends RecyclerView.Adapter<ArticlesVerticalRecyclerAdapter.ArticlesViewHolder>
implements  SaveSwipeLeft{

    private List<Article> mList;
    private boolean mIsSaveFragment;
    private int mPosition;
    Context mContext;
    View view;
    boolean isTop;

    /**
     * Recycler view consturctor
     * @param list is the list that is going to be displayed by the recycler view
     * @param isSaveFragment is a boolean to determine if the recycler view is going to be display in the saved fragment or not
     */
    public ArticlesVerticalRecyclerAdapter(List<Article> list, boolean isSaveFragment) {
        mList = list;
        mIsSaveFragment = isSaveFragment;
    }

    /**
     * Inflating the view by the XML file
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ArticlesViewHolder(inflater.inflate(R.layout.custom_top_stories,parent,false));
    }

    /**
     * Setting each view content
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ArticlesViewHolder holder, final int position) {

        isTop = false;
        mPosition = position;
        view = holder.mRoot;
        mContext = holder.mCategory.getContext();
        holder.mTitle.setText(mList.get(position).getTitle());
        holder.mCategory.setText(mList.get(position).getCategory());
        holder.mDate.setText(mList.get(position).getDate());
        if(mList.get(position).isTopStory())
            isTop = true;
        Picasso.with(holder.mImage.getContext())
                .load(mList.get(holder.getAdapterPosition()).getImage())
                .fit()
                .centerCrop()
                .into(holder.mImage);

        //if this is not going to be shown in the save fragment
        if(!mIsSaveFragment) {
            //Depending on if the article is saved or not show a different heart icon
            if (mList.get(position).isSaved())
                holder.mHeart.setImageResource(R.mipmap.ic_favorite_black_24dp);
            else
            {
                holder.mHeart.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
            }
            //Handling the heart click
            holder.mHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mList.get(holder.getAdapterPosition()).isSaved())
                    {
                        holder.mHeart.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
                        DatabaseHelper.getInstance(v.getContext()).unSaveArticle(mList.get(holder.getAdapterPosition()).getId());
                        mList.get(holder.getAdapterPosition()).setSaved(false);
                        //Showing a Snackbar
                        Snackbar.make(holder.mRoot, "Article unsaved", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        holder.mHeart.setImageResource(R.mipmap.ic_favorite_black_24dp);
                                        mList.get(holder.getAdapterPosition()).setSaved(true);
                                        DatabaseHelper.getInstance(v.getContext()).saveArticle(mList.get(holder.getAdapterPosition()).getId());
                                    }
                                })
                                .setActionTextColor(v.getResources().getColor(R.color.colorPrimaryDark))
                                .show();
                    }
                    else
                    {
                        holder.mHeart.setImageResource(R.mipmap.ic_favorite_black_24dp);
                        mList.get(holder.getAdapterPosition()).setSaved(true);
                        DatabaseHelper.getInstance(v.getContext()).saveArticle(mList.get(holder.getAdapterPosition()).getId());
                        //Showing a snackbar
                        Snackbar.make(holder.mRoot, "Article saved", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        holder.mHeart.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
                                        mList.get(holder.getAdapterPosition()).setSaved(false);
                                        DatabaseHelper.getInstance(v.getContext()).unSaveArticle(mList.get(holder.getAdapterPosition()).getId());
                                    }
                                })
                                .setActionTextColor(v.getResources().getColor(R.color.colorPrimaryDark))
                                .show();
                    }
                }

            });
        }
        else
        {
            //if it a save fragment the heart is not going to be shown
            holder.mHeart.setVisibility(View.GONE);
        }
        //Share icon click
        holder.mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mList.get(holder.getAdapterPosition()).getUrl();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mList.get(holder.getAdapterPosition()).getUrl());
                sendIntent.setType("text/plain");
                v.getContext().startActivity(Intent.createChooser(sendIntent, "Share this article using.."));
            }
        });

        //CLICK LISTENER WHEN CLICKING ON A PRODUCT
        holder.mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnProduct(v,mList.get(holder.getAdapterPosition()).getId());
            }
        });
    }

    /**
     * Launch Detail View activity when clicking on an article
     * @param v
     * @param id
     */
    private void clickOnProduct(View v, long id)
    {
        Intent intent = new Intent(v.getContext().getApplicationContext(), CollectionDemoActivity.class);
        intent.putExtra(DatabaseHelper.COL_ID,id);
        //depending on the fragment that is launching detail view, the intent TYPE_OF_INTENT is going to be
        // different representing which list is going to be shown in detail view when scrolling left and right
        if(mIsSaveFragment) {
            intent.putExtra(CollectionDemoActivity.TYPE_OF_INTENT, "save");
            Log.d(TAG, "clickOnProduct: "+intent.getStringExtra(CollectionDemoActivity.TYPE_OF_INTENT));
        }
        else if(isTop) {
            intent.putExtra(CollectionDemoActivity.TYPE_OF_INTENT, "top");
            Log.d(TAG, "clickOnProduct: "+intent.getStringExtra(CollectionDemoActivity.TYPE_OF_INTENT));

        }
        else {
            intent.putExtra(CollectionDemoActivity.TYPE_OF_INTENT, "allStories");
            Log.d(TAG, "clickOnProduct: "+intent.getStringExtra(CollectionDemoActivity.TYPE_OF_INTENT));

        }
        v.getContext().startActivity(intent);
    }

    /**
     * @return the list size
     */
    @Override
    public int getItemCount() {
        return mList.size();
    }


    /**
     * Handling when an icon is being swipe left to remove from the recycler view
     * @param position
     */
    @Override
    public void onItemDismiss(final int position) {
        DatabaseHelper.getInstance(mContext).unSaveArticle(mList.get(position).getId());
        final Article article = mList.get(position);
        mList.remove(position);
        notifyItemRemoved(position);
        Snackbar.make(view, "Article unsaved", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseHelper.getInstance(mContext).saveArticle(article.getId());
                        mList.add(position,article);
                        notifyItemInserted(position);
                    }
                })
                .setActionTextColor(view.getResources().getColor(R.color.colorPrimaryDark))
                .show();
    }

    /**
     * Swap the list for a new one
     * @param newList is going to be the new list
     */
    public void swapData(List<Article> newList) {
        mList = newList;
        notifyDataSetChanged();
    }

    /**
     * Custom View Holder
     */
    public class ArticlesViewHolder extends RecyclerView.ViewHolder {

        //Setting Views including Recycler View
        TextView mTitle, mDate, mCategory;
        ImageView mImage;
        ImageView mHeart, mShare;
        View mRoot;

        /**
         * Custom view holder constructor
         * @param itemView
         */
        public ArticlesViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.top_stories_title);
            mDate = (TextView) itemView.findViewById(R.id.top_stories_date);
            mCategory = (TextView) itemView.findViewById(R.id.top_stories_category);
            mHeart = (ImageView) itemView.findViewById(R.id.top_stories_heart);
            mShare = (ImageView) itemView.findViewById(R.id.top_stories_share);
            mRoot = itemView.findViewById(R.id.top_stories_root);
            mImage = (ImageView) itemView.findViewById(R.id.top_stories_image);
        }
    }
}
