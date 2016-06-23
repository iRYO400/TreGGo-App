package com.treggo.flexible.lists;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.florent37.hollyviewpager.HollyViewPager;
import com.github.florent37.hollyviewpager.HollyViewPagerConfigurator;
import com.treggo.flexible.R;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.board.Board;
import com.treggo.flexible.utilities.Constants;

import java.util.ArrayList;

import io.realm.Realm;

public class MiddleActivity extends AppCompatActivity{


    private Realm realm;
    private long boardID;
    private Board board;
    private int boardType;

    private DynamicFragmentPagerAdapter myStatePagerAdapter;
    private HollyViewPager hollyViewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.realm = RealmController.getInstance().getRealm();

        //Вытягиваем ID доски
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            boardID = extra.getLong("BoardID");
        }

        //По ID находим Доску
        board = realm.where(Board.class).equalTo("id", boardID).findFirst();
        //Проверяем, пустая ли доска(листы)
        if (board.getMyLists().isEmpty()) {
            setupAllFragments(realm, board);
        }


        getSupportActionBar().setTitle(board.getName());
        hollyViewPager = (HollyViewPager) findViewById(R.id.hollyViewPager);
        hollyViewPager.getViewPager().setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        hollyViewPager.setConfigurator(new HollyViewPagerConfigurator() {
            @Override
            public float getHeightPercentForPage(int page) {
                return 0.4f;
            }
        });
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < board.getMyLists().size(); i++) {
            names.add(board.getMyLists().get(i).getName());
        }

        myStatePagerAdapter = new DynamicFragmentPagerAdapter(getSupportFragmentManager(), board, boardID);
        hollyViewPager.setAdapter(myStatePagerAdapter);

        hollyViewPager.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("mLogs", " Position is " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("mLogs", " State is " + state);
            }
        });
    }

    private void setupAllFragments(Realm realm, Board board) {
        ArrayList<MyList> myLists = new ArrayList<>();

        boardType = board.getType();
        MyList myList;
        switch (boardType) {
            case 0:
                for (int i = 0; i < Constants.STANDART.length; i++) {
                    myList = new MyList();
                    myList.setName(Constants.STANDART[i]);
                    myLists.add(myList);
                }
                break;
            case 1:
                for (int i = 0; i < Constants.WEEK.length; i++) {
                    myList = new MyList();
                    myList.setName(Constants.WEEK[i]);
                    myLists.add(myList);
                }
                break;
            case 2:
                for (int i = 0; i < Constants.MONTHS.length; i++) {
                    myList = new MyList();
                    myList.setName(Constants.MONTHS[i]);
                    myLists.add(myList);
                }
                break;
            case 3:
                for (int i = 0; i < Constants.QUARTER.length; i++) {
                    myList = new MyList();
                    myList.setName(Constants.QUARTER[i]);
                    myLists.add(myList);
                }
                break;
        }
        for (MyList mL : myLists) {
            realm.beginTransaction();
            realm.copyToRealm(mL);
            board.getMyLists().add(mL);
            realm.commitTransaction();
        }
    }


}
