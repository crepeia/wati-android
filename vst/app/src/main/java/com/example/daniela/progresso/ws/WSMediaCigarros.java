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

public class WSMediaCigarros extends AsyncTask<String, Void, Integer> {

    private Exception exception;
    public Date data;

    public WSMediaCigarros(Date data){
        this.data = data;
    }

    @Override
    protected Integer doInBackground(String... params) {
        String namespace = "http://ws.wati/";
        String method = "mediaGrafico";
        String url = "http://192.168.25.50:8080/wati/AppWebService?wsdl";
        String actionURL = "";

        //dados que serão submetidos ao servidor
        Date dt = data;

        //Imprimindo os dados para teste
        System.out.println("ws data media: " + dt);

        //criando o objeto SOAP
        SoapObject soap = new SoapObject(namespace, method);

        //parametros
        soap.addProperty("data", dt.getTime());

        //Imprimindo os dados para teste
        System.out.println("ws data media: " + dt.getTime());

        //Criando o envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        Log.i("webServiceMedia", "Testando o webservice");

        //Criando um transporte via HTTP
        HttpTransportSE httpTransportSE = new HttpTransportSE(url);
        try{
            httpTransportSE.debug = true;
            httpTransportSE.call(actionURL, envelope);
            Object msg = envelope.getResponse();
            Log.i("WebService",  "Response Media: " + msg);

            SoapObject soapObject = (SoapObject) msg;

            int k = Integer.parseInt(String.valueOf(soapObject.getProperty("media")));
            System.out.println("SoapObject k media: " + k);


            String x;
            x = msg.toString();
            System.out.println("SoapObject media: " + x);
            int media = Integer.valueOf(x);
            System.out.println("media ws: " + media);
            return k;
            //int media = 0;
            //System.out.println("media media: "+ media);
            //media = Integer.valueOf(msg.toString());
            //System.out.println("media media 2: "+ media);
            //System.out.println("media ws: " + media);
            //return media;
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
