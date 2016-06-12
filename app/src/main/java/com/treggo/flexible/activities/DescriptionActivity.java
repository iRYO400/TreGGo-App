package com.treggo.flexible.activities;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.treggo.flexible.R;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.model.Board;
import com.treggo.flexible.model.Card;

import java.util.ArrayList;

import io.realm.Realm;

public class DescriptionActivity extends AppCompatActivity implements View.OnClickListener {

    private Realm realm;
    private Board board;
    private long boardID;
    private int bPosition;
    private int lPosition;
    private int cPosition;

    private FloatingActionButton miniFab1;
    private FloatingActionButton miniFab2;
    private FloatingActionButton miniFab3;
    private FloatingActionButton miniFab4;
    private FloatingActionsMenu menuFab;

    CollapsingToolbarLayout collapsingToolbarLayout;
    private EditText etAddDescription;
    private TextView tvDescription;
    private Button btnAddDescrition;
    private LinearLayout layoutDescription;
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

        if(board.getMyLists().get(lPosition).getCards().get(cPosition).isValid()) {
            collapsingToolbarLayout.setTitle(board.getMyLists().get(lPosition).getCards().get(cPosition).getName());
        }
    }


    private void initViewStuff(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        layoutDescription = (LinearLayout) findViewById(R.id.layout_description);
        etAddDescription = (EditText) findViewById(R.id.etAddDescription);
        tvDescription = (TextView) findViewById(R.id.tvDescription);

        btnAddDescrition = (Button) findViewById(R.id.btnAddDescription);
        btnAddDescrition.setOnClickListener(this);

        menuFab = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        miniFab1 = (FloatingActionButton) findViewById(R.id.miniFab1);
        miniFab1.setOnClickListener(this);
        miniFab2 = (FloatingActionButton) findViewById(R.id.miniFab2);
        miniFab2.setOnClickListener(this);
        miniFab3 = (FloatingActionButton) findViewById(R.id.miniFab3);
        miniFab3.setOnClickListener(this);
        miniFab4 = (FloatingActionButton) findViewById(R.id.miniFab4);
        miniFab4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.miniFab1:
                layoutDescription.setVisibility(View.VISIBLE);
                menuFab.collapse();
                break;
            case R.id.miniFab2:

                menuFab.collapse();
                break;
            case R.id.miniFab3:

                menuFab.collapse();
                break;
            case R.id.miniFab4:

                menuFab.collapse();
                break;
            case R.id.btnAddDescription:
                tvDescription.setText(etAddDescription.getText().toString());
                etAddDescription.getText().clear();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etAddDescription.getWindowToken(), 0);
                layoutDescription.setVisibility(View.GONE);
                break;
        }
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
