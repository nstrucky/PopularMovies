package com.ventoray.popularmovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsAndVidzFragment extends Fragment {


    private TextView testView;

    public ReviewsAndVidzFragment() {
        // Required empty public constructor
    }

    public static ReviewsAndVidzFragment newInstance(int position) {
        ReviewsAndVidzFragment fragment = new ReviewsAndVidzFragment();
        Bundle args = new Bundle();
        String pageTitle;
        switch (position) {
            case 0:
                pageTitle = "Videos!";
                break;

            case 1:
                pageTitle = "Reviews~!";
                break;

            default:
                pageTitle = "Error";
        }
        args.putString("tempKey", pageTitle);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reviews_and_vidz, container, false);


        testView = (TextView) view.findViewById(R.id.tv_test);

        String thing;

        thing = getArguments() != null ? getArguments().getString("tempKey") : "Error1";

        testView.setText(thing);




        // Inflate the layout for this fragment
        return view;
    }

}
