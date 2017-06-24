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
 * Created by daniela on 17/06/17.
 */

public class WSPosicao extends AsyncTask<String, Void, Integer> {

    private Exception exception;
    public String email;

    public  WSPosicao(String email){
        this.email = email;
    }

    @Override
    protected Integer doInBackground(String... params) {
        String namespace = "http://ws.wati/";
        String method = "calculaPosicao";
        String url = "http://192.168.25.50:8080/wati/AppWebService?wsdl";
        String actionURL = "";

        //criando o objeto SOAP
        SoapObject soap = new SoapObject(namespace, method);
        //dados submetidos
        soap.addProperty("email", email);

        //Imprimindo os dados para teste
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

            String x;
            x = msg.toString();
            System.out.println("SoapObject x: " + x);
            int num = Integer.valueOf(x);
            System.out.println("SoapObject num: " + num);
            //SoapObject soapObject = (SoapObject) msg;
            System.out.println("SoapObject: " + msg);
            //num = (int) soapObject.getProperty("posicao");

            System.out.println("num" + num);
            return num;
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;

    }
}
