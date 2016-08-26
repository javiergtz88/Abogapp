package Models;

import android.net.SSLCertificateSocketFactory;
import android.util.Log;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by saul on 5/2/15.
 */
public class HTTPBaseModel {

    protected static final String url_root = "http://app.appbogado.com.mx";

    //protected static final String url_root = "http://sortigoza3.ddns.net/"; // Production server
    //protected static final String url_root = "http://10.0.2.2:3000"; // Local test
    //protected static final String url_root = "http://192.168.1.93:3000";

    private static final Integer read_timeout = 15000;
    private static final Integer connect_timeout = 20000;
    private static final Boolean ssl_enabled = false;
    private static final String TAG = "HTTPBaseModel";


    protected String request(String http_verb, String myurl) throws Exception{
        if(ssl_enabled)
            return https_request(http_verb, myurl);
        else
            return http_request(http_verb, myurl);
    }

    protected String request(String http_verb, String myurl, JSONObject json) throws Exception{
        if(ssl_enabled)
            return https_request(http_verb, myurl, json);
        else
            return http_request(http_verb, myurl, json);
    }

    protected String request(String http_verb, String myurl, JSONObject json, int timeout) throws Exception{
        if(ssl_enabled)
            return https_request(http_verb, myurl, json); // TBD
        else
            return http_request(http_verb, myurl, json, timeout);
    }


    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    protected String https_request(String http_verb, String myurl) throws Exception {
        InputStream is = null;
        validate_http_verb(http_verb);
        StringBuilder the_url = new StringBuilder();
        the_url.append(url_root);
        the_url.append(myurl);
        //Log.d(TAG, "url used: " + the_url.toString());
        try {
            URL url = new URL(the_url.toString());
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            configure_timeouts(conn);
            // method type
            conn.setRequestMethod(http_verb);
            conn.setDoInput(true);
            // SSL certificate
            configure_ssl(conn);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            //Log.d(TAG, "https_request.response: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    protected String https_request(String http_verb, String myurl, JSONObject json) throws Exception {
        InputStream is = null;
        validate_http_verb(http_verb);
        StringBuilder the_url = new StringBuilder();
        the_url.append(url_root);
        the_url.append(myurl);
        //Log.d(TAG, "https_request; url used: " + the_url.toString());
        try {
            URL url = new URL(the_url.toString());
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            configure_timeouts(conn);
            // method type
            conn.setRequestMethod(http_verb);
            conn.setRequestProperty("content-type", "application/json");
            conn.setRequestProperty("ACCEPT", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // SSL certificate
            configure_ssl(conn);
            // JSON post data
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bw.write(json.toString());
            bw.flush();
            bw.close();
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            //Log.d(TAG, "https_request.response: " + response);
            //Log.d(TAG, "https_request.response_message: " + conn.getResponseMessage());
            if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK){
                is = conn.getInputStream();
            }else{
                is = conn.getErrorStream();
            }

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            //Log.d(TAG, "https_request contentAsString: " + contentAsString);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        }catch (Exception e){
            e.printStackTrace();
            //Log.e(TAG, "https_request: " + e.getLocalizedMessage());
            return null;
        }finally {
            if (is != null) {
                is.close();
            }
        }
    }

    protected String http_request(String http_verb, String myurl) throws Exception {
        InputStream is = null;
        validate_http_verb(http_verb);
        StringBuilder the_url = new StringBuilder();
        the_url.append(url_root);
        the_url.append(myurl);
        //Log.d(TAG, "url_used: " + the_url.toString());
        //Log.d(TAG, "http_request; url used: " + the_url.toString());
        try {
            URL url = new URL(the_url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            configure_timeouts(conn);
            conn.setRequestMethod(http_verb);
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "http_request.response: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    protected String http_request(String http_verb, String myurl, JSONObject json) throws Exception {
        return  http_request(http_verb, myurl, json, 0);
    }

    protected String http_request(String http_verb, String myurl, JSONObject json, int timeout) throws Exception {
        InputStream is = null;
        validate_http_verb(http_verb);
        StringBuilder the_url = new StringBuilder();
        the_url.append(url_root);
        the_url.append(myurl);
        Log.d(TAG, "http_request; url used: " + http_verb + " " + the_url.toString());
        try {
            URL url = new URL(the_url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(timeout == 0)
                configure_timeouts(conn);
            else
                configure_timeouts(conn, timeout);
            // method type
            conn.setRequestMethod(http_verb);
            conn.setRequestProperty("content-type", "application/json");
            conn.setRequestProperty("ACCEPT", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // JSON post data
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bw.write(json.toString());
            bw.flush();
            bw.close();

            Log.d(TAG, "http_request; JSON sent: " + json.toString());
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "http_request.response: " + response);
            Log.d(TAG, "http_request.response_message: " + conn.getResponseMessage());
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                is = conn.getInputStream();
            }else{
                is = conn.getErrorStream();
            }

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            Log.d(TAG, "https_response: contentAsString: " + contentAsString);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "http_request: " + e.getLocalizedMessage());
            return null;
        }finally {
            if (is != null) {
                is.close();
            }
        }
    }

    protected void validate_http_verb(String http_verb) throws Exception{
        final String[] VALUES = new String[] {"GET", "POST", "PUT", "DELETE"};
        if(!Arrays.asList(VALUES).contains(http_verb))
            throw new Exception("Http Verb is nor RESTful");
    }

    private void configure_timeouts(HttpsURLConnection conn) {
        conn.setReadTimeout(read_timeout /* milliseconds */);
        conn.setConnectTimeout(connect_timeout /* milliseconds */);
    }
    private void configure_timeouts(HttpURLConnection conn) {
        conn.setReadTimeout(read_timeout /* milliseconds */);
        conn.setConnectTimeout(connect_timeout /* milliseconds */);
    }
    private void configure_timeouts(HttpURLConnection conn, int timeout) {
        conn.setReadTimeout(timeout /* milliseconds */);
        conn.setConnectTimeout(timeout /* milliseconds */);
    }

    private void configure_ssl(HttpsURLConnection conn) {
        conn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
        conn.setHostnameVerifier(new AllowAllHostnameVerifier());
    }

    private void not_implemented_exception() throws Exception{
        throw new Exception("Not implemented");
    }

    // Reads an InputStream and converts it to a String.
    protected String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);
        return responseStrBuilder.toString();
    }
}
