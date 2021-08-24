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
import id.co.pegadaian.diarium.model.ReviewFeaturedModel;

public class ReviewFeaturedAdapter extends RecyclerView.Adapter<ReviewFeaturedAdapter.MyViewHolder> {


  // step 3 : create list model
  private List<ReviewFeaturedModel> listmodel;

  // step 4 : create a contructor

  public ReviewFeaturedAdapter(List<ReviewFeaturedModel> listmodel) {
    this.listmodel = listmodel;
    //Constructor stuff
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // step 5 : inflate the layout inflater

    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_list_reviewfeatured_adapter,parent,false);


    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    // step 6 : lets bind our view with data

    ReviewFeaturedModel model = listmodel.get(position);


    holder.name.setText(model.getFull_name());
    holder.comment.setText(model.getIsi_testimoni());
//    holder.partner.setText(model.getPartner_name());

    Picasso.get()
            .load(R.drawable.profile)
            .into(holder.profile);

//    holder.badge.setText(model.getBadge());


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

    public TextView name, comment, job;
    ImageView profile;


    // step 2 : initializing our views
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      name = itemView.findViewById(R.id.name);
      comment = itemView.findViewById(R.id.comment);
      profile = itemView.findViewById(R.id.profile);
      job = itemView.findViewById(R.id.job);
    }
  }
}
