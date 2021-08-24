package id.co.pegadaian.diarium.controller.home.main_menu.mydevelopment.mytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.DetailMentoringAdapter;
import id.co.pegadaian.diarium.model.DetailMentoringModel;
import id.co.pegadaian.diarium.util.TimeHelper;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;
import id.co.pegadaian.diarium.util.photo.PhotoUtil;
import id.co.pegadaian.diarium.util.photo.TakePhotoUtil;

public class MentoringDetailActivity extends AppCompatActivity {

  String  mentoring_id;
  String  bussines_code;
  String  batch;
  String  batch_name;
  String  event_id;
  String  event_name;
  String  begin_date;
  String  end_date;
  String  event_curr_stat;
  String  evnt_curr_statid;
  String  event_status;
  String  event_stat_id;
  String  location_id;
  String  location;
  String  cur_id;
  String  curriculum;
  String  event_type;
  String  participant_id;
  String  partcipant_name;
  String  parti_nicknm;
  String  company_name;
  String  activity_name;
  String  session_name;
  String  begin_date_activity;
  String  end_date_activity;
  String  title;
  String  topic;
  String  description;
  TextView tvEvent, tvBatch, tvLocation, tvType, tvCurriculum, tvDate, tvActivitName, tvSessionName, tvDateActivity, tvCompany;
  TextView tvTitle, tvTopic, tvDescription, tvMentor;
  Button btnUpload, btnSave;
  ListView lvComment;
  TimeHelper timeHelper;
  Typeface font,fontbold;
  UserSessionManager session;
  private List<DetailMentoringModel> listModel;
  private DetailMentoringModel model;
  private DetailMentoringAdapter adapter;
  private ProgressDialogHelper progressDialogHelper;
  EditText etComment;
  TextView tvPost;
  String TAG = "MentoringDetailActivity";
  private String img_data, mCurrentPhotoPath;
  private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
  private static final int CAPTURE_PHOTO = 2;
  private static final int CAPTURE_GALLERY = 3;
  private PhotoUtil imageUtil;
  private TakePhotoUtil photoUtil;
  private String userID;
  private Bitmap bitmapPhoto;
  ImageView ivInsight;

