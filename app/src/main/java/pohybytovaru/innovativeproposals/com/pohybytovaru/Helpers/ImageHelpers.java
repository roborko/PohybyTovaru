package pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Robert on 20.01.2018.
 */

public class ImageHelpers {

    //Converts Bitmap image from ImageView to Byte[]
    public static byte[] getImageBytesFromImageView(ImageView imageView) {
        try {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            //tu vieme zmenit kvalitu obrazku
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            return output.toByteArray();
        } catch (Exception ex) {
            return null;
        }
    }

    public static Bitmap convertBytesToBitmap(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length < 1) return null;

        try{
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
            Bitmap resultImage = BitmapFactory.decodeStream(imageStream);
            return resultImage;

        }catch (Exception ex){
            return null;
        }
    }
}
