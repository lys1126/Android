package com.example.easyorder;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
        holder.et_amount.setText(arrayList.get(position).getAmount()+"");
        holder.et_uPrice.setText(arrayList.get(position).getUPrice()+"");
        holder.tv_price.setText(arrayList.get(position).getPrice()+"");

        holder.et_amount.addTextChangedListener(new TextWatcher() {
            int amount, uPrice;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int posit = holder.getAdapterPosition();
                String getAmount = holder.et_amount.getText().toString();
                if(!"".equals(getAmount) && holder.et_amount.getText() != null && !getAmount.equals("-")) {
                    amount = Integer.parseInt(holder.et_amount.getText().toString());
                    if(amount != ListSubActivity.temp.get(posit).getAmount()) {
                        ListSubActivity.btn_update.setEnabled(true);
                    } else {
                        ListSubActivity.btn_update.setEnabled(false);
                    }
                } else {
                    amount = 0;
                    if(amount != ListSubActivity.temp.get(posit).getAmount()) {
                        ListSubActivity.btn_update.setEnabled(true);
                    } else {
                        ListSubActivity.btn_update.setEnabled(false);
                    }
                }
                uPrice = Integer.parseInt(holder.et_uPrice.getText().toString());
                holder.tv_price.setText(amount*uPrice + "");
                arrayList.get(posit).setAmount(amount);
                arrayList.get(posit).setPrice(amount*uPrice);

                int totPrice = 0;
                for(int n=0; n<arrayList.size(); n++) {
                    totPrice = totPrice + arrayList.get(n).getPrice();
                }
                ListSubActivity.tv_totPrice.setText(totPrice+"원");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.et_uPrice.addTextChangedListener(new TextWatcher() {
            int amount, uPrice;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int posit = holder.getAdapterPosition();
                if(!"".equals(holder.et_uPrice.getText().toString()) && holder.et_uPrice.getText() != null) {
                    uPrice = Integer.parseInt(holder.et_uPrice.getText().toString());
                    if(uPrice != ListSubActivity.temp.get(posit).getUPrice()) {
                        ListSubActivity.btn_update.setEnabled(true);
                    } else {
                        ListSubActivity.btn_update.setEnabled(false);
                    }
                } else {
                    uPrice = 0;
                    if(uPrice != ListSubActivity.temp.get(posit).getUPrice()) {
                        ListSubActivity.btn_update.setEnabled(true);
                    } else {
                        ListSubActivity.btn_update.setEnabled(false);
                    }
                }
                amount = Integer.parseInt(holder.et_amount.getText().toString());
                holder.tv_price.setText(uPrice*amount+"");
                arrayList.get(posit).setUPrice(uPrice);
                arrayList.get(posit).setPrice(amount*uPrice);

                int totPrice = 0;
                for(int n=0; n<arrayList.size(); n++) {
                    totPrice = totPrice + arrayList.get(n).getPrice();
                }
                ListSubActivity.tv_totPrice.setText(totPrice+"원");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_prodNm, tv_price;
        protected EditText et_amount, et_uPrice;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_prodNm = (TextView) itemView.findViewById(R.id.tv_prodNm);
            this.et_amount = (EditText) itemView.findViewById(R.id.et_amount);
            this.et_uPrice = (EditText) itemView.findViewById(R.id.et_uPrice);
            this.tv_price = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}
