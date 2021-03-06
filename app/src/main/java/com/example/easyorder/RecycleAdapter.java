package com.example.easyorder;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.CustomViewHolder>{

    private ArrayList<BusinessData> arrayList;
    private Context mContext;
    private InputMethodManager imm;

    public RecycleAdapter(ArrayList<BusinessData> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

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
        holder.et_price.setText(arrayList.get(position).getPrice()+"");
        holder.et_amount.setText(arrayList.get(position).getAmount()+"");
        holder.et_uPrice.setText(arrayList.get(position).getUPrice()+"");

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
                } else {
                    amount = 0;
                }
                uPrice = Integer.parseInt(holder.et_uPrice.getText().toString());
                holder.et_price.setText(amount*uPrice + "");
                arrayList.get(posit).setAmount(amount);
                arrayList.get(posit).setPrice(amount*uPrice);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int totPrice = 0;
                for(int n=0; n<arrayList.size(); n++) {
                    totPrice = totPrice + arrayList.get(n).getPrice();
                }
                PopupActivity.tv_total.setText(totPrice+"???");
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
                int getUprice = 0;
                if(!"".equals(holder.et_uPrice.getText().toString()) && holder.et_uPrice.getText() != null) {
                    arrayList.get(posit).setUPrice(Integer.parseInt(holder.et_uPrice.getText().toString()));
                    getUprice = Integer.parseInt(holder.et_uPrice.getText().toString());
                    uPrice = getUprice;
                } else {
                    uPrice = 0;
                }
                amount = Integer.parseInt(holder.et_amount.getText().toString());
                holder.et_price.setText(getUprice*amount+"");
                arrayList.get(posit).setAmount(amount);
                arrayList.get(posit).setUPrice(uPrice);
                arrayList.get(posit).setPrice(amount*uPrice);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int totPrice = 0;
                for(int n=0; n<arrayList.size(); n++) {
                    totPrice = totPrice + arrayList.get(n).getPrice();
                }
                PopupActivity.tv_total.setText(totPrice+"???");
            }
        });
        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posit = holder.getAdapterPosition();
                remove(posit);
            }
        });

        if(position == arrayList.size() - 1) {
            Log.e("lastAmountFocus", "????????? ?????????!!");
            holder.et_amount.requestFocus();
            imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.RESULT_SHOWN, InputMethodManager.RESULT_SHOWN);
            holder.et_amount.setSelection(holder.et_amount.length());
        }

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position); //??????????????? ??????
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected EditText et_prodNm, et_uPrice, et_amount, et_price;
        protected ImageButton btn_remove;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.et_prodNm = (EditText) itemView.findViewById(R.id.et_prodNm);
            this.et_uPrice = (EditText) itemView.findViewById(R.id.et_uPrice);
            this.et_amount = (EditText) itemView.findViewById(R.id.et_amount);
            this.et_price = (EditText) itemView.findViewById(R.id.et_price);
            this.btn_remove = (ImageButton) itemView.findViewById(R.id.btn_remove);
        }
    }

}
