package com.example.limit_breaker.auto_task.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.limit_breaker.auto_task.R;
import com.example.limit_breaker.auto_task.adapters.AppListAdapter;
import com.example.limit_breaker.auto_task.interfaces.OnItemClickListener;
import com.example.limit_breaker.auto_task.utils.AppConditionsInfoUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppListActivity extends AppCompatActivity {

    RecyclerView rvAppView;
    ProgressBar pbProgress;
    PackageManager packageManager;
    AppListAdapter appListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        initFields();
        rvAppView.setLayoutManager(new LinearLayoutManager(this));
        GetAllAppInfos allAppInfos = new GetAllAppInfos();
        allAppInfos.execute();


    }

    private  void initFields(){
        rvAppView=(RecyclerView)findViewById(R.id.rvAppView);
        pbProgress=(ProgressBar)findViewById(R.id.pbProgress);
        packageManager=getPackageManager();
    }

    private List<ApplicationInfo> checkAppForLaunchIntent(List<ApplicationInfo> list){
        ArrayList<ApplicationInfo> appList= new ArrayList<>();
        for(ApplicationInfo thisAppInfo: list){
            if(packageManager.getLaunchIntentForPackage(thisAppInfo.packageName)!=null){
                appList.add(thisAppInfo);
            }
        }
        Collections.sort(appList, new Comparator<ApplicationInfo>() {
            @Override
            public int compare(ApplicationInfo applicationInfo, ApplicationInfo t1) {
                return applicationInfo.packageName.compareToIgnoreCase(t1.packageName);
            }
        });
        return appList;
    }

    private class GetAllAppInfos extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pbProgress.setVisibility(View.GONE);
            rvAppView.setAdapter(appListAdapter);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<ApplicationInfo> applicationInfos = (ArrayList<ApplicationInfo>) checkAppForLaunchIntent(packageManager.
                    getInstalledApplications(PackageManager.GET_META_DATA));
            appListAdapter=new AppListAdapter(applicationInfos,AppListActivity.this);
            appListAdapter.setItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(String name,String packageName) {
                    Intent i = new Intent(AppListActivity.this, ConditionsActivity.class);
                    i.putExtra(AppConditionsInfoUtils.APP_NAME,name);
                    i.putExtra(AppConditionsInfoUtils.PACKAGE_NAME,packageName);
                    startActivity(i);
                }
            });
            return null;
        }
    }
}
