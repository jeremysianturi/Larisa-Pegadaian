package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.marketplace.PayActivity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.HistoryPaymentAdapter;
import id.co.pegadaian.diarium.model.HistoryPaymentModel;

public class HistoryPaymentActivity extends AppCompatActivity {

  private ListView listView;
  private List<HistoryPaymentModel> listmodel;
  private HistoryPaymentModel model;
  private HistoryPaymentAdapter adapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history_payment);

    listView = findViewById(R.id.listview);
    listmodel = new ArrayList<HistoryPaymentModel>();

    model = new HistoryPaymentModel("","","","","","","","");
    listmodel.add(model);

    adapter = new HistoryPaymentAdapter(HistoryPaymentActivity.this,listmodel);
    listView.setAdapter(adapter);


    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("History ");
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
