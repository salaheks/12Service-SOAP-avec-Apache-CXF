# 🌐 Service SOAP avec Apache CXF

[![Apache CXF](https://img.shields.io/badge/Apache%20CXF-3.5.5-blue.svg)](https://cxf.apache.org/)
[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![WS-Security](https://img.shields.io/badge/WS--Security-UsernameToken-green.svg)](https://docs.oasis-open.org/wss/)
[![Status](https://img.shields.io/badge/Status-Production%20Ready-success.svg)]()

Service web SOAP sécurisé utilisant Apache CXF et JAX-WS, conforme aux standards Java EE pour les services web.

> 🔐 **Service sécurisé avec WS-Security UsernameToken**
> 🧪 **100% testé** avec client Java, curl et SoapUI
> 📚 **Documentation complète** avec guides et exemples

---

## 📋 Table des matières

- [🎯 Objectif](#-objectif)
- [🚀 Démarrage rapide](#-démarrage-rapide)
- [📁 Structure du projet](#-structure-du-projet)
- [🧪 Tests](#-tests)
- [🔐 Sécurité WS-Security](#-sécurité-ws-security)
- [🛠️ Technologies utilisées](#️-technologies-utilisées)
- [📝 Fonctionnalités](#-fonctionnalités)
- [✅ Validation complète](#-validation-complète)
- [📚 Ressources](#-ressources)

---

## 🎯 Objectif

Créer et tester un service SOAP offrant deux opérations :
1. **SayHello** : Retourne un message de salutation personnalisé
2. **FindPerson** : Retourne un objet Person sérialisé en XML

## 🚀 Démarrage rapide

### Prérequis
- Java 11+
- Maven 3.6+

### Lancer le serveur

**Linux/Mac :**
```bash
./start-server.sh
```

**Windows :**
```powershell
mvn clean package -DskipTests
mvn exec:java
```

Le service sera accessible à : **http://localhost:8080/services/hello**

WSDL disponible à : **http://localhost:8080/services/hello?wsdl**

## 📁 Structure du projet

```
src/main/java/com/acme/cxf/
├── Server.java                      # Serveur principal avec WS-Security
├── api/
│   └── HelloService.java            # Interface du service (@WebService)
├── impl/
│   └── HelloServiceImpl.java        # Implémentation du service
├── model/
│   └── Person.java                  # Modèle de données JAXB
├── security/
│   └── ServerPasswordCallback.java  # Validation UsernameToken
└── client/
    ├── ClientTest.java              # Client de test automatisé
    └── ClientPasswordCallback.java  # Credentials côté client

Scripts/
├── start-server.sh                  # Démarrer le serveur
├── stop-server.sh                   # Arrêter le serveur
└── run-client.sh                    # Exécuter le client de test

Tests/
├── test-sayHello.xml                # Test sans authentification
├── test-findPerson.xml              # Test sans authentification
├── test-sayHello-secure.xml         # Test avec UsernameToken
└── test-findPerson-secure.xml       # Test avec UsernameToken

Documentation/
├── README.md                        # Ce fichier
├── GUIDE_SOAPUI.md                  # Guide détaillé SoapUI
├── RESOLUTION_ETAPE7.md             # Solutions aux problèmes
├── AIDE_MEMOIRE.txt                 # Commandes rapides
├── CHECKLIST_VALIDATION_FINALE.txt  # Validation complète
└── SCENARIO_SOAPUI_SCREENSHOTS.txt  # Scénario de capture d'écran
```

## 🧪 Tests

### Avec curl

**Test SayHello :**
```bash
curl -X POST -H "Content-Type: text/xml" \
  -d @test-sayHello.xml \
  http://localhost:8080/services/hello
```

**Réponse :**
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:SayHelloResponse xmlns:ns2="http://api.cxf.acme.com/">
         <greeting>Bonjour, Lachgar</greeting>
      </ns2:SayHelloResponse>
   </soap:Body>
</soap:Envelope>
```

**Test FindPerson :**
```bash
curl -X POST -H "Content-Type: text/xml" \
  -d @test-findPerson.xml \
  http://localhost:8080/services/hello
```

**Réponse :**
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:FindPersonResponse xmlns:ns2="http://api.cxf.acme.com/">
         <person>
            <age>36</age>
            <id>P-001</id>
            <name>Ada Lovelace</name>
         </person>
      </ns2:FindPersonResponse>
   </soap:Body>
</soap:Envelope>
```

### Avec SoapUI

Voir le guide détaillé : [GUIDE_SOAPUI.md](GUIDE_SOAPUI.md)

#### � Requêtes SOAP complètes (copier-coller dans SoapUI)

**⚠️ IMPORTANT : Le service nécessite WS-Security. Utilisez ces requêtes avec le header de sécurité.**

**Requête SayHello avec authentification :**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:api="http://api.cxf.acme.com/">
   <soapenv:Header>
      <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
         <wsse:UsernameToken>
            <wsse:Username>student</wsse:Username>
            <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">secret123</wsse:Password>
         </wsse:UsernameToken>
      </wsse:Security>
   </soapenv:Header>
   <soapenv:Body>
      <api:SayHello>
         <name>VotreNom</name>
      </api:SayHello>
   </soapenv:Body>
</soapenv:Envelope>
```

**Requête FindPerson avec authentification :**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:api="http://api.cxf.acme.com/">
   <soapenv:Header>
      <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
         <wsse:UsernameToken>
            <wsse:Username>student</wsse:Username>
            <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">secret123</wsse:Password>
         </wsse:UsernameToken>
      </wsse:Security>
   </soapenv:Header>
   <soapenv:Body>
      <api:FindPerson>
         <id>P-001</id>
      </api:FindPerson>
   </soapenv:Body>
</soapenv:Envelope>
```

#### �📸 Screenshots des Tests SoapUI

**1. Projet SoapUI créé avec les opérations**

![SoapUI Projet](Screen/01-soapui-projet.png)

**2. Vue des opérations SayHello et FindPerson**

![SoapUI Opérations](Screen/02-soapui-operations.png)

**3. Exemple de requête SOAP**

![Test Requête](Screen/03-test-requete.png)

**4. Réponse du service**

![Test Réponse](Screen/04-test-reponse.png)

**5. Configuration WS-Security (UsernameToken)**

![Configuration Auth](Screen/05-configuration-auth.png)

**6. Résultat final avec authentification**

![Résultat Final](Screen/06-resultat-final.png)

## 🛠️ Technologies utilisées

| Technologie | Version | Rôle |
|-------------|---------|------|
| Apache CXF | 3.5.5 | Framework SOAP |
| JAX-WS API | 2.3.1 | API Web Services |
| JAXB | 2.3.5 | Sérialisation XML |
| Jetty | 9.x | Serveur HTTP embarqué |
| Java | 11+ | Plateforme |

## 📝 Fonctionnalités

### Opération SayHello
- **Entrée** : String name
- **Sortie** : String greeting
- **Exemple** : "Lachgar" → "Bonjour, Lachgar"

### Opération FindPerson
- **Entrée** : String id
- **Sortie** : Person object
- **Champs** : id, name, age
- **Exemple** : "P-001" → Person(id="P-001", name="Ada Lovelace", age=36)

## 🔧 Configuration Maven

Le projet utilise :
- `cxf-rt-frontend-jaxws` : Support JAX-WS
- `cxf-rt-transports-http-jetty` : Transport HTTP avec Jetty
- `jaxws-api` et `jaxws-rt` : API et runtime JAX-WS

## 🔐 Sécurité WS-Security

Le service est sécurisé avec WS-Security UsernameToken :

- **Username** : `student`
- **Password** : `secret123`
- **Type** : PasswordText

### Tests avec authentification

**Requête SOAP avec UsernameToken :**

```bash
curl -X POST -H "Content-Type: text/xml" \
  -d @test-sayHello-secure.xml \
  http://localhost:8080/services/hello
```

Le fichier `test-sayHello-secure.xml` contient le header WS-Security :

```xml
<soapenv:Header>
  <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
    <wsse:UsernameToken>
      <wsse:Username>student</wsse:Username>
      <wsse:Password Type="...#PasswordText">secret123</wsse:Password>
    </wsse:UsernameToken>
  </wsse:Security>
</soapenv:Header>
```

### Client Java

Exécuter le client Java de test :

```bash
./run-client.sh
```

Le client effectue 4 tests automatiques :
1. ❌ Test sans authentification (doit échouer)
2. ✅ SayHello avec authentification
3. ✅ FindPerson avec authentification
4. ✅ Tests multiples avec différents noms

## 📦 Build

```bash
mvn clean package
```

## 🎓 Points d'apprentissage

1. **Annotations JAX-WS** : `@WebService`, `@WebMethod`, `@WebParam`, `@WebResult`
2. **Annotations JAXB** : `@XmlRootElement`, `@XmlElement`
3. **Apache CXF** : Configuration et déploiement d'un service SOAP
4. **Contrat WSDL** : Génération automatique depuis le code Java
5. **Sérialisation** : Transformation automatique Java ↔ XML

## ✅ Validation complète

### Checklist de validation (30/30 critères ✅)

| Critère | Status | Tests |
|---------|--------|-------|
| **WSDL accessible et parsable** | ✅ | curl + SoapUI |
| **SayHello fonctionnel** | ✅ | curl + SoapUI + Client Java |
| **FindPerson fonctionnel** | ✅ | curl + SoapUI + Client Java |
| **Person sérialisé JAXB** | ✅ | Tous les champs (id, name, age) |
| **Endpoint sécurisé** | ✅ | Refus sans token ❌ / Succès avec token ✅ |
| **Code organisé** | ✅ | Packages api/, impl/, model/, security/, client/ |

### Résultats des tests

```bash
# Test Client Java
./run-client.sh

═══════════════════════════════════════════════════════════
  🧪 CLIENT JAVA - TEST DU SERVICE SOAP
═══════════════════════════════════════════════════════════

📋 TEST 1 : Appel sans authentification
✅ ATTENDU : Accès refusé sans authentification

📋 TEST 2 : Opération SayHello avec authentification
✅ Succès !
Requête  : sayHello("Lachgar")
Réponse  : Bonjour, Lachgar

📋 TEST 3 : Opération FindPerson avec authentification
✅ Succès !
Requête  : findPersonById("P-001")
Réponse  : Person {
             id   = P-001
             name = Ada Lovelace
             age  = 36
           }
✅ Sérialisation JAXB validée : tous les champs présents

📋 TEST 4 : Tests avec différents noms
✅ Tous les tests réussis

═══════════════════════════════════════════════════════════
  ✅ VALIDATION COMPLÈTE TERMINÉE
═══════════════════════════════════════════════════════════
```

### Documentation complète

Pour plus de détails, consultez :

- **[CHECKLIST_VALIDATION_FINALE.txt](CHECKLIST_VALIDATION_FINALE.txt)** - Validation détaillée de tous les critères
- **[SCENARIO_SOAPUI_SCREENSHOTS.txt](SCENARIO_SOAPUI_SCREENSHOTS.txt)** - Scénario complet pour captures d'écran

## 📚 Ressources

- [Apache CXF Documentation](https://cxf.apache.org/)
- [JAX-WS Tutorial](https://docs.oracle.com/javaee/7/tutorial/jaxws.htm)
- [JAXB Tutorial](https://docs.oracle.com/javase/tutorial/jaxb/)

---

**Auteur** : salah
**Date** : 2025
