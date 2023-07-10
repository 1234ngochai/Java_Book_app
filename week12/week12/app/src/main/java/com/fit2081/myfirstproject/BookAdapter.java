package com.fit2081.myfirstproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.myfirstproject.provider.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    List<Book> dp;

    public void setDp(List<Book> dp) {
        this.dp = dp;
        Log.d("week6App","setdp");
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_cardview_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        Log.d("week6App","onCreateViewHolder"+dp.size());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        holder.book_id.setText(dp.get(position).getBook_id());
        holder.title.setText(dp.get(position).getTitle());
        holder.isbn.setText(dp.get(position).getIsbn());
        holder.author.setText(dp.get(position).getAuthor());
        holder.descrition.setText(dp.get(position).getDescrition());
        String price = dp.get(position).getPrice()+"";
        holder.price.setText(price);
        String upper = dp.get(position).getAuthor();
        upper = upper.toUpperCase();
        holder.author_upper.setText(String.valueOf(dp.get(position).getId()));
        Log.d("week6App","onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        if (dp == null){
            return 0;
        }
        return dp.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView book_id;
        TextView title;
        TextView isbn;
        TextView author;
        TextView descrition;
        TextView price;
        TextView author_upper;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id = itemView.findViewById(R.id.id_card_view);
            title = itemView.findViewById(R.id.title_card_view);
            isbn = itemView.findViewById(R.id.isbn_card_view);
            author = itemView.findViewById(R.id.author_card_view);
            descrition = itemView.findViewById(R.id.desc_card_view);
            price = itemView.findViewById(R.id.price_card_view);
            author_upper = itemView.findViewById(R.id.author_upper_cardview);
        }
    }
}
