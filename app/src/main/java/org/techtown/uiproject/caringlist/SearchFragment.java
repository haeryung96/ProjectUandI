package org.techtown.uiproject.caringlist;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.techtown.uiproject.R;
import org.techtown.uiproject.caringlist.DBHelper;
import org.techtown.uiproject.caringlist.Person;
import org.techtown.uiproject.caringlist.PersonAdapter;

import java.util.ArrayList;
public class SearchFragment extends Fragment { //조회하기 탭의 부분화면
    RecyclerView recyclerView;
    PersonAdapter adapter;
    TextView textView;
    DBHelper dbHelper;
    public SearchFragment() {// Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView =(ViewGroup)inflater.inflate(R.layout.fragment_recyclerview,container,false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        textView= rootView.findViewById(R.id.textView4); // 총 몇명인지 count

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        dbHelper = new DBHelper(getContext());
        adapter = new PersonAdapter();
        recyclerView.setAdapter(adapter);

        Button button = rootView.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Person> result = dbHelper.selectAll();
                adapter.setItems(result);
                textView.setText("Total: "+adapter.getItemCount()+'명');
                adapter.notifyDataSetChanged();
                //adapter.addItem(new Person(dbHelper.getResult(),"010-1111-1111","한국","112"));
            }
        });
        return rootView;

    }

}