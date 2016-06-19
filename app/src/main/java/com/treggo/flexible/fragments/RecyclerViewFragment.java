package com.treggo.flexible.fragments;

import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.florent37.hollyviewpager.HollyViewPagerBus;
import com.treggo.flexible.R;
import com.treggo.flexible.activities.MoveViewPager;
import com.treggo.flexible.adapters.ListsRecyclerAdapter;
import com.treggo.flexible.adapters.RealmBoardsAdapter;
import com.treggo.flexible.adapters.helper.ItemTouchHelperViewHolder;
import com.treggo.flexible.adapters.helper.OnStartDragListener;
import com.treggo.flexible.adapters.helper.SimpleItemTouchHelperCallback;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.model.Board;
import com.treggo.flexible.model.Card;
import com.treggo.flexible.utilities.TinyDB;

import org.solovyev.android.views.llm.DividerItemDecoration;

import java.lang.reflect.Method;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by iRYO400 on 07.06.2016.
 */
public class RecyclerViewFragment extends Fragment implements OnStartDragListener {

    private ItemTouchHelper mItemTouchHelper;
    private MoveViewPager moveViewPager;

    private static final String BUNDLE_ID = "bundle_id";
    private static final String BUNDLE_LIST_POSITION = "bundle_lposition";
    private static final String TAG = "mLogs";

    private int devWidth;
    private int devHeight;

    private Board board;
    private Realm realm;
    private long boardID;
    private int boardPosition;
    private int listPosition;
    private TinyDB tinyDB;
    private View view;

    private RecyclerView recyclerView;
    private ListsRecyclerAdapter rAdapter;
//    private ObservableScrollView scrollView;
//    private TextView title;
//    private Button btnAdd;

    public static RecyclerViewFragment newInstance(long id, int listPosition){
        Bundle args = new Bundle();
        args.putLong(BUNDLE_ID, id);
        args.putInt(BUNDLE_LIST_POSITION, listPosition);
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recyclerview, container,false);
        tinyDB = new TinyDB(getContext());
        boardPosition = tinyDB.getInt("boardPosition");
        boardID = this.getArguments().getLong(BUNDLE_ID);
        listPosition = this.getArguments().getInt(BUNDLE_LIST_POSITION, 0);
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        scrollView = (ObservableScrollView)view.findViewById(R.id.scrollView2);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        getRealScreenSize();
        moveViewPager = (MoveViewPager) getActivity();

//        title = (TextView) view.findViewById(R.id.titleRecycler);
//        btnAdd = (Button) view.findViewById(R.id.btnXAddRec);

        this.realm = RealmController.with(this).getRealm();

        board = realm.where(Board.class).equalTo("id", boardID).findFirst();
        if(board.getMyLists().get(listPosition).getCards().isEmpty()) {
            setupAllCards(realm, board, listPosition);
        }

        setupRecyclerView();
        setRealmAdapter(RealmController.with(this).getBoards());

//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//                View subView = layoutInflater.inflate(R.layout.dialog_laoyut2, null);
//                final EditText editText = (EditText) subView.findViewById(R.id.editCardName);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Add card?");
//                builder.setView(subView);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Card card = new Card();
//                        card.setName(editText.getText().toString());
//                        if(editText.getText().toString().equals("")) {
//                            Toast.makeText(getContext(), "Wrong Name", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            realm.beginTransaction();
//                            Log.d(TAG, listPosition + " it's a listPostion");
//                            card = realm.copyToRealm(card);
//                            board.getMyLists().get(listPosition).getCards().add(card);
//                            realm.commitTransaction();
//                            rAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.show();
//            }
//        });
//        recyclerView.setOnDragListener(new MyDragListener());

        if(board.getMyLists().get(listPosition).isValid()) {
//            title.setText(board.getMyLists().get(listPosition).getName());
        }

        HollyViewPagerBus.registerRecyclerView(getActivity(), recyclerView);
//        HollyViewPagerBus.registerScrollView(getActivity(), scrollView);
    }

    public void setRealmAdapter(RealmResults<Board> boards){
        RealmBoardsAdapter boardsAdapter = new RealmBoardsAdapter(getActivity().getApplicationContext(), boards, true);
        rAdapter.setRealmAdapter(boardsAdapter);
        rAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //ItemClick and LongItemClick @Utilities/RecyclerItemClickListener
//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        rAdapter = new ListsRecyclerAdapter(getActivity(), boardID, boardPosition, listPosition, this);

        recyclerView.setAdapter(rAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(rAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private void setupAllCards(Realm realm, Board board, int myListPosition){
        ArrayList<Card> cards = new ArrayList<>();
        Card card;
        for(int i = 0; i < 4; i++) {
            card = new Card();
            card.setName("Example Card");
            cards.add(card);
        }
        for(Card c: cards) {
            realm.beginTransaction();
            realm.copyToRealm(c);
            board.getMyLists().get(myListPosition).getCards().add(c);
            realm.commitTransaction();
        }
    }



//    @Override
//    public void onItemClick(View childView, int position) {
//        Intent intent = new Intent(getActivity(), DescriptionActivity.class);
//        intent.putExtra("c_bID", boardID);
//        intent.putExtra("c_bPosition", boardPosition);
//        intent.putExtra("c_lPosition", listPosition);
//        intent.putExtra("c_cPosition", position);
//        startActivity(intent);
//        Toast.makeText(getActivity(), "Position " + position
//                + " BPosition " + boardPosition
//                + " LPostion " + listPosition, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onItemLongPress(View childView, int position, final RecyclerView.ViewHolder viewHolder) {
//        childView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN){
//                    mItemTouchHelper.startDrag(viewHolder);
//                    Toast.makeText(getActivity(), "Long Touch Click", Toast.LENGTH_LONG).show();
//                }
//                return false;
//            }
//        });
//
//    }

    private class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();
            switch (action){
                case DragEvent.ACTION_DRAG_STARTED:
                    //do nothing
                    Log.d(TAG, rAdapter.getItemID(listPosition)+"");
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:

                    if(event.getX()>(devWidth - (devWidth/10))){
                        moveViewPager.moveViewPagerRight();
                    }
                    if(event.getX()<((devWidth/10))){
                        moveViewPager.moveViewPagerLeft();
                    }

//                    Log.d(TAG, ((ViewGroup) view.getParent()).toString());
//                    rAdapter.onItemMove(0,1);
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:


                    return true;
                case DragEvent.ACTION_DROP:


                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String[] strings = item.getText().toString().split(",");
                    Log.d(TAG, strings[0] + " List " + strings[1] + " position");
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    RecyclerView containerRecycler = (RecyclerView) v;
                    containerRecycler.addView(view, 4);

                    view.setVisibility(View.VISIBLE);
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    return true;
                default:
                    Log.d(TAG,"Unknown action type received by OnDragListener.");
                    break;
            }
            return false;
        }
    }

    private void getRealScreenSize(){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 17){
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            devWidth = realMetrics.widthPixels;
            devHeight = realMetrics.heightPixels;

        } else if (Build.VERSION.SDK_INT >= 14) {
            //reflection for this weird in-between time
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                devWidth = (Integer) mGetRawW.invoke(display);
                devHeight = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                //this may not be 100% accurate, but it's all we've got
                devWidth = display.getWidth();
                devHeight = display.getHeight();
                Log.e("Display Info", "Couldn't use reflection to get the real display metrics.");
            }

        } else {
            //This should be close, as lower API devices should not have window navigation bars
            devWidth = display.getWidth();
            devHeight = display.getHeight();
        }
    }
}
