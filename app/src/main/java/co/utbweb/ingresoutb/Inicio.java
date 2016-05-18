package co.utbweb.ingresoutb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.*;

public class Inicio extends AppCompatActivity {

    String tokenTemp, code, pass;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final Intent intent_name = new Intent(), deVuelta = new Intent();
        intent_name.setClass(getApplicationContext(), IngresoActivity.class);
        intent_name.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent_name.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent_name.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        RequestQueue queue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        tokenTemp = getDefaults("TokenGuardado", getApplicationContext());
        if (tokenTemp != "") {
            code = getDefaults("Codigo", getApplicationContext());
            pass = getDefaults("Password", getApplicationContext());
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, "http://raoapi.utbvirtual.edu.co:8082/token",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // response
                            try {
                                Log.d("Response", response.getString("token"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(intent_name);
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof AuthFailureError) {
                                Toast.makeText(Inicio.this, "Error de autenticacion", LENGTH_SHORT).show();
                                startActivity(deVuelta);
                            }else
                                setContentView(R.layout.activity_inicio);
                                Toast.makeText(Inicio.this, "Error de conexion", LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", code);
                    params.put("password", pass);

                    return params;
                }
            };
            queue.add(postRequest);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public static String getDefaults(String jorge, Context pepo) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(pepo);
        return preferences.getString(jorge, null);
    }

    private void setDefaults(String jorge, String estas, Context pepo) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(pepo);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(jorge, estas);
        editor.commit();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Inicio Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://co.utbweb.ingresoutb/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Inicio Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://co.utbweb.ingresoutb/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        public int codigo;
        Context context;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection connection = null;
            try {
                //Create connection
                String urlParameters = "username=" + URLEncoder.encode(params[1], "UTF-8") +
                        "&password=" + URLEncoder.encode(params[2], "UTF-8");

                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                PrintWriter out = new PrintWriter(connection.getOutputStream());
                out.print(urlParameters);
                out.close();

                //Leer la respuesta del servidor
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                    StringBuilder sb = new StringBuilder();
                    String output;
                    while ((output = br.readLine()) != null)
                        sb.append(output);

                    JSONObject jsonObject = new JSONObject(sb.toString());
                    String token = jsonObject.getString("token");
                    System.out.print("Token Guardado: " + token);
                    setDefaults("Codigo", code, getApplicationContext());
                    setDefaults("Password", pass, getApplicationContext());
                    setDefaults("TokenGuardado", token, getApplicationContext());
                    Log.e("Respuesta", sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //return response.toString();
                this.codigo = connection.getResponseCode();
                Log.e("Respuesta", "ID = " + connection.getResponseCode());


                return 1;

            } catch (Exception e) {
                Log.e("onExecute", "Error de app");
                codigo = -1;
                e.printStackTrace();
                return null;

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (codigo == 200) {
                Log.e("onPostExecute", "on PostExec");

                SharedPreferences settings = getSharedPreferences("TokenStorage", 0);
                Log.e("onPostExecute", "TokenSaved:" + settings.getString("token", ""));
                Log.e("onPostExecute", "IdSaved:" + settings.getString("id", ""));

                Intent intent_name = new Intent(), deVuelta = new Intent();
                intent_name.setClass(getApplicationContext(), IngresoActivity.class);
                deVuelta.setClass(getApplicationContext(), login.class);
                intent_name.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_name.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent_name.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                if (tokenTemp != "") {
                    if (tokenTemp == getDefaults("TokenGuardado", getApplicationContext())) {
                        startActivity(intent_name);
                    } else
                        startActivity(deVuelta);
                }
                startActivity(intent_name);
            } else {
                switch (codigo) {
                    case (401):
                        makeText(Inicio.this, "Error de autenticacion", LENGTH_SHORT).show();
                        break;
                    case (-1):
                        makeText(Inicio.this, "Error de conexion", LENGTH_SHORT).show();
                }
            }

        }
    }

}
