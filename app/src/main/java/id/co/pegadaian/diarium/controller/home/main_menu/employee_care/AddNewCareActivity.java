package id.co.pegadaian.diarium.controller.home.main_menu.employee_care;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.model.KategoriModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;
import id.co.pegadaian.diarium.util.photo.PhotoUtil;
import id.co.pegadaian.diarium.util.photo.TakePhotoUtil;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class AddNewCareActivity extends AppCompatActivity {
    private Bitmap bitmapPhoto;
    ArrayList<String> opsi= new ArrayList<>();
    Typeface font,fontbold;
    UserSessionManager session;
    private String img_data, mCurrentPhotoPath;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int CAPTURE_PHOTO = 2;
    private static final int CAPTURE_GALLERY = 3;
    private PhotoUtil imageUtil;
    private TakePhotoUtil photoUtil;
    private String TAG = "TAG Posting Employee";
    private String userID;

    Button btnSubmit;
    ImageView imageView;
    EditText etDesc;

    String generatedPostId;
    SpinnerDialog spinnerDialogs;
    Spinner spinnerKategori;
//    Spinner spinnerSubKategori;
    String title;
    private ProgressDialogHelper progressDialogHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_care);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        photoUtil = new TakePhotoUtil();
        imageUtil = new PhotoUtil();
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
//        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");

        btnSubmit = findViewById(R.id.btnCreate);
        etDesc = findViewById(R.id.etDesc);
//        etTitle = findViewById(R.id.etTitle);
        imageView = findViewById(R.id.ivImage);
        spinnerKategori = findViewById(R.id.spinnerKategori);
//        spinnerSubKategori = findViewById(R.id.spinnerSubKategori);

        getKategori();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = etDesc.getText().toString();
//                String title = etTitle.getText().toString();
                if (desc.equals("")) {
                    Toast.makeText(AddNewCareActivity.this, "Please fill all field before submit", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(img_data)) {
                        Toast.makeText(AddNewCareActivity.this, "Select image first", Toast.LENGTH_SHORT).show();
                    } else {
                        generatePostId(title, desc);
                    }
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAction();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Ticket");
    }

    private void getKategori(){
//        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/employeeCare/getKategori/buscd/*?")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final ArrayList<KategoriModel> kategori = new ArrayList<>();
                        System.out.println(response+"RESPONBUILDING");
                        try {
                            if(response.getInt("status")==200){
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length()==0) {
                                } else {
                                    kategori.add(new KategoriModel("","","","","","","Choose Chategory","",""));
                                    for (int a = 0; a < jsonArray.length(); a++) {
                                        JSONObject object = jsonArray.getJSONObject(a);
                                        String object_code = object.getString("object_code");
                                        String object_name = object.getString("object_name");
//                                        String begin_date = object.getString("begin_date");
//                                        String end_date = object.getString("end_date");
//                                        String business_code = object.getString("business_code");
//                                        String empcorner_id = object.getString("empcorner_id");
//                                        String empcorner_name = object.getString("empcorner_name");
//                                        String theme_id = object.getString("theme_id");
//                                        String empcorner_desc = object.getString("empcorner_desc");
//                                        String build_code = object.getString("build_code");
//                                        String change_date = object.getString("change_date");
//                                        String change_user = object.getString("change_user");
//                                        String empcorner_loc = object.getString("empcorner_loc");
                                        kategori.add(new KategoriModel("","","","","","",object_name,"",""));
                                    }

                                    ArrayAdapter<KategoriModel> adapter =
                                            new ArrayAdapter<KategoriModel>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, kategori);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerKategori.setAdapter(adapter);
                                    spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            title = kategori.get(position).getObject_name();
                                            String id_contact = kategori.get(position).getObject_name();
//                                            getSubKategori(name);
                                        }
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                }
                            }else{
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(getActivity());

                        System.out.println(error);
                    }
                });
    }

