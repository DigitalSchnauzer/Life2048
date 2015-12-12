package schnauzer.digital.life2048;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class players_dialog extends Activity {
    static boolean active = false;
    static private ListView playersListView;
    private Button goBack;

    static protected ArrayList<Player> playersList = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        active = true;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.players_dialog);

        playersListView = (ListView) findViewById(R.id.playersListView);
        goBack = (Button) findViewById(R.id.goBack);

        PlayerAdapter playerAdapter = new PlayerAdapter(this, R.layout.item_player, playersList);
        playersListView.setAdapter(playerAdapter);

        ServiceManager.playersDialog = this;
        ServiceManager.getListOfPlayers();
        Toast.makeText(getBaseContext(), "Loading list of players...", Toast.LENGTH_SHORT).show();

        playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player player = playersList.get(position);
                if (player.getStatus().equalsIgnoreCase("D")) {
                    MainActivity.host = true;
                    MainActivity.otherid = Integer.toString(player.getId());
                    MainActivity.multiAlreadyStarted = false;
                    ServiceManager.invitePlayer(Integer.parseInt(MainActivity.userid), player.getId());
                    Toast.makeText(getBaseContext(), "Inviting " + player.getName() + "...", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    protected void updateAdapter () {
        PlayerAdapter playerAdapter = new PlayerAdapter(this, R.layout.item_player, playersList);
        playersListView.setAdapter(playerAdapter);
    }
}
