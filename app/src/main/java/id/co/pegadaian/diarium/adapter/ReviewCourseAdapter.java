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
import id.co.pegadaian.diarium.model.ReviewCourseModel;

public class ReviewCourseAdapter extends RecyclerView.Adapter<ReviewCourseAdapter.MyViewHolder> {

  // step 3 : create list model
  private List<ReviewCourseModel> listmodel;

  // step 4 : create a contructor
  public ReviewCourseAdapter(List<ReviewCourseModel> listmodel) {
    this.listmodel = listmodel;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // step 5 : inflate the layout inflater

    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_list_review_adapter,parent,false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    // step 6 : lets bind our view with data

    ReviewCourseModel model = listmodel.get(position);

    holder.comment.setText( model.getIsi_testimoni());
    holder.name.setText(model.getFull_name());
    holder.date.setText(model.getBegin_date());

    Picasso.get().load(R.drawable.profile)
            .placeholder(R.color.grey)
            .error(R.drawable.profile)
            .into(holder.profile);
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

    public TextView name, comment, date ;
    public ImageView profile;

    // step 2 : initializing our views
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      name = itemView.findViewById(R.id.name);
      comment = itemView.findViewById(R.id.comment);
      date = itemView.findViewById(R.id.date);
      profile = itemView.findViewById(R.id.profile);

    }
  }
}
