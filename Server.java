import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void start() {
        try{
            while(!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("New User Has connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
            
        }
        catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void close(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }
        
        catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String args[]){
        try{
            ServerSocket serverSocket = new ServerSocket(8080);
            Server server = new Server(serverSocket);
            server.start();
        }
        catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
