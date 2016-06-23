package com.treggo.flexible.lists;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.hollyviewpager.HollyViewPagerBus;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.treggo.flexible.R;
import com.treggo.flexible.adapters.RealmBoardsAdapter;
import com.treggo.flexible.adapters.dragDropHelper.OnStartDragListener;
import com.treggo.flexible.adapters.dragDropHelper.SimpleItemTouchHelperCallback;
import com.treggo.flexible.adapters.dragDropHelper.UpdateViews;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.board.Board;
import com.treggo.flexible.card.Card;
import com.treggo.flexible.utilities.TinyDB;

import org.solovyev.android.views.llm.DividerItemDecoration;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by iRYO400 on 07.06.2016.
 */
public class RecyclerViewFragment extends Fragment implements OnStartDragListener, UpdateViews {

    private ItemTouchHelper mItemTouchHelper;

    private static final String BUNDLE_ID = "bundle_id";
    private static final String BUNDLE_LIST_POSITION = "bundle_lposition";
    private static final String TAG = "mLogs";

    private Board board;
    private Realm realm;
    private long boardID;
    private int boardPosition;
    private int listPosition;
    private TinyDB tinyDB;

    private View view;
    private RecyclerView recyclerView;
    private ListsRecyclerAdapter rAdapter;
    private ObservableScrollView scrollView;
    private TextView title;
    private Button btnAdd;
    private Button btnOptions;

    public static RecyclerViewFragment newInstance(long id, int listPosition) {
        Bundle args = new Bundle();
        args.putLong(BUNDLE_ID, id);
        args.putInt(BUNDLE_LIST_POSITION, listPosition);
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        rAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        tinyDB = new TinyDB(getContext());
        boardPosition = tinyDB.getInt("boardPosition");
        boardID = this.getArguments().getLong(BUNDLE_ID);
        listPosition = this.getArguments().getInt(BUNDLE_LIST_POSITION, 0);
        this.realm = RealmController.with(this).getRealm();

        board = realm.where(Board.class).equalTo("id", boardID).findFirst();
        if (board.getMyLists().get(listPosition).getCards().isEmpty()) {
            setupAllCards(realm, board, listPosition);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initiateViewStuff();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View subView = layoutInflater.inflate(R.layout.dialog_add_card, null);
                final EditText editText = (EditText) subView.findViewById(R.id.editCardName);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Create a new Card");
                builder.setView(subView);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Card card = new Card();
                        card.setName(editText.getText().toString());
                        if (editText.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Wrong Name", Toast.LENGTH_SHORT).show();
                        } else {
                            realm.beginTransaction();
                            card = realm.copyToRealm(card);
                            board.getMyLists().get(listPosition).getCards().add(card);
                            realm.commitTransaction();
                            rAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    private void initiateViewStuff() {
        scrollView = (ObservableScrollView) view.findViewById(R.id.scrollView2);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        title = (TextView) view.findViewById(R.id.titleRecycler);
        btnAdd = (Button) view.findViewById(R.id.btnXAddRec);
        btnOptions = (Button) view.findViewById(R.id.btnXOptions);

        setupRecyclerView();
        setRealmAdapter(RealmController.with(this).getBoards());

        if (board.getMyLists().get(listPosition).isValid()) {
            title.setText(board.getMyLists().get(listPosition).getName());
        }

        HollyViewPagerBus.registerRecyclerView(getActivity(), recyclerView);
        HollyViewPagerBus.registerScrollView(getActivity(), scrollView);
    }

    public void setRealmAdapter(RealmResults<Board> boards) {
        RealmBoardsAdapter boardsAdapter = new RealmBoardsAdapter(getActivity().getApplicationContext(), boards, true);
        rAdapter.setRealmAdapter(boardsAdapter);
        rAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateCurrentView(int position) {
        recyclerView.removeViewAt(position);
        rAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //ItemClick and LongItemClick @Utilities/RecyclerItemClickListener
//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        rAdapter = new ListsRecyclerAdapter(getActivity(), boardID, boardPosition, listPosition, this, this);
        recyclerView.setAdapter(rAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(rAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private void setupAllCards(Realm realm, Board board, int myListPosition) {
        ArrayList<Card> cards = new ArrayList<>();
        Card card;
        for (int i = 0; i < 1; i++) {
            card = new Card();
            card.setName("Example Card");
            cards.add(card);
        }
        for (Card c : cards) {
            realm.beginTransaction();
            realm.copyToRealm(c);
            board.getMyLists().get(myListPosition).getCards().add(c);
            realm.commitTransaction();
        }
    }
}
