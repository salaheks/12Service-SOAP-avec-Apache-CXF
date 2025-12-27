#!/bin/bash
cd "$(dirname "$0")"
mvn clean package -q
CP="target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)"
echo "Starting SOAP Server..."
echo "WSDL will be available at: http://localhost:8080/services/hello?wsdl"
java -cp "$CP" com.acme.cxf.Server
