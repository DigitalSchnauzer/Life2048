package schnauzer.digital.life2048;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class settings_dialog extends Activity implements View.OnClickListener {
    String name = "settings_dialog";
    static final String FILENAME = "settings_data";
    Intent intent;

    Switch soundSwitch;
    Switch landscapeSwitch;
    Switch fullscreenSwitch;
    Switch blackAndWhiteSwitch;
    Switch originalModeSwitch;
    Switch timedSwitch;
    Switch unlimitedSwitch;
    EditText userID;
    EditText userName;
    Button login;
    Button cancel;
    Button ok;
    Button restore;

    MyStorageManager sm = new MyStorageManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        setContentView(R.layout.settings_dialog);

        // INPUT
        soundSwitch = (Switch) findViewById(R.id.sound_switch);
        landscapeSwitch = (Switch) findViewById(R.id.landscape_switch);
        fullscreenSwitch = (Switch) findViewById(R.id.fullscreen_switch);
        blackAndWhiteSwitch = (Switch) findViewById(R.id.blackAndWhite_switch);
        originalModeSwitch = (Switch) findViewById(R.id.originalMode_switch);
        timedSwitch = (Switch) findViewById(R.id.timed_switch);
        unlimitedSwitch = (Switch) findViewById(R.id.unlimited_switch);
        userID = (EditText) findViewById(R.id.id);
        userName = (EditText) findViewById(R.id.username);
        login = (Button) findViewById(R.id.login);
        cancel = (Button) findViewById(R.id.cancel);
        ok = (Button) findViewById(R.id.ok);
        restore = (Button) findViewById(R.id.restore);

        login.setOnClickListener(this);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
        restore.setOnClickListener(this);
        // INPUT END
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.wtf(this.name, "onStart() llamado");

        FileInputStream fis = null;
        try {fis = openFileInput(FILENAME);} catch (FileNotFoundException e) {e.printStackTrace();}
        //try {fis.close();} catch (IOException e) {e.printStackTrace(); Log.wtf(this.name, "Exception on FileInputStream closing");}
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            fis.close();
        } catch(OutOfMemoryError om){om.printStackTrace(); Log.wtf(this.name, "Out of Memory Exception on FileInputStream closing");
        } catch (IOException e) {e.printStackTrace(); Log.wtf(this.name, "IOException on FileInputStream closing");
        } catch(Exception ex){ex.printStackTrace(); Log.wtf(this.name, "Exception on FileInputStream closing");
        }
        String result = sb.toString();
        if (result.length() > 0)
            readString(result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                intent = new Intent(this, login_dialog.class);
                startActivity(intent);
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.ok:
                String data = writeString();

                FileOutputStream fos = null;
                try {fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);} catch (FileNotFoundException e) {e.printStackTrace(); Log.wtf(this.name, "Exception on FileOutputStream opening");}
                try {fos.write(data.getBytes());} catch (IOException e) {e.printStackTrace(); Log.wtf(this.name, "Exception on fos writing");}
                finish();
                break;
            case R.id.restore:
                deleteFile(FILENAME);
                assignDefaultData();
                break;
        }
    }

    private String writeString() {
        String user="", u = userName.getText().toString();
        char cur;

        for (int i=0; i<u.length(); i++) {
            cur = u.charAt(i);
            if (cur != '|')
                user += cur;
        }

        return sm.booleanToString(soundSwitch.isChecked())+"|"+
                sm.booleanToString(landscapeSwitch.isChecked())+"|"+
                sm.booleanToString(fullscreenSwitch.isChecked())+"|"+
                sm.booleanToString(blackAndWhiteSwitch.isChecked())+"|"+
                sm.booleanToString(originalModeSwitch.isChecked())+"|"+
                sm.booleanToString(timedSwitch.isChecked())+"|"+
                sm.booleanToString(unlimitedSwitch.isChecked())+"|"+
                userID.getText().toString()+"|"+
                user+"|";
    }

    private void readString(String data) {
        String currentString = "";
        char current;
        int j=0;
        for (int i=0; i<data.length(); i++) {
            current = data.charAt(i);
            if (current != '|')
                currentString += current;
            else {
                j = assignData(j, currentString);
                currentString = "";
            }
        }
    }

    private void assignDefaultData() {
        soundSwitch.setChecked(true);
        landscapeSwitch.setChecked(false);
        fullscreenSwitch.setChecked(false);
        blackAndWhiteSwitch.setChecked(false);
        originalModeSwitch.setChecked(false);
        timedSwitch.setChecked(false);
        unlimitedSwitch.setChecked(false);
        userID.setText("");
        userName.setText("");
    }

    private int assignData(int j, String data) {
        switch (j) {
            case 0:
                Log.wtf(this.name, "SoundSwitch: "+data);
                soundSwitch.setChecked(sm.stringToBoolean(data));
                break;
            case 1:
                Log.wtf(this.name, "LandscapeSwitch: "+data);
                landscapeSwitch.setChecked(sm.stringToBoolean(data));
                break;
            case 2:
                Log.wtf(this.name, "FullscreenSwitch: "+data);
                fullscreenSwitch.setChecked(sm.stringToBoolean(data));
                break;
            case 3:
                Log.wtf(this.name, "BlackAndWhiteSwitch: "+data);
                blackAndWhiteSwitch.setChecked(sm.stringToBoolean(data));
                break;
            case 4:
                Log.wtf(this.name, "OriginalModeSwitch: "+data);
                originalModeSwitch.setChecked(sm.stringToBoolean(data));
                break;
            case 5:
                Log.wtf(this.name, "TimedSwitch: "+data);
                timedSwitch.setChecked(sm.stringToBoolean(data));
                break;
            case 6:
                Log.wtf(this.name, "UnlimitedSwitch: "+data);
                unlimitedSwitch.setChecked(sm.stringToBoolean(data));
                break;
            case 7:
                Log.wtf(this.name, "UserID: "+data);
                userID.setText(data);
                break;
            case 8:
                Log.wtf(this.name, "UserName: "+data);
                userName.setText(data);
                break;
        }
        return j+1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.wtf(this.name, "onResume() llamado");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.wtf(this.name, "onPause() llamado");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.wtf(this.name, "onStop() llamado");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.wtf(this.name, "onDestroy() llamado");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.wtf(this.name, "onRestart() llamado");
    }
}
