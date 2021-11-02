package com.example.easyorder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReListMainAdapter extends RecyclerView.Adapter<ReListMainAdapter.CustomViewHolder> {

    private Context mContext;
    private ArrayList<BusinessData> arrayList;

    public ReListMainAdapter(Context mContext, ArrayList<BusinessData> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ReListMainAdapter.CustomViewHolder holder, int position) {
        holder.tv_martNm.setText(arrayList.get(position).getMartNm());
        holder.tv_date.setText(arrayList.get(position).getCrtDate());
        holder.tv_totAmount.setText(arrayList.get(position).getTotAmount());
        holder.tv_totAmount.setText(arrayList.get(position).getTotPrice());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posit = holder.getAdapterPosition();
                Intent intent = new Intent(mContext, ListSubActivity.class);
                intent.putExtra("martNo", arrayList.get(posit).getMartNo());
                intent.putExtra("srDate", arrayList.get(posit).getCrtDate());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView tv_martNm, tv_date, tv_totAmount, tv_totPrice;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_martNm = (TextView) itemView.findViewById(R.id.tv_martNm);
            this.tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            this.tv_totAmount = (TextView) itemView.findViewById(R.id.tv_totAmount);
            this.tv_totPrice = (TextView) itemView.findViewById(R.id.tv_totPrice);
        }
    }
}
