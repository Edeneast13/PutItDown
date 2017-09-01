package com.brianroper.putitdown.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brianroper.putitdown.R;
import com.brianroper.putitdown.model.DrivingEventLog;
import com.brianroper.putitdown.views.DrivingLogActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by brianroper on 8/29/17.
 */

public class DrivingLogEventAdapter extends RecyclerView.Adapter<DrivingLogEventAdapter.DrivingLogEventViewHolder> {

    private Context mContext;
    private RealmResults<DrivingEventLog> mRealmResults;
    final int DASHBOARD_LIST_ITEM_COUNT = 2;
    private boolean mIsDashboard = false;

    public DrivingLogEventAdapter(Context context) {
        mContext = context;
    }

    @Override
    public DrivingLogEventAdapter.DrivingLogEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View root = inflater.inflate(R.layout.neura_log_item, parent, false);
        final DrivingLogEventViewHolder drivingLogEventViewHolder = new DrivingLogEventViewHolder(root);

        if(mIsDashboard){
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent logIntent = new Intent(mContext, DrivingLogActivity.class);
                    mContext.startActivity(logIntent);
                }
            });
        }

        return drivingLogEventViewHolder;
    }

    @Override
    public void onBindViewHolder(DrivingLogEventAdapter.DrivingLogEventViewHolder holder, int position) {

        if(position == getItemCount() - 1){
            holder.mDivider.setVisibility(View.GONE);
        }

        if(mRealmResults.get(position).isSuccessful() == true){
            holder.mEventNameTextView.setText("you had a safe trip");
        }
        else if(mRealmResults.get(position).isSuccessful() == false){
            holder.mEventNameTextView.setText("you used your device while driving");
        }
        holder.mEventDateTextView.setText(mRealmResults.get(position).getDate());
        holder.mEventTimeTextView.setText(mRealmResults.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        if(mIsDashboard){
            return DASHBOARD_LIST_ITEM_COUNT;
        }
        else{
            return mRealmResults.size();
        }
    }

    public class DrivingLogEventViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.event_name)
        TextView mEventNameTextView;
        @BindView(R.id.event_date)
        TextView mEventDateTextView;
        @BindView(R.id.event_time)
        TextView mEventTimeTextView;
        @BindView(R.id.divider)
        RelativeLayout mDivider;

        public DrivingLogEventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * returns NeuraEventLog data from realm db
     */
    public RealmResults<DrivingEventLog> getDrivingEventLogFromRealm(){
        Realm realm;
        Realm.init(mContext);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfiguration);
        mRealmResults = realm.where(DrivingEventLog.class).findAll();
        return mRealmResults;
    }

    /**
     * returns all time driving logs
     */
    public void returnAllTimeDrivingEventLogs(){
        //TODO: sort all time logs
        //TODO: exlude this week and this month
    }

    /**
     * returns this week driving logs
     */
    public void returnThisWeekDrivingEventLogs(){
        //TODO: sort this week logs
    }

    /**
     * returns this month driving logs
     */
    public void returnThisMonthDrivingEventLogs(){
        //TODO: sort this month logs
    }

    /**
     * enables the listener for the recycler view in the dashboard
     */
    public void isDashboard(){
        mIsDashboard = true;
    }
}
