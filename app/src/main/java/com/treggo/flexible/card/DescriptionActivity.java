package com.treggo.flexible.card;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.treggo.flexible.R;
import com.treggo.flexible.adapters.CheckListAdapter;
import com.treggo.flexible.adapters.RealmBoardsAdapter;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.board.Board;
import com.treggo.flexible.card.label.Label;
import com.treggo.flexible.card.label.LabelRecyclerViewAdapter;
import com.treggo.flexible.card.attachment.AttachedString;
import com.treggo.flexible.utilities.SaveCameraAttachments;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class DescriptionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "mLogs";
    int TAKE_PHOTO_CODE = 0;

    //Realm
    private Realm realm;
    private Board board;
    private long boardID;
    private int bPosition;
    private int lPosition;
    private int cPosition;

    //FAB
    private FloatingActionButton miniFab1;
    private FloatingActionButton miniFab2;
    private FloatingActionButton miniFab3;
    private FloatingActionButton miniFab4;
    private FloatingActionsMenu menuFab;

    //Labels
    private RecyclerView mRecyclerViewLabel;
    private LabelRecyclerViewAdapter rAdapterLabel;
    private LinearLayout layoutLabels;


    //CheckList
    private ListView checkList;
    private CheckListAdapter checkListAdapter;
    private EditText etAddCheckItem;
    private LinearLayout layoutCheckList;

    //Toolbar and base
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private TextView tvNameInfo;
    private EditText etAddDescription;
    private TextView tvDescription;



    private LinearLayout layoutYesNo;
    private Button btnYes;
    private Button btnNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        this.realm = RealmController.with(this).getRealm();

        initViewStuff();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            boardID = extras.getLong("c_bID");
            bPosition = extras.getInt("c_bPosition");
            lPosition = extras.getInt("c_lPosition");
            cPosition = extras.getInt("c_cPosition");
        }
        board = realm.where(Board.class).equalTo("id", boardID).findFirst();


        //Initiate Stuff from Realm
        if(board.getMyLists().get(lPosition).getCards().get(cPosition).isValid()) {

            collapsingToolbarLayout.setTitle(board.getMyLists().get(lPosition).getCards().get(cPosition).getName());
            tvNameInfo.setText("'" + board.getName() + "'" + " in list '" + board.getMyLists().get(lPosition).getName() +"'");

            //Description from Realm
            if(board.getMyLists().get(lPosition).getCards().get(cPosition).getDescription() != null) {
                tvDescription.setText(board.getMyLists().get(lPosition).getCards().get(cPosition).getDescription());
            }

            //LabelList from Realm
            if(!board.getMyLists().get(lPosition).getCards().get(cPosition).getLabelList().isEmpty()) {
                layoutLabels.setVisibility(View.VISIBLE);
                setupRecyclerViews();
                setRealmAdapter(RealmController.with(this).getBoards());
            }

            //CheckList from Realm
            if(board.getMyLists().get(lPosition).getCards().get(cPosition).getCheckLists() != null) {
                checkListAdapter = new CheckListAdapter(this, board.getMyLists().get(lPosition).getCards().get(cPosition).getCheckLists());
                checkList.setAdapter(checkListAdapter);
            }

            //Attachments from Realm
            if(!board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().isEmpty()){
                Log.d(TAG, "FROM REALM " + board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().get(0).getString());

            }
        }
    }


    private void initViewStuff(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        tvNameInfo = (TextView) findViewById(R.id.tvNameInfo);


        etAddDescription = (EditText) findViewById(R.id.etAddDescription);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDescription.setOnClickListener(this);


        //Accept Decline btns
        layoutYesNo = (LinearLayout)findViewById(R.id.layout_YesNo);
        btnYes = (Button) findViewById(R.id.btnYes);
        btnYes.setOnClickListener(this);
        btnNo = (Button) findViewById(R.id.btnNo);
        btnNo.setOnClickListener(this);


        //Label Stuff
        mRecyclerViewLabel = (RecyclerView) findViewById(R.id.recyclerLabelView);
        layoutLabels = (LinearLayout) findViewById(R.id.layout_labels);

        //Fab Menu and Buttons
        menuFab = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        miniFab1 = (FloatingActionButton) findViewById(R.id.miniFab1);
        miniFab1.setOnClickListener(this);
        miniFab2 = (FloatingActionButton) findViewById(R.id.miniFab2);
        miniFab2.setOnClickListener(this);
        miniFab3 = (FloatingActionButton) findViewById(R.id.miniFab3);
        miniFab3.setOnClickListener(this);
        miniFab4 = (FloatingActionButton) findViewById(R.id.miniFab4);
        miniFab4.setOnClickListener(this);

        layoutCheckList = (LinearLayout) findViewById(R.id.layout_checkList);
        checkList = (ListView) findViewById(R.id.checkListView);
        etAddCheckItem = (EditText) findViewById(R.id.etAddCheckItem);
        etAddCheckItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    tvDescription.setVisibility(View.VISIBLE);
                    layoutYesNo.setVisibility(View.VISIBLE);
                    if(etAddDescription.getVisibility() == View.VISIBLE) {
                        etAddDescription.setVisibility(View.GONE);
                        etAddDescription.getText().clear();
                    }
                }
            }
        });
    }

    public void setRealmAdapter(RealmResults<Board> boards) {
        RealmBoardsAdapter boardsAdapter = new RealmBoardsAdapter(getApplicationContext(), boards, true);
        rAdapterLabel.setRealmAdapter(boardsAdapter);
        rAdapterLabel.notifyDataSetChanged();
    }

    private void setupRecyclerViews(){
        mRecyclerViewLabel.setHasFixedSize(true);
        mRecyclerViewLabel.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        rAdapterLabel = new LabelRecyclerViewAdapter(this, board, lPosition, cPosition);
        mRecyclerViewLabel.setAdapter(rAdapterLabel);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvDescription:
                tvDescription.setVisibility(View.GONE);
                etAddDescription.setVisibility(View.VISIBLE);
                layoutYesNo.setVisibility(View.VISIBLE);
                etAddDescription.requestFocus();
                break;

            case R.id.btnYes:
                etAddDescription.setVisibility(View.GONE);
                layoutYesNo.setVisibility(View.GONE);

                if(tvDescription.getVisibility() == View.GONE){
                    if(!etAddDescription.getText().toString().equals("")) {
                        tvDescription.setText(etAddDescription.getText().toString());
                    }
                    tvDescription.setVisibility(View.VISIBLE);

                    realm.beginTransaction();
                    board.getMyLists().get(lPosition).getCards().get(cPosition).setDescription(etAddDescription.getText().toString());
                    realm.commitTransaction();

                    etAddDescription.getText().clear();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etAddDescription.getWindowToken(), 0);
                }

                break;

            case R.id.btnNo:
                etAddDescription.setVisibility(View.GONE);
                layoutYesNo.setVisibility(View.GONE);
                tvDescription.setVisibility(View.VISIBLE);
                break;

            case R.id.miniFab1:
                tvDescription.setVisibility(View.GONE);
                etAddDescription.setVisibility(View.VISIBLE);
                layoutYesNo.setVisibility(View.VISIBLE);
                etAddDescription.requestFocus();
                menuFab.collapse();
                break;

            case R.id.miniFab2:
                fillLabelList();
                menuFab.collapse();
                break;

            case R.id.miniFab3:
