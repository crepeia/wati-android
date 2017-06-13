package com.example.daniela.progresso.ws;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Date;

/**
 * Created by daniela on 10/06/17.
 */

public class WSFacebook extends AsyncTask<String, Void, Boolean> {

    private Exception exception;
    public String name;
    public String email;
    public String gender;

    public WSFacebook(String name, String email, String gender){
        this.name = name;
        this.email = email;
        this.gender = gender;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String namespace = "http://ws.wati/";
        String method = "usuariosFacebook";
        String url = "http://192.168.0.105:8080/wati/AppWebService?wsdl";
        String actionURL = "";

        //criando o objeto SOAP
        SoapObject soap = new SoapObject(namespace, method);
        //dados submetidos
        soap.addProperty("name", name);
        soap.addProperty("email", email);
        soap.addProperty("gender", gender);

        //Imprimindo os dados para teste
        System.out.println("name: " + name);
        System.out.println("email: " + email);
        System.out.println("gender: " + gender);

        //Criando o envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        Log.i("webServiceUser", "Testando o webservice");

        //Criando um transporte via HTTP
        HttpTransportSE httpTransportSE = new HttpTransportSE(url);
        try{
            httpTransportSE.debug = true;
            httpTransportSE.call(actionURL, envelope);
            Object msg = envelope.getResponse();
            Log.i("WebService",  "Response: " + msg);
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
