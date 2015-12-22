package com.getyasa;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by maxim.vasilkov@gmail.com on 22/12/15.
 */
public class FiltersAdapter  extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> {
    private int[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView)v.findViewById(R.id.effect_thumbnail);

        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageView iv = (ImageView)v;
            int resId = (int)iv.getTag();
            Message msg = new Message();
            msg.arg1 = resId;
            notifier.sendMessage(msg);
        }
    };

    Handler notifier;

    // Provide a suitable constructor (depends on the kind of dataset)
    public FiltersAdapter(int[] resourcesDataset, Handler notifier) {
        mDataset = resourcesDataset;
        this.notifier = notifier;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FiltersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_pic_filter_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mImageView.setImageResource(mDataset[position]);
        holder.mImageView.setTag(mDataset[position]);
        holder.mImageView.setOnClickListener(clickListener);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}