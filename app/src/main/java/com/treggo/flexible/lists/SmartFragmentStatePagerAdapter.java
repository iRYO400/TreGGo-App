package com.treggo.flexible.lists;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.treggo.flexible.board.Board;

/**
 * Created by iRYO400 on 25.06.2016.
 */
public class SmartFragmentStatePagerAdapter<T extends Fragment> extends FragmentStatePagerAdapter {
    // Sparse array to keep track of registered fragments in memory
    private SparseArray<T> registeredFragments = new SparseArray<T>();

    private Board board;
    private long boardID;

    public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager, Board board, long id) {
        super(fragmentManager);
        this.board = board;
        this.boardID = id;
    }

    @Override
    public Fragment getItem(int position) {
        return RecyclerViewFragment.newInstance(boardID, position);
    }

    @Override
    public int getCount() {
        return board.getMyLists().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return board.getMyLists().get(position).getName();
    }

    // Register the fragment when the item is instantiated
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        T fragment = (T) super.instantiateItem(container, position);
//        if(registeredFragments.get(position) != null){
//            registeredFragments.remove(position);
//        }
        registeredFragments.put(position, fragment);
        return fragment;
    }

    // Unregister when the item is inactive
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    // Returns the fragment for the position (if instantiated)
    public T getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
