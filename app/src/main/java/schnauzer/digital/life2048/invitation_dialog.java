package schnauzer.digital.life2048;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class invitation_dialog extends Activity implements View.OnClickListener {
    static protected Boolean active = false;
    Button join;
    Button dismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        active = true;
        setContentView(R.layout.invitation_dialog);

        join = (Button) findViewById(R.id.join);
        dismiss = (Button) findViewById(R.id.dismiss);

        join.setOnClickListener(this);
        dismiss.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join:
                ServiceManager.acceptRequest(Integer.parseInt(MainActivity.userid));
                finish();
                break;
            case R.id.dismiss:
                ServiceManager.releasePlayer(Integer.parseInt(MainActivity.userid));
                finish();
            default:

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }
}
