package pohybytovaru.innovativeproposals.com.pohybytovaru.Shared.Converters;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Helpers.ImageHelpers;

/**
 * Created by Robert on 21.01.2018.
 */

public class DataBindingConverters {

    //adapter used to deserialize drawable ID to actual drawable
    // xx @BindingAdapter({"bind:drawableSource"})
    @BindingAdapter({"drawableSource"})
    public static void loadImageDrawable(ImageView view, int imageId){
        view.setImageResource(imageId);
    }

    //adapter used to deserialize byte array of images to imageview
    //xx @BindingAdapter({"bind:imageSource", "bind:error"})
    @BindingAdapter({"imageSource", "error"})
    public static void loadImage(ImageView view, byte[] imageBytes, Drawable error){
        if(imageBytes == null || imageBytes.length == 0){
            //Drawable noImageFound =
            view.setImageDrawable(error);
        }
        else {
            Bitmap bitmap = ImageHelpers.convertBytesToBitmap(imageBytes);
            view.setImageBitmap(bitmap);
        }
    }

    @BindingConversion
    public static String convertDateToDisplayedText(Date date){
        if(date == null)
            return null;

        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
    }
}
