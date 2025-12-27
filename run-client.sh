#!/bin/bash
cd "$(dirname "$0")"

echo "🧪 Exécution du client Java de test..."
echo ""

# Construire le classpath
CP="target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)"

# Exécuter le client
java -cp "$CP" com.acme.cxf.client.ClientTest
