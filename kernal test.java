package main;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LoginSecurityKernel {

    private static final ConcurrentHashMap<String, Integer> loggedInUsers = new ConcurrentHashMap<>();
    private static final int maxParticipantsPerUnit = 7;
    private static final int maxUnits = 300;
    private static final ReentrantLock lock = new ReentrantLock();

    // Tenta autenticar o usuário com o username e password fornecidos
    public boolean login(String username, String password) {
        // Lógica de autenticação
        boolean isAuthenticated = authenticateUser(username, password);

        // Se autenticado com sucesso, registra o usuário logado
        if (isAuthenticated) {
            lock.lock();
            try {
                loggedInUsers.put(username, loggedInUsers.getOrDefault(username, 0) + 1);
            } finally {
                lock.unlock();
            }
        }

        return isAuthenticated;
    }

    // Realiza o logout do usuário especificado
    public boolean logout(String username) {
        lock.lock();
        try {
            if (loggedInUsers.containsKey(username)) {
                int count = loggedInUsers.get(username);
                if (count > 0) {
                    if (count == 1) {
                        loggedInUsers.remove(username);
                    } else {
                        loggedInUsers.put(username, count - 1);
                    }
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    // Verifica se o número de unidades ativas é menor ou igual a 300
    public boolean tokenClosed() {
        lock.lock();
        try {
            return loggedInUsers.size() <= maxUnits;
        } finally {
            lock.unlock();
        }
    }

    // Verifica se o número de participantes na unidade atual é menor que 7
    public boolean canJoinUnit(String username) {
        lock.lock();
        try {
            return loggedInUsers.getOrDefault(username, 0) < maxParticipantsPerUnit;
        } finally {
            lock.unlock();
        }
    }

    // Simula a lógica de autenticação
    private boolean authenticateUser(String username, String password) {
        // Lógica de autenticação - a ser implementada de acordo com requisitos específicos
        return true; // Simulação de autenticação bem-sucedida
    }

    public static void main(String[] args) {
        // Exemplo de uso do modelo de autenticação
        String username = "user123";
        String password = "password123";

        LoginSecurityKernel kernel = new LoginSecurityKernel();

        if (kernel.login(username, password)) {
            System.out.println("Login successful");
            if (kernel.canJoinUnit(username)) {
                System.out.println("User can join unit");
            } else {
                System.out.println("Unit is full");
            }
        } else {
            System.out.println("Login failed");
        }

        // Simulação de logout
        if (kernel.logout(username)) {
            System.out.println("Logout successful");
        } else {
            System.out.println("Logout failed");
        }

        // Verifica se o token está fechado
        if (kernel.tokenClosed()) {
            System.out.println("Token is closed");
        } else {
            System.out.println("Token is still open");
        }
    }
}
