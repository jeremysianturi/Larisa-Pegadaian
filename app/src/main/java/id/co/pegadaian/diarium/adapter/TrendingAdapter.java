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
import id.co.pegadaian.diarium.model.TrendingModel;
import id.co.pegadaian.diarium.util.UserSessionManager;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.MyViewHolder> {


  private UserSessionManager session;
  // step 3 : create list model
  private List<TrendingModel> listmodel;

  // step 4 : create a contructor

  public TrendingAdapter(List<TrendingModel> listmodel) {
    this.listmodel = listmodel;
    //Constructor stuff
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // step 5 : inflate the layout inflater

    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_list_trending_adapter,parent,false);


    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    // step 6 : lets bind our view with data

    TrendingModel model = listmodel.get(position);


    holder.tittle.setText(model.getCourse_name());
    holder.desc.setText(model.getCourse_description());
    holder.partner.setText(model.getPartner_name());

    String url_curse =model.getImage_course();
    String url_partner = model.getImage_partner();

      Picasso.get()
              .load(url_curse)
              .placeholder(R.color.grey)
              .error(R.drawable.events)
              .into(holder.images);

      Picasso.get()
              .load(url_partner)
              .placeholder(R.color.grey)
              .error(R.drawable.events)
              .into(holder.img_partner);

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

    public TextView tittle, desc, badge, partner;
    ImageView images, img_partner;


    // step 2 : initializing our views
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      tittle = itemView.findViewById(R.id.tittle);
      desc = itemView.findViewById(R.id.desc);
      images = itemView.findViewById(R.id.image);
      img_partner = itemView.findViewById(R.id.ic_partner);
      badge = itemView.findViewById(R.id.badge);
      partner = itemView.findViewById(R.id.partner);
    }
  }
}
