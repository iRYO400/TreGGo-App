package com.treggo.flexible.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerConfigurator;
import com.nakama.arrayviewpager.ArrayFragmentPagerAdapter;
import com.treggo.flexible.R;
import com.treggo.flexible.fragments.RecyclerViewFragment;
import com.treggo.flexible.fragments.ScrollViewFragment;
import com.treggo.flexible.utilities.RecyclerAdapter;
import com.treggo.flexible.utilities.TinyDB;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TinyDB tinyDB;
    private String boardType;
    private String boardName;
    private ArrayList<String> boardListType;

    private MyFragmentPagerAdapter myStatePagerAdapter;
    private HollyViewPager hollyViewPager;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tinyDB = new TinyDB(this);
        boardType = tinyDB.getString("BoardType");
        boardName = tinyDB.getString("BoardName");
        boardListType = new ArrayList<>();
        switch (boardType) {
            case "Base Four":
                boardListType.add("To-Do");
                boardListType.add("Do today");
                boardListType.add("In progress");
                boardListType.add("Done");
                break;
            case "Week":
                boardListType.add("Monday");
                boardListType.add("Tuesday");
                boardListType.add("Wednesday");
                boardListType.add("Thursday");
                boardListType.add("Friday");
                boardListType.add("Saturday");
                boardListType.add("Sunday");
                break;
            case "Months":
                boardListType.add("Jan");
                boardListType.add("Feb");
                boardListType.add("Mar");
                boardListType.add("Apr");
                boardListType.add("May");
                boardListType.add("Jn");
                boardListType.add("Jl");
                boardListType.add("Aug");
                boardListType.add("Sep");
                boardListType.add("Oct");
                boardListType.add("Nov");
                boardListType.add("Dec");
                break;
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFF53F63F);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hollyViewPager = (HollyViewPager) findViewById(R.id.hollyViewPager);
        hollyViewPager.getViewPager().setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        hollyViewPager.setConfigurator(new HollyViewPagerConfigurator() {
            @Override
            public float getHeightPercentForPage(int page) {

                return 0.4f;
            }
        });

        myStatePagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), boardListType);
        hollyViewPager.setAdapter(myStatePagerAdapter);
    }


    class MyFragmentPagerAdapter extends ArrayFragmentPagerAdapter<String> {

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<String> datas) {
            super(fm, datas);
        }

        @Override
        public Fragment getFragment(String item, int position) {
            if (position == 1)
                return RecyclerViewFragment.newInstance(item);

            else
                return ScrollViewFragment.newInstance(item);

        }


        @Override
        public CharSequence getPageTitle(int position) {
            return boardListType.get(position);
        }
    }
}
