package id.co.pegadaian.diarium.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Aang on 04-Nov-16.
 *
 * Dibuat oleh : Gilang Rahman
 *
 * Kelas ini berisi modul-modul untuk penggunaan kamera dan pengambilan serta pengolahan gambar pada aplikasi.
 *
 */

public class CameraManager {

    public Bitmap timestampItAndSave(Bitmap toEdit){
        Bitmap dest = Bitmap.createBitmap(toEdit.getWidth(), toEdit.getHeight(), Bitmap.Config.ARGB_8888);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(35);
        tPaint.setColor(Color.YELLOW);
        tPaint.setStyle(Paint.Style.FILL);
        float height = tPaint.measureText("yY");
        cs.drawText(dateTime, 20f, height+15f, tPaint);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            dest.compress(Bitmap.CompressFormat.JPEG, 85,  byteArrayOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return dest;
    }

    private Bitmap addWaterMark(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dates = new Date();
        String datetime = dateFormat.format(dates);

        //String date =
        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.YELLOW);
        paintText.setTextSize(25);
        //paintText.setStyle(Style.FILL);
        //paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);

        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Rect rectText = new Rect();
        String wmText = datetime;
        paintText.getTextBounds(wmText, 0, wmText.length(), rectText);
        rectText.centerX();

        int centerX = (w - rectText.width()) / 2;
        int centerY = h - (h/3);

        canvas.drawText(wmText, centerX, centerY, paintText);

        return result;
    }

    public String compressImage(String imageUri) {

        //String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imageUri, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 300.0f;
        float maxWidth = 300.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

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

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(imageUri, options);
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

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(imageUri);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
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
            scaledBitmap = addWaterMark(Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }



    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    /*
    private File createImageFile() throws IOException {
        // Create an image file name

        Calendar cal = Calendar.getInstance();
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        String date = String.valueOf(month) + String.valueOf(day) + String.valueOf(hour) + String.valueOf(minute) + String.valueOf(second);
        Log.d("masuk date", date);
        String imageFileName = username + "_" + date;

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName,   //prefix
                ".jpg",         //suffix
                storageDir      // directory
        );
        // Save a file: path for use with ACTION_VIEW intents
        String mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }*/

    /*
    private void setPic() {
        Bitmap bitmap;
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

        Log.d("masuk gak sih2",String.valueOf(actualHeight));
        Log.d("masuk gak sih2",String.valueOf(actualWidth));

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
        Log.d("masuk gak sih","1a");
        Log.d("masuk gak sih1",bmOptions.toString());
        bmOptions.inSampleSize = imageUtil.calculateInSampleSize(bmOptions, actualWidth, actualHeight);
        Log.d("masuk gak sih","12a");
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

        bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        img_data = photoUtil.getStringBase64Bitmap(scaledBitmap);

        Log.d("masuk image data", img_data);
        AddList(imageFileName,scaledBitmap);
        // img_bg.setImageBitmap(scaledBitmap);
        // img_ic.setVisibility(View.GONE);
    }*/
}
