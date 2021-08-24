package id.co.pegadaian.diarium.controller.home.main_menu.myevent.ems.gallery;

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
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jsibbold.zoomage.ZoomageView;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;
import id.co.pegadaian.diarium.util.photo.PhotoUtil;
import id.co.pegadaian.diarium.util.photo.TakePhotoUtil;

public class TakePhotoActivity extends AppCompatActivity {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int CAPTURE_PHOTO = 2;
    private static final int CAPTURE_GALLERY = 3;
    private ZoomageView imageView;
    private EditText etTitle, etDesc;
    private Button btn_upload;
    private TakePhotoUtil photoUtil;
    private Bitmap bitmapPhoto;
    private String img_data, mCurrentPhotoPath;
    private PhotoUtil imageUtil;
    private String TAG = "TAG Selfie";
    private UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        session = new UserSessionManager(getApplicationContext());
        progressDialogHelper = new ProgressDialogHelper();
        img_data = "";

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        photoUtil = new TakePhotoUtil();
        imageUtil = new PhotoUtil();

        imageView = findViewById(R.id.iv_take_photo);
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        btn_upload = findViewById(R.id.btn_upload);


        Intent intent_get = getIntent();
        String flag = intent_get.getStringExtra("flag");
        if (flag.equals("1")) {
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
        } else if (flag.equals("2")){
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            // Start the Intent
            startActivityForResult(galleryIntent, CAPTURE_GALLERY);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAction();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String desc = etDesc.getText().toString();
                System.out.println("tes image data di galery my event: " + img_data);

                if (title.equals("") & desc.equals("") & img_data.equals("null")){
                    Toast.makeText(TakePhotoActivity.this,"Please fill the form",Toast.LENGTH_LONG).show();
                } else if (title.equals("")){
                    Toast.makeText(TakePhotoActivity.this,"Please fill the title",Toast.LENGTH_LONG).show();
                } else if (desc.equals("")){
                    Toast.makeText(TakePhotoActivity.this,"Please fill the description",Toast.LENGTH_LONG).show();
                } else if (img_data.equals("")){
                    Toast.makeText(TakePhotoActivity.this,"Please fill the image",Toast.LENGTH_LONG).show();
                } else {
                    generateGalleryId(title, desc);
//                  sendData();
                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(session.getEventColoRPrimary())));
//        actionBar.setTitle("Upload Photo");
//        actionBar.setHomeAsUpIndicator(R.drawable.back_panah);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gallery Add");
    }

    private void generateGalleryId(final String title, final String desc){
//        progressDialogHelper.showProgressDialog(ContentActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/eventEMS/"+session.getEventId()+"/generateIDGallery")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"asdw333r3");
                        try {
                            if(response.getInt("status")==200){
//                                session.setGalleryId(response.getJSONObject("data").getString("id"));
                                String id = response.getJSONObject("data").getString("id");
                                submitPostImage(id, title, desc);
                            }else{
//                                popUpLogin();
//                                progressDialogHelper.dismissProgressDialog(ContentActivity.this);
                            }
                        }catch (Exception e){
//                            progressDialogHelper.dismissProgressDialog(ContentActivity.this);
                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(ContentActivity.this);
                        System.out.println(error);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(TakePhotoActivity.this);
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
        String imageFileName = session.getUserNIK() + "_";
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
        imageView.setImageBitmap(scaledBitmap);
//        img_bg.setImageBitmap(scaledBitmap);
//        img_ic.setVisibility(View.GONE);
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
        imageView.setImageBitmap(scaledBitmap);
        //img_bg.setImageBitmap(scaledBitmap);
        //img_ic.setVisibility(View.GONE);
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

    private void submitPostImage(String id, String title, String desc) {
        progressDialogHelper.showProgressDialog(TakePhotoActivity.this, "Sending data...");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());
        AndroidNetworking.upload(session.getServerURL()+"users/eventEMS/"+session.getEventId()+"/gallery/"+id+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addMultipartParameter("begin_date",tRes)
                .addMultipartParameter("end_date","9999-12-31")
                .addMultipartParameter("business_code",session.getUserBusinessCode())
                .addMultipartParameter("personal_number",session.getUserNIK())
                .addMultipartParameter("event_id",session.getEventId())
                .addMultipartParameter("image_id",id)
                .addMultipartParameter("title",title)
                .addMultipartParameter("description",desc)
                .addMultipartFile("image", saveImage(TakePhotoActivity.this, img_data))
                .addMultipartParameter("change_user",session.getUserNIK())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"KONTENIMAGE");
                        try {
                            if(response.getInt("status")==200){
                                Intent i = new Intent(TakePhotoActivity.this, GalleryActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                                Toast.makeText(TakePhotoActivity.this,"Success submit photo",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(TakePhotoActivity.this, "Failed submit photo", Toast.LENGTH_SHORT).show();
                            }
                            progressDialogHelper.dismissProgressDialog(TakePhotoActivity.this);

                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(TakePhotoActivity.this);
                            System.out.println("bbb"+e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(TakePhotoActivity.this);
                        System.out.println("ccc"+error);
                    }
                });
    }

//    private void sendData() {
//        JSONObject json1 = new JSONObject();
//        String caption = editText.getText().toString();
//
//        try {
//            json1.put("begin_date", "2019-02-07");
//            json1.put("end_date", "9999-12-31");
//            json1.put("business_code", session.getUserBusinessCode());
//            json1.put("personal_number", session.getUserNIK());
//            json1.put("event_id", session.getEventId());
//            json1.put("image_id", session.getGalleryId());
//            json1.put("title", "");
//            json1.put("description", caption);
//            json1.put("image", img_data);
//            json1.put("change_user", session.getUserNIK());
//        } catch (JSONException e) {
//            //Log.d("hasil exception", e.toString());
//        }
//
//        AndroidNetworking.post(session.getServerURL()+"users/eventEMS/"+session.getEventId()+"/gallery"+session.getGalleryId())
//                .addHeaders("Accept","application/json")
//                .addHeaders("Content-Type","application/json")
//                .addHeaders("Authorization",session.getToken())
//                .addJSONObjectBody(json1)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                        System.out.println(response+"edknwkjrn4");
//                        try {
//                            if(response.getInt("status")==200){
//                                Intent i = new Intent(TakePhotoActivity.this, HomeActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(i);
//                                Toast.makeText(TakePhotoActivity.this,"Success submit photo",Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(TakePhotoActivity.this, "Failed submit photo", Toast.LENGTH_SHORT).show();
//                            }
//                        }catch (Exception e){
//                            System.out.println(e);
//                        }
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        System.out.println(error);
//                    }
//                });
//    }
}
