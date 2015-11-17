package schnauzer.digital.life2048;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    static final int USER_SCORE_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;

    static final String name = "MainActivity";
    static final String FILENAME = "settings_data";

    PackageManager pm;

    Button photoButton;
    //Button newGameBtn;
    Button scoreBtn;
    TextView userScore;
    TextView currentScore;
    ImageView userPhoto;

    TextView soundSwitch;
    TextView landscapeSwitch;
    TextView fullscreenSwitch;
    TextView blackAndWhiteSwitch;
    TextView originalModeSwitch;
    TextView timedSwitch;
    TextView unlimitedSwitch;
    EditText userID;
    EditText userName;

    MyStorageManager sm = new MyStorageManager();

    public static int count;
    GestureOverlayView gestureOverlayView;

    static float x1, y1, x2, y2, xDifference, yDifference;

    GameController gameController;
    public int size = 4;
    public int score;

    TextView tileViews[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf("Life2048", "onCreate() llamado");

        // ORIENTATION

        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().heightPixels){
            //Toast.makeText(this,"Screen switched to Landscape mode",Toast.LENGTH_SHORT).show();
            //setContentView(R.layout.activity_main_landscape);
            setContentView(R.layout.activity_main);
        } else {
            //Toast.makeText(this,"Screen switched to Portrait mode",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);
        }

        // ORIENTATION END

        // DISPLAY LOGIN?

        if (count<1) {
            Intent intent = new Intent(this, login_dialog.class);
            startActivity(intent);
        }

        count++;
        // DISPLAY LOGIN? END

        // INPUT CODE

        pm = this.getPackageManager();

        photoButton = (Button) findViewById(R.id.drawer_photo_button);
        //newGameBtn = (Button) findViewById(R.id.newGameButton);
        scoreBtn = (Button) findViewById(R.id.scoreButton);
        userScore = (TextView) findViewById(R.id.userScore);
        currentScore = (TextView) findViewById(R.id.currentScoreText);
        gestureOverlayView = (GestureOverlayView) findViewById(R.id.gestureOverlayView);
        userPhoto = (ImageView) findViewById(R.id.userPhoto);

        soundSwitch = (TextView) findViewById(R.id.sound_On);
        landscapeSwitch = (TextView) findViewById(R.id.landscape_On);
        fullscreenSwitch = (TextView) findViewById(R.id.fullscreen_On);
        blackAndWhiteSwitch = (TextView) findViewById(R.id.blackAndWhite_On);
        originalModeSwitch = (TextView) findViewById(R.id.originalMode_On);
        timedSwitch = (TextView) findViewById(R.id.timed_On);
        unlimitedSwitch = (TextView) findViewById(R.id.unlimited_On);
        userID = (EditText) findViewById(R.id.drawer_id);
        userName = (EditText) findViewById(R.id.drawer_username);

        //newGameBtn.setOnClickListener(this);
        scoreBtn.setOnClickListener(this);
        photoButton.setOnClickListener(this);
        gestureOverlayView.setOnTouchListener(this);

        // INPUT CODE END

        // SET TILES AND GAME CONTROLLER

        tileViews = new TextView[size][size];
        int resId;

        for (int j=0; j<size; j++) {
            for (int i=0; i<size; i++) {
                resId = getResources().getIdentifier("tile" + i +"" + j, "id", getPackageName());
                tileViews[i][j] = (TextView) findViewById(resId);
            }
        }

        gameController = new GameController(size, tileViews, this);
        updateView();

        // SET TILES AND GAME CONTROLLER END

        // NAVIGATION DRAWER CODE

        mTitle = mDrawerTitle = getTitle().toString();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                FileInputStream fis = null;

                try {fis = openFileInput(FILENAME);} catch (FileNotFoundException e) {e.printStackTrace(); assignDefaultData();}
                //try {fis.close();} catch (IOException e) {e.printStackTrace(); Log.wtf(this.name, "Exception on FileInputStream closing");}
                StringBuilder sb = new StringBuilder();
                try{
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    fis.close();
                } catch(OutOfMemoryError om){om.printStackTrace(); Log.wtf(name, "Out of Memory Exception on FileInputStream closing");
                } catch (IOException e) {e.printStackTrace(); Log.wtf(name, "IOException on FileInputStream closing");
                } catch(Exception ex){ex.printStackTrace(); Log.wtf(name, "Exception on FileInputStream closing");
                }
                String result = sb.toString();
                if (result.length() > 0)
                    readString(result);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // NAVIGATION DRAWER CODE END
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void updateView() {

        for (int j=0; j<gameController.grid.size; j++) {
            for (int i=0; i<gameController.grid.size; i++) {
                if (gameController.grid.cells[i][j] == false) {
                    tileViews[i][j].setText("");
                    tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTileDefault));
                } else {
                    tileViews[i][j].setText(gameController.getTileAtPosition(i, j).value + " ");
                    switch (gameController.getTileAtPosition(i, j).value) {
                        case 2:
                            tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTile0002));
                            break;
                        case 4:
                            tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTile0004));
                            break;
                        case 8:
                            tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTile0008));
                            break;
                        case 16:
                            tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTile0016));
                            break;
                        case 32:
                            tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTile0032));
                            break;
                        case 64:
                            tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTile0064));
                            break;
                        case 128:
                            tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTile0128));
                            break;
                        case 256:
                            tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTile0256));
                            break;
                        case 512:
                            tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTile0512));
                            break;
                        case 1024:
                            tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTile1024));
                            break;
                        case 2048:
                            tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTile2048));
                            break;
                        default:
                            tileViews[i][j].setBackgroundColor(getResources().getColor(R.color.colorTileMore));
                            break;
                    }

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.new_game_actionbar: case R.id.new_game_menu_item:
                gameController = new GameController(size, tileViews, this);
                updateView();
                return true;
            case R.id.settings_menu_item:
                Intent intent = new Intent(this, settings_dialog.class);
                startActivity(intent);
                return true;
            case R.id.about_menu_item:
                createCustomDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scoreButton:
                Intent intent = new Intent(this, currentScore_dialog.class);
                intent.putExtra("score", score);
                startActivityForResult(intent, USER_SCORE_REQUEST);
                break;
            case R.id.drawer_photo_button:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA))
                    return;
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
        }
    }

    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: {
                x1 = event.getX();
                y1 = event.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                x2 = event.getX();
                y2 = event.getY();

                xDifference = Math.abs(x2)-Math.abs(x1);
                yDifference = Math.abs(y1)-Math.abs(y2);

                if (Math.abs(xDifference) > Math.abs(yDifference)) {
                    if (xDifference > 0) {
                        //Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
                        gameController.moveTiles(1);
                    } else if (xDifference < 0) {
                        //Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
                        gameController.moveTiles(3);
                    }
                } else {
                    if (yDifference > 0) {
                        //Toast.makeText(MainActivity.this, "Up", Toast.LENGTH_SHORT).show();
                        gameController.moveTiles(0);
                    } else if (yDifference < 0){
                        //Toast.makeText(MainActivity.this, "Down", Toast.LENGTH_SHORT).show();
                        gameController.moveTiles(2);
                    }
                }
                if (gameController.gameOver) {
                    Intent intent = new Intent(this, currentScore_dialog.class);
                    intent.putExtra("score", score);
                    startActivityForResult(intent, USER_SCORE_REQUEST);
                    //gameController = new GameController(size, tileViews, this);
                }
                updateView();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                break;
            }
        }
        return true;
    }

    public void updateScore () {
        currentScore.setText(score+"");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == USER_SCORE_REQUEST) {
            if (resultCode == RESULT_OK) {
                userScore.setText(data.getStringExtra("user"));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Score was not saved", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            userPhoto.setImageBitmap(imageBitmap);
        }
    }

    private void createCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        Button dialogButton = (Button) dialog.findViewById(R.id.close_button);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.wtf("Life2048", "onStart() llamado");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.wtf("Life2048", "onResume() llamado");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.wtf("Life2048", "onPause() llamado");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.wtf("Life2048", "onStop() llamado");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.wtf("Life2048", "onDestroy() llamado");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.wtf("Life2048", "onRestart() llamado");
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
        String data = "Off";
        soundSwitch.setText("On");
        landscapeSwitch.setText(data);
        fullscreenSwitch.setText(data);
        blackAndWhiteSwitch.setText(data);
        originalModeSwitch.setText(data);
        timedSwitch.setText(data);
        unlimitedSwitch.setText(data);
        userID.setText("");
        userName.setText("");
    }

    private int assignData(int j, String data) {
        switch (j) {
            case 0:
                Log.wtf(this.name, "SoundSwitch: "+data);
                soundSwitch.setText(data);
                break;
            case 1:
                Log.wtf(this.name, "LandscapeSwitch: "+data);
                landscapeSwitch.setText(data);
                break;
            case 2:
                Log.wtf(this.name, "FullscreenSwitch: "+data);
                fullscreenSwitch.setText(data);
                break;
            case 3:
                Log.wtf(this.name, "BlackAndWhiteSwitch: "+data);
                blackAndWhiteSwitch.setText(data);
                break;
            case 4:
                Log.wtf(this.name, "OriginalModeSwitch: "+data);
                originalModeSwitch.setText(data);
                break;
            case 5:
                Log.wtf(this.name, "TimedSwitch: "+data);
                timedSwitch.setText(data);
                break;
            case 6:
                Log.wtf(this.name, "UnlimitedSwitch: "+data);
                unlimitedSwitch.setText(data);
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
}