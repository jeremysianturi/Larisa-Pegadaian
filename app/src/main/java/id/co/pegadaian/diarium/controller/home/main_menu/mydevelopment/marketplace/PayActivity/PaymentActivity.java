package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.PaymentAdapter;
import id.co.pegadaian.diarium.model.PaymentModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;

public class PaymentActivity extends AppCompatActivity {

  private PaymentModel model;
  private List<PaymentModel> listmodel;
  private PaymentAdapter adapter;
  private ListView listView;

  private UserSessionManager session;
  private ProgressDialogHelper progressDialogHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment);

    session= new UserSessionManager(this);
    progressDialogHelper = new ProgressDialogHelper();

    listView = findViewById(R.id.list_payment);

    listmodel = new ArrayList<PaymentModel>();

    model = new PaymentModel("","","");
    listmodel.add(model);

    adapter  = new PaymentAdapter(PaymentActivity.this,listmodel);
    listView.setAdapter(adapter);

    //    model = new PromosiSCPModel("","","");
//    listModel.add(model);

//    adapter = new Promosi_SuccessionCPAdapter(getActivity(),listModel);
//    listpromosi.setAdapter(adapter);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Pay");

  }
  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
