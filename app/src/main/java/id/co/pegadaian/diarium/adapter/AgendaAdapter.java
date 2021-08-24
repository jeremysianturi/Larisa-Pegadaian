package id.co.pegadaian.diarium.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.AgendaModel;
import id.co.pegadaian.diarium.util.OnItemClickListener;

/**
 * Created by Biting on 2/27/2018.
 */

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder> {

    MyViewHolder holder;
    private AgendaModel model;
    private List<AgendaModel> modelList;
    private List<String> dayList;
    private Context context;
    private String idAgenda;
    private OnItemClickListener onItemClickListener;
    private int row_index = -1;
    private int pos;

    public AgendaAdapter(Context context, List<String> dayList, OnItemClickListener onItemClickListener) {
        this.dayList = dayList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public AgendaAdapter(Context context, List<String> dayList, int pos) {
        this.dayList = dayList;
        this.context = context;
        this.pos = pos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_dayx, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder hold, final int position) {
        holder = hold;
        holder.titleTextView.setText(dayList.get(position).toString());

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idAgenda = dayList.get(position).toString();
                onItemClickListener.onItemClick(idAgenda);
                row_index = position;
                notifyDataSetChanged();
            }
        });

        if (row_index == position) {
            holder.frameLayout.setBackgroundColor(Color.RED);
            holder.titleTextView.setTextColor(Color.WHITE);
        } else {
            holder.frameLayout.setBackgroundColor(Color.GRAY);
            holder.titleTextView.setTextColor(Color.BLACK);
        }

//        if (pos < 0) {
//            holder.frameLayout.setBackgroundColor(Color.GRAY);
//            holder.titleTextView.setTextColor(Color.BLACK);
//        } else {
//            System.out.println("hmm " + pos);
//            row_index = pos;
//            if (row_index == position) {
//                holder.frameLayout.setBackgroundColor(Color.RED);
//                holder.titleTextView.setTextColor(Color.WHITE);
//            } else {
//                holder.frameLayout.setBackgroundColor(Color.GRAY);
//                holder.titleTextView.setTextColor(Color.BLACK);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        FrameLayout frameLayout;

        MyViewHolder(View v) {
            super(v);
            frameLayout = v.findViewById(R.id.lay_kotak);
            titleTextView = v.findViewById(R.id.textTime);
        }
    }
}