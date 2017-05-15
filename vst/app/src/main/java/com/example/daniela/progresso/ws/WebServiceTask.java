package com.example.daniela.progresso.ws;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by hedersb on 5/15/17.
 */

public class WebServiceTask extends AsyncTask<String, Void, Boolean> {

    private Exception exception;

    protected Boolean doInBackground(String... urls) {
        //data required by the web service
        String namespace = "http://ws.wati/";
        String method = "validate";
        String url = "http://192.168.2.108:31932/wati/AppWebService";
        String actionURL = "";

        //data to be submited to the service
        String email = "hedersb@yahoo.com.br";
        String password = "1234";

        // Creating a SOAP object
        SoapObject soap = new SoapObject(namespace, method);
        //Parameters
        soap.addProperty("email", email);
        soap.addProperty("password", password);
        //Create an envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        Log.i("WebService", "Testing the webservice for validating the authentication of the users.");

        //Creating a transport via HTTP
        HttpTransportSE httpTransport = new HttpTransportSE(url);


        try {
            httpTransport.debug = true;
            httpTransport.call(actionURL, envelope);
            Object msg = envelope.getResponse();

            Log.d("WebService", "Response: " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return true;
    }

    protected void onPostExecute() {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}