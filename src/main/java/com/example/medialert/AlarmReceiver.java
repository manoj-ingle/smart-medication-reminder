package com.example.medialert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "medicine_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("medicineName");
        String dose = intent.getStringExtra("dose");
        String time = intent.getStringExtra("time"); // Ensure you send time in intent
        int notifId = intent.getIntExtra("medicineId", 0);

        Log.d("AlarmReceiver", "Alarm received! Medicine: " + name);

        // --- Notification ---
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Medicine Reminder", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Medicine Reminder")
                .setContentText("Take " + name + " (" + dose + ")")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        nm.notify(notifId, builder.build());

        // --- Text-to-Speech ---
        TextToSpeech[] ttsWrapper = new TextToSpeech[1]; // wrapper to use inside lambda
        ttsWrapper[0] = new TextToSpeech(context.getApplicationContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                ttsWrapper[0].setLanguage(Locale.ENGLISH);
                String speechText = "Medicine " + name + ", Dose " + dose + ", Time " + time + " has arrived. Please take this medicine.";
                ttsWrapper[0].speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "MedicineTTS");

                ttsWrapper[0].setOnUtteranceProgressListener(new android.speech.tts.UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {}

                    @Override
                    public void onDone(String utteranceId) {
                        ttsWrapper[0].shutdown();
                    }

                    @Override
                    public void onError(String utteranceId) {
                        ttsWrapper[0].shutdown();
                    }
                });
            }
        });
    }
}