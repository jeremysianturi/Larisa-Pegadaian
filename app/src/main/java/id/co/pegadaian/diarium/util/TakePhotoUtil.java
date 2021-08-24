package id.co.pegadaian.diarium.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by biting on 26/04/16.
 */
public class TakePhotoUtil {

        public static int MEDIA_TYPE_IMAGE = 1;
        public static int MEDIA_TYPE_VIDEO = 2;

        public static int PICTURE_QUALITY = 80;

        /** Create a file Uri for saving an image or video */

        public static final Uri getOutputMediaFileUri(int type, String fileName) {
            return Uri.fromFile(getOutputMediaFile(type, fileName));
        }

        /**
         * @param type
         * @param path
         *            , Default Location DCIM+path
         * @return
         */
        public static File getOutputMediaFile(int type, String path) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), path);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory");
                    return null;
                }
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                // TODO ingat dihapus nanti.. for testing purpose only
                // mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                // "IMG_"+ timeStamp + ".jpg");
                //
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                Log.d("MyCameraApp", "mediaFile");
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        }

        /**
         * Compress super kecil ..quality image bakalan berubah
         *
         * @param bitmap
         * @param fileUri
         */
        public static void compressImage(Bitmap bitmap, Uri fileUri) {
            try {
                FileOutputStream out = new FileOutputStream(fileUri.getPath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        /**
         * sama seperti method compress Image, bedanya disini menggunakan path
         *
         * @param type
         * @param folderName
         * @param bitmap
         * @return
         */
        public static Bitmap compressImageFilePath(int type, String folderName, Bitmap bitmap) {
            try {
                FileOutputStream out = new FileOutputStream(getOutputMediaFile(type, folderName));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;

        }

        /**
         * Decode a file to Bitmap untuk menghidar OutofMemory
         *
         * @param f
         *            File to be decoded
         * @return Bitmap
         * @throws IOException
         */
        public static Bitmap decodeFile(File f, final int IMAGE_MAX_SIZE) throws IOException {
            Bitmap b = null;

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(
                        2,
                        (int) Math.round(Math.log(IMAGE_MAX_SIZE
                                / (double) Math.max(o.outHeight, o.outWidth))
                                / Math.log(0.5)));
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inPurgeable = true;
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();

            return b;
        }

        /**
         * remove last Image yang ada di DCIM setelah camera Intent capture
         *
         * @param cr
         * @param id
         */
        public static void removeImage(ContentResolver cr, int id) {
            cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Media._ID + " = ?", new String[] { "" + id });
        }

        /**
         * Gets the last image id from the media store
         *
         * @return
         */
        public static int getLastImageId(ContentResolver cr) {
            final String[] imageColumns = { MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA };
            final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
            Cursor imageCursor = cr.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns,
                    null, null, imageOrderBy);
            if (imageCursor.moveToFirst()) {
                int id = imageCursor.getInt(imageCursor
                        .getColumnIndex(MediaStore.Images.Media._ID));
                imageCursor.close();
                return id;
            } else {
                return 0;
            }
        }

        /**
         * method konversi bitmap ke String base 64 unutk siap dikirim lewat WS
         * @Auhtor adi.ramadhan
         * @param bitmap
         * @return
         */
        public static String getStringBase64Bitmap(Bitmap bitmap){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapBytes = bos.toByteArray();
            String encodedImage = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            Log.i("getStringBase64Bitmap",encodedImage);
            return encodedImage;
        }

        /**
         * method konversi String base 64 ke Bitmap untuk disimpan ke File
         * @Auhtor Yosia Sihombing
         * @param imageString
         * @return
         */
        public static Bitmap getBitmapFromStringBase64(String imageString){
            byte[] imageAsBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap bitmap =  BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            return bitmap;
        }

        public static File resizeBitmap(File file){
            int maxWidth=1024;
            int maxHeight=800;
            Bitmap bitmapOriginal = BitmapFactory.decodeFile(file.getAbsolutePath());
            Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmapOriginal, maxWidth, maxHeight, true);
            bitmapOriginal.recycle();
            try {
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmapsimplesize.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                ostream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return file;
        }

        public static Uri saveBitmapToFile(Bitmap bm, String name) throws FileNotFoundException, IOException {
            String path= Environment.getExternalStorageDirectory()+"/DCIM/BIN/Evidence/Image";
            File myFolder=new File(path);
            if(!myFolder.exists())
                myFolder.mkdirs();

            //create a file to write bitmap data
            File f = new File(path,name);
            if(f.getParentFile() != null)
                myFolder.createNewFile();

            //Convert bitmap to byte array
            Bitmap bitmap = bm;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            try{
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                return Uri.fromFile(f);
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
}
