import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.imageio.*;
import java.net.Socket;

public class Client {
    

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = username;  
        }
        catch(Exception e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            System.out.println("Error: " + e.getMessage());
        }
    }


    public void sendMessage(){
        try{
            bufferedWriter.write(clientUsername);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(clientUsername+" Say "+messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }
        catch(Exception e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listenForMessages(){
        new Thread(new Runnable(){
            public void run(){
                String messageFromServer;
                while(socket.isConnected()) {
                    try{
                        messageFromServer = bufferedReader.readLine();
                        System.out.println(messageFromServer);
                    }
                    catch(Exception e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }  
    
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }
        catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter Your UserName for the group chat: ");
        String username = scan.nextLine();

        try{
            Socket socket = new Socket("localhost", 8080);
            Client client = new Client(socket, username);
            client.listenForMessages();
            client.sendMessage();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