  @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mentoring_detail);

    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    StrictMode.setVmPolicy(builder.build());
    photoUtil = new TakePhotoUtil();
    imageUtil = new PhotoUtil();

    progressDialogHelper = new ProgressDialogHelper();
    timeHelper = new TimeHelper();
    font = Typeface.createFromAsset(MentoringDetailActivity.this.getAssets(),"fonts/Nexa Light.otf");
    fontbold = Typeface.createFromAsset(MentoringDetailActivity.this.getAssets(),"fonts/Nexa Bold.otf");
    tvEvent = findViewById(R.id.tvEvent);
    tvBatch = findViewById(R.id.tvBatch);
    tvLocation = findViewById(R.id.tvLocation);
    tvType = findViewById(R.id.tvType);
    tvCurriculum = findViewById(R.id.tvCurriculum);
    tvDate = findViewById(R.id.tvDate);
    tvActivitName = findViewById(R.id.tvActivityName);
    tvSessionName = findViewById(R.id.tvSessionName);
    tvDateActivity = findViewById(R.id.tvDateActivity);
    tvCompany = findViewById(R.id.tvCompany);
    ivInsight = findViewById(R.id.ivInsight);
    session = new UserSessionManager(this);

    tvTitle = findViewById(R.id.tvTitle);
    tvTopic = findViewById(R.id.tvTopic);
    tvDescription = findViewById(R.id.tvDescription);
    tvMentor = findViewById(R.id.tvMentor);
    btnUpload = findViewById(R.id.btnUpload);
    lvComment = findViewById(R.id.lvComment);
    etComment = findViewById(R.id.etComment);
    etComment = findViewById(R.id.etComment);
    tvPost = findViewById(R.id.tvPost);
    btnSave = findViewById(R.id.btnSave);
    ivInsight.setVisibility(View.GONE);
    btnSave.setVisibility(View.GONE);
    Intent intent = getIntent();
    mentoring_id = intent.getStringExtra("mentoring_id");
    bussines_code = intent.getStringExtra("bussines_code");
    batch = intent.getStringExtra("batch");
    batch_name = intent.getStringExtra("batch_name");
    event_id = intent.getStringExtra("event_id");
    event_name = intent.getStringExtra("event_name");
    begin_date = intent.getStringExtra("begin_date");
    end_date = intent.getStringExtra("end_date");
    event_curr_stat = intent.getStringExtra("event_curr_stat");
    evnt_curr_statid = intent.getStringExtra("evnt_curr_statid");
    event_status = intent.getStringExtra("event_status");
    event_stat_id = intent.getStringExtra("event_stat_id");
    location_id = intent.getStringExtra("location_id");
    location = intent.getStringExtra("location");
    cur_id = intent.getStringExtra("cur_id");
    curriculum = intent.getStringExtra("curriculum");
    event_type = intent.getStringExtra("event_type");
    participant_id = intent.getStringExtra("participant_id");
    partcipant_name = intent.getStringExtra("partcipant_name");
    parti_nicknm = intent.getStringExtra("parti_nicknm");
    company_name = intent.getStringExtra("company_name");

    activity_name = intent.getStringExtra("activity_name");
    session_name = intent.getStringExtra("session_name");
    begin_date_activity = intent.getStringExtra("begin_date_activity");
    end_date_activity = intent.getStringExtra("end_date_activity");

    title = intent.getStringExtra("title");
    topic = intent.getStringExtra("topic");
    description = intent.getStringExtra("description");

    session.setactivity_nameLMS(activity_name);
    session.setsession_nameLMS(session_name);
    session.setbegin_date_activityLMS(begin_date_activity);
    session.setend_date_activityLMS(end_date_activity);

    tvEvent.setText(event_name);
    tvBatch.setText(batch+ " "+ batch_name);
    tvLocation.setText(location);
    tvType.setText(event_type+" Event");
    tvDate.setText(timeHelper.getTimeFormat(begin_date)+" - "+timeHelper.getTimeFormat(end_date));
    tvEvent.setText(event_name);
    tvCompany.setText(company_name+" - "+location_id);
    tvCurriculum.setText(curriculum);

    tvActivitName.setText(activity_name);
    tvSessionName.setText(session_name);
    tvDateActivity.setText(timeHelper.getTimeFormat(begin_date_activity)+" - "+timeHelper.getTimeFormat(end_date_activity));

    tvMentor.setText("Mentor : "+ "Mentor 1");
    tvTitle.setText("Title : "+ title);
    tvTopic.setText("Topic : "+ topic);
    tvDescription.setText("Description : "+ description);
    btnUpload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        selectAction();
