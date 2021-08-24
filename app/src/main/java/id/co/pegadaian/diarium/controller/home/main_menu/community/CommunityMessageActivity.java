package id.co.pegadaian.diarium.controller.home.main_menu.community;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.adapter.MessageCommunityAdapter;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.model.MessageContentModel;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;
import id.co.pegadaian.diarium.util.photo.PhotoUtil;
import id.co.pegadaian.diarium.util.photo.TakePhotoUtil;

public class CommunityMessageActivity extends AppCompatActivity {
    String community_name, community_id, community_role;
    Typeface font, fontbold;
    UserSessionManager session;
    private ProgressDialogHelper progressDialogHelper;
    private List<MessageContentModel> listModel;
    private MessageContentModel model;
    private MessageCommunityAdapter adapter;
    private TextView tvNull;
    ListView list;
    TextView btnSend;
    LinearLayout lay_create;
//    EditText etField;
//    ImageView ivAttach;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int CAPTURE_PHOTO = 2;
    private static final int CAPTURE_GALLERY = 3;
    private String img_data, mCurrentPhotoPath;
    private Bitmap bitmapPhoto;
    private TakePhotoUtil photoUtil;
    private PhotoUtil imageUtil;
    Dialog dialog;

    @SuppressLint({"WrongViewCast", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_message);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        photoUtil = new TakePhotoUtil();
        imageUtil = new PhotoUtil();
        session = new UserSessionManager(this);
        progressDialogHelper = new ProgressDialogHelper();
        Intent intent = getIntent();
        community_id = intent.getStringExtra("community_id");
        community_name = intent.getStringExtra("community_name");
        community_role = intent.getStringExtra("community_role");
        System.out.println(community_role+" mkekek");
        font = Typeface.createFromAsset(getApplication().getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(), "fonts/Nexa Bold.otf");

//        ivAttach = (ImageView) findViewById(R.id.ivAttach);
        list = (ListView) findViewById(R.id.list_message);
        lay_create = (LinearLayout) findViewById(R.id.sendpost);
        lay_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CommunityMessageActivity.this, CreatePostCommunityActivity.class);
                i.putExtra("community_id",community_id);
                startActivity(i);
            }
        });
//        etField = (EditText) findViewById(R.id.message);
//        ivAttach.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PopupMenu popup = new PopupMenu(CommunityMessageActivity.this, v);
//                popup.getMenuInflater().inflate(R.menu.menu_attach, popup.getMenu());
//
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        if (item.getTitle().equals("Image")) {
//                            generateContentId("", "2");
//                        } else if (item.getTitle().equals("PDF")) {
//                            generateContentId("", "3");
////                            Intent intent = new Intent();
////                            intent.setAction(Intent.ACTION_GET_CONTENT);
////                            intent.setType("application/pdf");
////                            startActivityForResult(intent, 7);
//                        }
////                        Toast.makeText(CommunityMessageActivity.this,
////                                "Clicked popup menu item " + item.getTitle(),
////                                Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                });
//
//                popup.show();
//            }
//        });
//        etField.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.setFocusable(true);
//                v.setFocusableInTouchMode(true);
//                return false;
//            }
//        });
        getContentMessage(community_id);
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String field = etField.getText().toString();
//                generateContentId(field, "1");
////                @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
////                String tRes = tgl.format(new Date());
//            }
//        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(community_name);
    }

    @Override
    public void onResume(){
        super.onResume();
        getContentMessage(community_id);

    }

    public static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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
            } else if (requestCode == 7){
                String PathHolder = data.getData().getPath();
                confirmPDF(PathHolder);
//                Toast.makeText(CommunityMessageActivity.this, PathHolder, Toast.LENGTH_LONG).show();
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
        return file;
    }

    private void confirmPDF(final String Path) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommunityMessageActivity.this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to upload this file ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                submitPostPdf(Path);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void submitPostImage(String image_data) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());
        AndroidNetworking.upload(session.getServerURL()+"users/post_CommunityContent/"+community_id+"/2")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addMultipartParameter("begin_date",tRes)
                .addMultipartParameter("end_date","9999-12-31")
                .addMultipartParameter("business_code",session.getUserBusinessCode())
                .addMultipartParameter("personal_number",session.getUserNIK())
                .addMultipartParameter("community_id",community_id)
                .addMultipartParameter("content_type","2")
                .addMultipartFile("content_field", saveImage(CommunityMessageActivity.this, image_data))
                .addMultipartParameter("change_user",session.getUserNIK())
                .addMultipartParameter("content_id",session.getGeneratedContentId())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"KONTENIMAGE");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(CommunityMessageActivity.this, "Success add new post !", Toast.LENGTH_SHORT).show();
