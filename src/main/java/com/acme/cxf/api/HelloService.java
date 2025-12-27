package com.acme.cxf.api;

import com.acme.cxf.model.Person;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

 @WebService(targetNamespace = "http://api.cxf.acme.com/")
public interface HelloService {

  @WebMethod(operationName = "SayHello")
  @WebResult(name = "greeting")
  String sayHello( @WebParam(name = "name") String name);

  @WebMethod(operationName = "FindPerson")
  @WebResult(name = "person")
  Person findPersonById( @WebParam(name = "id") String id);
}