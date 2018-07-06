package com.example.juliane.cadecafe.utils;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import com.example.juliane.cadecafe.R;
import de.hdodenhof.circleimageview.CircleImageView;

import com.example.juliane.cadecafe.modelo.Cafe;

public class CustomListAdapter extends ArrayAdapter<Cafe> {
    private LayoutInflater mInflater;
    private List<Cafe> mCafes = null;
    private ArrayList<Cafe> arrayList; //usado para search bar
    private int layoutResource;
    private Context mContext;
    private String mAppend;

    public CustomListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Cafe> cafes, String append) {
        super(context, resource, cafes);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        mAppend = append;
        this.mCafes = cafes;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(mCafes);
    }

    private static class ViewHolder{
        TextView nome;
        CircleImageView contactImage;
        ProgressBar mProgressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /*
         ************ ViewHolder Build Pattern Start ************
         */
        final ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

            //---------------------------Stuff to change--------------------------------------------
            holder.nome = (TextView) convertView.findViewById(R.id.cafeNome);
            holder.contactImage = (CircleImageView) convertView.findViewById(R.id.cafeImage);
            //--------------------------------------------------------------------------------------

            holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.cafeProgressBar);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        //---------------------------Stuff to change--------------------------------------------
        String nome_ = getItem(position).getNome();
        String imagePath = getItem(position).getProfileImage();
        holder.nome.setText(nome_);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(mAppend + imagePath, holder.contactImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                holder.mProgressBar.setVisibility(View.GONE);
            }
        });
        //--------------------------------------------------------------------------------------

        return convertView;
    }
}





