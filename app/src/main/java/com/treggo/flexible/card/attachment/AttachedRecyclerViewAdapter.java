package com.treggo.flexible.card.attachment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.treggo.flexible.R;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.board.Board;

import java.io.File;
import java.io.FileNotFoundException;

import io.realm.Realm;

/**
 * Created by iRYO400 on 23.06.2016.
 */
public class AttachedRecyclerViewAdapter extends RealmRecyclerViewAttachedAdapter<Board> {

    public static final int PICTURE = 0;
    public static final int DOCUMENT = 1;

    private static final String TAG = "mLogs";
    private Context context;
    private Realm realm;
    private Board board;
    private int lPosition;
    private int cPosition;

    public AttachedRecyclerViewAdapter(Context context, Board board, int lPosition, int cPosition) {
        this.context = context;
        this.board = board;
        this.lPosition = lPosition;
        this.cPosition = cPosition;
    }

    @Override
    public int getItemViewType(int position) {
        if (!board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().isEmpty()) {
            switch (board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().get(position).getViewType()) {
                case 0:
                    return PICTURE;
                default:
                    return DOCUMENT;
            }
        }
        return 0;
    }

    @Override
    public AttachedRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case PICTURE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_attach_pic, parent, false);
                return new AttachedRecyclerViewHolder.AttachedRVPics(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_attach_doc, parent, false);
                return new AttachedRecyclerViewHolder.AttachedRVDocs(view);
        }
    }

    @Override
    public void onBindViewHolder(AttachedRecyclerViewHolder holder, int position) {
        this.realm = RealmController.getInstance().getRealm();

        if (board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().isValid()
                && !board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().isEmpty()) {
            if (holder.getItemViewType() == PICTURE) {
                final AttachedRecyclerViewHolder.AttachedRVPics aRVPics = (AttachedRecyclerViewHolder.AttachedRVPics) holder;

                //Open attachment in Gallery
                aRVPics.card_pic_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse("file://" + board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().get(aRVPics.getAdapterPosition()).getPicPath()), "image/*");
                        context.startActivity(intent);
                    }
                });
                //Open setting for attachment
                aRVPics.card_pic_img.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        View subView = layoutInflater.inflate(R.layout.dialog_att_settings, null);
                        Button btnDeleteAtt = (Button) subView.findViewById(R.id.btnDeleteAtt);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setView(subView);
                        final AlertDialog show = builder.show();

                        btnDeleteAtt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                realm.beginTransaction();
                                new File(board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().get(aRVPics.getAdapterPosition()).getPicPath()).delete();
                                board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().get(aRVPics.getAdapterPosition()).deleteFromRealm();
                                realm.commitTransaction();
                                show.dismiss();
                                notifyDataSetChanged();
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return false;
                    }
                });

                Picasso.with(context)
                        .load("file://" + board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().get(position).getPicPath())
                        .resize(100, 100)
                        .into(aRVPics.card_pic_img);

            } else {
                AttachedRecyclerViewHolder.AttachedRVDocs aRVDocs = (AttachedRecyclerViewHolder.AttachedRVDocs) holder;
                aRVDocs.card_doc.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Toast.makeText(context, "LONG CLICK TO DOCUMENT", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                aRVDocs.card_doc_name.setText(board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().get(position).getDocName());
                aRVDocs.card_doc_info.setText(board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().get(position).getDocInfo());

            }

        }
    }

    @Override
    public int getItemCount() {
        return board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().size();
    }
}
