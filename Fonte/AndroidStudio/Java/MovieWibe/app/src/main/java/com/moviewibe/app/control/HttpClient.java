package com.moviewibe.app.control;

import android.content.Context;
import android.os.AsyncTask;

import com.moviewibe.app.R;
import com.moviewibe.app.utils.Utilitarios;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient extends AsyncTask<String[], Void, JSONObject> {

    private OkHttpClient client = new OkHttpClient();

    private Context context;

    public HttpClient() {}

    public HttpClient(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Utilitarios.showSimpleProgressDialog(context,
                                             context.getString(R.string.alerta_title),
                                             context.getString(R.string.hint_aguarde), false);
    }

    @Override
    protected JSONObject doInBackground(String[]... params) {
        JSONObject jsonObject = null;
        Response response = null;

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, "{}");

        String parametros = params[0][0] + params[0][1] + params[0][2];

        if (params[0].length > 3) {
            parametros += params[0][3];
        }

        Request request = new Request.Builder()
        .url(parametros)
        .get()
        .build();

        try {
            response = client.newCall(request).execute();
            jsonObject = new JSONObject(response.body().string());
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        Utilitarios.removeSimpleProgressDialog();
    }

}
