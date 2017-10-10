package com.example.limit_breaker.auto_task;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class twoActivity extends AppCompatActivity {
    RecyclerView rv;
    MyRecycler myRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        ArrayList<location> arrayList = new ArrayList<>();
        arrayList.add(new location(28.668939,77.231097,9,"Haryana Roadways"));
        arrayList.add(new location(28.66739,77.222192,10,"Lala Hardev Sahai Marg"));
        arrayList.add(new location(28.664861,77.230539,8,"Lothian Rd"));
        arrayList.add(new location(28.670969,77.228426,7,"Qudsia Bagh"));
        arrayList.add(new location(28.664673,77.22393,9,"Ram Bazar"));
        rv=(RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        myRecycler=new MyRecycler(this,arrayList);
        rv.setAdapter(myRecycler);

    }
}
