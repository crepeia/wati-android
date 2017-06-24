package com.example.daniela.progresso.ws;

import android.os.AsyncTask;
import android.util.Log;

import com.example.daniela.progresso.Entidade.Cigarros;
import com.example.daniela.progresso.Entidade.User;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Date;

import static com.example.daniela.progresso.UserManager.user;

/**
 * Created by daniela on 07/06/17.
 */

public class WSCigarro extends AsyncTask<String, Void, Boolean> {

    private Exception exception;
    public Date data;
    public int cigarro;
    public String email;

    public WSCigarro(Date data, int cigarros, String email){
        this.data = data;
        this.cigarro = cigarros;
        this.email = email;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String namespace = "http://ws.wati/";
        String method = "enviaCigarros";
        String url = "http://192.168.25.50:8080/wati/AppWebService?wsdl";
        String actionURL = "";

        //dados que serão submetidos ao servidor
        Cigarros cigarros = new Cigarros();
        cigarros.setCigarrosDiario(cigarro);
        cigarros.setDate(data);

        //Imprimindo os dados para teste
        System.out.println("ws cigarros: " + cigarro);
        System.out.println("ws data: " + data);
        System.out.println("ws user: " + email);
        //System.out.println("ws user: " + user);

        //criando o objeto SOAP
        SoapObject soap = new SoapObject(namespace, method);

        //parametros
        //soap.addProperty("cigarros", cigarros.getCigarrosDiario());
        //soap.addProperty("data", cigarros.getDate().getTime());
        //soap.addProperty("email", cigarros.getUser().getEmail());
        soap.addProperty("data", data.getTime());
        soap.addProperty("cigarros", cigarro);
        soap.addProperty("email", email);

        //Imprimindo os dados para teste
        System.out.println("ws cigarros: " + cigarro);
        System.out.println("ws data: " + data.getTime());
        System.out.println("ws user: " + email);

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
