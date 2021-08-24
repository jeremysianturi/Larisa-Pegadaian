package id.co.pegadaian.diarium.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.MyCourseModel;

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.MyViewHolder> implements Filterable {

  private List<MyCourseModel> listmodelfull;
  // step 3 : create list model
  private List<MyCourseModel> listmodel;


  // step 4 : create a contructor\
  public MyCoursesAdapter(List<MyCourseModel> listmodel) {
    this.listmodel = listmodel;
    listmodelfull =  new ArrayList<>(listmodel) ;
  }
  @NonNull
  @Override
  public MyCoursesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // step 5 : inflate the layout inflater

    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_list_mycourse_adapter,parent,false);

    return new MyCoursesAdapter.MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MyCoursesAdapter.MyViewHolder holder, int position) {


    // step 6 : lets bind our view with data

    MyCourseModel model = listmodel.get(position);

    holder.tittle.setText(model.getCourse_name());
    holder.by.setText(model.getPartner_name());
    Picasso.get().load(model.getImg_url())
            .placeholder(R.color.grey)
            .error(R.drawable.events)
            .into(holder.images);

  }

  @Override
  public int getItemCount() {
    return listmodel.size();
  }

  /** This is ViewHolder class **/

  public class MyViewHolder extends RecyclerView.ViewHolder {

    // define all item to our views
    // step 1 : difine textview or item dll

    public TextView tittle, by ;
    ImageView images;


    // step 2 : initializing our views
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      tittle = itemView.findViewById(R.id.text1);
      by = itemView.findViewById(R.id.text2);
      images = itemView.findViewById(R.id.image);
    }
  }

  // filter : step 2 => overide method from Filterable interface
  @Override
  public Filter getFilter() {
    return listmodelfilter;
  }
  // filter : step 3 => create method listmodelfilter
  private Filter listmodelfilter  = new Filter() {

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      List<MyCourseModel> filteredlist = new ArrayList<>();
      if (constraint == null || constraint.length() == 0){
        filteredlist.addAll(listmodelfull);
      }else {
        String filterpatern = constraint.toString().toLowerCase().trim();

        for (MyCourseModel item : listmodelfull){
          if (item.getCourse_name().toLowerCase().contains(filterpatern)){
            filteredlist.add(item);
          }
        }
      }
      FilterResults results = new FilterResults();
      results.values = filteredlist;
      return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
      listmodel.clear();
      listmodel.addAll((Collection<? extends MyCourseModel>) results.values);
      notifyDataSetChanged();

    }
  };

}