//    private void getSubKategori(final String build_code_get){
////        progressDialogHelper.showProgressDialog(getActivity(), "Getting data...");
//        AndroidNetworking.get(session.getServerURL()+"users/employeeCare/getKategori/buscd/*?")
//                .addHeaders("Accept","application/json")
//                .addHeaders("Content-Type","application/json")
//                .addHeaders("Authorization",session.getToken())
//                //.addJSONObjectBody(body)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        final ArrayList<SubKategoriModel> subKategori = new ArrayList<>();
//                        System.out.println(response+"hiewuhruTheme");
//                        try {
//                            if(response.getInt("status")==200){
//                                JSONArray jsonArray = response.getJSONArray("data");
//                                subKategori.add(new SubKategoriModel("","","","","","","Choose Sub Chategory","",""));
//                                for (int a = 0; a < jsonArray.length(); a++) {
//                                    JSONObject object = jsonArray.getJSONObject(a);
//                                    String begin_date = object.getString("begin_date");
//                                    String end_date = object.getString("end_date");
//                                    String business_code = object.getString("business_code");
//                                    String plan_version = object.getString("plan_version");
//                                    String otype_code = object.getString("otype_code");
//                                    String object_code = object.getString("object_code");
//                                    String object_name = object.getString("object_name");
//                                    String change_date = object.getString("change_date");
//                                    String change_user = object.getString("change_user");
//                                    if (object_code.equals(build_code_get)) {
//                                        subKategori.add(new SubKategoriModel(begin_date, end_date, business_code, plan_version, otype_code, object_code, object_name, change_date, change_user));
//                                    }
//                                }
//
//                                ArrayAdapter<SubKategoriModel> adapter =
//                                        new ArrayAdapter<SubKategoriModel>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, subKategori);
//                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                spinnerSubKategori.setAdapter(adapter);
//                                spinnerSubKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                                        title = subKategori.get(position).getObject_name();
//                                    }
//                                    public void onNothingSelected(AdapterView<?> parent) {
//                                    }
//                                });
//                            }else{
//                            }
//                        }catch (Exception e){
//                            System.out.println(e);
//                        }
//
//                    }
//                    @Override
//                    public void onError(ANError error) {
////                        progressDialogHelper.dismissProgressDialog(getActivity());
//
//                        System.out.println(error);
//                    }
//                });
//    }

    private void generatePostId(String title, String desc) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
//        progressDialogHelper.showProgressDialog(CheckinActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL() + "users/employeeCare/generateIDTiket?")
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONIDCARE" + response);
                        // do anything with response
                        try {
                            if (response.getInt("status") == 200) {
                                generatedPostId = response.getJSONObject("data").getString("id");
                                submitPost(title, desc, generatedPostId);
//                                session.setGeneratedPostId(id);
                            } else {
//                                popUpLogin();
                            }
                            System.out.println("status ya : " + session.getStat());
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        } catch (Exception e) {
//                            progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                            System.out.println(e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(CheckinActivity.this);
                        System.out.println(error);
                    }
                });
    }


    private void submitPost(String title,String desc, String tiket) {
        progressDialogHelper.showProgressDialog(AddNewCareActivity.this, "Sending data...");

        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());
        AndroidNetworking.upload(session.getServerURL()+"users/employeeCare/postEmployeeCare/nik/"+session.getUserNIK()+"/buscd/"+session.getUserBusinessCode()+"/tiket/"+tiket)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addMultipartParameter("begin_date",tRes)
                .addMultipartParameter("end_date","9999-12-31")
                .addMultipartParameter("business_code",session.getUserBusinessCode())
                .addMultipartParameter("personal_number",session.getUserNIK())
                .addMultipartParameter("ticket_number",tiket)
                .addMultipartParameter("problem_type",title)
                .addMultipartParameter("problem_desc",desc)
                .addMultipartFile("image", saveImage(AddNewCareActivity.this, img_data))
                .addMultipartParameter("problem_status","01")
                .addMultipartParameter("change_user",session.getUserNIK())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONSUBMITNEWCARE");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(AddNewCareActivity.this, "Success submit data !", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(AddNewCareActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                            }
                            progressDialogHelper.dismissProgressDialog(AddNewCareActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(AddNewCareActivity.this);
                            System.out.println("bbb"+e);
                        }
                    }
                    @Override

                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(AddNewCareActivity.this);
                        System.out.println("ccc"+error);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "all permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                            showDialogOK("The Permissions are required for this application",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    //checkAndRequestPermissions();
                                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                            Uri.fromParts("package", getPackageName(), null));
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            Log.d("yes", "masuk");
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("SETTINGS", okListener)
                .create()
                .show();
    }

    private void selectAction() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewCareActivity.this);
        builder.setTitle("Select Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Capture Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
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

        bitmapPhoto = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        img_data = photoUtil.getStringBase64Bitmap(scaledBitmap);
        Log.d("GAMBER", "GAMBAR : " +img_data);
        imageView.setImageBitmap(scaledBitmap);
//        img_bg.setImageBitmap(scaledBitmap);
//        img_ic.setVisibility(View.GONE);
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
        return file;
    }

    private void selectGallery(Intent data) {
        Bitmap scaledBitmap = null;
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), selectedImageUri, projection, null, null, null);
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

        bitmapPhoto = BitmapFactory.decodeFile(selectedImagePath, options);
        img_data = photoUtil.getStringBase64Bitmap(scaledBitmap);
        Log.d("GAMBER", "GAMBAR : " +img_data);
        imageView.setImageBitmap(scaledBitmap);
        //img_bg.setImageBitmap(scaledBitmap);
        //img_ic.setVisibility(View.GONE);
    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    private void initItem() {
        opsi.add("Friends");
        opsi.add("Followers");
        opsi.add("Friends and followers");
        opsi.add("Communities");
        opsi.add("Only Me");
    }


}
