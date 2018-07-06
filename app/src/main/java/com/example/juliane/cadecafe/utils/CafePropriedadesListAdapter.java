package com.example.juliane.cadecafe.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.juliane.cadecafe.utils.DatabaseHelper;

import com.example.juliane.cadecafe.R;
import com.example.juliane.cadecafe.activity.MainActivity;
import com.example.juliane.cadecafe.activity.MapActivity;
import com.example.juliane.cadecafe.modelo.Cafe;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;

public class CafePropriedadesListAdapter extends ArrayAdapter<String>{

    private static final String TAG = "CafePropriedadesListAda";

    private List<String> mPropriedades  = null;
    private int layoutResource;
    private LayoutInflater mInflater;
    private Context mContext;
    private String mAppend;


    public CafePropriedadesListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> propriedades) {
        super(context, resource, propriedades);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        this.mPropriedades = propriedades;


        ;


    }

    private static class ViewHolder{

        TextView property;
        ImageView leftIcon;


    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        /*
         ************ ViewHolder Build Pattern Start ************
         */
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

            //---------------------------Stuff to change--------------------------------------------
            holder.property = (TextView) convertView.findViewById(R.id.tvMiddleCardView);
            holder.leftIcon = (ImageView) convertView.findViewById(R.id.iconLeftCardView);

            //--------------------------------------------------------------------------------------

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //---------------------------Coisas para alterar--------------------------------------------
        final String property = getItem(position);
        holder.property.setText(property);





        if (property.contains(",")) {
            holder.leftIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_rua", null, mContext.getPackageName()));
            holder.leftIcon.setOnClickListener(new View.OnClickListener(){
                @Override
                    public void onClick(View v) {
                      // Intent intent = new Intent(mContext.getApplicationContext(), MapActivity.class);
                      // mContext.startActivity(intent);
                      Intent intent = new Intent(Intent.ACTION_VIEW);
                      intent.setData(Uri.parse("geo:0,0?z=14&q=" + property));
                     mContext.startActivity(intent);

                }
                });

        } else if ((property.length() != 0 )) {
            holder.leftIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_telefone", null, mContext.getPackageName()));
            holder.leftIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((MainActivity)mContext).checkPermission(Init.PHONE_PERMISSIONS)){
                        Log.d(TAG, "onClick: iniciando ligação...");
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", property, null));
                        mContext.startActivity(callIntent);
                    }else{
                        ((MainActivity)mContext).verifyPermissions(Init.PHONE_PERMISSIONS);
                    }


                }
            });

        }


        return convertView;
    }

}
