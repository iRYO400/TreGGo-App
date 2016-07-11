package com.treggo.flexible.lists;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.treggo.flexible.R;
import com.treggo.flexible.card.DescriptionActivity;
import com.treggo.flexible.adapters.dragDropHelper.ItemTouchHelperAdapter;
import com.treggo.flexible.adapters.dragDropHelper.ItemTouchHelperViewHolder;
import com.treggo.flexible.adapters.dragDropHelper.OnStartDragListener;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.board.Board;

import java.io.File;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by iRYO400 on 07.06.2016.
 */
public class ListsRecyclerAdapter extends RealmRecyclerViewListAdapter<Board> implements ItemTouchHelperAdapter {

    public static final int HEADER = 0;
    public static final int TYPE_CELL = 1;
    private static final String TAG = "mLogs";

    private final OnStartDragListener mDragStartListener;
    private AddCards updateViews;
    private Refresher refresher;


    private Context context;
    private Realm realm;
    private long boardID;
    private int boardPosition;
    private int listPosition;

    private boolean isLabelsCreated;

    public ListsRecyclerAdapter(Context context, long boardID, int boardPosition, int listPosition,
                                OnStartDragListener dragListener, AddCards update) {
        this.context = context;
        this.boardID = boardID;
        this.boardPosition = boardPosition;
        this.listPosition = listPosition;
        this.mDragStartListener = dragListener;
        this.updateViews = update;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public ListsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_header_in_list, parent, false);
                return new CardHeaderViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_in_list, parent, false);
                return new CardListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ListsRecyclerAdapter.MyViewHolder holder, int position) {
        this.realm = RealmController.getInstance().getRealm();
        refresher = (Refresher) context;
        if (holder.getItemViewType() == HEADER) {

            final CardHeaderViewHolder cardHeaderVH = (CardHeaderViewHolder) holder;
            ((CardHeaderViewHolder) holder).card_btnOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateViews.addCard(listPosition);
                }
            });
        } else {
            final CardListViewHolder cardListVH = (CardListViewHolder) holder;
            setupCardView(cardListVH);
        }
    }

    private void setupCardView(final CardListViewHolder cardListVH) {
        final RealmResults<Board> results = realm.where(Board.class).findAll();
        Board mModel = results.get(boardPosition);
        cardListVH.card_labels_layout.removeAllViews();
        cardListVH.card_image_layout.removeAllViews();

        //Имя карточки в списке
        if (mModel.getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()).isValid()) {
            cardListVH.card_list_name.setText(getItem(boardPosition).getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()).getName());
        }

        //Attachments
        if (mModel.getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()).isValid()
                && !mModel.getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()).getAttachments().isEmpty()) {
            cardListVH.card_image_layout.setVisibility(View.VISIBLE);
            ImageView imageView = new ImageView(context);
//            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, R.dimen.app_bar_height);
//            llp.setMargins(0,-8,0,0);
            imageView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            cardListVH.card_image_layout.addView(imageView);


            Picasso.with(context)
                    .load(new File(mModel.getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()).getAttachments().get(0).getPicPath()))
                    .noPlaceholder()
                    .resize(300, 150)
                    .into(imageView);

        }

        //Labels of Card
        if (getItem(boardPosition).getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()).isLoaded()
                && !getItem(boardPosition).getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()).getLabelList().isEmpty()) {
            cardListVH.card_labels_layout.setVisibility(View.VISIBLE);

            for (int i = 0; i < getItem(boardPosition).getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()).getLabelList().size(); i++) {
                TextView textView = new TextView(context);
                textView.setMinWidth(context.getResources().getDimensionPixelOffset(R.dimen.minimum_width_free_label_card));
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, context.getResources().getDimensionPixelOffset(R.dimen.minimum_height_free_label_card));
                llp.setMargins(1, 2, 6, 10);
                textView.setLayoutParams(llp);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                switch (getItem(boardPosition).getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()).getLabelList().get(i).getColor()) {
                    case 0:
                        textView.setBackgroundResource(R.drawable.label_background_blue);
                        break;
                    case 1:
                        textView.setBackgroundResource(R.drawable.label_background_green);
                        break;
                    case 2:
                        textView.setBackgroundResource(R.drawable.label_background_grey);
                        break;
                    case 3:
                        textView.setBackgroundResource(R.drawable.label_background_red);
                        break;
                    case 4:
                        textView.setBackgroundResource(R.drawable.label_background_yellow);
                        break;
                    case 5:
                        textView.setBackgroundResource(R.drawable.label_background_pink);
                        break;
                }
                isLabelsCreated = true;
                cardListVH.card_labels_layout.addView(textView);
            }

        }

        //Open Card
        cardListVH.card_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DescriptionActivity.class);
                intent.putExtra("c_bID", boardID);
                intent.putExtra("c_bPosition", boardPosition);
                intent.putExtra("c_lPosition", listPosition);
                intent.putExtra("c_cPosition", cardListVH.getAdapterPosition());
                context.startActivity(intent);
            }
        });

        //Drag'n'Drop Card( Only Up and Down)
        cardListVH.card_list.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "Adapter Position " + cardListVH.getAdapterPosition());

                mDragStartListener.onStartDrag(cardListVH);
                return false;
            }
        });

        //Popup menu for every individual Card( Rename, Delete, Move Left and Right)
        cardListVH.card_btnOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.inflate(R.menu.list_options);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                LayoutInflater layoutInflater = LayoutInflater.from(context);
                                View subView = layoutInflater.inflate(R.layout.dialog_add_card, null);
                                final EditText editText = (EditText) subView.findViewById(R.id.editCardName);

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Rename card.");
                                builder.setView(subView);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (editText.getText().toString().equals("")) {
                                            Toast.makeText(context, "Wrong Name", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Board board = results.get(boardPosition);
                                            realm.beginTransaction();
                                            board.getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()).setName(editText.getText().toString());
                                            realm.copyToRealmOrUpdate(board);
                                            realm.commitTransaction();
                                            notifyDataSetChanged();
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
                                return true;
                            case R.id.menu2:

                                realm.beginTransaction();
                                results.get(boardPosition).getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()).deleteFromRealm();
                                realm.commitTransaction();

                                cardListVH.card_labels_layout.setVisibility(View.GONE);
                                cardListVH.card_image_layout.setVisibility(View.GONE);
                                notifyDataSetChanged();
                                return true;
                            case R.id.menu3:
                                if (listPosition != 0) {
                                    int leftFrag = listPosition - 1;
                                    Board board = results.get(boardPosition);
                                    realm.beginTransaction();
                                    board.getMyLists().get(leftFrag).getCards().add(results.get(boardPosition).getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()));
                                    realm.copyToRealmOrUpdate(board);
                                    results.get(boardPosition).getMyLists().get(listPosition).getCards().remove(cardListVH.getAdapterPosition());
                                    realm.commitTransaction();
                                    cardListVH.card_labels_layout.setVisibility(View.GONE);
                                    cardListVH.card_image_layout.setVisibility(View.GONE);
                                    refresher.refreshActivity(cardListVH.getAdapterPosition());
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "Oops...", Toast.LENGTH_SHORT).show();
                                }
                                notifyDataSetChanged();
                                return true;
                            case R.id.menu4:
                                if (listPosition < (getItem(boardPosition).getMyLists().size() - 1)) {
                                    int rightFrag = listPosition + 1;
                                    Board board = results.get(boardPosition);
                                    realm.beginTransaction();
                                    board.getMyLists().get(rightFrag).getCards().add(results.get(boardPosition).getMyLists().get(listPosition).getCards().get(cardListVH.getAdapterPosition()));
                                    realm.copyToRealmOrUpdate(board);
                                    results.get(boardPosition).getMyLists().get(listPosition).getCards().remove(cardListVH.getAdapterPosition());
                                    realm.commitTransaction();
                                    cardListVH.card_labels_layout.setVisibility(View.GONE);
                                    cardListVH.card_image_layout.setVisibility(View.GONE);
                                    refresher.refreshActivity(cardListVH.getAdapterPosition());
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "Oops...", Toast.LENGTH_SHORT).show();
                                }
                                notifyDataSetChanged();
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        realm.beginTransaction();
        Collections.swap(getItem(boardPosition).getMyLists().get(listPosition).getCards(), fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        realm.commitTransaction();
        Log.d(TAG, "Dropped_____");
        return false;
    }

    @Override
    public void onItemDismiss(int position, int direction) {
    }

    @Override
    public long getItemID(int position) {
        return super.getItemID(position);
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            try {
                return getRealmAdapter().getItem(boardPosition).getMyLists().get(listPosition).getCards().size();
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.d(TAG, "Cyka o5 ETA OSHIBKA. B_Position " + boardPosition + " L_Position " + listPosition);
            }

        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View v) {
            super(v);
        }
    }

    public class CardHeaderViewHolder extends MyViewHolder {

        private CardView card_header;
        private Button card_btnOptions;

        public CardHeaderViewHolder(View itemView) {
            super(itemView);
            card_header = (CardView) itemView.findViewById(R.id.card_header);
            card_btnOptions = (Button) itemView.findViewById(R.id.card_btn_option);
        }
    }

    public class CardListViewHolder extends MyViewHolder
            implements ItemTouchHelperViewHolder {

        private CardView card_list;
        private TextView card_list_name;
        private Button card_btnOpt;
        private LinearLayout card_labels_layout;
        private LinearLayout card_image_layout;


        public CardListViewHolder(View itemView) {
            super(itemView);
            card_list = (CardView) itemView.findViewById(R.id.card_list);
            card_list_name = (TextView) itemView.findViewById(R.id.card_list_name);
            card_btnOpt = (Button) itemView.findViewById(R.id.card_btn_Opt);
            card_labels_layout = (LinearLayout) itemView.findViewById(R.id.card_labels_layout);
            card_image_layout = (LinearLayout) itemView.findViewById(R.id.card_image_layout);

        }

        @Override
        public void onItemSelected() {
//            itemView.setBackgroundResource(android.R.color.transparent);
        }

        @Override
        public void onItemClear() {
//            itemView.setBackgroundColor(Color.WHITE);
        }
    }
}
