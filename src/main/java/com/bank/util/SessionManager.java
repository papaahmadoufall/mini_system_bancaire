package com.bank.util;

import com.bank.model.Client;

public class SessionManager {
    private static Client currentClient;

    public static void setCurrentClient(Client client) {
        currentClient = client;
    }

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void clearSession() {
        currentClient = null;
    }
} 