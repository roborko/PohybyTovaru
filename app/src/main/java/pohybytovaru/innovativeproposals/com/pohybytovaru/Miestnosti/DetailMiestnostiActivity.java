package pohybytovaru.innovativeproposals.com.pohybytovaru.Miestnosti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.androidannotations.annotations.EActivity;

import pohybytovaru.innovativeproposals.com.pohybytovaru.R;

@EActivity
public class DetailMiestnostiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_miestnosti_detail);
    }
}
