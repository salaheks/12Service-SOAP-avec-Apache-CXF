#!/bin/bash
# Script d'arrêt du serveur SOAP

echo "🛑 Arrêt du serveur SOAP..."

# Trouver le PID du processus Java exécutant Server
PID=$(ps aux | grep "com.acme.cxf.Server" | grep -v grep | awk '{print $2}')

if [ -z "$PID" ]; then
    echo "❌ Aucun serveur en cours d'exécution"
    exit 1
else
    echo "🔍 Serveur trouvé (PID: $PID)"
    kill $PID
    sleep 2
    
    # Vérifier si le processus est bien arrêté
    if ps -p $PID > /dev/null 2>&1; then
        echo "⚠️  Le serveur résiste, force l'arrêt..."
        kill -9 $PID
    fi
    
    echo "✅ Serveur arrêté avec succès"
fi
