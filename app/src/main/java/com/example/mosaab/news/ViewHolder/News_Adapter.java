package com.example.mosaab.news.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mosaab.news.Common.Common;
import com.example.mosaab.news.Interface.ItemClickListner;
import com.example.mosaab.news.Model.Data;
import com.example.mosaab.news.Model.News;
import com.example.mosaab.news.R;

import java.util.ArrayList;

public class News_Adapter extends RecyclerView.Adapter<News_Adapter.ViewHolder> {

    private ArrayList<News> news_ArrayList;
    private ItemClickListner itemClickListner;

    public void setOnItemClickListner(ItemClickListner listner)
    {
        itemClickListner=listner;
    }

    public News_Adapter(ArrayList<News> news_ArrayList, ItemClickListner itemClickListner) {

        this.news_ArrayList = news_ArrayList;
        this.itemClickListner = itemClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View single_news_item= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_news_item,viewGroup,false);
        return new ViewHolder(single_news_item);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.news_title.setText(String.valueOf(news_ArrayList.get(i).getTitle()));
        viewHolder.news_desc.setText(String.valueOf(news_ArrayList.get(i).getDescription()));


    }

    @Override
    public int getItemCount() {
        return news_ArrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        protected CardView news_card;
        protected TextView news_title,news_desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            news_card = itemView.findViewById(R.id.news_card);
            news_title = itemView.findViewById(R.id.news_title);
            news_desc = itemView.findViewById(R.id.news_desc);

            itemView.setOnCreateContextMenuListener(this);

            news_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (itemClickListner != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            itemClickListner.onItemClick(v,getAdapterPosition());
                        }

                    }

                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Select the Action");


            menu.add(0,0,getAdapterPosition(),Common.UPDATE);
            menu.add(0,1,getAdapterPosition(),Common.DELETE);
        }
    }
}
