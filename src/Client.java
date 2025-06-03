import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String userName;

    public Client(Socket socket, String userName){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = userName;
        }catch (Exception e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessages(){
        try{
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String message = scanner.nextLine();
//                if(message.equalsIgnoreCase("exit")){
//                    closeEverything(socket, bufferedReader,bufferedWriter);
//                    System.exit(0);
//                    return ;
//                }
                bufferedWriter.write(userName+" : "+ message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (Exception e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    public  void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw) {
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

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat ;
                while(socket.isConnected()){
                    try{
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    }catch (IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }

            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Server's IP address: ");
        String ipadr = sc.nextLine();
        System.out.print("Enter your username: ");
        String username = sc.nextLine();
        Socket socket = new Socket(ipadr, 1234);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessages();


    }
}
