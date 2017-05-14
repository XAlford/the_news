package com.example.ivonneortega.the_news_project.swipe;

/**
 * Created by ivonneortega on 4/10/17.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.recyclerViewAdapters.ArticlesVerticalRecyclerAdapter;

/**
 * Helper call to handle swiping in a recycler view adapter
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public static final float ALPHA_FULL = 1.0f;


    private final ArticlesVerticalRecyclerAdapter mAdapterArticles;

    /**
     * Simple Item Touch Helper CallBack constructor
     * @param adapter the adapter from the custom ArticleVerticalRecyclerAdapter
     */
    public SimpleItemTouchHelperCallback(ArticlesVerticalRecyclerAdapter adapter) {
        mAdapterArticles = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled()
    {
        if(mAdapterArticles ==null)
        {
            return true;
        }
        return false;
    }

    /**
     * enabling the ability to swipe on an article
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * Returning the movement flags
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Set movement flags based on the layout manager

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START ;//| ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * enabling only swipe left
     * @param viewHolder
     * @param i
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
            if (i  == ItemTouchHelper.END){
                //
            } else {
                mAdapterArticles.onItemDismiss(viewHolder.getAdapterPosition());
            }
    }

    /**
     * Handling the background color for each swipe
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
        {
            View itemView = viewHolder.itemView;
            Paint p = new Paint();
            Bitmap icon;

            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;
            //TODO SAM DON'T DELETE THIS
            if (dX > 0) {
                //do nothing
            } else {
                p.setColor(Color.parseColor("#D32F2F"));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background,p);
                icon = BitmapFactory.decodeResource(itemView.getContext().getResources(), R.mipmap.ic_delete_sweep_white_24dp);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);     c.drawBitmap(icon,null,icon_dest,p);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
