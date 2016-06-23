package com.treggo.flexible.board;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.treggo.flexible.R;
import com.treggo.flexible.adapters.RealmBoardsAdapter;
import com.treggo.flexible.app.Preferences;
import com.treggo.flexible.app.RealmController;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class TopActivity extends AppCompatActivity {

    private BoardsRecyclerAdapter mmAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private Realm realm;

    private ArrayList<String> spTemplates;
    private ArrayAdapter<String> spinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        fab = (FloatingActionButton) findViewById(R.id.fabTop);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerTop);

        //get realm Instance
        this.realm = RealmController.with(this).getRealm();

        //set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);

        setupRecyclerView();

        if(!Preferences.with(this).getPreLoad()){
//            setRealmData();
        }

        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(this).getBoards());

        //Adding Spinner items
        spTemplates = new ArrayList<>();
        spTemplates.add("Base Four");
        spTemplates.add("Week");
        spTemplates.add("Months");
        //Spinner Adapter
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spTemplates);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //FAB
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = LayoutInflater.from(TopActivity.this);
                    View subView = inflater.inflate(R.layout.dialog_layout, null);
                    final EditText editName = (EditText) subView.findViewById(R.id.EditTextName);
                    final Spinner spinner = (Spinner) subView.findViewById(R.id.spinner);
                    spinner.setAdapter(spinnerAdapter);
                    spinner.setSelection(0);

                    AlertDialog.Builder builder = new AlertDialog.Builder(TopActivity.this);
                    builder.setTitle("Create your Board");
                    builder.setView(subView);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Board board = new Board();
                            board.setId(RealmController.getInstance().getBoards().size() + System.currentTimeMillis());
                            board.setName(editName.getText().toString());
                            board.setType(spinner.getSelectedItemPosition());

                            if(editName.getText().toString().equals("")){
                                Toast.makeText(TopActivity.this, "ENTER CORRECT NAME", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //Добавили в имя и тип
                                realm.beginTransaction();
                                realm.copyToRealm(board);
                                realm.commitTransaction();
                                mmAdapter.notifyDataSetChanged();
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
    }

    public void setRealmAdapter(RealmResults<Board> boards){

        RealmBoardsAdapter modelsAdapter = new RealmBoardsAdapter(this.getApplicationContext(), boards, true);
        mmAdapter.setRealmAdapter(modelsAdapter);
        mmAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView(){

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mmAdapter = new BoardsRecyclerAdapter(this);
        recyclerView.setAdapter(mmAdapter);
    }

    private void setRealmData() {

        ArrayList<Board> mModels = new ArrayList<>();

        Board mModel = new Board();

        mModel.setId(1 + System.currentTimeMillis());
        mModel.setName("Reto Meier");
        mModel.setType(0);
        mModels.add(mModel);

        mModel = new Board();
        mModel.setId(2 + System.currentTimeMillis());
        mModel.setName("Itzik Ben-Gan");
        mModel.setType(0);
        mModels.add(mModel);

        mModel = new Board();
        mModel.setId(3 + System.currentTimeMillis());
        mModel.setName("Magnus Lie Hetland");
        mModel.setType(0);
        mModels.add(mModel);

        for (Board b : mModels) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }
        Preferences.with(this).setPreLoad(true);
    }
}

