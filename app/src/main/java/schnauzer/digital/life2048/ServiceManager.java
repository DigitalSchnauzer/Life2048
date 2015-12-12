package schnauzer.digital.life2048;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rogelio on 11/19/2015.
 */
public class ServiceManager {

    static String name = "ServiceManager";
    static String baseUrl = "http://www.xpesd.com/DEV/Services/Game2048/api/Game2048/";
    static OnlineGameController gameController;
    static MainActivity currentActivity;
    static players_dialog playersDialog;

    protected OnlineGameController getGameController() {
        return gameController;
    }

    protected static void setActivity (MainActivity act) {
        currentActivity = act;
    }

    protected static void setGameController(OnlineGameController gameControl) {
        gameController = gameControl;
    }

    protected static void savePlayer (int id, String name) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Matricula", Integer.toString(id));
        } catch (JSONException e) {
            Log.wtf(name, "Matricula JSON failed.");
        }
        try {
            jsonObject.put("Name", name);
        } catch (JSONException e) {
            Log.wtf(name, "Name JSON failed.");
        }
        try {
            jsonObject.put("Status", "D");
        } catch (JSONException e) {
            Log.wtf(name, "Status JSON failed.");
        }
        executeRequest(baseUrl+"SavePlayer", jsonObject);
    }

    /*protected static void savePlayer (int id, String name, char status) {
        executeRequest(baseUrl+"SavePlayer?Matricula={"+id+"}&Name={"+name+"}&Status={"+status+"}");
        executeRequest(baseUrl+"SavePlayer?Player={json}");
    }*/

    protected static void getListOfPlayers () {
        //ArrayList<Player> players = new ArrayList<Player>();
        String stringUrl = baseUrl+"GetPlayers";
        executeRequest(stringUrl);
        //return players;
    }

    protected static void getStatus (int id) {
        //Player.Status status = null;
        executeRequest(baseUrl+"GetStatus?matricula={"+id+"}");
        //return status;
    }

    protected static void invitePlayer (int myId, int id) { // invite player
        executeRequest(baseUrl+"InvitePlayer?matriculaP1={"+myId+"}&matriculaP2={"+id+"}");
    }

    protected static void acceptRequest (int id) {
        executeRequest(baseUrl+"AcceptInvitation?matricula={"+id+"}");
    }

    protected static void releasePlayer (int id) { // deny invitation or release when game is over
        executeRequest(baseUrl+"ReleasePlayers?matricula={"+id+"}");
    }

    protected static void setStartStatus (int myId, int id, int x1, int y1, int value1, int x2, int y2, int value2) { // set start status for second player
        gameController.setStartStatus(x1, y1, value1, x2, y2, value2);
        executeRequest(baseUrl+"SetInitialBoard?MatriculaP1={"+myId+"}&MatriculaP2={"+id+"}&X1={"+x1+"}&Y1={"+y1+"}&Value1={"+value1+"}&X2={"+x2+"}&Y2={"+y2+"}&Value2={"+value2+"}");
    }

    protected static void getStartStatus (int id) {
        //int[] startStatus = new int[6];
        executeRequest(baseUrl+"GetInitialBoard?matricula={"+id+"}");
        //return startStatus;
    }

    protected static void setTurn (int movement, int x, int y, int value, int id) { // termina el turno
        gameController.myTurn = false;
        executeRequest(baseUrl+"SetTurn?Matricula={"+id+"}&X1={"+x+"}&Y1={"+y+"}&Value={"+value+"}&Direction={"+movement+"}");
    }

    protected static void getTurn (int id) {
        int movement, x, y, value;
        movement = 0;
        x = 0;
        y = 0;
        value = 0;
        executeRequest(baseUrl+"GetTurn?matricula={"+id+"}");
        gameController.moveTiles(movement, x, y, value);
        gameController.myTurn = true;
    }

    protected static void checkConnection () {

    }

    private static void executeRequest (String url) {
        ConnectivityManager connMgr = (ConnectivityManager) currentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.wtf(currentActivity.getLocalClassName(), "Network connection available; sending "+url);
            new DownloadWebpageTask().execute(url);
        } else {
            Toast.makeText(currentActivity, "No network connection available.", Toast.LENGTH_SHORT).show();
            Log.wtf(currentActivity.getLocalClassName(), "No network connection available.");
        }
    }

    private static void executeRequest (String url, JSONObject jsonObject) {
        ConnectivityManager connMgr = (ConnectivityManager) currentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.wtf(currentActivity.getLocalClassName(), "Network connection available; sending " + url);
            DownloadWebpageTask download = new DownloadWebpageTask();
            download.jsonObject = jsonObject;
            download.execute(url);
        } else {
            Toast.makeText(currentActivity, "No network connection available.", Toast.LENGTH_SHORT).show();
            Log.wtf(currentActivity.getLocalClassName(), "No network connection available.");
        }
    }
}

