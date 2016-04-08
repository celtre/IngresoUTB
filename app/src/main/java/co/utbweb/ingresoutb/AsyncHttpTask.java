package co.utbweb.ingresoutb;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * Created by jgavir on 15/04/16.
 * Clase para conexion asincronica con el servidor
 */
public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

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
            connection = (HttpURLConnection)url.openConnection();
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
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }


                Log.e("Respuesta",sb.toString());
            } catch (Exception e){
                e.printStackTrace();
            }

            //return response.toString();
            Log.e("Respuesta", "ID = "+connection.getResponseCode());
            return 1;

        } catch (Exception e) {
            Log.e("onExecute", "Error de app");
            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(Integer result) {

      /*/  Log.e("onPostExecute", "on PostExec");

        SharedPreferences settings = getSharedPreferences("TokenStorage", 0);
        Log.e("onPostExecute", "TokenSaved:" + settings.getString("token", ""));
        Log.e("onPostExecute", "IdSaved:" + settings.getString("id", ""));

        Intent intent_name = new Intent();
        intent_name.setClass(getApplicationContext(), MainActivity.class);
        startActivity(intent_name);

        if (LoginActivity.this.pd != null) {
            LoginActivity.this.pd.dismiss();
        }
      */
    }
}
