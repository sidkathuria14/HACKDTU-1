package com.example.limit_breaker.auto_task;

/**
 * Created by the-limit-breaker on 7/10/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.limit_breaker.auto_task.location;

import java.util.ArrayList;

/**
 * Created by sidkathuria14 on 7/10/17.
 */

public class MyRecycler extends RecyclerView.Adapter<MyRecycler.MyViewHolder>{
    private Context context;
    private ArrayList<location> arrayList;

    public MyRecycler(Context context, ArrayList<location> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void  UpdateLocations(ArrayList<location> arrayList){
this.arrayList = arrayList;
notifyDataSetChanged();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutType;
        if(viewType == 0){
            layoutType = R.layout.layout_green;
        }else if (viewType == 1){
            layoutType = R.layout.layout_yellow;
        }
        else layoutType = R.layout.layout_red;
        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = li.inflate(layoutType,parent,false);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyRecycler.MyViewHolder holder, int position) {
        location currentloc = arrayList.get(position);
        Log.d("hello", "onBindViewHolder: "+holder.tvRating);
        holder.tvRating.setText(String.valueOf(currentloc.getRating()));
      //  Log.d("hello", "onBindViewHolder: " + currentloc.getName() + currentloc.getRating());
        holder.tvName.setText(String.valueOf(currentloc.getName()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        location currentloc = arrayList.get(position);
        if(currentloc.getRating() == 5){
            return 2;
        }
        else if(currentloc.getRating() == 3 || currentloc.getRating() == 4 ){
            return 1;
        }
        else return 0;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvRating;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView)itemView.findViewById(R.id.tvLocationY);
            tvRating = (TextView)itemView.findViewById(R.id.tvRatingY);
        }
    }
}
