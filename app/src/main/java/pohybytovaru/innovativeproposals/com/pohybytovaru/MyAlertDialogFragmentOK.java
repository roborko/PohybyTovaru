package pohybytovaru.innovativeproposals.com.pohybytovaru;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyAlertDialogFragmentOK extends DialogFragment implements View.OnClickListener{

    public static final String ARG_TITLE = "AlertDialog.Title";
    public static final String ARG_MESSAGE = "AlertDialog.Message";
    // private EditText mEditText;
    private TextView tvMessage;
    private Button btOK;
    private Button btCancel;
    public boolean zobrazCancel = false;
    public int stlacenyButton = 0;
    // OK = 1
    // Cancel = 2


    public MyAlertDialogFragmentOK() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below

    }

    public static MyAlertDialogFragmentOK newInstance(String title) {
        MyAlertDialogFragmentOK frag = new MyAlertDialogFragmentOK();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // musim poskytnut View
        View theView = inflater.inflate(R.layout.fragment_dialog, container, false);
        // prepojime dialogove tlacidla
        View yesButton = theView.findViewById(R.id.buttonOK);
        View noButton = theView.findViewById(R.id.buttonCancel);

         yesButton.setOnClickListener(this);
        /*yesButton.setOnClickListener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(View v) {
                MyDialog dialog = new MyDialog();
                dialog.setTargetFragment(MainFragment.this, REQ_CODE);
                dialog.show(getFragmentManager(), "dialog");
            }
        });
        */


        noButton.setOnClickListener(this);

        yesButton.requestFocus(); // vyzaduje focus

        // Takyto dialog sa vsak zavrie ak kliknem mimo neho. To sa nastavuje v dialogu
        Dialog theDialog = getDialog();
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
      //  theDialog.setTitle("Nazov dialogu");
        theDialog.setCanceledOnTouchOutside(false); // zakaze zmiznutie dialogu pri kliknuti mimo dialogu
        if(zobrazCancel)
            noButton.setVisibility(View.VISIBLE);


        return theView;


        // bolo len toto
        //return inflater.inflate(R.layout.fragment_dialog, container);
    }

    /*
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState ) {

        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        tvMessage = (TextView) view.findViewById(R.id.lbl_message);

        btOK = (Button)view.findViewById(R.id.buttonOK);
        btCancel = (Button)view.findViewById(R.id.buttonCancel);

        if(zobrazCancel)
            btCancel.setVisibility(View.VISIBLE);

        btOK.setOnClickListener(this);
        btCancel.setOnClickListener(this);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        tvMessage.setText(title);


        // Show soft keyboard automatically and request focus to field
        // mEditText.requestFocus();

        // vyvolanie klavesnice
        // getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
*/

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonCancel:
                stlacenyButton = 2;
                break;
            case R.id.buttonOK:
                stlacenyButton = 1;
                break;
        }

        dismiss();

    }
}
