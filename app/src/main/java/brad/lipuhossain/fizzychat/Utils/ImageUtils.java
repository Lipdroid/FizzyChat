package brad.lipuhossain.fizzychat.Utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class ImageUtils {
    public static Context sContext = null;

    public ImageUtils(Context context) {
        sContext = context;
    }

    /**
     * TODO Function:<br>
     * Convert bitmap to base64 string
     *
     * @param image the resource
     * @return base64 string
     * @author: Phan Tri
     * @date: Jan 26, 2015
     */
    public static String encodeToBase64(Bitmap image) {
        int defautW = 480;
        float ratio = 0.0f;
        int w = image.getWidth();
        int h = image.getHeight();
        if (w / h > 1.0f) {
            ratio = w * 1.0f / h;
        } else {
            ratio = h * 1.0f / w;
        }

        Bitmap immagex = getBitmapThumb(image, defautW, Math.round(defautW * ratio));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        } catch (Exception e) {

        }

//		try {
//			immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
//		} catch (Exception e) {
//			
//		}
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    /**
     * TODO Function:<br>
     * Convert string base64 to bitmap
     *
     * @param input the string for convert
     * @return bitmap of base64 string resource
     * @author: Phan Tri
     * @date: Jan 26, 2015
     */
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    /**
     * TODO Function:<br>
     * Get bitmap of view
     *
     * @param v resource
     * @return bitmap of view
     * @author: Phan Tri
     * @date: Mar 23, 2015
     */
    public static Bitmap captureView(View v) {
//		Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth() * 2, v.getMeasuredHeight() * 2,
//				Bitmap.Config.RGB_565);
//		Canvas bitmapHolder = new Canvas(b);
//		Options options = new BitmapFactory.Options();       
//		options.inDither=true;                               
//		options.inScaled = true; 
//		options.inPreferredConfig = Bitmap.Config.RGB_565; 
//		options.inPurgeable=true;
//		v.draw(bitmapHolder);
//		return b;
        v.setDrawingCacheEnabled(true);
        v.measure(640, 1136);
//		v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        v.buildDrawingCache();
        Bitmap bm = v.getDrawingCache();
        // copy this bitmap otherwise distroying the cache will destroy
        // the bitmap for the referencing drawable and you'll not
        // get the captured view
        Bitmap b = bm.copy(Bitmap.Config.ARGB_8888, false);
        v.setDrawingCacheEnabled(false);
        return b;
    }

    /**
     * TODO Function:<br>
     * Get URI of specific bimap
     *
     * @param bm resource
     * @return uri of specific bitmap
     * @author: Phan Tri
     * @date: Mar 23, 2015
     */
    public static Uri getBitmapUri(Bitmap bm) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = Images.Media.insertImage(sContext.getContentResolver(), bm,
                String.valueOf(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }

    public static boolean storeImage(Bitmap image) {
        try {
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/toretan";
            File dir = new File(file_path);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(dir, "schedule.jpg");
            FileOutputStream fOut = new FileOutputStream(file);

            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();

            return true;
        } catch (Exception e) {
            //Toast.makeText(sContext, "Save image failed", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static void deleteImage(File file) {
        file.delete();
    }


    public static Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = sContext.getContentResolver().query(
                Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{Images.Media._ID},
                Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(Images.Media.DATA, filePath);
                return sContext.getContentResolver().insert(
                        Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * TODO
     * Function:<br>Compress bitmap
     *
     * @param original       bitmap
     * @param qualityPercent is quality of compression (0-100)
     * @return
     * @author: Phan Tri
     * @date: Apr 16, 2015
     */
    public static Bitmap compressBitmap(Bitmap original, int qualityPercent) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.PNG, qualityPercent, out);
        Bitmap compressed = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

        return compressed;
    }

    /**
     * TODO
     * Function:<br>Get bitmap from uri
     *
     * @param uri
     * @return
     * @author: Phan Tri
     * @date: Apr 16, 2015
     */
    public static Bitmap getBitmapFromUri(String uri) {
        Bitmap bm = null;
        try {
            bm = Images.Media.getBitmap(sContext.getContentResolver(), Uri.parse(uri));
        } catch (Exception e) {

        }
        return bm;
    }

    /**
     * TODO Function:<br>
     * Get thumb bitmap from another bitmap
     *
     * @param bitmap resource
     * @param w      width to scale
     * @param h      height to scale
     * @return thumb bitmap
     * @author: Phan Tri
     * @date: Feb 12, 2015
     */
    public static Bitmap getBitmapThumb(Bitmap bitmap, int w, int h) {
        Bitmap bmThumb = Bitmap.createScaledBitmap(bitmap, w, h, false);
        return bmThumb;
    }

    /**
     * TODO Function:<br>
     * Get max texture size
     *
     * @return max texture size
     * @author: Phan Tri
     * @date: Feb 25, 2015
     */
    public static int getMaxTextureSize() {
        // Safe minimum default size
        final int IMAGE_MAX_BITMAP_DIMENSION = 2048;

        // Get EGL Display
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        // Initialise
        int[] version = new int[2];
        egl.eglInitialize(display, version);

        // Query total number of configurations
        int[] totalConfigurations = new int[1];
        egl.eglGetConfigs(display, null, 0, totalConfigurations);

        // Query actual list configurations
        EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
        egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

        int[] textureSize = new int[1];
        int maximumTextureSize = 0;

        // Iterate through all the configurations to located the maximum texture
        // size
        for (int i = 0; i < totalConfigurations[0]; i++) {
            // Only need to check for width since opengl textures are always
            // squared
            egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH,
                    textureSize);

            // Keep track of the maximum texture size
            if (maximumTextureSize < textureSize[0])
                maximumTextureSize = textureSize[0];
        }

        // Release
        egl.eglTerminate(display);

        // Return largest texture size found, or default
        return Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION);
    }


    /**
     * TODO
     * Function:<br>Rotate bitmap
     *
     * @param source bimap
     * @param angle  to rotate
     * @return rotated bitmap
     * @author: Phan Tri
     * @date: Mar 27, 2015
     */
    public static Bitmap rotateBitmapWithAngle(Bitmap source, float angle, int scale) {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle);
        switch (scale) {
            case ExifInterface.ORIENTATION_UNDEFINED:
                break;
            case ExifInterface.ORIENTATION_NORMAL:
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                break;
        }
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }

    /**
     * TODO Function:<br>
     * True rotation bitmap
     *
     * @param bitmap resource
     * @param uri    resource
     * @return rotated bitmap
     * @author: Phan Tri
     * @date: Feb 12, 2015
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, Uri uri) {
        try {
            Matrix matrix = new Matrix();
            ExifInterface exifInterface = new ExifInterface(uri.toString());
            int rotation = Integer.parseInt(exifInterface
                    .getAttribute(ExifInterface.TAG_ORIENTATION));
            switch (rotation) {
                case ExifInterface.ORIENTATION_UNDEFINED:
                    return bitmap;
                case ExifInterface.ORIENTATION_NORMAL:
                    return bitmap;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    break;
            }

            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            return rotatedBitmap;
        } catch (Exception e) {
            return bitmap;
        }
    }


    /**
     * TODO Function:<br>
     * Fix big size resource bitmap with small size base on phone dimension
     *
     * @param bitmap resouce bitmap
     * @param uri    resource uri (can be null) for rotate bitmap
     * @return fixed bitmap
     * @author: Phan Tri
     * @date: Mar 18, 2015
     */
    public static Bitmap fixBitmapWithSmallSize(Bitmap bitmap, Uri uri) {
        int maxTexture = getMaxTextureSize();
        Bitmap fixedBitmap = null;
        Bitmap rotatedBitmap = null;
        // rotate bitmap
        if (uri != null) {
            rotatedBitmap = rotateBitmap(bitmap, uri);
        } else {
            rotatedBitmap = bitmap;
        }
        if (maxTexture < bitmap.getWidth() || maxTexture < bitmap.getHeight()) {
            if (bitmap.getWidth() < bitmap.getHeight()) {
                fixedBitmap = getBitmapThumb(rotatedBitmap, Math.round(maxTexture
                        * (bitmap.getWidth() * 1.0f / bitmap.getHeight()) / 2), maxTexture);
            } else {
                fixedBitmap = getBitmapThumb(rotatedBitmap, maxTexture, Math.round(maxTexture
                        * (bitmap.getHeight() * 1.0f / bitmap.getWidth()) / 2));
            }
        } else {
            fixedBitmap = rotateBitmap(
                    getBitmapThumb(bitmap, bitmap.getWidth(), bitmap.getHeight()), uri);
        }

        return fixedBitmap;
    }

    /**
     * TODO
     * Function:<br>Get byte array of bitmap
     *
     * @param bm resource
     * @return byte array of resource
     * @author: Phan Tri
     * @date: Apr 10, 2015
     */
    public static byte[] getByteArrayFromBitmap(Bitmap bm) {
        //calculate how many bytes our image consists of.
        int bytes = bm.getByteCount();
        // or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
        //int bytes = b.getWidth()*b.getHeight()*4;

        ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
        bm.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

        byte[] array = buffer.array(); //Get the underlying array containing the data.
        return array;
    }

    /**
     * TODO
     * Function:<br>Get bitmap from byte array
     *
     * @param byteArr resource array
     * @return bitmap
     * @author: Phan Tri
     * @date: Apr 16, 2015
     */
    public static Bitmap getBitmapFromByteArray(byte[] byteArr) {
        Bitmap bm = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length);
        return bm;
    }

    /**
     * TODO Function:<br>
     * Convert bitmap to file
     *
     * @param bm resource
     * @return file after convert
     * @author: PhanTri
     * @date: Jan 15, 2015
     */
    public static File bitmapToFile(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bmData = stream.toByteArray();
        File file = new File(sContext.getCacheDir(), System.currentTimeMillis() + ".png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bmData);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
