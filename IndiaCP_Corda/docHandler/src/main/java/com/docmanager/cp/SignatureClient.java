package com.docmanager.cp;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import java.net.URL;

/**
 * Created by Electrania.com on 12/10/2016.
 */
public class SignatureClient {

    public static void main(String[] args) {
        try {

            String UrlString = "http://secmsgdemo.emudhra.com:8282/emsigner/services/dsverifyWS?wsdl";  //
            String nameSpaceUri = "urn:emudhra";
            String serviceName = "dsverifyWS";
            String portName = "8282";

            System.out.println("UrlString = " + UrlString);
            URL signWsdlUrl = new URL(UrlString);

            ServiceFactory serviceFactory =
                    ServiceFactory.newInstance();

            Service signService =
                    serviceFactory.createService(signWsdlUrl,
                            new QName(nameSpaceUri, serviceName));

            /*DSVerifyWS ws= new DSVerifyWSProxy().getDSVerifyWS();
            String result= ws.signPdf("test1", base64encodedpdfdata, "", "400,170,500,250","all", "test", "test");

            dynamicproxy.HelloIF myProxy =
                    (dynamicproxy.HelloIF)
                            signService.getPort(
                                    new QName(nameSpaceUri, portName),
                                    dynamicproxy.HelloIF.class);

            System.out.println(myProxy.sayHello("Buzz"));
        */
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}