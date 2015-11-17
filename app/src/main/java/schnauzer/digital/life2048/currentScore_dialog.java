package schnauzer.digital.life2048;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class currentScore_dialog extends Activity {

    TextView scoreTxtView;
    EditText userNameTxtView;
    Button okBtn;

    Intent intent;
    String currentScore;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.current_score_dialog);

        intent = getIntent();
        currentScore = ""+intent.getIntExtra("score", 2048);

        scoreTxtView = (TextView) findViewById(R.id.currentScore);
        userNameTxtView = (EditText) findViewById(R.id.yourName);
        okBtn = (Button) findViewById(R.id.okBtn);

        scoreTxtView.setText(currentScore);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = ""+userNameTxtView.getText();
                if (userName.length()>=4) {
                    intent.putExtra("user", userName + ": " + currentScore);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(currentScore_dialog.this, "Name should at least be 4 characters long", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
