package com.acme.cxf.security;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Callback handler pour valider les credentials UsernameToken
 * Accepte uniquement : username=student, password=secret123
 */
public class ServerPasswordCallback implements CallbackHandler {
    
    // Base de données des utilisateurs autorisés
    private static final Map<String, String> USERS = new HashMap<>();
    
    static {
        USERS.put("student", "secret123");
        // Ajouter d'autres utilisateurs si nécessaire
        // USERS.put("admin", "admin456");
    }
    
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) callback;
                String username = pc.getIdentifier();
                String password = USERS.get(username);
                
                if (password != null) {
                    pc.setPassword(password);
                    System.out.println("✅ Authentification réussie pour l'utilisateur : " + username);
                } else {
                    System.err.println("❌ Utilisateur inconnu : " + username);
                    throw new IOException("Utilisateur inconnu : " + username);
                }
            } else {
                throw new UnsupportedCallbackException(callback, "Callback non supporté");
            }
        }
    }
}
