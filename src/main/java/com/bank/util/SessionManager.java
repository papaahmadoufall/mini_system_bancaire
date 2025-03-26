package com.bank.util;

import com.bank.model.Admin;
import com.bank.model.Client;

public class SessionManager {
    private static Client currentClient;
    private static Admin currentAdmin;

    public static void setCurrentClient(Client client) {
        currentClient = client;
    }

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void setCurrentAdmin(Admin admin) {
        currentAdmin = admin;
    }

    public static Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public static void clearSession() {
        currentClient = null;
        currentAdmin = null;
    }
} 