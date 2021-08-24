package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import id.co.pegadaian.diarium.R;

public class InstructionFragment extends Fragment {

  public InstructionFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.instruction_fragment, container, false);


    LinearLayout layout = view.findViewById(R.id.layout_linkaja);
    layout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(getActivity(),IntructionDetailActivity.class);
        startActivity(i);
      }
    });


    return view;
  }
}
