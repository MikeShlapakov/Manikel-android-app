package com.example.project_part2.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.util.Base64;
import android.widget.ImageView;

import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtil {

    public static Uri decodeBase64ToUri(String base64String, Context context) {

        // Check if the base64String contains the data URI prefix and remove it before decoding
        if (base64String.startsWith("data:image/png;base64,")) {
            base64String = base64String.substring("data:image/png;base64,".length());

        } else if (base64String.startsWith("data:image/jpeg;base64,")) {
            base64String = base64String.substring("data:image/png;base64,".length());

        } else if (base64String.startsWith("data:image/webp;base64,")) {
            base64String = base64String.substring("data:image/png;base64,".length());
        }

        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return saveImageAndGetUri(bitmap, context);
//
//
//        String base64Image = base64String.split(",")[1];
//
//        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
//
//        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//
//        return saveImageAndGetUri(bitmap, context);
    }

    private static Uri saveImageAndGetUri(Bitmap bitmap, Context context) {

        File imageFile = new File(context.getCacheDir(), "decodedImage.png");

        try {
            OutputStream os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
            return Uri.fromFile(imageFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String imageViewToBase64(ImageView imageView, Context context) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } else {
            throw new IllegalArgumentException("Unsupported drawable type");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
