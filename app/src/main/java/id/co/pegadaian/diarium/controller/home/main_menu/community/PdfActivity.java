package id.co.pegadaian.diarium.controller.home.main_menu.community;//package id.co.telkomsigma.Diarium.controller.more.community;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//import java.io.BufferedInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import id.co.telkomsigma.Diarium.R;
//
//public class PdfActivity extends AppCompatActivity {
//    private PDFView pdfView;
//    String pdf_url;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pdf);
//        pdfView = findViewById(R.id.pdfView);
//        Intent intent = getIntent();
//        pdf_url = intent.getStringExtra("pdf_url");
//        new PdfActivity.RetrievePDFStream().execute(pdf_url);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }
//
//    class RetrievePDFStream extends AsyncTask<String, Void, InputStream> {
//
//        @Override
//        protected InputStream doInBackground(String... strings) {
//            InputStream inputStream = null;
//            try {
//                URL url = new URL(strings[0]);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                if (urlConnection.getResponseCode() == 200) {
//                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return inputStream;
//        }
//
//        @Override
//        protected void onPostExecute(InputStream inputStream) {
//            pdfView.fromStream(inputStream).load();
//        }
//    }
//
//    @Override
//    public boolean onSupportNavigateUp(){
//        finish();
//        return true;
//    }
//
//}
