package com.acme.cxf;

import com.acme.cxf.impl.HelloServiceImpl;
import com.acme.cxf.security.ServerPasswordCallback;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import java.util.HashMap;
import java.util.Map;

public class Server {
  public static void main(String[] args) {
    String address = "http://localhost:8080/services/hello";
    JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
    factory.setServiceClass(HelloServiceImpl.class);
    factory.setAddress(address);
    
    // Configuration WS-Security
    Map<String, Object> inProps = new HashMap<>();
    inProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
    inProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");
    inProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ServerPasswordCallback.class.getName());
    
    WSS4JInInterceptor wssIn = new WSS4JInInterceptor(inProps);
    factory.getInInterceptors().add(wssIn);
    
    factory.create();
    System.out.println("🔐 Serveur sécurisé avec WS-Security (UsernameToken)");
    System.out.println("WSDL: " + address + "?wsdl");
    System.out.println("Credentials acceptés : student/secret123");
  }
}