//                Toast.makeText(MentoringDetailActivity.this, "Upload Success!", Toast.LENGTH_SHORT).show();
      }
    });
    etComment.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        return false;
      }
    });
    tvPost.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String text = etComment.getText().toString();
        if (text.isEmpty() || text.equals("")||text.length()==0) {
          Toast.makeText(MentoringDetailActivity.this, "Plase input comment first !", Toast.LENGTH_SHORT).show();
        } else {
          submitTextMentoring(text);
        }
      }
    });
    getListComment(mentoring_id);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Mentoring Detail");
  }
  @Override
  public boolean onSupportNavigateUp(){
    finish();
    return true;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == AppCompatActivity.RESULT_OK) {
      if (requestCode == CAPTURE_PHOTO) {
        if (bitmapPhoto != null) {
          bitmapPhoto.recycle();
        }
        setPic();
      } else if (requestCode == CAPTURE_GALLERY) {
        selectGallery(data);
      }
    } else {
      //Toast.makeText(getActivity().getApplicationContext(), "request code == null", Toast.LENGTH_SHORT).show();
    }
  }

  public  static File saveImage(Context context, String imageData)  {
    System.out.println(imageData+"kqn3kje");
    final byte[] imgBytesData;
    if (imageData.equals("")||imageData.equals(null)) {
      imgBytesData = android.util.Base64.decode("0",
              android.util.Base64.DEFAULT);
    } else {
      imgBytesData = android.util.Base64.decode(imageData,
              android.util.Base64.DEFAULT);
    }

    File file;

    FileOutputStream fileOutputStream;
    try {
      file = File.createTempFile("image", null, context.getCacheDir());
      fileOutputStream = new FileOutputStream(file);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
            fileOutputStream);
    try {
      bufferedOutputStream.write(imgBytesData);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        bufferedOutputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    System.out.println("BENTUKFILE"+file);
    return file;
  }

  private void submitPost(String mentoring, String sender) {
    System.out.println("MASUKNEWINSIGHT");
    progressDialogHelper.showProgressDialog(MentoringDetailActivity.this, "Sending data...");
    @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
    String tRes = tgl.format(new Date());
    @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
    String jamRes = jam.format(new Date());
    System.out.println("idMentoring "+mentoring);
    AndroidNetworking.upload("https://testapi.digitalevent.id/lms/api/mentoringchat")
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","multipart/form-data")
            .addHeaders("Authorization","Bearer "+session.getTokenLdap())
            .addMultipartParameter("business_code",session.getUserBusinessCode())
            .addMultipartParameter("mentoring",mentoring)
            .addMultipartParameter("sender_type","PARTI")
            .addMultipartParameter("sender",sender)
            .addMultipartParameter("otype","FILE")
            .addMultipartFile("file_mentoring", saveImage(this, img_data))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                // do anything with response
                System.out.println(response+"RESPONNEWINSIGHT");
                try {
                  Toast.makeText(MentoringDetailActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                  progressDialogHelper.dismissProgressDialog(MentoringDetailActivity.this);
                }catch (Exception e){
                  progressDialogHelper.dismissProgressDialog(MentoringDetailActivity.this);
                  System.out.println("bbb"+e);
                }
              }
              @Override
              public void onError(ANError error) {
                progressDialogHelper.dismissProgressDialog(MentoringDetailActivity.this);
                Log.d(TAG, "onError response " + error.getResponse());
                System.out.println("ccc"+error);
                System.out.println("error"+error.getResponse());
                System.out.println("error"+error.getErrorDetail());
                System.out.println("error"+error.getErrorCode());
              }
            });
  }

  private void setPic() {
    Bitmap scaledBitmap = null;
    // Get the dimensions of the bitmap
    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
    bmOptions.inJustDecodeBounds = true;
    Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

    // Decode the image file into a Bitmap sized to fill the View
    int actualHeight = bmOptions.outHeight;
    int actualWidth = bmOptions.outWidth;
    float maxHeight = 816.0f;
    float maxWidth = 612.0f;
    float imgRatio = actualWidth / actualHeight;
    float maxRatio = maxWidth / maxHeight;

    if (actualHeight > maxHeight || actualWidth > maxWidth) {
      if (imgRatio < maxRatio) {
        imgRatio = maxHeight / actualHeight;
        actualWidth = (int) (imgRatio * actualWidth);
        actualHeight = (int) maxHeight;
      } else if (imgRatio > maxRatio) {
        imgRatio = maxWidth / actualWidth;
        actualHeight = (int) (imgRatio * actualHeight);
        actualWidth = (int) maxWidth;
      } else {
        actualHeight = (int) maxHeight;
        actualWidth = (int) maxWidth;
      }
    }

    bmOptions.inSampleSize = imageUtil.calculateInSampleSize(bmOptions, actualWidth, actualHeight);
    bmOptions.inJustDecodeBounds = false;
    bmOptions.inDither = false;
    bmOptions.inPurgeable = true;
    bmOptions.inInputShareable = true;
    bmOptions.inTempStorage = new byte[16 * 1024];

    try {
      bmp = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    } catch (OutOfMemoryError exception) {
      exception.printStackTrace();

    }
    try {
      scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
    } catch (OutOfMemoryError exception) {
      exception.printStackTrace();
    }

    float ratioX = actualWidth / (float) bmOptions.outWidth;
    float ratioY = actualHeight / (float) bmOptions.outHeight;
    float middleX = actualWidth / 2.0f;
    float middleY = actualHeight / 2.0f;

    Matrix scaleMatrix = new Matrix();
    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

    Canvas canvas = new Canvas(scaledBitmap);
    canvas.setMatrix(scaleMatrix);
    canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


    ExifInterface exif;
    try {
      exif = new ExifInterface(mCurrentPhotoPath);

      int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
      Log.d("EXIF", "Exif: " + orientation);
      Matrix matrix = new Matrix();
      if (orientation == 6) {
        matrix.postRotate(90);
        Log.d("EXIF", "Exif: " + orientation);
      } else if (orientation == 3) {
        matrix.postRotate(180);
        Log.d("EXIF", "Exif: " + orientation);
      } else if (orientation == 8) {
        matrix.postRotate(270);
        Log.d("EXIF", "Exif: " + orientation);
      }
      scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    } catch (IOException e) {
      e.printStackTrace();
    }
    ivInsight.setVisibility(View.VISIBLE);
    btnSave.setVisibility(View.VISIBLE);
    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        submitPost(mentoring_id,"2");
//                submitPost("5","2");
//                Toast.makeText(MentoringDetailActivity.this, "Sukses Save", Toast.LENGTH_SHORT).show();
      }
    });
    bitmapPhoto = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    img_data = photoUtil.getStringBase64Bitmap(scaledBitmap);
    Log.d("GAMBER", "GAMBAR : " +img_data);
    ivInsight.setImageBitmap(scaledBitmap);
