package pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleExpandableListAdapter;

public class handleSimpleExpandableListAdapter extends SimpleExpandableListAdapter {

    public handleSimpleExpandableListAdapter(Context context,
                                             List<? extends Map<String, ?>> groupData, int groupLayout,
                                             String[] groupFrom, int[] groupTo,
                                             List<? extends List<? extends Map<String, ?>>> childData,
                                             int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData,
                childLayout, childFrom, childTo);
        // TODO Auto-generated constructor stub
    }


    @SuppressLint("LongLogTag")
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = super.getChildView( groupPosition, childPosition, isLastChild, convertView, parent );
        Log.d( LOG_TAG, "getChildView: groupPosition: "+groupPosition+"; childPosition: "+childPosition+"; v: "+v );
        return v;
    }

    @SuppressLint("LongLogTag")
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = super.getGroupView( groupPosition, isExpanded, convertView, parent );
        Log.d( LOG_TAG, "getGroupView: groupPosition: "+groupPosition+"; isExpanded: "+isExpanded+"; v: "+v );
        return v;
    }

    private static final String LOG_TAG = "handleSimpleExpandableListAdapter";


}