class DownloadWebpageTask extends AsyncTask<String, Void, String> {
    private String urlString;
    private String method;
    protected JSONObject jsonObject;

    private void getMethod () {
        int i=ServiceManager.baseUrl.length(); // 56
        String method = new String();
        char current=urlString.charAt(i);
        while (i<urlString.length() && current!='/' && current!='?') {
            current = urlString.charAt(i);
            if (current!='/' && current!='?')
                method += current;
            else
            break;
            i++;
        }
        Log.i("ServiceManager", "Method obtained from URL is: "+method);
        this.method = method;
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        // result after connection is done
        //Toast.makeText(ServiceManager.currentActivity, "Connection Succesful", Toast.LENGTH_SHORT).show();
        Log.wtf("ServiceManager", "Result received: " + result);

        switch (method) {
            case "GetTurn":
                break;
            case "SetTurn":
                break;
            case "GetPlayers":
                JSONArray players = null;
                try {
                    players = new JSONArray(result);
                    Log.d("DownloadWebPageTask", "players JSONArray: "+players.toString());
                    Log.d("DownloadWebPageTask", "players JSONArray.length(): "+players.length());
                    for (int i=0; i<players.length(); i++) {
                        JSONObject player = players.getJSONObject(i);
                        if (players_dialog.active && ServiceManager.playersDialog != null) {
                            players_dialog.playersList.add(new Player(player.getString("Name"), player.getString("Matricula"), player.getString("Status")));
                        }
                    }
                    if (players_dialog.active && ServiceManager.playersDialog != null) {
                        ServiceManager.playersDialog.updateAdapter();
                    }
                } catch (JSONException e) {
                    Log.wtf("DownloadWebPageTask", "Creation of players in OnPostExecute's GetPlayers failed.");
                    Log.d("DownloadWebPageTask", "Exception: players JSONArray: "+players.toString());
                    e.printStackTrace();
                }
                break;
            case "InvitePlayer":
                break;
            case "GetStatus":
                JSONObject status = null;
                String stat = new String();
                if (!result.equalsIgnoreCase("")) {
                    try {
                        status = new JSONObject(result);
                        stat = status.toString();
                        Log.d("DownloadWebPageTask", "status JSONArray: " + stat);
                        if (!MainActivity.userid.isEmpty()) {
                            ServiceManager.getStatus(Integer.parseInt(MainActivity.userid));
                        }
                    } catch (JSONException e) {
                        if (status != null)
                            Log.wtf("DownloadWebPageTask", "Exception: status JSONArray: " + status.toString());
                    }
                }
                Log.d("DownloadWebPageTask", "result: " + result + " | invitation active? "+invitation_dialog.active.toString());
                if (result.equalsIgnoreCase("\"I\"") && !invitation_dialog.active) {
                    ServiceManager.currentActivity.startInvitationDialog();
                }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ServiceManager.getStatus(Integer.parseInt(MainActivity.userid));
                        }
                    }, 5000);
                break;
            case "AcceptInvitation":
                break;
            case "ReleasePlayer":
                break;
            case "SetInitialBoard":
                break;
            case "GetInitialBoard":
                break;
            case "SavePlayer":
                break;
            default:
                Log.wtf("ServiceManager", "Method from the URL not recognized.");
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        this.urlString = myurl;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 1;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            getMethod();

            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");

            if (method.equalsIgnoreCase("GetTurn") || method.equalsIgnoreCase("GetPlayers") || method.equalsIgnoreCase("GetStatus") || method.equalsIgnoreCase("GetInitialBoard")) {
                conn.setRequestMethod("GET");
                Log.i("ServiceManager", "GET method");
                //else if (method.equalsIgnoreCase("SetTurn") || method.equalsIgnoreCase("InvitePlayers") || method.equalsIgnoreCase("AcceptInvitation") || method.equalsIgnoreCase("ReleasePlayers") || method.equalsIgnoreCase("SetInitialBoard") || method.equalsIgnoreCase("SavePlayer"))
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                Log.i("ServiceManager", "POST method");

                if (jsonObject != null) {
                    OutputStream os = conn.getOutputStream();
                    os.write(jsonObject.toString().getBytes("UTF-8"));
                    os.flush();
                    os.close();
                }
            }

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.wtf("ServiceManager", "The response from the service is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        String result = "";
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        //reader.read(buffer);
        //return new String(buffer);
        while(reader.read(buffer) >= 0)
        {
            result = result + (new String(buffer));
            buffer = new char[len];
        }
        return result;
    }
}
