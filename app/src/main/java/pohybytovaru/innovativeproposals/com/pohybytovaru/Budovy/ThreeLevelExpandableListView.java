package pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;

import pohybytovaru.innovativeproposals.com.pohybytovaru.R;


public class ThreeLevelExpandableListView extends ExpandableListActivity    { // ExpandableListActivity

    ListBudovaDataModel dm = new ListBudovaDataModel(this);
    private phoneExpandableListAdapter mietnostiListAdapter;

    // nove
//    ExpandableListView expandableListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threelevelexpandablelistview);
        String myRooms [][][][]  = dm.getZoznamMiestnostiSBudovami(); // expandableListDetail
        Context myContext = this.getBaseContext();

        mietnostiListAdapter = new phoneExpandableListAdapter(this,getExpandableListView(),myRooms, myContext, this);
        setListAdapter(mietnostiListAdapter);


    }


}
