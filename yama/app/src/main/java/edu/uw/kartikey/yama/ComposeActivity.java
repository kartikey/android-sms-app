package edu.uw.kartikey.yama;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ComposeActivity extends AppCompatActivity {

    static final int PICK_CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        final EditText phoneNumber = (EditText) findViewById(R.id.phoneNumberField);
        final EditText messageText = (EditText) findViewById(R.id.messageText);
        ImageButton imgButton = (ImageButton) findViewById(R.id.imageButton);
        Button send = (Button)findViewById(R.id.sendButton);

        final String number = phoneNumber.toString();
        final String text = messageText.toString();



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, text, null, null);
                phoneNumber.clearComposingText();
                messageText.clearComposingText();
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
