package edu.uw.kartikey.yama;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ComposeActivity extends AppCompatActivity {

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
            }
        });



    }
}
