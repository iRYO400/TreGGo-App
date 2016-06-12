package com.treggo.flexible.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.hollyviewpager.HollyViewPagerBus;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.treggo.flexible.R;

/**
 * Created by iRYO400 on 30.05.2016.
 */
public class ScrollViewFragment extends Fragment {

    private static final String BUNDLE_CODE = "titleScroll";
    private static final String TAG = "mLogs";
    ObservableScrollView scrollView;
    LinearLayout linearLayout;
    TextView title;
    Button buttonMenu;
    Button buttonAdd;


    public static ScrollViewFragment newInstance(String title){
        Bundle args = new Bundle();
        args.putString(BUNDLE_CODE, title);
        ScrollViewFragment fragment = new ScrollViewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scroll, container, false);
        scrollView = (ObservableScrollView)view.findViewById(R.id.scrollView);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearCard);

        buttonMenu = (Button) view.findViewById(R.id.btnXOptions);
        buttonAdd = (Button) view.findViewById(R.id.btnXAdd);
        title = (TextView)view.findViewById(R.id.title);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = (LayoutInflater)
                        getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                linearLayout.addView(layoutInflater.inflate(R.layout.card_in_list, (ViewGroup) view, false), 2);

            }
        });

        title.setText(getArguments().getString(BUNDLE_CODE));
        Log.d(TAG, " SCROLL " + getArguments().getString(BUNDLE_CODE));
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.inflate(R.menu.popupmenu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case 0:
                                Toast.makeText(getContext(), "Menu 1", Toast.LENGTH_SHORT).show();
                                return true;
                            case 1:
                                Toast.makeText(getContext(), "Menu 2", Toast.LENGTH_SHORT).show();
                                return true;
                            case 2:
                                Toast.makeText(getContext(), "Menu 3", Toast.LENGTH_SHORT).show();
                                return true;
                            case 3:
                                Toast.makeText(getContext(), "Menu 4", Toast.LENGTH_SHORT).show();
                                return true;
                            case 4:
                                Toast.makeText(getContext(), "Menu 5", Toast.LENGTH_SHORT).show();
                                return true;
                            case 5:
                                Toast.makeText(getContext(), "Menu 6", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

        scrollView.setFocusable(false);
        scrollView.fullScroll(View.FOCUS_BACKWARD);
        HollyViewPagerBus.registerScrollView(getActivity(), scrollView);
    }



}