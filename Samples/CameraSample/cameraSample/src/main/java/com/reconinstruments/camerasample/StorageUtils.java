package com.reconinstruments.camerasample;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by chris on 28/05/15.
 */
public class StorageUtils {

    public static final String DIRECTORY =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()+"/Camera";

    public static final String JPEG = ".jpg";
    public static final String MP4 = ".mp4";
    public static final String MP4_MIME = "video/mp4";

    public static SimpleDateFormat dateFormat;
    static {
        dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static ContentValues getVideoData(CamcorderProfile camProfile, long dateTaken) {
        String title = "VID_"+dateFormat.format(dateTaken);
        String filename = title + MP4;
        String path = DIRECTORY + '/' + filename;
        ContentValues videoValues = new ContentValues(9);
        videoValues.put(MediaStore.Video.Media.TITLE, title);
        videoValues.put(MediaStore.Video.Media.DISPLAY_NAME, filename);
        videoValues.put(MediaStore.Video.Media.DATE_TAKEN, dateTaken);
        videoValues.put(MediaStore.MediaColumns.DATE_MODIFIED, dateTaken / 1000);
        videoValues.put(MediaStore.Video.Media.MIME_TYPE, MP4_MIME);
        videoValues.put(MediaStore.Video.Media.DATA, path);
        videoValues.put(MediaStore.Video.Media.RESOLUTION,
                Integer.toString(camProfile.videoFrameWidth) + "x" + Integer.toString(camProfile.videoFrameHeight));

        return videoValues;
    }

    public static ContentValues getPhotoData(long dateTaken) {
        String title = "IMG_"+dateFormat.format(dateTaken);
        String filename = title + JPEG;
        String path = DIRECTORY + '/' + filename;
        ContentValues photoValues = new ContentValues(5);
        photoValues.put(MediaStore.Images.ImageColumns.TITLE, title);
        photoValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, filename);
        photoValues.put(MediaStore.Images.ImageColumns.DATE_TAKEN, dateTaken);
        photoValues.put(MediaStore.Images.ImageColumns.DATA, path);
        return photoValues;
    }

    public static String getVideoPath(ContentValues values) {
        return values.getAsString(MediaStore.Video.Media.DATA);
    }

    public static String getPhotoPath(ContentValues values) {
        return values.getAsString(MediaStore.Images.ImageColumns.DATA);
    }

    public static Uri insertVideo(Context context, String tmpPath, ContentValues metadata, long duration) {
        metadata.put(MediaStore.Video.Media.SIZE, new File(tmpPath).length());
        metadata.put(MediaStore.Video.Media.DURATION, duration);

        ContentResolver resolver = context.getContentResolver();
        Uri uri = null;
        try {
            uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, metadata);

            // Rename the video file to the final name. This avoids other
            // apps reading incomplete data.  We need to do it after we are
            // certain that the previous insert to MediaProvider is completed.
            String finalName = metadata.getAsString(
                    MediaStore.Video.Media.DATA);
            if(!new File(tmpPath).renameTo(new File(finalName))) {
                Log.e("SaveVideo", "failed to rename tmp file");
            } else {
                Log.v("SaveVideo", "Saved video to "+finalName);
            }

            resolver.update(uri, metadata, null, null);
        } catch (Exception e) {
            // We failed to insert into the database. This can happen if
            // the SD card is unmounted.
            Log.e("SaveVideo", "failed to add video to media store", e);
            uri = null;
        } finally {
            Log.v("SaveVideo", "Current video URI: " + uri);
        }
        return uri;
    }

    public static Uri insertJpeg(Context context, byte[] data, long dateTaken) {

        ContentValues metaData = getPhotoData(dateTaken);
        String path = getPhotoPath(metaData);

        FileOutputStream out = null;
        Uri uri = null;
        try {
            out = new FileOutputStream(path);
            out.write(data);
            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, metaData);
            Log.v("SavePhoto", "Saved image to "+path);
        } catch (Exception e) {
            Log.e("SavePhoto", "Failed to write data", e);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                Log.e("SavePhoto", "Failed to close file after write", e);
            }
        }
        return uri;
    }
}
