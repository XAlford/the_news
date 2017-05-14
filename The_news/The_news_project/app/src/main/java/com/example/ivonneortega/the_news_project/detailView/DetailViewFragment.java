package com.example.ivonneortega.the_news_project.detailView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivonneortega.the_news_project.R;
import com.example.ivonneortega.the_news_project.data.Article;

public class DetailViewFragment extends Fragment {

    //TODO I KNOW WE ARE NOT USING THIS BUT DON'T DELETE IT JUST YET
    private long mId;
    private ImageView mImage;
    private TextView mTitle, mDate, mContent;

    private OnFragmentInteractionListener mListener;

    public DetailViewFragment() {
        // Required empty public constructor
    }

    public static DetailViewFragment newInstance(Article article) {
        DetailViewFragment fragment = new DetailViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        creatingViews(view);
    }

    public void creatingViews(View view) {
        mImage = (ImageView) view.findViewById(R.id.detail_image);
        mTitle = (TextView) view.findViewById(R.id.detail_title);
        mDate = (TextView) view.findViewById(R.id.detail_date);
        mContent = (TextView) view.findViewById(R.id.detail_content);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}