//                if(board.getMyLists().get(lPosition).getCards().get(cPosition).getCheckLists() == null) {
//                    layoutCheckList.setVisibility(View.VISIBLE);
//                    checkListAdapter = new CheckListAdapter(this, board.getMyLists().get(lPosition).getCards().get(cPosition).getCheckLists());
//                    checkList.setAdapter(checkListAdapter);
//                }
                menuFab.collapse();
                break;

            case R.id.miniFab4:
                addAttachments();
                menuFab.collapse();
                break;
        }
    }

    private void fillLabelList(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View subView = layoutInflater.inflate(R.layout.dialog_label_creator, null);
        final EditText et1 = (EditText) subView.findViewById(R.id.etLabel1);
        final EditText et2 = (EditText) subView.findViewById(R.id.etLabel2);
        final EditText et3 = (EditText) subView.findViewById(R.id.etLabel3);
        final EditText et4 = (EditText) subView.findViewById(R.id.etLabel4);
        final EditText et5 = (EditText) subView.findViewById(R.id.etLabel5);
        final EditText et6 = (EditText) subView.findViewById(R.id.etLabel6);

        final CheckBox cb1 = (CheckBox) subView.findViewById(R.id.chLabel1);
        final CheckBox cb2 = (CheckBox) subView.findViewById(R.id.chLabel2);
        final CheckBox cb3 = (CheckBox) subView.findViewById(R.id.chLabel3);
        final CheckBox cb4 = (CheckBox) subView.findViewById(R.id.chLabel4);
        final CheckBox cb5 = (CheckBox) subView.findViewById(R.id.chLabel5);
        final CheckBox cb6 = (CheckBox) subView.findViewById(R.id.chLabel6);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Label");
        builder.setView(subView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<Label> labels = new ArrayList<Label>();
                Label label;

                label = new Label();
                label.setLabelName(et1.getText().toString());
                label.setChecked(cb1.isChecked());
                label.setColor(0);
                labels.add(label);

                label = new Label();
                label.setLabelName(et2.getText().toString());
                label.setChecked(cb2.isChecked());
                label.setColor(1);
                labels.add(label);

                label = new Label();
                label.setLabelName(et3.getText().toString());
                label.setChecked(cb3.isChecked());
                label.setColor(2);
                labels.add(label);

                label = new Label();
                label.setLabelName(et4.getText().toString());
                label.setChecked(cb4.isChecked());
                label.setColor(3);
                labels.add(label);

                label = new Label();
                label.setLabelName(et5.getText().toString());
                label.setChecked(cb5.isChecked());
                label.setColor(4);
                labels.add(label);

                label = new Label();
                label.setLabelName(et6.getText().toString());
                label.setChecked(cb6.isChecked());
                label.setColor(5);
                labels.add(label);

                realm.beginTransaction();
                board.getMyLists().get(lPosition).getCards().get(cPosition).getLabelList().clear();
                for(Label l: labels){
                    if(l.isChecked()) {

                        realm.copyToRealm(l);
                        board.getMyLists().get(lPosition).getCards().get(cPosition).getLabelList().add(l);

                    }
                }
                realm.commitTransaction();
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
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

    private void addAttachments(){
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View subView = layoutInflater.inflate(R.layout.dialog_add_attachs, null);
        final Button btnAttach1 = (Button) subView.findViewById(R.id.btn_attach1);
        final Button btnAttach2 = (Button) subView.findViewById(R.id.btn_attach2);
        final Button btnAttach3 = (Button) subView.findViewById(R.id.btn_attach3);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attach my ...");
        builder.setView(subView);
        builder.show();
        btnAttach1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            SaveCameraAttachments sca = new SaveCameraAttachments();
//            String selectedImagePath = getRealPathFromURI(imageUri);
            String imagePath = "";
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imagePath = sca.SaveImage(this, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Save path to Realm
            AttachedString attachedString = new AttachedString();
            attachedString.setString(imagePath);
            realm.beginTransaction();
            board.getMyLists().get(lPosition).getCards().get(cPosition).getAttachments().add(attachedString);
            realm.commitTransaction();
        }
    }

    public String getRealPathFromURI(Uri uri){
        if(uri == null){
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri,projection,null,null,null);
        if(cursor != null){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        menuFab.collapse();
    }

    //Close FABMenu, when missClick
    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (menuFab.isExpanded()) {

                Rect outRect = new Rect();
                menuFab.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
                    menuFab.collapse();
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
