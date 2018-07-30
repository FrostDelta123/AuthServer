package ru.frostdelta.authserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MonoThreadClientHandler implements Runnable {

    private static Socket clientDialog;

    UserSession session = new UserSession();


    public MonoThreadClientHandler(Socket client) {
        MonoThreadClientHandler.clientDialog = client;
    }

    @Override
    public void run() {

        try {

            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            DataInputStream in = new DataInputStream(clientDialog.getInputStream());


            while (!clientDialog.isClosed()) {
                Network auth = new Network();

                String type = in.readUTF();

                if(type.equals("endSession")){

                    session.session.remove(in.readUTF());
                }

                if(type.equals("checkUpdate")){

                   String clientVersion =  in.readUTF();

                   //ТУТ ПРОВЕРКА ВЕРСИЙ ИЗ БД
                   if(clientVersion.equalsIgnoreCase("1.1")){

                       out.writeBoolean(true);

                   }else out.writeBoolean(false);

                }

                if(type.equals("addKey")){

                    String username = in.readUTF();
                    String key = in.readUTF();

                    if(auth.keyAuth(username).equalsIgnoreCase( "false")){

                        out.writeBoolean(true);

                        auth.addKey(username, key);
                    }else {

                        out.writeBoolean(false);
                    }

                }

                if(type.equals("sendMsg")){

                    String username = in.readUTF();
                    String msg = in.readUTF();
                    String theme = in.readUTF();

                    auth.sendMsg(username, msg, theme);

                }

                if(type.equals("changeEmail")){

                    String username = in.readUTF();
                    String email = in.readUTF();

                    if(session.session.containsKey(username)) {
                        auth.eMailChange(username, email);
                    }else System.out.println(username + " NOT auth");

                }

                if(type.equals("changePass")){

                    String username = in.readUTF();
                    String hashedPassword = in.readUTF();

                    if(session.session.containsKey(username)) {
                    auth.passChange(username, hashedPassword);
                    }else System.out.println(username + " NOT auth");
                }

                if (type.equals("passAuth")){

                    String username = in.readUTF();
                    String hashedPassword = in.readUTF();

                    if (hashedPassword.equals(auth.passAuth(username))){

                        out.writeBoolean(true);
                        out.flush();
                        UserSession us = new UserSession(username);

                    }else out.writeBoolean(false);

                }

                if (type.equals("keyAuth")){

                    String key = in.readUTF();

                    String name = in.readUTF();

                    if (auth.keyAuth(name).equals(key)){
                        out.writeBoolean(true);
                        UserSession us = new UserSession(name);

                    }else out.writeBoolean(false);

                }

                if(type.equals("server")){
                    System.out.println("server accept");

                    System.out.println(in.available());

                    String name = in.readUTF();

                    System.out.println("name - " + name);
                    String ip = auth.getServer(name, "ip");

                    int port = Integer.parseInt(auth.getServer(name, "port"));
                    out.writeUTF(ip);
                    out.flush();
                    System.out.println("Отправлен ip " + ip);

                    //out.writeInt(port);
                    //System.out.println("Отправлен port " + port);
                    //out.flush();
                }
            }

            System.out.println("Client disconnected");

            in.close();
            out.close();

            clientDialog.close();

        } catch (IOException e) {

        }
    }
}