package id.co.pegadaian.diarium.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.ChartModel;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.MyViewHolder> {

  private OnItemClickListener mListener;

  public interface OnItemClickListener{
    void onSave(int position);
    void onDelete(int position, int obj_id);
  }

  // step 3 : create list model
  private List<ChartModel> listmodel ;

  // step 4 : create a contructor

  public ChartAdapter (List<ChartModel> listmodel) {
    this.listmodel = listmodel;
  }

  public void setOnItemClickListener(OnItemClickListener listener){
    mListener = listener;
  }

  @NonNull
  @Override
  public ChartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // step 5 : inflate the layout inflater

    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_list_chart_adapter,parent,false);

    return new ChartAdapter.MyViewHolder(itemView, mListener);
  }

  @Override
  public void onBindViewHolder(@NonNull ChartAdapter.MyViewHolder holder, int position) {

    // step 6 : lets bind our view with data

    ChartModel model = listmodel.get(position);

    holder.tittle.setText(model.getTittle());
    holder.creator.setText(model.getCreator());
    holder.price.setText(model.getPrice());
    Picasso.get().load(model.getUrl())
            .placeholder(R.color.grey)
            .error(R.drawable.events)
            .into(holder.imageView);

  }

  @Override
  public int getItemCount() {
    return listmodel.size();
  }

  /** This is ViewHolder class **/

  public class MyViewHolder extends RecyclerView.ViewHolder {

    // define all item to our views
    // step 1 : difine textview or item dll

    public TextView tittle, creator, price, save, remove;
    ImageView imageView;


    // step 2 : initializing our views
    public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
      super(itemView);

      tittle = itemView.findViewById(R.id.text1);
      creator = itemView.findViewById(R.id.text2);
      price = itemView.findViewById(R.id.text3);

      imageView = itemView.findViewById(R.id.image);

      save = itemView.findViewById(R.id.save);

      remove = itemView.findViewById(R.id.remove);

      remove.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (listener !=null){
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
              listener.onDelete(position,listmodel.get(position).getObject_identifier());
            }
          }
        }
      });

      save.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (listener !=null){
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
              listener.onSave(position);
            }
          }
        }
      });
    }
  }
}
