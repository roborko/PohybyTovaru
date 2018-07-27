package pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseExpandableListAdapter;

import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import pohybytovaru.innovativeproposals.com.pohybytovaru.Prehlady.PohybTovarov.PohybTovarActivityDetail_;
import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

import static android.app.Activity.RESULT_OK;

public class phoneExpandableListAdapter extends BaseExpandableListAdapter {

    private ThreeLevelExpandableListView threelevelexpandablelistview;  // bolo mainActivity
    private String mietnostiList[][][][];
    private Activity activity;

    private LayoutInflater inflater;
    private ExpandableListView expandableListView;
    private handleExpandableListView listViewManager[];
    private Context myContext;
    private String KEY_BUDOVA = "budova"; // phoneName
    private String POSCHODIE = "poschodie"; // version
    private String MIESTNOST = "miestnost"; // model

    public String myMiestnost, myPoschodie, myBudova;
    int myBudovaId, myPoschodieId;

    public String[] umiestnenieAdapter = new String[3];

    /*

    select budova.nazov,poschodie.nazov, miestnost.nazov from miestnost
join poschodie on poschodie.id = miestnost.idposchodie
join budova on budova.id = miestnost.idbudova

     */

    public phoneExpandableListAdapter(ThreeLevelExpandableListView threelevelexpandablelistview,
                                      ExpandableListView expandableListView, String[][][][] mietnostiList, Context myContext, Activity activity) {
        // TODO Auto-generated constructor stub
        this.threelevelexpandablelistview = threelevelexpandablelistview;
        this.expandableListView = expandableListView;
        this.mietnostiList = mietnostiList;
        this.myContext = myContext;
        this.activity = activity;
        inflater = LayoutInflater.from(threelevelexpandablelistview);
        listViewManager = new handleExpandableListView[mietnostiList.length];
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub

       // int aa = mietnostiList.length; // 2 = pocet riadkov pod urovnou na ktoru sa kliklo

        return mietnostiList.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub

        //Object xx = mietnostiList[groupPosition][0][0][0];  // obsah riadku 1. urovne, cize nazov

        return mietnostiList[groupPosition][0][0][0];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // poschodie

        Object xx = mietnostiList[groupPosition][childPosition];
        return mietnostiList[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        // id budovy

        //long xx = (long)(groupPosition*1024); // groupPosition = cislo riadku 1. urovne * 1024. Pri rozkliku zistuje GroupId

        return (long)(groupPosition*1024);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // id poschodia

        long xx = (long)(groupPosition*1024+childPosition);

        return (long)(groupPosition*1024+childPosition);
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // co je toto?
        View v = null;
        if( convertView != null )
            v = convertView;
        else
            v = inflater.inflate(R.layout.threelevelexpandablelistview_row_first, parent, false);

       // String gt = (String)getGroup( groupPosition ); // toto vracia hodnoty 1 urovne, ale aj druhej
        TextView phoneGroup = (TextView)v.findViewById( R.id.groupname );
        if( (String)getGroup( groupPosition ) != null ) { // bolo gt
            phoneGroup.setText((String)getGroup( groupPosition )); // bolo gt : nazov budovy
          //  myBudova = gt;
        }
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // pocet poschodi?
        View view = null;

        int aa = calculateRowCount(groupPosition, null);

        if(listViewManager[groupPosition]!=null){
            view = listViewManager[groupPosition];
        }
        else{
            final handleExpandableListView handle = new handleExpandableListView(threelevelexpandablelistview);
            handle.setRows(calculateRowCount(groupPosition,null));
            handle.setAdapter(
                    new handleSimpleExpandableListAdapter(
                            threelevelexpandablelistview, // context
                            createGroupList(groupPosition), // BUDOVY
                            R.layout.threelevelexpandablelistview_row_second, // groupLayout
                            new String[]{ KEY_BUDOVA}, // groupForm
                            new int[] { R.id.childname }, // groupTo
                            createChildList(groupPosition), // POSCHODIA
                            R.layout.threelevelexpandablelistview_row_second, // childLayout
                            new String [] {POSCHODIE,MIESTNOST}, // childFrom string
                            new int[] { R.id.childname, R.id.version } // childTo int


                    )
            );
            handle.setOnGroupClickListener(new Level2GroupExpandListener( groupPosition ));
            handle.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    myMiestnost = mietnostiList[myBudovaId][myPoschodieId][childPosition + 1][1]; // childPosition + 1 pretoze na nulke je nazov poschodia

                    umiestnenieAdapter[0] = myBudova;
                    umiestnenieAdapter[1] = myPoschodie;
                    umiestnenieAdapter[2] = myMiestnost;

                    // google: How to finish an activity from an Adapter..?

                   // Intent intent = new Intent(myContext,PohybTovarActivityDetail_.class);
                    Intent intent = new Intent();
                    intent.putExtra("umiestnenie", umiestnenieAdapter);
                   // intent.putExtra("miestnost",myMiestnost);
                    activity.setResult(RESULT_OK, intent);
                    activity.finish();

                   //  Toast.makeText(v.getContext(),"Entered: "+myBudova+ ", " + myPoschodie + ", " + myMiestnost  ,Toast.LENGTH_SHORT).show();
                    parent.collapseGroup( groupPosition );

                    return false;
                }
            });
            listViewManager[groupPosition] = handle;
            view = handle;
        }
        return view;
    }

    private List createGroupList(int groupPosition) {
        // tu sa naplnaju budovy

        myBudova = (String)getGroup( groupPosition );
        myBudovaId = groupPosition;

        ArrayList result = new ArrayList();
        for( int i = 0 ; i < mietnostiList[groupPosition].length ; ++i ) {
            HashMap m = new HashMap();

            String hh = mietnostiList[groupPosition][i][0][1];

            if(mietnostiList[groupPosition][i][0][1] != null) {  // VZOR OK

                m.put( KEY_BUDOVA,mietnostiList[groupPosition][i][0][1] );
                result.add( m );
            }
        }
        return (List)result;
    }

    private List createChildList(int groupPosition) {
        // vytvorenie zoznamu poschodi

        ArrayList result = new ArrayList();

        for( int i = 0 ; i < mietnostiList[groupPosition].length ; ++i ) {

            // rozklik budovy
            // String hh0 = mietnostiList[groupPosition][i][0][1];

            ArrayList secList = new ArrayList();
            for( int n = 1 ; n < mietnostiList[groupPosition][i].length ; ++n ) {
                HashMap child = new HashMap();

             //   String hh = mietnostiList[groupPosition][i][n][0];
             //   String hh1 = mietnostiList[groupPosition][i][n][1];

               // if(mietnostiList[groupPosition][i][n][1] != null && mietnostiList[groupPosition][i][n][1] != null) { // bolo
                if(mietnostiList[groupPosition][i][n][1] != null ) {

                    child.put(POSCHODIE, mietnostiList[groupPosition][i][n][0]);
                    child.put(MIESTNOST, mietnostiList[groupPosition][i][n][1]);  // todo tu by som pridal dalsiu layout
                    secList.add(child);
                }
            }

            if(secList.size()>0)
                result.add( secList );
        }
        return result;
    }

    private int calculateRowCount(int groupPosition, ExpandableListView object) {
        // toto je rozhodujuce
        int level2GroupCount = 0; // mietnostiList[groupPosition].length;

        for( int x = 1 ; x < mietnostiList[groupPosition].length  ; ++x ) {

            if(mietnostiList[groupPosition][x][0][0] != null)
             ++ level2GroupCount;
        }


        int rowCtr = 0; // pocita riadku ktore sa rozkliknu?

        for( int i = 0 ; i < mietnostiList[groupPosition].length ; ++i ) {
            ++rowCtr;       // for the group row
            if( ( object != null ) && ( object.isGroupExpanded( i ) ) ) {
               // ++rowCtr; // posunul som to sem
                int kolkoChildren = 0;
                for( int n = 1 ; n < mietnostiList[groupPosition][i].length -1 ; ++n ) {
                    HashMap child = new HashMap();

                    //if(mietnostiList[groupPosition][i][0][1] != null) dopln?

                        String hh = mietnostiList[groupPosition][i][n][0];
                         String hh1 = mietnostiList[groupPosition][i][n][1];

                    // bolo if(mietnostiList[groupPosition][i][n][1] != null && mietnostiList[groupPosition][i][n][1] != null) {
                    if(mietnostiList[groupPosition][i][n][0] != null && mietnostiList[groupPosition][i][n][1] != null) {
                        ++kolkoChildren;
                    }
                }

                //rowCtr += mietnostiList[groupPosition][i].length - 1;    // then add the children too (minus the group descriptor)
                rowCtr += kolkoChildren;
            }
        }

        int kolko = rowCtr;

        return rowCtr;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }
    public void onGroupCollapsed (int groupPosition) {}
    public void onGroupExpanded(int groupPosition) {}

    class Level2GroupExpandListener implements ExpandableListView.OnGroupClickListener {

        private int level1GroupPosition;
        public Level2GroupExpandListener( int level1GroupPosition ) {
            this.level1GroupPosition = level1GroupPosition;
        }

        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            // klikol na poschodie

            myPoschodie = mietnostiList[myBudovaId][groupPosition][0][1];
            myPoschodieId = groupPosition;

            if( parent.isGroupExpanded( groupPosition ) )
                parent.collapseGroup( groupPosition );
            else
                parent.expandGroup( groupPosition ); // rozklikol poschodie
            if( parent instanceof handleExpandableListView ) {
                handleExpandableListView dev = (handleExpandableListView)parent;
                dev.setRows( calculateRowCount( level1GroupPosition, parent ) );
            }

            expandableListView.requestLayout();
            return true;
        }
    }

}

