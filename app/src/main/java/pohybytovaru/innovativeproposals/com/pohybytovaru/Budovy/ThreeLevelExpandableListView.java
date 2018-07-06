package pohybytovaru.innovativeproposals.com.pohybytovaru.Budovy;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import pohybytovaru.innovativeproposals.com.pohybytovaru.R;


public class ThreeLevelExpandableListView extends ExpandableListActivity    { // ExpandableListActivity

    ListBudovaDataModel dm = new ListBudovaDataModel(this);
    private phoneExpandableListAdapter mietnostiListAdapter;

    private TextView myHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threelevelexpandablelistview);

        myHeader = findViewById(R.id.txZoznamBudov);

        myHeader.setText(getIntent().getStringExtra("header"));


        String myRooms [][][][]  = dm.getZoznamMiestnostiSBudovami(); // expandableListDetail
        Context myContext = this.getBaseContext();

        mietnostiListAdapter = new phoneExpandableListAdapter(this,getExpandableListView(),myRooms, myContext, this);
        setListAdapter(mietnostiListAdapter);

    }



}
