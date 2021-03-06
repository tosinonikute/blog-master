package com.blog.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blog.R;
import com.blog.activity.DetailActivity;
import com.blog.model.Wp;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hp on 27/09/2015.
 */

public class ListingAdapter
        extends RecyclerView.Adapter<ListingAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private ArrayList<Wp> mValues;
    Context mContext;
    ArrayList<String> imageUrlList =  new ArrayList<String>();
    RecyclerView recyclerView;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mProductTitle, mProductDesc;
        // fonts
        public Typeface typeFace;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mImageView = (ImageView) view.findViewById(R.id.product_image);
            mProductTitle = (TextView) view.findViewById(R.id.product_title);
            mProductDesc = (TextView) view.findViewById(R.id.product_desc);
            typeFace= Typeface.createFromAsset(mContext.getAssets(),"fonts/OpenSans-Light.ttf");

            mProductTitle.setTypeface(typeFace);
            mProductDesc.setTypeface(typeFace);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mProductTitle.getText();
        }
    }

    public String getValueAt(int position) {
        return String.valueOf(mValues.get(position).getId());
    }

    public ListingAdapter(Context context, ArrayList<Wp> wpValues, RecyclerView recyclerV, ArrayList<String> imgUrlList) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mContext = context;
        mBackground = mTypedValue.resourceId;
        mValues = wpValues;
        imageUrlList = imgUrlList;
        recyclerView = recyclerV;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_list, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /* Set your values */

        final Wp newValues = (Wp) mValues.get(position);
        String title = newValues.getTitle().getRendered().replace("-", " ");
        int tLength = title.length();
        if(tLength >= 40){
            title = title.substring(0, 40).toLowerCase() + "...";
            title = title.substring(0, 1).toUpperCase() + title.substring(1);
        } else {
            if(tLength > 5) {
                title = title.substring(0, tLength).toLowerCase();
                title = title.substring(0, 1).toUpperCase() + title.substring(1);
            }
        }


        holder.mProductTitle.setText(title);
        String description = newValues.getContent().getRendered().substring(0, 80);
        String desc = android.text.Html.fromHtml(description).toString() + "...";
        holder.mProductDesc.setText(desc);

            Glide.with(holder.mImageView.getContext())
                    .load(newValues.getEmbedded().getWpFeaturedmedia().get(0).getMediaDetails().getSizes().getFull().getSourceUrl())
                    .placeholder(R.drawable.placeholder_small)
                    .into(holder.mImageView);


                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Context context = v.getContext();
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("mValues", mValues);
                        context.startActivity(intent);
                    }
                });




    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addAll(List<Wp> data, int startPosition, int itemCount){
        mValues.addAll(data);
        notifyDataSetChanged();
        //notifyItemRangeInserted(startPosition, itemCount);
    }

    public void addNew(List<Wp> data, int startPosition, int itemCount){
        mValues.addAll(data);
        notifyItemRangeInserted(startPosition, itemCount);
    }

    public void clear(){
        mValues.clear();
        notifyDataSetChanged();
    }


}

