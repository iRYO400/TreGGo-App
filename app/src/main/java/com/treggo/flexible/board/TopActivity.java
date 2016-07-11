package com.treggo.flexible.board;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.treggo.flexible.R;
import com.treggo.flexible.adapters.RealmBoardsAdapter;
import com.treggo.flexible.app.Preferences;
import com.treggo.flexible.app.RealmController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class TopActivity extends AppCompatActivity {

    private BoardsRecyclerAdapter mmAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private Realm realm;
    private RealmResults<Board> boards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fabTop);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerTop);
        //get realm Instance
        this.realm = RealmController.with(this).getRealm();

        setupRecyclerView();

        if(!Preferences.with(this).getPreLoad()){
            setRealmData();
        }

        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        boards = realm.where(Board.class).findAll();
        setRealmAdapter(boards);

        //FAB
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = LayoutInflater.from(TopActivity.this);
                    final View subView = inflater.inflate(R.layout.dialog_add_board, null);
                    final RadioGroup radioGroup = (RadioGroup) subView.findViewById(R.id.radioGroup);
                    final EditText editName = (EditText) subView.findViewById(R.id.EditTextName);

                    AlertDialog.Builder builder = new AlertDialog.Builder(TopActivity.this);
                    builder.setTitle("Create your Board");
                    builder.setView(subView);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int selectedType = radioGroup.indexOfChild(subView.findViewById(radioGroup.getCheckedRadioButtonId()));
                            String data = getCurrentTimeAndDate();
                            Board board = new Board();
                            board.setId(RealmController.getInstance().getBoards().size() + System.currentTimeMillis());
                            board.setName(editName.getText().toString());
                            board.setType(selectedType);
                            board.setDateLastView(data);
                            if(editName.getText().toString().equals("")){
                                Toast.makeText(TopActivity.this, "ENTER CORRECT NAME", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //Добавили в имя и тип
                                Toast.makeText(TopActivity.this, "TIME IS " + getCurrentTimeAndDate(), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.top_action_settings:
                return true;
            case R.id.top_action_about:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRealmAdapter(RealmResults<Board> boards){

        RealmBoardsAdapter modelsAdapter = new RealmBoardsAdapter(this.getApplicationContext(), boards);
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

        String data = getCurrentTimeAndDate();
        Board mModel = new Board();
        mModel.setId(1 + System.currentTimeMillis());
        mModel.setName("Example Board");
        mModel.setType(0);
        mModel.setDateLastView(data);

        // Persist your data easily
        realm.beginTransaction();
        realm.copyToRealm(mModel);
        realm.commitTransaction();

        Preferences.with(this).setPreLoad(true);
    }

    private String getCurrentTimeAndDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
        return smf.format(c.getTime());
    }
}

