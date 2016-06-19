package com.treggo.flexible.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.nakama.arraypageradapter.ArrayFragmentStatePagerAdapter;
import com.treggo.flexible.fragments.BoardFragment;
import com.treggo.flexible.fragments.RecyclerViewFragment;
import com.treggo.flexible.fragments.ScrollViewFragment;
import com.treggo.flexible.model.Board;

import java.util.ArrayList;

/**
 * Created by iRYO400 on 11.06.2016.
 */
public class DynamicFragmentPagerAdapter extends FragmentStatePagerAdapter {


    private Board board;
    private long boardID;

    public DynamicFragmentPagerAdapter(FragmentManager fm, Board board, long id) {
        super(fm);
        this.board = board;
        this.boardID = id;
    }

    @Override
    public int getCount() {
        return board.getMyLists().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return board.getMyLists().get(position).getName();
    }

    @Override
    public Fragment getItem(int position) {
        return RecyclerViewFragment.newInstance(boardID, position);
//        return BoardFragment.newInstance(boardID, position);
//            return ScrollViewFragment.newInstance(item);
    }


}