//                                Intent a = new Intent(CommunityMessageActivity.this, ProfileActivity.class);
//                                a.putExtra("key", "none");
//                                startActivity(a);
                                finish();
                            }else {
                                Toast.makeText(CommunityMessageActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            System.out.println("bbb"+e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("ccc"+error);
                    }
                });
    }


    private void submitPostPdf(String Path) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());
        AndroidNetworking.upload(session.getServerURL()+"users/post_CommunityContent/"+community_id+"/3")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addMultipartParameter("begin_date","2019-04-29")
                .addMultipartParameter("end_date","9999-12-29")
                .addMultipartParameter("business_code",session.getUserBusinessCode())
                .addMultipartParameter("personal_number",session.getUserNIK())
                .addMultipartParameter("community_id",community_id)
                .addMultipartParameter("content_type","3")
                .addMultipartFile("content_field", saveImage(CommunityMessageActivity.this, img_data))
                .addMultipartParameter("change_user",session.getUserNIK())
                .addMultipartParameter("content_id",session.getGeneratedContentId())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"KONTENPDF");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(CommunityMessageActivity.this, "Success add new post !", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(CommunityMessageActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            System.out.println("bbb"+e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("ccc"+error);
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

        bitmapPhoto = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        img_data = photoUtil.getStringBase64Bitmap(scaledBitmap);
        Log.d("GAMBER", "GAMBAR : " +img_data);
        popUpUploadImage(scaledBitmap, img_data);
//        Toast.makeText(this, "isi dari camera"+scaledBitmap, Toast.LENGTH_SHORT).show();
//        imageView.setImageBitmap(scaledBitmap);
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
        Log.d("GAMBER", "GAMBAR : " +img_data);
//        Toast.makeText(this, "isi dari gallery : "+scaledBitmap, Toast.LENGTH_SHORT).show();
        popUpUploadImage(scaledBitmap, img_data);
//        imageView.setImageBitmap(scaledBitmap);
        //img_bg.setImageBitmap(scaledBitmap);
        //img_ic.setVisibility(View.GONE);
    }

    private void selectAction() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CommunityMessageActivity.this);
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

    private void generateContentId(final String field, final String type) {
        SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        AndroidNetworking.get(session.getServerURL() + "users/generateIDContent")
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("djhwebfheActivityId" + response);
                        // do anything with response
                        try {
                            if (response.getInt("status") == 200) {
                                String generatedContentId = response.getJSONObject("data").getString("id");
                                session.setGeneratedContentId(generatedContentId);
                                if (type.equals("1")) {
                                    submitPostText(field, community_id);
                                } else if (type.equals("2")) {
                                    selectAction();
                                } else {
                                    File pdfFile = new File(Environment.getExternalStorageDirectory(),"PdfFile.pdf");//File path
                                    if (pdfFile.exists()) //Checking for the file is exist or not
                                    {
                                        Uri path = Uri.fromFile(pdfFile);
                                        Intent objIntent = new Intent(Intent.ACTION_VIEW);
                                        objIntent.setDataAndType(path, "application/pdf");
                                        objIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivityForResult(objIntent, 7);//Staring the pdf viewer
                                    }
                                }
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

    private void submitPostText(final String field, final String community_id) {
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());
        AndroidNetworking.post(session.getServerURL()+"users/post_CommunityContent/"+community_id+"/1")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .addBodyParameter("begin_date","2019-04-29")
                .addBodyParameter("end_date","9999-12-29")
                .addBodyParameter("business_code",session.getUserBusinessCode())
                .addBodyParameter("personal_number",session.getUserNIK())
                .addBodyParameter("community_id",community_id)
                .addBodyParameter("content_type","1")
                .addBodyParameter("content_field",field)
                .addBodyParameter("change_user",session.getUserNIK())
                .addBodyParameter("content_id",session.getGeneratedContentId())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"POstKONTEN"+session.getUserNIK()+session.getUserBusinessCode()+session.getGeneratedContentId()+community_id+field);
                        try {
                            if(response.getInt("status")==200){
//                                etField.setText("");
                                adapter.notifyDataSetChanged();
                                getContentMessage(community_id);
                                Toast.makeText(CommunityMessageActivity.this, "Success add new content !", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(CommunityMessageActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            System.out.println("bbb"+e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("ccc"+error);
                    }
                });
    }

    private void getContentMessage(final String community_id){
        System.out.println(community_id+"mnelj4");
        progressDialogHelper.showProgressDialog(CommunityMessageActivity.this, "Getting data...");
        AndroidNetworking.get(session.getServerURL()+"users/showCommunityContent/comid/"+community_id+"/buscd/"+session.getUserBusinessCode())
//        AndroidNetworking.get(session.getServerURL()+"users/showsAllContent?")
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
                        System.out.println("KONTENDENGANID"+response+community_id);
                        try {
                            if(response.getInt("status")==200){
//                                session.setToken(response.getString("token"));
                                listModel = new ArrayList<MessageContentModel>();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = jsonArray.length()-1; a >=0; a--) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String begin_date = object.getString("begin_date");
                                    String end_date = object.getString("end_date");
                                    String business_code = object.getString("business_code");
                                    String personal_number = object.getString("personal_number");
                                    String community_id = object.getString("community_id");
                                    String content_type = object.getString("content_type");
                                    String content_field = object.getString("content_field");
                                    String change_date = object.getString("change_date");
                                    String change_user = object.getString("change_user");
                                    String content_id = object.getString("content_id");
                                    String content_title = object.getString("content_title");
                                    String content_desc = object.getString("content_desc");
                                    String profile = object.getString("profile");
                                    String nama = null;
                                    JSONArray namaArray = object.getJSONArray("full_name");
                                    for (int k=0;k<namaArray.length();k++) {
                                        JSONObject objNama = namaArray.getJSONObject(k);
                                        nama = objNama.getString("full_name");
                                    }
                                    model = new MessageContentModel(begin_date, end_date, business_code, personal_number, community_id, content_type, content_field, change_date, change_user, content_id, content_title, content_desc, profile, nama);
                                    listModel.add(model);
                                }
                                adapter = new MessageCommunityAdapter(CommunityMessageActivity.this, listModel);
                                list.setAdapter(adapter);
                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        generateActivityId();
                                        Intent i = new Intent(CommunityMessageActivity.this, DetailCommunityContent.class);
                                        i.putExtra("name",listModel.get(position).getNama());
                                        i.putExtra("title",listModel.get(position).getContent_title());
                                        i.putExtra("begin_date",listModel.get(position).getChange_date());
                                        i.putExtra("desc",listModel.get(position).getContent_desc());
                                        i.putExtra("ava",listModel.get(position).getProfile());
                                        startActivity(i);
                                    }
                                });

                                progressDialogHelper.dismissProgressDialog(CommunityMessageActivity.this);
                            }else{
                                popUpLogin();
                                progressDialogHelper.dismissProgressDialog(CommunityMessageActivity.this);

                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(CommunityMessageActivity.this);

                            System.out.println(e);
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(CommunityMessageActivity.this);

                        System.out.println(error);
                    }
                });
    }

    private void popUpUploadImage(Bitmap bitmap, final String image_date) {
        final Dialog dialog = new Dialog(CommunityMessageActivity.this);
        dialog.setContentView(R.layout.layout_confirm_image);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        Button btnNo =(Button) dialog.findViewById(R.id.btnNo);
        ImageView ivFoto =(ImageView) dialog.findViewById(R.id.ivPhoto);
        ivFoto.setImageBitmap(bitmap);
        dialog.show();
        dialog.setCancelable(false);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPostImage(image_date);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void popUpLogin() {
        final Dialog dialog = new Dialog(CommunityMessageActivity.this);
        dialog.setContentView(R.layout.layout_session_end);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        dialog.show();
        dialog.setCancelable(false);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLoginState(false);
                Intent i = new Intent(CommunityMessageActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(CommunityMessageActivity.this, CommunityActivity.class);
        startActivity(i);
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (community_role.equals("AD")) {
            inflater.inflate(R.menu.activity_main_actions, menu);
        } else {
            inflater.inflate(R.menu.menu_community_user, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void popReschedule(String booking_code) {
        dialog = new Dialog(CommunityMessageActivity.this);
        dialog.setContentView(R.layout.layout_confirm_reschedule);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        Button btnNo =(Button) dialog.findViewById(R.id.btnNo);
        dialog.show();
        dialog.setCancelable(false);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sub1:
                Intent i = new Intent(CommunityMessageActivity.this, DetailCommunityActivity.class);
                i.putExtra("community_name", community_name);
                i.putExtra("community_id", community_id);
                i.putExtra("community_role", community_role);
                startActivity(i);
                return true;
            case R.id.sub2:
                popUpDeleteCommunity(community_id,community_name );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void popUpDeleteCommunity(String id, String name) {
        dialog = new Dialog(CommunityMessageActivity.this);
        dialog.setContentView(R.layout.layout_confirm_delete_community);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setTitle("Input Code Here");
        Button btnYes =(Button) dialog.findViewById(R.id.btnYes);
        Button btnNo =(Button) dialog.findViewById(R.id.btnNo);
        dialog.show();
        dialog.setCancelable(false);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDelete(community_id);
                dialog.dismiss();
            }
        });
    }

    private void submitDelete(String id) {
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
        String jamRes = jam.format(new Date());
        AndroidNetworking.post(session.getServerURL()+"users/deleteCommunity/cmtid/"+id+"/buscd/"+session.getUserBusinessCode())
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONHASILDELETE");
                        try {
                            if(response.getInt("status")==200){
                                Toast.makeText(CommunityMessageActivity.this, "Success delete community", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(CommunityMessageActivity.this,"Error delete !",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            System.out.println("bbb"+e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println("ccc"+error);
                    }
                });
    }
}