//        img_bg.setImageBitmap(scaledBitmap);
//        img_ic.setVisibility(View.GONE);
  }


  private void selectGallery(Intent data) {
    Bitmap scaledBitmap = null;
    Uri selectedImageUri = data.getData();
    String[] projection = {MediaStore.MediaColumns.DATA};
    CursorLoader cursorLoader = new CursorLoader(MentoringDetailActivity.this, selectedImageUri, projection, null, null, null);
    Cursor cursor = cursorLoader.loadInBackground();
    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
    cursor.moveToFirst();
    String selectedImagePath = cursor.getString(column_index);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    Bitmap bmp = BitmapFactory.decodeFile(selectedImagePath, options);

    int actualHeight = options.outHeight;
    int actualWidth = options.outWidth;
    float maxHeight = 816.0f;
    float maxWidth = 612.0f;
    float imgRatio = actualWidth / actualHeight;
    float maxRatio = maxWidth / maxHeight;

    if (actualHeight > maxHeight || actualWidth > maxWidth) {
      if (imgRatio < maxRatio) {
        imgRatio = maxHeight / actualHeight;
        actualWidth = (int) (imgRatio * actualWidth);
        actualHeight = (int) maxHeight;
      } else if (imgRatio > maxRatio) {
        imgRatio = maxWidth / actualWidth;
        actualHeight = (int) (imgRatio * actualHeight);
        actualWidth = (int) maxWidth;
      } else {
        actualHeight = (int) maxHeight;
        actualWidth = (int) maxWidth;
      }
    }

    options.inSampleSize = imageUtil.calculateInSampleSize(options, actualWidth, actualHeight);
    options.inJustDecodeBounds = false;
    options.inDither = false;
    options.inPurgeable = true;
    options.inInputShareable = true;
    options.inTempStorage = new byte[16 * 1024];

    try {
      bmp = BitmapFactory.decodeFile(selectedImagePath, options);
    } catch (OutOfMemoryError exception) {
      exception.printStackTrace();

    }
    try {
      scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
    } catch (OutOfMemoryError exception) {
      exception.printStackTrace();
    }

    float ratioX = actualWidth / (float) options.outWidth;
    float ratioY = actualHeight / (float) options.outHeight;
    float middleX = actualWidth / 2.0f;
    float middleY = actualHeight / 2.0f;

    Matrix scaleMatrix = new Matrix();
    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

    Canvas canvas = new Canvas(scaledBitmap);
    canvas.setMatrix(scaleMatrix);
    canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


    ExifInterface exif;
    try {
      exif = new ExifInterface(selectedImagePath);

      int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
      Log.d("EXIF", "Exif: " + orientation);
      Matrix matrix = new Matrix();
      if (orientation == 6) {
        matrix.postRotate(90);
        Log.d("EXIF", "Exif: " + orientation);
      } else if (orientation == 3) {
        matrix.postRotate(180);
        Log.d("EXIF", "Exif: " + orientation);
      } else if (orientation == 8) {
        matrix.postRotate(270);
        Log.d("EXIF", "Exif: " + orientation);
      }
      scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    } catch (IOException e) {
      e.printStackTrace();
    }
    ivInsight.setVisibility(View.VISIBLE);
    btnSave.setVisibility(View.VISIBLE);
    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        submitPost(mentoring_id,"2");
//                Toast.makeText(MentoringDetailActivity.this, "Sukses Save", Toast.LENGTH_SHORT).show();
      }
    });
    bitmapPhoto = BitmapFactory.decodeFile(selectedImagePath, options);
    img_data = photoUtil.getStringBase64Bitmap(scaledBitmap);
    Log.d("GAMBER", "GAMBAR : " +img_data);
    ivInsight.setImageBitmap(scaledBitmap);
    //img_bg.setImageBitmap(scaledBitmap);
    //img_ic.setVisibility(View.GONE);
  }

  private void selectAction() {
    final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
    AlertDialog.Builder builder = new AlertDialog.Builder(MentoringDetailActivity.this);
    builder.setTitle("Select Action");
    builder.setItems(items, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int item) {
        if (items[item].equals("Capture Photo")) {
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          if (intent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
              photoFile = createImageFile();
            } catch (IOException ex) {
              // Error occurred while creating the File;
              Log.d("catch", "me");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
              intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
              startActivityForResult(intent, CAPTURE_PHOTO);
            }
          }
        } else if (items[item].equals("Choose from Gallery")) {
          // Create intent to Open Image applications like Gallery, Google Photos
          Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                  MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

          // Start the Intent
          startActivityForResult(galleryIntent, CAPTURE_GALLERY);


        } else if (items[item].equals("Cancel")) {
          dialog.dismiss();
        }
      }
    });
    builder.show();
  }

  private File createImageFile() throws IOException {
    // Create an image file name
    //String imageFileName = username + "_";
    String imageFileName = userID + "_";
    File storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
    );

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = image.getAbsolutePath();
    return image;
  }

  private void submitTextMentoring(String text) {
    @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
    String tRes = tgl.format(new Date());
    @SuppressLint("SimpleDateFormat") SimpleDateFormat jamParam = new SimpleDateFormat("yyyMMddHHmmss");
    String jamResParam = jamParam.format(new Date());
    JSONObject obj = new JSONObject();
    System.out.println("idMentoring_text "+mentoring_id);
    try {
      obj.put("business_code", session.getUserBusinessCode());
      obj.put("mentoring", mentoring_id);
      obj.put("otype", "TEXT");
      obj.put("sender", "2");
      obj.put("sender_type", "PARTI");
      obj.put("text", text);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    System.out.println(obj + "ARRAYMENTORING");
    AndroidNetworking.post("https://testapi.digitalevent.id/lms/api/mentoringchat")
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization","Bearer "+session.getTokenLdap())
            .addJSONObjectBody(obj)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
              @Override
              public void onResponse(JSONObject response) {
                // do anything with response
                System.out.println(response+"RESPONSENDCHAT");
                try {
                  Toast.makeText(MentoringDetailActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                  ivInsight.setVisibility(View.GONE);
                  btnSave.setVisibility(View.GONE);
                  adapter.notifyDataSetChanged();
                  etComment.setText("");
                  getListComment(mentoring_id);

                }catch (Exception e){
                  System.out.println(e);
                }
              }
              @Override
              public void onError(ANError error) {
                System.out.println(error);
                Log.d(TAG, "onError response " + error.getResponse());
              }
            });
  }

  private void getListComment(String mentoring_id){
    AndroidNetworking.get("https://testapi.digitalevent.id/lms/api/mentoringchat?mentoring_id="+mentoring_id)
            .addHeaders("Accept","application/json")
            .addHeaders("Content-Type","application/json")
            .addHeaders("Authorization","Bearer "+session.getTokenLdap())
            //.addJSONObjectBody(body)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONArray(new JSONArrayRequestListener() {
              @Override
              public void onResponse(JSONArray response) {
                System.out.println("COMMENTRESPONSE"+response);
                // do anything with response
                try {
                  listModel = new ArrayList<DetailMentoringModel>();
                  for (int a = 0; a < response.length(); a++) {
                    JSONObject object = response.getJSONObject(a);
                    String begin_date = object.getString("begin_date");
                    String chat_text = object.getString("chat_text");
                    String sender_name = object.getString("sender_name");
                    model = new DetailMentoringModel(begin_date,chat_text,sender_name);
                    listModel.add(model);
                  }
                  adapter = new DetailMentoringAdapter(MentoringDetailActivity.this, listModel);
                  lvComment.setAdapter(adapter);
                  progressDialogHelper.dismissProgressDialog(MentoringDetailActivity.this);
                }catch (Exception e){
                  progressDialogHelper.dismissProgressDialog(MentoringDetailActivity.this);
                  System.out.println(e);
                }
              }
              @Override
              public void onError(ANError error) {
                progressDialogHelper.dismissProgressDialog(MentoringDetailActivity.this);
                System.out.println(error);
              }
            });
  }



}
