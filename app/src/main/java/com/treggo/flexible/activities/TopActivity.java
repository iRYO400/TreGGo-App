package com.treggo.flexible.activities;

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

import com.shamanland.fab.FloatingActionButton;
import com.treggo.flexible.R;
import com.treggo.flexible.adapters.MainModelsAdapter;
import com.treggo.flexible.adapters.RealmMainModelsAdapter;
import com.treggo.flexible.app.Preferences;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.model.Board;
import com.treggo.flexible.model.MainModel;
import com.treggo.flexible.utilities.TinyDB;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class TopActivity extends AppCompatActivity {

    private MainModelsAdapter mmAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private Realm realm;

    private ArrayList<String> spTemplates;
    private ArrayAdapter<String> spinnerAdapter;

    private ArrayList<Board> boardList;


    private TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);


        tinyDB = new TinyDB(this);
        boardList = new ArrayList<>();
        boardList = tinyDB.getListBoards("AllBoards", Board.class);

        fab = (FloatingActionButton) findViewById(R.id.fabTop);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerTop);

        //get realm Instance
        this.realm = RealmController.with(this).getRealm();

        //set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);

        setupRecyclerView();

        if(!Preferences.with(this).getPreLoad()){
            setRealmData();
        }

        //waiting for REFRESH Realm instance
//        RealmController.with(this).waitForChange();

        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(this).getMainModels());

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
                    spinner.setSelection(1);

                    AlertDialog.Builder builder = new AlertDialog.Builder(TopActivity.this);
                    builder.setTitle("Create your Board");
                    builder.setMessage("Name it, and choose Template");
                    builder.setView(subView);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainModel mainModel = new MainModel();
                            mainModel.setId(RealmController.getInstance().getMainModels().size()+1);
                            mainModel.setNameBoard(editName.getText().toString());
                            mainModel.setBoardType(spinner.getSelectedItemPosition());

                            if(editName.getText().toString().equals("")){
                                Toast.makeText(TopActivity.this, "ENTER CORRECT NAME", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //Добавили в имя и тип
                                realm.beginTransaction();
                                realm.copyToRealm(mainModel);
                                realm.commitTransaction();
                                mmAdapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(RealmController.getInstance().getMainModels().size()-1);
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                }
            });
        }
    }

    public void setRealmAdapter(RealmResults<MainModel> mainModels){

        RealmMainModelsAdapter modelsAdapter = new RealmMainModelsAdapter(this.getApplicationContext(), mainModels, true);
        mmAdapter.setRealmAdapter(modelsAdapter);
        mmAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView(){

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mmAdapter = new MainModelsAdapter(this);
        recyclerView.setAdapter(mmAdapter);
    }

    private void setRealmData() {

        ArrayList<MainModel> mModels = new ArrayList<>();

        MainModel mModel = new MainModel();

        mModel.setId(1 + System.currentTimeMillis());
        mModel.setNameBoard("Reto Meier");
        mModel.setBoardType(0);
        mModels.add(mModel);

        mModel = new MainModel();
        mModel.setId(2 + System.currentTimeMillis());
        mModel.setNameBoard("Itzik Ben-Gan");
        mModel.setBoardType(1);
        mModels.add(mModel);

        mModel = new MainModel();
        mModel.setId(3 + System.currentTimeMillis());
        mModel.setNameBoard("Magnus Lie Hetland");
        mModel.setBoardType(2);
        mModels.add(mModel);

        mModel = new MainModel();
        mModel.setId(4 + System.currentTimeMillis());
        mModel.setNameBoard("Chad Fowler");
        mModel.setBoardType(3);
        mModels.add(mModel);

        mModel = new MainModel();
        mModel.setId(5 + System.currentTimeMillis());
        mModel.setNameBoard("Yashavant Kanetkar");
        mModel.setBoardType(4);
        mModels.add(mModel);


        for (MainModel b : mModels) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }
        Preferences.with(this).setPreLoad(true);

    }
}

