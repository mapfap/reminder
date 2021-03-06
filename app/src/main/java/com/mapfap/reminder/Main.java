package com.mapfap.reminder;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class Main extends Activity {

    private NotificationManager mNotifyMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNotification();
        setupInput();
    }

    private void setupNotification() {
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void MakeNotification(String text) {
        Log.d("Main", text);
        Calendar timestamp = Calendar.getInstance();
        appendLog(timestamp.getTime().toString() + ": " + text);
        Notification.Builder mBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)
                        .setContentTitle(text);
        mNotifyMgr.notify((int) timestamp.getTimeInMillis(), mBuilder.build());
    }

    public void appendLog(String text) {
        File logFile = new File("/sdcard/Download/reminder-log.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupInput() {
        final EditText editText = (EditText) findViewById(R.id.text);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.subSequence(s.length() - 1, s.length()).toString().equalsIgnoreCase("\n")) {
                    String text = editText.getText().toString();
                    editText.getText().clear();
                    MakeNotification(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
