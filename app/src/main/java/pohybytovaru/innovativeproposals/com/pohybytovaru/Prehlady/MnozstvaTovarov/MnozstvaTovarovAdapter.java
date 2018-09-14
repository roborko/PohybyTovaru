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


public class MnozstvaTovarovAdapter extends ArrayAdapter<MnozstvaTovaru> {
//public class MnozstvaTovarovAdapter extends DataBoundAdapter<MnozstvaTovaru> {

    // public class PohybTovarAdapter extends DataBoundAdapter<ActivityPohybTovarRowBinding, Pohyb> {


    Context context;
    int layoutResourceId;
    public List<MnozstvaTovaru> original_data = new ArrayList<MnozstvaTovaru>();
    public List<MnozstvaTovaru> filtered_list = new ArrayList<MnozstvaTovaru>();


    public MnozstvaTovarovAdapter(@NonNull Context context, int resource, @NonNull List<MnozstvaTovaru> data) {
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
        TextView mnozstvo = null;

     //   Bitmap bitmap= BitmapFactory.decodeResource(MnozstvaTovarovAdapter.this.getResources(), R.drawable.ic_delete_black_24dp);

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        image = row.findViewById(R.id.detailView_Image);
        tovarnazov = row.findViewById(R.id.tovarnazov );
        mnozstvo = row.findViewById(R.id.aktualne_mnozstvo);

        MnozstvaTovaru mnozstvaTovaru = filtered_list.get(position);
        tovarnazov.setText(mnozstvaTovaru.getTovarName());  // oprav mnozstvaTovaru.getTovar()

        //mnozstvo.setText(String.valueOf(mnozstvaTovaru.getMnozstvo()));
        mnozstvo.setText(String.valueOf((int)mnozstvaTovaru.getMnozstvo()));

        //datum je string, netreba konvertovat
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//        Date convertedDate = new Date();
//        if (inventar.getDatum() != null && !inventar.getDatum().isEmpty()) {
//
//            try {
//                convertedDate = dateFormat.parse(inventar.getDatum());
//
//
//                dateFormat = new SimpleDateFormat("dd.MM.yyyy");
//                itemDatum.setText(dateFormat.format(convertedDate));
//
//            } catch (Exception ex){
//                Log.d("Chyba" ,ex.toString());
//            }
//        }

      //  String xx = mnozstvaTovaru.getDatum();
      //  itemDatum.setText(mnozstvaTovaru.getDatum());



        //ulozenie ID-cka do riadku; ale mozeme sem ulozit aj cely objekt inventara (toto moze byy overkill pri vacsom obsahu dat)
        row.setTag(mnozstvaTovaru.getId());

        if (mnozstvaTovaru.getImage() != null && mnozstvaTovaru.getImage().length > 1) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(mnozstvaTovaru.getImage());
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            image.setImageBitmap(theImage);
        } else {
            //image.setImageBitmap(null); // bolo null @drawable/ic_do_not_disturb_alt_white_18dp
            image.setImageResource(R.drawable.ic_do_not_disturb_alt_black_18dp);
        }
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

                } /*

                naco to tu je??

                else {
                    // Iterate in the original List and add it to filter list...
                    for (MnozstvaTovaru item : original_data) {
                        if (item.getTovarNazov().toLowerCase().contains(searchText.toLowerCase())) {
                            // Adding Matched items
                            filtered_list.add(item);
                        }
                    }
                } */

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
