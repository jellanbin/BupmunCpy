package com.wonbuddism.bupmun.Progress;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wonbuddism.bupmun.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by user on 2015-12-25.
 */
public class ProgressTitleLowRankAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> mList;



    public ProgressTitleLowRankAdapter(Context context, ArrayList<String> mList) {
        this.context = context;
        this.mList = mList;
    }




    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView text    = null;
        CustomHolder    holder  = null;

        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_progress_title_lowrank, parent, false);
            text = (TextView) convertView.findViewById(R.id.item_title_lowrank_textview);
            // 홀더 생성 및 Tag로 등록
            holder = new CustomHolder();
            holder.m_TextView = text;
            convertView.setTag(holder);
        }else{
            holder  = (CustomHolder) convertView.getTag();
            text    = holder.m_TextView;

        }
        text.setText(mList.get(position));


        return convertView;
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void add(String _msg) {
        mList.add(_msg);
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        mList.remove(_position);
    }


    //외부에서 아이템 초기화 요청
    public void clear(){
        mList.clear();
    }

    private class CustomHolder {
        TextView    m_TextView;
    }
}
