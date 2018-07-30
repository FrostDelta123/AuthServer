package ru.frostdelta.authserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {

    static ExecutorService executeIt = Executors.newCachedThreadPool();

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Network net = new Network();
        net.openConnection();

        try (ServerSocket server = new ServerSocket(3345);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Server socket created, command console reader for listen to server commands");

            while (!server.isClosed()) {

                Socket client = server.accept();


                executeIt.execute(new MonoThreadClientHandler(client));
                System.out.print("Connection accepted." + "\n");
            }

            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}