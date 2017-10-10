package com.example.limit_breaker.auto_task.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.limit_breaker.auto_task.R;
import com.example.limit_breaker.auto_task.interfaces.OnItemClickListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by limit-breaker on 22/7/17.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppListViewHolder> {
    ArrayList<ApplicationInfo> applicationInfos;
    Context mContext;
    PackageManager mPackageManager;
    OnItemClickListener itemClickListener;

    public AppListAdapter(ArrayList<ApplicationInfo> applicationInfos, Context mContext) {
        this.applicationInfos = applicationInfos;
        this.mContext = mContext;
        this.mPackageManager=mContext.getPackageManager();
    }

    public void setItemClickListener( OnItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    @Override

    public AppListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate(R.layout.list_item_application,parent,false);
        return new AppListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppListViewHolder holder, int position) {
        if(applicationInfos!=null && applicationInfos.size()!=0){
            final ApplicationInfo  thisAppInfo = applicationInfos.get(position);
            holder.ivAppIcon.setImageDrawable(thisAppInfo.loadIcon(mPackageManager));
            holder.tvAppName.setText(thisAppInfo.loadLabel(mPackageManager));
            holder.itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener!=null){
                        String appLabel = thisAppInfo.loadLabel(mPackageManager).toString();
                        itemClickListener.onItemClick(appLabel,thisAppInfo.packageName);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return applicationInfos.size();
    }

    public class AppListViewHolder extends RecyclerView.ViewHolder {
        View itemContainer;
        ImageView ivAppIcon;
        TextView tvAppName;
        public AppListViewHolder(View itemView) {
            super(itemView);
            itemContainer=itemView;
            ivAppIcon=(ImageView)itemView.findViewById(R.id.ivAppIcon);
            tvAppName=(TextView)itemView.findViewById(R.id.tvAppName);
        }
    }
}
