package com.example.easyorder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
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
        holder.et_uPrice.setText(arrayList.get(position).getUPrice()+"");
        Log.e("postion", position+"");
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

        protected EditText et_prodNm, et_uPrice, et_amount;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.et_prodNm = (EditText) itemView.findViewById(R.id.et_prodNm);
            this.et_uPrice = (EditText) itemView.findViewById(R.id.et_uPrice);
            this.et_amount = (EditText) itemView.findViewById(R.id.et_amount);
        }
    }
}
