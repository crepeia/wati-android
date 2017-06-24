package com.example.daniela.progresso.ws;

import android.os.AsyncTask;
import android.util.Log;

import com.example.daniela.progresso.Entidade.User;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by daniela on 15/06/17.
 */

public class WSPonto extends AsyncTask<String, Void, Boolean> {

    private Exception exception;
    public int pDica;
    public int pCadastroApp;
    public int pRegistro;
    public int pNaoFumar;
    public int pVisitaSite;
    public String email;

    public WSPonto(int pDica, int pCadastroApp, int pRegistro, int pNaoFumar, int pVisitaSite, String email){
        this.pDica = pDica;
        this.pCadastroApp = pCadastroApp;
        this.pRegistro = pRegistro;
        this.pNaoFumar = pNaoFumar;
        this.pVisitaSite = pVisitaSite;
        this.email = email;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String namespace = "http://ws.wati/";
        String method = "enviaPontos";
        String url = "http://192.168.25.50:8080/wati/AppWebService?wsdl";
        String actionURL = "";

        //criando o objeto SOAP
        SoapObject soap = new SoapObject(namespace, method);
        //dados submetidos
        soap.addProperty("pDica", pDica);
        soap.addProperty("pCadastroApp", pCadastroApp);
        soap.addProperty("pRegistro", pRegistro);
        soap.addProperty("pNaoFumar", pNaoFumar);
        soap.addProperty("pVisitaSite", pVisitaSite);
        soap.addProperty("email", email);

        //Imprimindo os dados para teste
        System.out.println("pontos" +  pDica);
        System.out.println("pCadastroApp" + pCadastroApp);
        System.out.println("pRegistro"+ pRegistro);
        System.out.println("pNaoFumar"+ pNaoFumar);
        System.out.println("pVisitaSite"+ pVisitaSite);
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
