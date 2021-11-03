package com.example.easyorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReListSubAdapter extends RecyclerView.Adapter<ReListSubAdapter.CustomViewHolder> {

    private ArrayList<BusinessData> arrayList;

    public ReListSubAdapter(ArrayList<BusinessData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ReListSubAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_sub, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReListSubAdapter.CustomViewHolder holder, int position) {
        holder.tv_prodNm.setText(arrayList.get(position).getProdNm());
        holder.tv_amount.setText(arrayList.get(position).getAmount()+"");
        holder.tv_uPrice.setText(arrayList.get(position).getUPrice()+"");
        holder.tv_price.setText(arrayList.get(position).getPrice()+"");
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_prodNm, tv_amount, tv_uPrice, tv_price;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_prodNm = (TextView) itemView.findViewById(R.id.tv_prodNm);
            this.tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            this.tv_uPrice = (TextView) itemView.findViewById(R.id.tv_uPrice);
            this.tv_price = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}
