package darknight98.com.danusapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class DanusActivity extends AppCompatActivity {

    EditText dateText;
    Spinner itemList, quantityPicker;
    Firebase mRefRoot, mRefDate, mRefItemKey;
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danus);

        dateText = (EditText)findViewById(R.id.date_text);

        confirmButton = (Button)findViewById(R.id.confirm_button);

        quantityPicker = (Spinner) findViewById(R.id.quantity_list_spinner);
        ArrayAdapter<CharSequence> integerArrayAdapter = ArrayAdapter.createFromResource(
                this, R.array.quantity, R.layout.support_simple_spinner_dropdown_item);
        integerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        quantityPicker.setAdapter(integerArrayAdapter);

        itemList = (Spinner)findViewById(R.id.item_list_spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                this, R.array.list_produk, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        itemList.setAdapter(arrayAdapter);

        mRefRoot = new Firebase("https://danus-app.firebaseio.com/");
    }

    public void confirmButtonPressed (View view) {
        startFireBase(dateText.getText().toString());
        try {
            String itemName = itemList.getSelectedItem().toString();

            mRefItemKey = mRefDate.child(itemName);

            mRefItemKey.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int quantity;
                    try {
                        quantity = dataSnapshot.getValue(Integer.class);
                    } catch (Exception e) {
                        quantity = 0;
                    }

                    mRefItemKey.setValue((quantity + Integer.parseInt(quantityPicker.getSelectedItem().toString())));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            Toast.makeText(DanusActivity.this, "Success!!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(DanusActivity.this).create();
            alertDialog.setTitle("Error!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setMessage("Fail to update database");
            alertDialog.show();
        }
    }

    public void startFireBase (String date) {
        mRefDate = new Firebase("https://danus-app.firebaseio.com/" + date + "/");
    }
}
