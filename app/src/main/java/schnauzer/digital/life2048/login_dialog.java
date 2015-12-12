package schnauzer.digital.life2048;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Rogelio on 10/20/2015.
 */
public class login_dialog  extends Activity {
    String name = "login_dialog";
    EditText username;
    EditText userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.wtf(this.name, "onCreate() llamado");

        setContentView(R.layout.login_dialog);

        username = (EditText) findViewById(R.id.username);
        userId = (EditText) findViewById(R.id.userId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.wtf(this.name, "onStart() llamado");

        final Button cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.wtf(name, "Cancel clicked");
                finish();
            }
        });
        final Button loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.wtf(name, "Log In clicked");
                String user = username.getText().toString();
                int id = Integer.parseInt(userId.getText().toString());
                Log.wtf("login_dialog", "User: "+user+" | ID: "+id);
                ServiceManager.savePlayer(id, user);
                String data = writeString();

                FileOutputStream fos = null;
                try {fos = openFileOutput(MainActivity.USERDATA_FILENAME, Context.MODE_PRIVATE);} catch (FileNotFoundException e) {e.printStackTrace(); Log.wtf(getLocalClassName(), "Exception on FileOutputStream opening");}
                try {fos.write(data.getBytes());} catch (IOException e) {e.printStackTrace(); Log.wtf(getLocalClassName(), "Exception on fos writing");}
                finish();
            }
        });
    }

    private String writeString() {
        String user="", u = username.getText().toString();
        char cur;

        for (int i=0; i<u.length(); i++) {
            cur = u.charAt(i);
            if (cur != '|')
                user += cur;
        }

        return user+"|"+userId.getText().toString()+"|";
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
