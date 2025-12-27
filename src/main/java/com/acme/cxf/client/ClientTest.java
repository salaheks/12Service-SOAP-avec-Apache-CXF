package com.acme.cxf.client;

import com.acme.cxf.api.HelloService;
import com.acme.cxf.model.Person;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Client Java pour tester le service SOAP HelloService
 * Supporte WS-Security avec UsernameToken
 */
public class ClientTest {
    
    private static final String SERVICE_URL = "http://localhost:8080/services/hello";
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("  🧪 CLIENT JAVA - TEST DU SERVICE SOAP");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        // Test 1 : Sans sécurité (doit échouer si sécurité activée)
        System.out.println("📋 TEST 1 : Appel sans authentification");
        System.out.println("─────────────────────────────────────────────────────────");
        try {
            HelloService serviceNonSecurise = createClient(false);
            String result = serviceNonSecurise.sayHello("Test");
            System.out.println("❌ ERREUR : Le service a répondu sans authentification !");
            System.out.println("Réponse : " + result);
        } catch (Exception e) {
            System.out.println("✅ ATTENDU : Accès refusé sans authentification");
            System.out.println("Message : " + e.getMessage());
        }
        
        System.out.println("\n");
        
        // Test 2 : Avec authentification (student/secret123)
        System.out.println("📋 TEST 2 : Opération SayHello avec authentification");
        System.out.println("─────────────────────────────────────────────────────────");
        try {
            HelloService service = createClient(true);
            String greeting = service.sayHello("Lachgar");
            System.out.println("✅ Succès !");
            System.out.println("Requête  : sayHello(\"Lachgar\")");
            System.out.println("Réponse  : " + greeting);
        } catch (Exception e) {
            System.err.println("❌ Échec : " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n");
        
        // Test 3 : FindPerson avec authentification
        System.out.println("📋 TEST 3 : Opération FindPerson avec authentification");
        System.out.println("─────────────────────────────────────────────────────────");
        try {
            HelloService service = createClient(true);
            Person person = service.findPersonById("P-001");
            System.out.println("✅ Succès !");
            System.out.println("Requête  : findPersonById(\"P-001\")");
            System.out.println("Réponse  : Person {");
            System.out.println("             id   = " + person.getId());
            System.out.println("             name = " + person.getName());
            System.out.println("             age  = " + person.getAge());
            System.out.println("           }");
            
            // Validation JAXB
            if (person.getId() != null && person.getName() != null && person.getAge() > 0) {
                System.out.println("\n✅ Sérialisation JAXB validée : tous les champs présents");
            }
        } catch (Exception e) {
            System.err.println("❌ Échec : " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n");
        
        // Test 4 : Différents noms
        System.out.println("📋 TEST 4 : Tests avec différents noms");
        System.out.println("─────────────────────────────────────────────────────────");
        try {
            HelloService service = createClient(true);
            String[] names = {"Alice", "Bob", "Charlie"};
            for (String name : names) {
                String greeting = service.sayHello(name);
                System.out.println("  " + name + " → " + greeting);
            }
            System.out.println("✅ Tous les tests réussis");
        } catch (Exception e) {
            System.err.println("❌ Échec : " + e.getMessage());
        }
        
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("  ✅ VALIDATION COMPLÈTE TERMINÉE");
        System.out.println("═══════════════════════════════════════════════════════════");
    }
    
    /**
     * Crée un client proxy pour le service HelloService
     * @param withSecurity true pour ajouter WS-Security UsernameToken
     * @return Proxy du service
     */
    private static HelloService createClient(boolean withSecurity) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(HelloService.class);
        factory.setAddress(SERVICE_URL);
        
        if (withSecurity) {
            // Configuration WS-Security côté client
            Map<String, Object> outProps = new HashMap<>();
            outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
            outProps.put(WSHandlerConstants.USER, "student");
            outProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");
            outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ClientPasswordCallback.class.getName());
            
            WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
            factory.getOutInterceptors().add(wssOut);
        }
        
        return (HelloService) factory.create();
    }
}
