package com.wikisearch.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wikisearch.app.R;
import com.wikisearch.app.model.ContentSource;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<ContentSource> dataSet;
    private Context mContext;
    private ListItemClickListener mListItemClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);


        }
    }

    public CustomAdapter(ArrayList<ContentSource> data, Context mContext, ListItemClickListener listItemClickListener) {

        this.dataSet = data;
        this.mContext = mContext;
        this.mListItemClickListener = listItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_recyclerview, parent, false);

//        view.setOnClickListener(WikiSearchContent.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;
        textViewName.setText(dataSet.get(listPosition).getTitle());
        textViewVersion.setText(dataSet.get(listPosition).getTerms().getDescription().get(0));
        Glide.with(mContext).load(dataSet.get(listPosition).getOriginal().getSource())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.search)
                .error(R.drawable.search)
                .into(imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListItemClickListener.onItemClickListener(listPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface ListItemClickListener {
        public void onItemClickListener(int clickedPosition);
    }
}
