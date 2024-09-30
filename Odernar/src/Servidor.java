import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor esperando conexões...");
            ExecutorService pool = Executors.newFixedThreadPool(1);

            while (true) {
                try {
                    Socket clienteSocket = serverSocket.accept();
                    System.out.println("Cliente conectado: " + clienteSocket.getInetAddress().getHostAddress());
                    pool.execute(new ClienteHandler(clienteSocket));
                } catch (IOException e) {
                    System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }
}

class ClienteHandler implements Runnable {
    private Socket clienteSocket;

    public ClienteHandler(Socket socket) {
        this.clienteSocket = socket;
    }


    @Override
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            ObjectInputStream in = new ObjectInputStream(clienteSocket.getInputStream());
            List<Integer> lista = (List<Integer>) in.readObject();

            //System.out.println("Lista recebida do cliente: " + lista);

            Collections.sort(lista);

            ObjectOutputStream out = new ObjectOutputStream(clienteSocket.getOutputStream());
            out.writeInt(lista.size());
            out.flush();

            System.out.println("Quantidade de números ordenados enviada ao cliente: " + lista.size());
            long endTime = System.currentTimeMillis(); // Marca o tempo de fim
            long duration = endTime - startTime; // Calcula a duração
            System.out.println("Tempo de execução: " + duration + " milissegundos");

            clienteSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao processar cliente: " + e.getMessage());
            }
        }
    }