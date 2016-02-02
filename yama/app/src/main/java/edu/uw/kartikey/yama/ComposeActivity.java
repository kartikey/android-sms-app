package edu.uw.kartikey.yama;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ComposeActivity extends AppCompatActivity {

    static final int PICK_CONTACT_REQUEST = 1;
    static final String TAG = "COMPOSE";
    String SMS_SENT = "SMS_SENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);


        ImageButton imgButton = (ImageButton) findViewById(R.id.imageButton);
        Button send = (Button)findViewById(R.id.sendButton);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();

                PendingIntent pendingIntent = PendingIntent.getActivity(ComposeActivity.this, 0,
                        new Intent(SMS_SENT), 0);



                EditText phoneNumber = (EditText) findViewById(R.id.phoneNumberField);
                EditText messageText = (EditText) findViewById(R.id.messageText);

                String number = phoneNumber.getText().toString();
                String text = messageText.getText().toString();

                Log.v(TAG, "numenr: "+number + " text: "+text);

                smsManager.sendTextMessage(number, null, text, pendingIntent, null);

                phoneNumber.setText("");
                messageText.setText("");
            }
        });



        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {

                Uri contactUri = data.getData();

                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);

                EditText phoneNumber = (EditText) findViewById(R.id.phoneNumberField);
                phoneNumber.setText(number);
            }
        }
    }




}
