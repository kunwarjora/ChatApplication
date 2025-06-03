import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers  = new ArrayList<>(); //To keep a list of all the clients
    private Socket socket;
    private BufferedReader bufferedReader;  //To read from the client
    private BufferedWriter bufferedWriter;  //To write to the client
    private String userName;

    public ClientHandler(){}
    public ClientHandler(Socket s){
        try{
            socket= s;
            this.bufferedWriter = new BufferedWriter (new OutputStreamWriter(s.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.userName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage ("SERVER : "+ this.userName+" has entered the chat!!");

        }catch (Exception e){
            closeEverything(socket, bufferedReader,bufferedWriter);
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }catch (IOException e){
                            closeEverything(socket, bufferedReader,bufferedWriter);
                break;
            }
        }
    }
    public void broadcastMessage(String message){
        for (ClientHandler c: clientHandlers ) {
            try{
                if (!c.userName.equals(userName)){
                    c.bufferedWriter.write(message);
                    c.bufferedWriter.newLine(); // it tells that the message is over
                    c.bufferedWriter.flush();
                }

            }catch (IOException e){
                            closeEverything(socket, bufferedReader,bufferedWriter);

            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER : "+userName+" has left the chat");
    }

    public void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw){
        removeClientHandler();
        try{
            if(br!=null){
                br.close();
            }
            if(bw!=null){
                bw.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
