package com.treggo.flexible.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by iRYO400 on 22.06.2016.
 */
public class SaveCameraAttachments {

    private final static String TAG = "mLogs";
    private Context mContext;
    private String nameOfFolder = "/FluxAttachments";
    private String nameOfFile = "FluxPicture";


    public String SaveImage(Context context, Uri uri) {
        this.mContext = context;

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + nameOfFolder;
        String currentTimeAndDate = getCurrentTimeAndDate();
        File dir = new File(filePath);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        File nomedia = new File(dir, ".NOMEDIA");
        if (!nomedia.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(nomedia);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap bitmap = null;
        File file = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            file = new File(dir, nameOfFile + currentTimeAndDate + ".jpeg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.flush();
            fos.close();
            isFileWasCreated(file);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "WTF?", Toast.LENGTH_SHORT).show();
        }
        return file.toString();

    }

    private void isFileWasCreated(File file) {
        MediaScannerConnection.scanFile(mContext, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d(TAG, "Scanned " + path + ":");
                        Log.d(TAG, "-> uri=" + uri);
                    }
                });
    }

    private String getCurrentTimeAndDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
        String formattedDate = smf.format(c.getTime());
        return formattedDate;
    }


}
