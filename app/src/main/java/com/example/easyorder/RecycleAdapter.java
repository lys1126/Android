package com.example.easyorder;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.CustomViewHolder>{

    private ArrayList<BusinessData> arrayList;

    public RecycleAdapter(ArrayList<BusinessData> arrayList) {this.arrayList = arrayList;}

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insert, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.et_prodNm.setText(arrayList.get(position).getProdNm());
        if(arrayList.get(position).getAmount() == 0) {
            holder.et_price.setText("");
        } else {
            holder.et_price.setText(arrayList.get(position).getPrice()+"");
        }
        holder.et_uPrice.setText(arrayList.get(position).getUPrice()+"");
        if(arrayList.get(position).getPrice() == 0) {
            holder.et_amount.setText("");
        } else {
            holder.et_amount.setText(arrayList.get(position).getAmount()+"");
        }
        holder.et_amount.addTextChangedListener(new TextWatcher() {
            int amount, uPrice;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String getAmount = holder.et_amount.getText().toString();
                if(!"".equals(getAmount) && holder.et_amount.getText() != null && !getAmount.equals("-")) {
                    amount = Integer.parseInt(holder.et_amount.getText().toString());
                }
                uPrice = Integer.parseInt(holder.et_uPrice.getText().toString());
                holder.et_price.setText(amount*uPrice + "");



            }

            @Override
            public void afterTextChanged(Editable editable) {
                int totPrice = 0;
                int posit = holder.getAdapterPosition();
                arrayList.get(posit).setAmount(amount);
                arrayList.get(posit).setPrice(amount*uPrice);
                for(int n=0; n<arrayList.size(); n++) {
                    totPrice = totPrice + arrayList.get(n).getPrice();
                }
                PopupActivity.tv_total.setText(totPrice+"원");
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

//    public void remove(int position) {
//        try {
//            arrayList.remove(position);
//            notifyItemRemoved(position); //새로고침용 함수
//        } catch (IndexOutOfBoundsException ex) {
//            ex.printStackTrace();
//        }
//    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected EditText et_prodNm, et_uPrice, et_amount, et_price;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.et_prodNm = (EditText) itemView.findViewById(R.id.et_prodNm);
            this.et_uPrice = (EditText) itemView.findViewById(R.id.et_uPrice);
            this.et_amount = (EditText) itemView.findViewById(R.id.et_amount);
            this.et_price = (EditText) itemView.findViewById(R.id.et_price);
        }
    }

}
