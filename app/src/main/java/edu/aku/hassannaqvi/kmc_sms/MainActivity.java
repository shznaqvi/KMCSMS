package edu.aku.hassannaqvi.kmc_sms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MessageListener {

    private static final String TAG = "Main Activity";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    Button smsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Register sms listener
        //MessageReceiver.bindListener(this);
        smsButton = findViewById(R.id.smsButton);
        checkForSmsPermission();

        KMCSMS.mainActivityContext = this;
    }

    @Override
    public void messageReceived(String message) {
        Toast.makeText(this, "New Message Received: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    enableSmsButton();

                    // Permission was granted.
                } else {
                    // Permission denied.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(MainActivity.this,
                            getString(R.string.failure_permission),
                            Toast.LENGTH_SHORT).show();
                    // Disable the message button.
                    disableSmsButton();
                }
            }
        }
    }

    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // Permission already granted. Enable the message button.
            enableSmsButton();
        }
    }

    private void enableSmsButton() {
        smsButton.setEnabled(true);
        smsButton.setText("SEND SMS");
    }

    private void disableSmsButton() {
        smsButton.setEnabled(false);
        smsButton.setText("");

    }

    public void smsSendMessage(View view) {
        // Find the TextView number_to_call and assign it to textView.
        TextView textView = findViewById(R.id.number_to_call);
        // Concatenate "smsto:" with phone number to create smsNumber.
        String smsNumber = "smsto:" + textView.getText().toString();
        // Find the sms_message view.
        EditText smsEditText = findViewById(R.id.sms_message);
        // Get the text of the sms message.
        String sms = smsEditText.getText().toString();
        // Create the intent.
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        // Set the data for the intent as the phone number.
        smsIntent.setData(Uri.parse(smsNumber));
        // Add the message (sms) with the key ("sms_body").
        smsIntent.putExtra("sms_body", sms);
        // If package resolves (target app installed), send intent.
        if (smsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(smsIntent);
            Toast.makeText(this, "SMS Sent", Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Can't resolve app for ACTION_SENDTO Intent.");
            Toast.makeText(this, "SMS NOT Sent", Toast.LENGTH_SHORT).show();


        }
    }

    public void smsSendMessageInAPP(View view) {
        EditText editText = findViewById(R.id.number_to_call);
        // Set the destination phone number to the string in editText.
        String destinationAddress = editText.getText().toString();
        // Find the sms_message view.
        EditText smsEditText = findViewById(R.id.sms_message);
        // Get the text of the SMS message.
        String smsMessage = smsEditText.getText().toString();
        // Set the service center address if needed, otherwise null.
        String scAddress = null;
        // Set pending intents to broadcast
        // when message sent and when delivered, or set to null.
        PendingIntent sentIntent = null, deliveryIntent = null;
        // Check for permission first.
        checkForSmsPermission();
        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage
                (destinationAddress, scAddress, smsMessage,
                        sentIntent, deliveryIntent);
    }

}