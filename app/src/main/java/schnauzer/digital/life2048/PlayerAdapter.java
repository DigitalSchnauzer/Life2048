package schnauzer.digital.life2048;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rogelio on 12/12/2015.
 */
public class PlayerAdapter extends ArrayAdapter<Player> {
    Context context;
    int layoutResourceId;
    ArrayList<Player> data;

    public PlayerAdapter (Context context, int layoutResourceId, ArrayList<Player> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlayerHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PlayerHolder();

            holder.playerName = (TextView)row.findViewById(R.id.player_name);
            holder.playerStatus = (TextView)row.findViewById(R.id.player_status);

            row.setTag(holder);
        }
        else
        {
            holder = (PlayerHolder)row.getTag();
        }

        Player player = data.get(position);
        holder.playerName.setText(player.getName());
        holder.playerStatus.setText(player.getStatus());

        return row;
    }

    static class PlayerHolder
    {
        TextView playerName;
        TextView playerStatus;
    }
}
