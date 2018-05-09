package pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.MnozstvaTovarov;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Models.MnozstvaTovaru;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

public class MnozstvaTovarovVMiestnostiachAdapter extends ArrayAdapter<MnozstvaTovaru> {

    Context context;
    int layoutResourceId;
    public List<MnozstvaTovaru> original_data = new ArrayList<MnozstvaTovaru>();
    public List<MnozstvaTovaru> filtered_list = new ArrayList<MnozstvaTovaru>();


    public MnozstvaTovarovVMiestnostiachAdapter(@NonNull Context context, int resource, @NonNull List<MnozstvaTovaru> data) {
        super(context, resource, data);

        this.layoutResourceId = resource;
        this.context = context;
        this.original_data = data;
        this.filtered_list.addAll(data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ImageView image = null;

        TextView tovarnazov = null;
        TextView miestnostnazov = null;
        TextView mnozstvo = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        image = row.findViewById(R.id.detailView_Image);
        miestnostnazov = row.findViewById(R.id.miestnostnazovTV );
        mnozstvo = row.findViewById(R.id.mnozstvoTV);

        MnozstvaTovaru mnozstvaTovaru = filtered_list.get(position);
        miestnostnazov.setText(mnozstvaTovaru.getMiestnost());
        //  itemDescription.setText(mnozstvaTovaru.getItemDescription());
        mnozstvo.setText(String.valueOf(mnozstvaTovaru.getMnozstvo()));

        //ulozenie ID-cka do riadku; ale mozeme sem ulozit aj cely objekt inventara (toto moze byy overkill pri vacsom obsahu dat)
        row.setTag(mnozstvaTovaru.getId());

/*        if (mnozstvaTovaru.getImage() != null && mnozstvaTovaru.getImage().length > 1) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(mnozstvaTovaru.getImage());
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            image.setImageBitmap(theImage);
        } else {
            image.setImageBitmap(null);
        } */
        return row;

    }

    @Override
    public int getCount() {
        return filtered_list.size();
    }

    public void filter(final String searchText) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filtered_list.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(searchText)) {
                    if (original_data != null)
                        filtered_list.addAll(original_data);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (MnozstvaTovaru item : original_data) {
                        if (item.getTovarNazov().toLowerCase().contains(searchText.toLowerCase())) {
                            // Adding Matched items
                            filtered_list.add(item);
                        }
                    }
                }

                // Set on UI Thread
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }
}
