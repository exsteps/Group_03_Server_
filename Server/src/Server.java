
/*import java.net.*;
import java.io.*;
import java.util.*;*/
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
	static java.util.Date serverCreate;
	private static int maxClients = 0;
	private static boolean morePlayersCanJoin = true;
	private static String[] playerNames;
	private static String client = "Client ";

	public int getNumberOfClients() {
		return maxClients;
	}

	public static void main(String[] args) {

		// Creating a thread for the server
		new Thread(() -> {
			try {
				ServerSocket server = new ServerSocket(7845);
				serverCreate = new java.util.Date();
				System.out.println("The Server Was Created: " + serverCreate);

				while (morePlayersCanJoin) {
					// Listen to clients
					Socket socket = server.accept();
					// adding to the number of clients connected
					maxClients++;
					InetAddress inetAddress = server.getInetAddress();
					System.out.println("Client " + maxClients + "'s host name is " + inetAddress.getHostName());
					System.out.println("Client " + maxClients + "'s IP Address is " + inetAddress.getHostAddress());

					// Creating a new thread for each client connecting
					new Thread(new HandleAClient(socket)).start();

					if (maxClients >= 4) {
						morePlayersCanJoin = false;
					}
				}
			} catch (IOException ex) {
				System.err.println(ex);
			}

		}).start();
	}

}

class HandleAClient implements Runnable {
	private Socket socket;

	HandleAClient(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			// data recieved and send
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());

			while (true) {

				// recive answer from the client - what did it roll?
				int diceRoll = in.readInt();

				// Add the diceRoll to the clients existing score
				int clientScore = 0 + diceRoll;

				// Send the clientScore back to the client
				out.writeInt(clientScore);
			}
		}

		catch (IOException e) {
			System.err.println(e);
		}
	}
}
