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
import id.co.pegadaian.diarium.model.WishlistModel;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyViewHolder> {

  // step 3 : create list model
  private List<WishlistModel> listmodel ;
  private List<WishlistModel> listmodel2;

  // step 4 : create a contructor
  public WishlistAdapter (List<WishlistModel> listmodel, List<WishlistModel> listmodel2) {
    this.listmodel = listmodel;
    this.listmodel2 = listmodel2;
  }

  @NonNull
  @Override
  public WishlistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // step 5 : inflate the layout inflater

    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_list_wishlist_adapter,parent,false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    WishlistModel model = listmodel.get(position);

    holder.tittle.setText(model.getTittle());
    holder.badge.setText(model.getBestseller());
    holder.rating.setText(model.getRating());
    holder.viewer.setText(model.getViews());
    holder.price.setText("Rp. "+model.getPrice());
    holder.partner.setText(model.getAccount());

    String url = model.getUrl();

    System.out.println("url_adapter " + url);

    Picasso.get()
            .load(model.getUrl())
            .placeholder(R.color.grey)
            .error(R.drawable.events)
            .into(holder.ivCourse);

  }


  @Override
  public int getItemCount() {
    return listmodel.size();
  }
  /** This is ViewHolder class **/

  public static class MyViewHolder extends RecyclerView.ViewHolder {

    // define all item to our views
    // step 1 : difine textview or item dll

    TextView tittle,badge , rating, viewer,partner, price;
    ImageView ivCourse;


    // step 2 : initializing our views
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      tittle = itemView.findViewById(R.id.text1);
      badge = itemView.findViewById(R.id.text2);
      rating = itemView.findViewById(R.id.text3_1);
      viewer = itemView.findViewById(R.id.text3_2);
      partner = itemView.findViewById(R.id.text4);
      price = itemView.findViewById(R.id.text5);

      ivCourse = itemView.findViewById(R.id.image_course);

    }
  }

}
