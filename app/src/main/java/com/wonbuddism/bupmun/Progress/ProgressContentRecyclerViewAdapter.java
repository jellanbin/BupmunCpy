package com.wonbuddism.bupmun.Progress;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wonbuddism.bupmun.Database.BUPMUN_TYPING_INDEX;
import com.wonbuddism.bupmun.Database.DbAdapter;
import com.wonbuddism.bupmun.R;

import java.util.ArrayList;

public class ProgressContentRecyclerViewAdapter extends RecyclerView.Adapter<ProgressContentRecyclerViewAdapter.ProgressViewHolder> {


    ArrayList<String> cotnents;
    Activity activity;
    String title1;
    String title2;
    DbAdapter dbAdapter;

    ProgressContentRecyclerViewAdapter(Activity activity, String title, ArrayList<String> cotnents){
        this.cotnents = cotnents;
        this.activity = activity;
        this.title1 = title;
        this.dbAdapter = new DbAdapter(activity);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public ProgressViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_progress_main_recyclerview, viewGroup, false);
        return new ProgressViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ProgressViewHolder holder, int position) {
        title2 = cotnents.get(position);
        Log.e("title1 : title2 : pos" , title1+" : "+title2+" : "+position);
        holder.cotnent.setText(cotnents.get(position));
        holder.lL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("click", "아이템클릭");
                Intent intent;
                dbAdapter.open();
                ArrayList<String> list = dbAdapter.getAllTitle3(title1, title2);
                Log.e("title2: ",title2);
                Log.e("title3",list.toString());
                if (list.size() <= 1) {
                    intent = new Intent(activity, ProgressContentActivity.class);
                    BUPMUN_TYPING_INDEX bupmunItem = dbAdapter.getContent(title1, title2);
                    intent.putExtra("title1", title1);
                    intent.putExtra("title", title2);
                    intent.putExtra("bupmunItem", bupmunItem);
                } else {
                    intent = new Intent(activity, ProgressTitleThridActivity.class);
                    intent.putExtra("title1", title1);
                    intent.putExtra("title2", title2);
                }
                dbAdapter.close();
                activity.startActivityForResult(intent, 1000);
            }
        });
    }



    @Override
    public int getItemCount() {
        /*return cards.size();*/
        return cotnents.size();
    }

    public final static class ProgressViewHolder extends RecyclerView.ViewHolder {
        TextView cotnent;
        LinearLayout lL;


        ProgressViewHolder(View itemView) {
            super(itemView);
            cotnent = (TextView)itemView.findViewById(R.id.item_progress_main_content_textview);
            lL = (LinearLayout)itemView.findViewById(R.id.item_progress_main_content_linearlayout);
        }
    }

    public ArrayList<String> getProgressItem(){
        return cotnents;
    }

    public String getValueAt(int position) {
        return cotnents.get(position);
    }
    // 외부에서 아이템 추가 요청 시 사용
    public void add(String _msg) {
        cotnents.add(_msg);
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        cotnents.remove(_position);
    }

    //외부에서 아이템 초기화 요청
    public void clear(){
        cotnents.clear();
    }

}
