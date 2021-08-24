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
import id.co.pegadaian.diarium.model.TopCoursesModel;

public class TopCoursesAdapter extends RecyclerView.Adapter<TopCoursesAdapter.MyViewHolder> {


  // step 3 : create list model
  private List<TopCoursesModel> listmodel;

  // step 4 : create a contructor

  public TopCoursesAdapter(List<TopCoursesModel> listmodel) {
    this.listmodel = listmodel;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // step 5 : inflate the layout inflater

    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_list_topcourse_adapter,parent,false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    // step 6 : lets bind our view with data

    TopCoursesModel model = listmodel.get(position);

    holder.tittle.setText(model.getCourse_name());
    holder.desc.setText(model.getCourse_description());
    holder.badge.setText("Best Seller");
    holder.partner.setText(model.getPartner_name());

    Picasso.get()
            .load(model.getImage_course())
            .placeholder(R.color.grey)
            .error(R.drawable.events)
            .into(holder.images);

    Picasso.get()
            .load(model.getImage_partner())
            .placeholder(R.color.grey)
            .error(R.drawable.events)
            .into(holder.img_partner);


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

    public TextView tittle, desc, badge, partner ;
    ImageView images, img_partner;


    // step 2 : initializing our views
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      badge = itemView.findViewById(R.id.badge);
      tittle = itemView.findViewById(R.id.tittle);
      desc = itemView.findViewById(R.id.desc);
      images = itemView.findViewById(R.id.image);
      partner = itemView.findViewById(R.id.partner);
      img_partner = itemView.findViewById(R.id.ic_item);
    }
  }

}
