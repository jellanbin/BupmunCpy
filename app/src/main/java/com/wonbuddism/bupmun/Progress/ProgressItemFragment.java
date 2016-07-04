package com.wonbuddism.bupmun.Progress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wonbuddism.bupmun.R;

/**
 * Created by VuHung on 1/23/2015.
 */
public class ProgressItemFragment extends Fragment implements View.OnClickListener{
    private static final String EXTRA_MESSAGE = "mes";
    private Context context;
    private Activity activity;
    private View view;
    String message;
    public static final ProgressItemFragment newInstance(String message){
        ProgressItemFragment f = new ProgressItemFragment();

        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
        this.activity = activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        message = getArguments().getString(EXTRA_MESSAGE);
        view = inflater.inflate(R.layout.item_progress_indicator, container, false);
        ImageView maintitle = (ImageView)view.findViewById(R.id.progres_item_image);
        maintitle.setOnClickListener(this);
        context = container.getContext();
        if(message.equals("정전")){
            maintitle.setImageResource(R.drawable.maintitle1);
        }else if(message.equals("대종경")){
            maintitle.setImageResource(R.drawable.maintitle2);
        }else if(message.equals("불조요경")){
            maintitle.setImageResource(R.drawable.maintitle3);
        }else if(message.equals("예전")){
            maintitle.setImageResource(R.drawable.maintitle4);
        }else if(message.equals("정산종사법어")){
            maintitle.setImageResource(R.drawable.maintitle5);
        }else if(message.equals("대산종사법어")){
            maintitle.setImageResource(R.drawable.maintitle6);
        }else if(message.equals("원불교교사")){
            maintitle.setImageResource(R.drawable.maintitle7);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.progres_item_image:
                if(message.equals("정전")){
                    Log.e("maintitle","정전");
                }else if(message.equals("대종경")){
                    Log.e("maintitle","대종경");
                }else if(message.equals("불조요경")){
                    Log.e("maintitle","불조요경");
                }else if(message.equals("예전")){
                    Log.e("maintitle","예전");
                }else if(message.equals("정산종사법어")){
                    Log.e("maintitle","정산종사법어");
                }else if(message.equals("대산종사법어")){
                    Log.e("maintitle","대산종사법어");
                }else if(message.equals("원불교교사")){
                    Log.e("maintitle","원불교교사");
                }
                String title1 = message;
                Intent intent = new Intent(context,ProgressTitleSecondActivity.class);
                intent.putExtra("title1", title1);
                activity.startActivityForResult(intent,1000);
                break;
        }

    }
}
