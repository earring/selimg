package su.ias.components.selimg.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    @Nullable
    public static String getImageFileName(Context context, Uri uri) {
        try {
            ParcelFileDescriptor mInputPFD;

            /*
             * Get the content resolver instance for this context, and use it
             * to get a ParcelFileDescriptor for the file.
             */
            mInputPFD = context.getContentResolver().openFileDescriptor(uri, "r");
            // Get a regular file descriptor for the file
            FileDescriptor fd = mInputPFD.getFileDescriptor();
            FileInputStream fileInputStream = new FileInputStream(fd);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);

            File outputDir = context.getCacheDir(); // context being the Activity pointer
            File outputFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".jpg", outputDir);
            FileOutputStream fos = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();

            return outputFile.getPath();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
