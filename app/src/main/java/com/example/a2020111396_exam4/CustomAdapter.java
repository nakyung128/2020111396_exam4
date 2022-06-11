package com.example.a2020111396_exam4;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private ArrayList<Dictionary> mList;
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView city;
        protected TextView defCnt;
        protected TextView stdDay;
        protected TextView deathCnt;
        protected TextView localCnt;
        protected TextView overflowCnt;

        public CustomViewHolder(View view) {
            super(view);
            this.city = (TextView) view.findViewById(R.id.id_city);
            this.defCnt = (TextView) view.findViewById(R.id.id_defCnt);
            this.stdDay = (TextView) view.findViewById(R.id.id_stdDay);
            this.deathCnt = (TextView) view.findViewById(R.id.id_deathCnt);
            this.localCnt = (TextView) view.findViewById(R.id.id_local);
            this.overflowCnt = (TextView) view.findViewById(R.id.id_overflow);
        }
    }

    public CustomAdapter(ArrayList<Dictionary> list) {
        this.mList = list;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.city.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.defCnt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.stdDay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.deathCnt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.localCnt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.overflowCnt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.city.setGravity(Gravity.CENTER);
        viewholder.defCnt.setGravity(Gravity.CENTER);
        viewholder.stdDay.setGravity(Gravity.CENTER);
        viewholder.deathCnt.setGravity(Gravity.CENTER);
        viewholder.localCnt.setGravity(Gravity.CENTER);
        viewholder.overflowCnt.setGravity(Gravity.CENTER);

        viewholder.city.setText(mList.get(position).getCity());
        viewholder.defCnt.setText(mList.get(position).getDefCnt());
        viewholder.stdDay.setText(mList.get(position).getStdDay());
        viewholder.deathCnt.setText(mList.get(position).getDeathCnt());
        viewholder.localCnt.setText(mList.get(position).getLocalCnt());
        viewholder.overflowCnt.setText(mList.get(position).getOverflowCnt());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
