package com.treggo.flexible.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.hollyviewpager.HollyViewPagerBus;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.treggo.flexible.R;
import com.treggo.flexible.activities.DescriptionActivity;
import com.treggo.flexible.activities.TopActivity;
import com.treggo.flexible.model.CardFace;
import com.treggo.flexible.utilities.RecyclerAdapter;
import com.treggo.flexible.utilities.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by iRYO400 on 07.06.2016.
 */
public class RecyclerViewFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener{

    private static final String BUNDLE_CODE = "titleRecycler";

    RecyclerView recyclerView;
    RecyclerAdapter rAdapter;
    ObservableScrollView scrollView;
    TextView title;
    private Button btnAdd;
    private ArrayList<CardFace> cardFaces;
    private ArrayAdapter<String> spinnerAdapter;

    public static RecyclerViewFragment newInstance(String title){
        Bundle args = new Bundle();
        args.putString(BUNDLE_CODE, title);
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        cardFaces = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollView = (ObservableScrollView)view.findViewById(R.id.scrollView2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        rAdapter = new RecyclerAdapter(getActivity(), cardFaces);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.scrollTo(0,0);
        //ItemClick and LongItemClick @Utilities/RecyclerItemClickListener
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        recyclerView.setAdapter(rAdapter);


        final String[] spinnerValues = { "Blur", "NFS", "Burnout","GTA IV", "Racing", };
        spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerValues);

        title = (TextView) view.findViewById(R.id.titleRecycler);
        title.setText(getArguments().getString(BUNDLE_CODE));

        btnAdd = (Button) view.findViewById(R.id.btnXAddRec);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View subView = layoutInflater.inflate(R.layout.dialog_laoyut2, null);
                final EditText editText = (EditText) subView.findViewById(R.id.editCardName);
                Button btnSpinner = (Button) subView.findViewById(R.id.btnOpenSpinner);
//                final MultiSelectSpinner multiSpinner = (MultiSelectSpinner) subView.findViewById(R.id.myMultiSpinner);

                final Spinner spinner = (Spinner) subView.findViewById(R.id.spinner2);
                spinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerValues));

                btnSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        multiSpinner.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                    }
                });


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add card?");
                builder.setView(subView);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!editText.getText().toString().equals("")){
                            CardFace cardFace = new CardFace();
                            cardFace.setName(editText.getText().toString());
                            cardFace.setColor(spinner.getSelectedItemPosition());
                            cardFaces.add(cardFace);

                            rAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();

            }
        });



        HollyViewPagerBus.registerRecyclerView(getActivity(), recyclerView);
        HollyViewPagerBus.registerScrollView(getActivity(), scrollView);

    }

    @Override
    public void onItemClick(View childView, int position) {
        Intent intent = new Intent(getActivity(), DescriptionActivity.class);
        intent.putExtra("DescriptionArray", cardFaces);
        intent.putExtra("DescriptionPosition", position);
        startActivity(intent);
        Toast.makeText(getActivity(), "Position " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        Toast.makeText(getActivity(), "Long Position " + position, Toast.LENGTH_SHORT).show();
    }
}
