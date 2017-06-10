package com.example.daniela.progresso.ws;

import android.os.AsyncTask;
import android.util.Log;

import com.example.daniela.progresso.Entidade.Cigarros;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Date;

/**
 * Created by daniela on 07/06/17.
 */

public class WebServiceMediaCigarros extends AsyncTask<String, Void, Float> {

    private Exception exception;
    public Date data;

    public WebServiceMediaCigarros(Date data){
        this.data = data;
    }

    @Override
    protected Float doInBackground(String... params) {
        String namespace = "http://ws.wati/";
        String method = "validate";
        String url = "http://192.168.0.103:8080/wati/AppWebService?wsdl";
        String actionURL = "";

        //dados que serão submetidos ao servidor
        Date dt = data;

        //Imprimindo os dados para teste
        System.out.println("ws data: " + dt);

        //criando o objeto SOAP
        SoapObject soap = new SoapObject(namespace, method);

        //parametros
        soap.addProperty("data", dt);

        //Imprimindo os dados para teste
        System.out.println("ws data: " + dt);

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

        return null;
    }
}
