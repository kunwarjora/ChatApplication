import java.net.ServerSocket;
import java.net.Socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String ipAddress = localhost.getHostAddress();
            System.out.println("Local IP Address: " + ipAddress);
        } catch (UnknownHostException e) {
            System.err.println("Error getting IP address: " + e.getMessage());
        }
    }
    public void startServer(){
        try{
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("A new connection is added!!!");

                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeServer(){
        try{
            if (serverSocket!=null){
                serverSocket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(1234);
            Server server = new Server(serverSocket);
            server.startServer();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
