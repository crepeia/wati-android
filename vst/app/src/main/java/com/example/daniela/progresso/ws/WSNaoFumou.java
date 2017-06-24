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

/**
 * Created by daniela on 18/06/17.
 */

public class WSNaoFumou  extends AsyncTask<String, Void, Boolean> {

    private Exception exception;
    String email;

    public WSNaoFumou(String email){
        this.email = email;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        String namespace = "http://ws.wati/";
        String method = "enviaNaoFumou";
        String url = "http://192.168.25.50:8080/wati/AppWebService?wsdl";
        String actionURL = "";

        //criando o objeto SOAP
        SoapObject soap = new SoapObject(namespace, method);
        //dados submetidos
        soap.addProperty("email", email);

        System.out.println("email" +  email);

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
