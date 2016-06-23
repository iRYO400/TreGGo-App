package com.treggo.flexible.lists;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.treggo.flexible.board.Board;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iRYO400 on 11.06.2016.
 */
public class DynamicFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private Map<Integer, String> mFragmentTags;
    private FragmentManager fragmentManager;

    private Board board;
    private long boardID;

    public DynamicFragmentPagerAdapter(FragmentManager fm, Board board, long id) {
        super(fm);
        this.board = board;
        this.boardID = id;
        mFragmentTags = new HashMap<>();
        fragmentManager = fm;
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
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object o = super.instantiateItem(container, position);
        if(o instanceof Fragment){
            Fragment fragment = (Fragment) o;
            String tag = fragment.getTag();
            mFragmentTags.put(position, tag);
        }
        return o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mFragmentTags.remove(position);
    }

    public RecyclerViewFragment getFragment(int position){
        RecyclerViewFragment fragment = null;
        String tag = mFragmentTags.get(position);
        if(tag != null){
            fragment = (RecyclerViewFragment) fragmentManager.findFragmentByTag(tag);
        }
        return fragment;
    }
}

