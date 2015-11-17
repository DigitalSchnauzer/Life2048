package schnauzer.digital.life2048;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by Rogelio on 10/20/2015.
 */
public class login_dialog  extends Activity {
    String name = "login_dialog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.wtf(this.name, "onCreate() llamado");

        setContentView(R.layout.login_dialog);
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
                finish();
            }
        });
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
