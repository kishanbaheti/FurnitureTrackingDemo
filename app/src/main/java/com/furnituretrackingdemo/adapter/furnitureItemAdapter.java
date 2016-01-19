package com.furnituretrackingdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.furnituretrackingdemo.R;
import com.furnituretrackingdemo.UI.detailScreenActivity;
import com.furnituretrackingdemo.model.FeedItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class furnitureItemAdapter extends RecyclerView.Adapter<furnitureItemAdapter.MyViewHolder> {
    List<FeedItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public furnitureItemAdapter(Context context, List<FeedItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.furniture_item_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FeedItem current = data.get(position);

        holder.title.setTag(position);

        holder.title.setText(current.getName());
        holder.description.setText(current.getDescription());

        Picasso.with(context).load(new File(current.getImg_path()))
                .resize(96, 96).centerCrop().into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView title;
        TextView description;

        public MyViewHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int itemPosition = (int) title.getTag();

                    Intent i = new Intent(context, detailScreenActivity.class);
                    i.putExtra("action", "update");
                    i.putExtra("detail",data.get(itemPosition));
                    context.startActivity(i);
                }
            });
        }
    }
}