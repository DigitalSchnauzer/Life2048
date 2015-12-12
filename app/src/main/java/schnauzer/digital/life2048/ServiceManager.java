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


    protected static void getListOfPlayers () {
        String stringUrl = baseUrl+"GetPlayers";
        executeRequest(stringUrl);
    }

    protected static void getStatus (int id) {
        executeRequest(baseUrl+"GetStatus?matricula={"+id+"}");
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MatriculaP1", Integer.toString(myId));
        } catch (JSONException e) {
            Log.wtf(name, "MatriculaP1 JSON failed.");
        }
        try {
            jsonObject.put("MatriculaP2", Integer.toString(id));
        } catch (JSONException e) {
            Log.wtf(name, "MatriculaP2 JSON failed.");
        }
        try {
            jsonObject.put("X1", x1);
        } catch (JSONException e) {
            Log.wtf(name, "X1 JSON failed.");
        }
        try {
            jsonObject.put("Y1", y1);
        } catch (JSONException e) {
            Log.wtf(name, "Y1 JSON failed.");
        }
        try {
            jsonObject.put("Value1", value1);
        } catch (JSONException e) {
            Log.wtf(name, "Value1 JSON failed.");
        }
        try {
            jsonObject.put("X2", x2);
        } catch (JSONException e) {
            Log.wtf(name, "X2 JSON failed.");
        }
        try {
            jsonObject.put("Y2", y2);
        } catch (JSONException e) {
            Log.wtf(name, "Y2 JSON failed.");
        }
        try {
            jsonObject.put("Value2", value2);
        } catch (JSONException e) {
            Log.wtf(name, "Value2 JSON failed.");
        }
        //gameController.setStartStatus(x1, y1, value1, x2, y2, value2);
        executeRequest(baseUrl+"SetInitialBoard", jsonObject);
    }

    protected static void getStartStatus (int id) {
        //int[] startStatus = new int[6];
        executeRequest(baseUrl+"GetInitialBoard?matricula={"+id+"}");
        //return startStatus;
    }

    protected static void setTurn (int movement, int x, int y, int value, int id) { // termina el turno
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Matricula", Integer.toString(id));
        } catch (JSONException e) {
            Log.wtf(name, "Matricula JSON failed.");
        }
        try {
            jsonObject.put("X", x);
        } catch (JSONException e) {
            Log.wtf(name, "X JSON failed.");
        }
        try {
            jsonObject.put("Y", y);
        } catch (JSONException e) {
            Log.wtf(name, "Y JSON failed.");
        }
        try {
            jsonObject.put("Value", value);
        } catch (JSONException e) {
            Log.wtf(name, "Value JSON failed.");
        }
        try {
            jsonObject.put("Direction", Integer.toString(movement));
        } catch (JSONException e) {
            Log.wtf(name, "Direction JSON failed.");
        }
        executeRequest(baseUrl+"SetTurn", jsonObject);
    }

    protected static void getTurn (int id) {
        executeRequest(baseUrl+"GetTurn?matricula={"+id+"}");
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
                JSONObject board = null;
                try {
                    board = new JSONObject(result);
                    Log.d("DownloadWebPageTask", "players JSONArray: "+board.toString());
                    Log.d("DownloadWebPageTask", "players JSONArray.length(): "+board.length());

                    String matricula = board.getString("Matricula");
                    int x = board.getInt("X1");
                    int y = board.getInt("Y1");
                    int value = board.getInt("Value");
                    int movement = board.getInt("Direction");

                    ServiceManager.currentActivity.executeTurn(matricula, x, y, value, movement);
                } catch (JSONException e) {
                    Log.wtf("DownloadWebPageTask", "Creation of board in OnPostExecute's GetInitialBoard failed.");
                    Log.d("DownloadWebPageTask", "Exception: board JSONObject: "+board.toString());
                    e.printStackTrace();
                }

                if (MainActivity.online) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ServiceManager.getTurn(Integer.parseInt(MainActivity.userid));
                        }
                    }, 5000);
                }
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
                /*JSONObject status = null;
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
                }*/
                Log.d("DownloadWebPageTask", "result: " + result + " | invitation active? "+invitation_dialog.active.toString() + " | multiAlreadyStarted? "+MainActivity.multiAlreadyStarted + " | host? "+MainActivity.host);
                if (result.equalsIgnoreCase("\"I\"") && !invitation_dialog.active) {
                    ServiceManager.currentActivity.startInvitationDialog();
                } else if (result.equalsIgnoreCase("\"P\"") && !MainActivity.multiAlreadyStarted) {
                    ServiceManager.currentActivity.startMultiPlayerGame();
                    if (MainActivity.host)
                        ServiceManager.currentActivity.myTurn = true;
                    else
                        ServiceManager.currentActivity.myTurn = false;
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
            case "ReleasePlayers":
                break;
            case "SetInitialBoard":
                break;
            case "GetInitialBoard":
                board = null;
                try {
                    board = new JSONObject(result);
                    Log.d("DownloadWebPageTask", "players JSONArray: "+board.toString());
                    Log.d("DownloadWebPageTask", "players JSONArray.length(): "+board.length());

                    int x1 = board.getInt("X1");
                    int y1 = board.getInt("Y1");
                    int value1 = board.getInt("Value1");
                    int x2 = board.getInt("X2");
                    int y2 = board.getInt("Y2");
                    int value2 = board.getInt("Value2");

                    ServiceManager.currentActivity.setMyStartStatus(x1, y1, value1, x2, y2, value2);
                } catch (JSONException e) {
                    Log.wtf("DownloadWebPageTask", "Creation of board in OnPostExecute's GetInitialBoard failed.");
                    Log.d("DownloadWebPageTask", "Exception: board JSONObject: "+board.toString());
                    e.printStackTrace();
                }
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
