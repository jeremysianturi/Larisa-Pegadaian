package id.co.pegadaian.diarium.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.SylabusModel;

public class SylabusAdapter extends RecyclerView.Adapter<SylabusAdapter.MyViewHolder> {

  // step 3 : create list model
  private List<SylabusModel> listmodel;

  // step 4 : create a contructor
  public SylabusAdapter(List<SylabusModel> listmodel) {
    this.listmodel = listmodel;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // step 5 : inflate the layout inflater

    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_list_sylabus_adapter,parent,false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    // step 6 : lets bind our view with data

    SylabusModel model = listmodel.get(position);

    holder.tvSylabus.setText(model.getName());

//
//    holder.comment.setText( model.getIsi_testimoni());
//    holder.name.setText(model.getFull_name());
//    holder.date.setText(model.getBegin_date());

//    Picasso.get().load(R.drawable.profile)
//            .placeholder(R.color.grey)
//            .error(R.drawable.profile)
//            .into(holder.profile);
  }

  @Override
  public int getItemCount() {
    // step 7 : getting size of list
    return listmodel.size();
  }

  /** This is ViewHolder class **/

  public class MyViewHolder extends RecyclerView.ViewHolder {

    // define all item to our views
    // step 1 : difine textview or item dll

   private TextView tvSylabus;

    // step 2 : initializing our views
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      tvSylabus = itemView.findViewById(R.id.txt_sylabus);

    }
  }
}
