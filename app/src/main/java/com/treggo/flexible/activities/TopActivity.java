package com.treggo.flexible.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.shamanland.fab.FloatingActionButton;
import com.treggo.flexible.R;
import com.treggo.flexible.model.Board;
import com.treggo.flexible.utilities.MyListViewAdapter;
import com.treggo.flexible.utilities.TinyDB;

import java.util.ArrayList;

public class TopActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton fab;

    private ArrayList<String> spTemplates;
    private ArrayAdapter<String> spinnerAdapter;

    private ArrayList<Board> boardList;
    private MyListViewAdapter myListViewAdapter;

    private TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tinyDB = new TinyDB(this);
        boardList = new ArrayList<>();
        boardList = tinyDB.getListBoards("AllBoards", Board.class);
        myListViewAdapter = new MyListViewAdapter(this, boardList);

        spTemplates = new ArrayList<>();
        spTemplates.add("Base Four");
        spTemplates.add("Week");
        spTemplates.add("Months");

        listView = (ListView) findViewById(R.id.myListView);

        listView.setAdapter(myListViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Intent intent = new Intent(TopActivity.this, MainActivity.class);
                tinyDB.putString("BoardType", myListViewAdapter.getBoard(position).getType());
                tinyDB.putString("BoardName", myListViewAdapter.getBoard(position).getName());
                startActivity(intent);
                Toast.makeText(TopActivity.this, myListViewAdapter.getBoard(position).getName()
                        +  " " + position + " "
                        + myListViewAdapter.getBoard(position).getType(), Toast.LENGTH_SHORT).show();

            }
        });


        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spTemplates);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fab = (FloatingActionButton) findViewById(R.id.fab);
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
                            if(!editName.getText().toString().equals("")) {

                                //Добавили в имя и тип
                                Board board = new Board();
                                board.setName(editName.getText().toString());
                                board.setType(spinner.getSelectedItem().toString());
                                boardList.add(board);
                                //Сохранили его
                                tinyDB.putListBoards("AllBoards", boardList);


                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();

    //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
    //                        .setAction("Action", null).show();
                }
            });
        }
//        listView.setOnTouchListener(new ShowHideOnScroll(fab));
    }


}
