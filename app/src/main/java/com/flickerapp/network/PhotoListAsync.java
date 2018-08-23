package com.flickerapp.network;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.flickerapp.utility.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Anup Lal on 21-08-2018.
 */

public class PhotoListAsync extends AsyncTask<String, Void, JSONObject> {

    private final OnTaskFinishListener mListener;

    public PhotoListAsync(OnTaskFinishListener listener) {
        this.mListener = listener;
    }


    @Override
    protected JSONObject doInBackground(String... strings) {
        String queryParam = strings[0];
        return fetchPhotoList(queryParam);
    }

    @Override
    protected void onPostExecute(JSONObject jsonReader) {
        super.onPostExecute(jsonReader);
        mListener.onSuccess(jsonReader);
    }

    private JSONObject fetchPhotoList(final String query) {
        try {
            final URL httpEndpoint = new URL(Constants.URL + query);
            final HttpsURLConnection myConnection
                    = (HttpsURLConnection) httpEndpoint.openConnection();
            myConnection.setRequestMethod("GET");
            myConnection.setRequestProperty("Accept",
                    "application/json");
            if (myConnection.getResponseCode() == 200) {
                InputStream inputStream = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(inputStream, "UTF-8");
                final JsonReader jsonReader = new JsonReader(responseBodyReader);
                final BufferedReader streamReader = new BufferedReader(responseBodyReader);
                final StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                jsonReader.close();
                myConnection.disconnect();
                return new JSONObject(responseStrBuilder.toString());

            } else {
                mListener.onError();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public interface OnTaskFinishListener {

        void onSuccess(JSONObject reader);

        void onError();
    }
}
