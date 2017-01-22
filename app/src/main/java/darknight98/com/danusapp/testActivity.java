package darknight98.com.danusapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class testActivity extends AppCompatActivity {

    private Spinner produkList, quantityList;
    private EditText dateText;
    private Firebase mRefRoot, mRefDate, mRefItemKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        produkList = (Spinner)findViewById(R.id.product_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.list_produk, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        produkList.setAdapter(adapter);

        quantityList = (Spinner)findViewById(R.id.quantity_list);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.quantity, R.layout.support_simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        quantityList.setAdapter(adapter1);

        dateText = (EditText)findViewById(R.id.date_input);

        mRefRoot = new Firebase("https://danus-app.firebaseio.com/");
    }

    public void buttonPressed (View view) {
        startFireBase(dateText.getText().toString());
        try {
            String itemName = produkList.getSelectedItem().toString();

            mRefItemKey = mRefDate.child(itemName);

            mRefItemKey.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int quantity;
                    try {
                        quantity = dataSnapshot.getValue(Integer.class);
                    } catch (Exception e) {
                        quantity = 0;
                    }

                    mRefItemKey.setValue((quantity + Integer.parseInt(quantityList.getSelectedItem().toString())));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            testActivity.hideKeyboard(testActivity.this);
            Toast.makeText(testActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(testActivity.this).create();
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
        mRefDate = mRefRoot.child(date);
    }

    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        backButtonHandler();
        return;
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                testActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Leave application?");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to leave the application?");
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(testActivity.this, LoginActivity.class));
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
