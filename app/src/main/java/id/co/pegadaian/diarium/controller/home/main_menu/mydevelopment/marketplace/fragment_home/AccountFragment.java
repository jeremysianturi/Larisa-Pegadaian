package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.fragment_home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import id.co.pegadaian.diarium.R;

public class AccountFragment extends Fragment {

//  private FeaturedViewModel mViewModel;
//
//  public static FeaturedFragment newInstance() {
//    return new FeaturedFragment();
//  }

  public AccountFragment(){

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.account_fragment, container, false);
//    return inflater.inflate(R.layout.featured_fragment, container, false);


    /**     ============== Share aplication ===============     **/
    LinearLayout shared = view.findViewById(R.id.share);
    shared.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Diarium Super App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "www.google.com");
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
      }
    });

    return view;
  }

}
