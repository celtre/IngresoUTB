package co.utbweb.ingresoutb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Inicio extends AppCompatActivity {

    String tokenTemp, code, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        tokenTemp = getDefaults("TokenGuardado", getApplicationContext());
        if (tokenTemp != "") {
            code=getDefaults("Codigo",getApplicationContext());
            pass=getDefaults("Password",getApplicationContext());
            AsyncHttpTask a = new AsyncHttpTask();
            a.execute("http://raoapi.utbvirtual.edu.co:8082/token", code, pass);
            Log.e("quelocura", Integer.toString(a.codigo));
        }
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
                    while ((output = br.readLine()) != null)
                        sb.append(output);

                    JSONObject jsonObject = new JSONObject(sb.toString());
                    String  token = jsonObject.getString("token");
                    System.out.print("Token Guardado: " + token);
                    setDefaults("Codigo", code, getApplicationContext());
                    setDefaults("Password", pass, getApplicationContext());
                    setDefaults("TokenGuardado",token,getApplicationContext());
                    Log.e("Respuesta", sb.toString());
                } catch (Exception e){
                    e.printStackTrace();
                }

                //return response.toString();
                this.codigo = connection.getResponseCode();
                Log.e("Respuesta", "ID = "+connection.getResponseCode());



                return 1;

            } catch (Exception e) {
                Log.e("onExecute", "Error de app");
                codigo = -1;
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
            if (codigo == 200) {
                Log.e("onPostExecute", "on PostExec");

                SharedPreferences settings = getSharedPreferences("TokenStorage", 0);
                Log.e("onPostExecute", "TokenSaved:" + settings.getString("token", ""));
                Log.e("onPostExecute", "IdSaved:" + settings.getString("id", ""));

                Intent intent_name = new Intent(),deVuelta=new Intent();
                intent_name.setClass(getApplicationContext(), IngresoActivity.class);
                deVuelta.setClass(getApplicationContext(),login.class);
                intent_name.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_name.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent_name.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                if(tokenTemp!=""){
                    if(tokenTemp==getDefaults("TokenGuardado", getApplicationContext())){
                        startActivity(intent_name);
                    }else
                        startActivity(deVuelta);
                }
                startActivity(intent_name);
            }else {
                switch (codigo){
                    case (401):
                        Toast.makeText(Inicio.this, "Error de autenticacion", Toast.LENGTH_SHORT).show();
                        break;
                    case (-1):
                        Toast.makeText(Inicio.this, "Error de conexion", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

}
