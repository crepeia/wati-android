package com.example.daniela.progresso.ws;

import android.os.AsyncTask;
import android.util.Log;

import com.example.daniela.progresso.Entidade.User;

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

    /*public User verifica(String email){
        //data required by the web service
        String namespace = "http://ws.wati/";
        String method = "validate2";
        String url = "http://192.168.2.108:31932/wati/AppWebService";
        String actionURL = "";

        User user = new User();
        user.setEmail(email);


        // Creating a SOAP object
        SoapObject soap = new SoapObject(namespace, method);
        //Parameters
        soap.addProperty("email", user.getEmail());
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

        return user;
    }*/

    protected Boolean doInBackground(String... urls) {
        //data required by the web service
        String namespace = "http://ws.wati/";
        String method = "validate";
        String url = "http://10.5.107.169:8080/wati/AppWebService?wsdl";
        String actionURL = "";

        //data to be submited to the service
        String email = "cwsdanipereira@gmail.com";
        String password = "123";

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
            Log.i("WebService",  "Response: " + msg);

        } catch (IOException e) {
            Log.i("WebService", "exception11111111");
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean sendUserFacebook(User user) {
        //data required by the web service
        String namespace = "http://ws.wati/";
        String method = "retornaMedia";
        String url = "http://10.5.107.169:8080/wati/AppWebService?wsdl";
        String actionURL = "";

        //data to submited to the service
        User usr = new User();
        usr = user;

        // Creating a SOAP object
        SoapObject soap = new SoapObject(namespace, method);
        //Parameters
        soap.addProperty("user", usr);
        //Create an envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        Log.i("WebService", "Testing webService for sending information of smoked cigarettes.");

        //Creating a transport via HTTP
        HttpTransportSE httpTransport = new HttpTransportSE(url);


        try {
            httpTransport.debug = true;
            httpTransport.call(actionURL, envelope);
            Object msg = envelope.getResponse();
            Log.i("WebService",  "Response: " + msg);

        } catch (IOException e) {
            Log.i("WebService", "exception11111111");
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return true;
    }

    public double calculaMedia(String... urls) {
        //data required by the web service
        String namespace = "http://ws.wati/";
        String method = "retornaMedia";
        String url = "http://10.5.107.169:8080/wati/AppWebService?wsdl";
        String actionURL = "";

        //data to be submited to the service
        int cigarrosFumados = 12;

        // Creating a SOAP object
        SoapObject soap = new SoapObject(namespace, method);
        //Parameters
        soap.addProperty("cigarrosFumados", cigarrosFumados);
        //Create an envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        Log.i("WebService", "Testing webService for sending information of smoked cigarettes.");

        //Creating a transport via HTTP
        HttpTransportSE httpTransport = new HttpTransportSE(url);


        try {
            httpTransport.debug = true;
            httpTransport.call(actionURL, envelope);
            Object msg = envelope.getResponse();
            Log.i("WebService",  "Response: " + msg);

        } catch (IOException e) {
            Log.i("WebService", "exception11111111");
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return 0;
    }

    protected void onPostExecute() {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}