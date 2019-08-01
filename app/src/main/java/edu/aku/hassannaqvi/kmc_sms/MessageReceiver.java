package edu.aku.hassannaqvi.kmc_sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class MessageReceiver extends BroadcastReceiver {

    public static final String pdu_type = "pdus";
    private static final String TAG = MessageReceiver.class.getSimpleName();
    private MessageListener listener = null;


    @Override
    public void onReceive(Context context, Intent intent) {

        listener = (MessageListener) KMCSMS.mainActivityContext;

        // Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_str = "";
        if (bundle != null) {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i = 0; i < smsm.length; i++) {
                smsm[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sms_str += "Sent From: " + smsm[i].getOriginatingAddress();
                sms_str += "\r\nMessage: ";
                sms_str += smsm[i].getMessageBody();
                sms_str += "\r\n";
            }
        }
        if (listener != null)
            listener.messageReceived(sms_str);
    }

}