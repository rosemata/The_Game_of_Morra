import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;


public class Server{
	int count = 1;
	public ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	MorraInfo mora = new MorraInfo();
	TheServer server;
	private Consumer<Serializable> callback;
    public int port;
    int whoLeaved = -999;
 
	Server(Consumer<Serializable> call, int portNum){
		callback = call;
		port = portNum;
		server = new TheServer();
		server.start();
	}
	
	public class TheServer extends Thread{
		public void run() {
			try(ServerSocket mysocket = new ServerSocket(port);){
			
		    while(true) {
				ClientThread c = new ClientThread(mysocket.accept(), count);
				clients.add(c);
				// check if there is 2 clients
				if(clients.size() == 2) {
					mora.have2Players = true;
					callback.accept("Number of Clients: " + clients.size() +
									mora.info());
				}
				c.start();
				count++;
				
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	
		class ClientThread extends Thread{
			// data members
			Socket connection;
			int count;
			int arrayNum = 0;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
				this.arrayNum = count;
			}
			
			// update every clients listVIew
			public void updateClients(String message) {
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
					 t.out.reset();
					 t.out.flush();
					 t.out.writeObject(message);
					}
					catch(Exception e) {}
				}
			}
			
			// update only one clients listview
			public void updateClient(String message, int whichClient) {
				ClientThread t = clients.get(whichClient);
				try {
					 t.out.reset();
					 t.out.flush();
					 t.out.writeObject(message);
					}
				catch(Exception e) {}
			}
			
			public void run(){
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				
				// taking care of leaving server
				if(whoLeaved == 1) {
					this.count = whoLeaved;
				}
				
				else if (whoLeaved == 2) {
					this.count = whoLeaved;
				}
				
				updateClients("New Player #" + count + " Joined The Game");
				
				if(clients.size() == 2) {
					updateClients("2 Players joined the game. Make your choices.");
				}

				if(clients.size() != 2) {
					updateClients("2 Players needed to play the game");
				}
				
				//reset this value
				whoLeaved = -999;
				
				while(true) {
				    try {
				    	// read mora object
				    	MorraInfo morraTemp = (MorraInfo) in.readObject();
				    	
				    	// for client 1
				    	if(count == 1) {
				    		mora.p1Guess = morraTemp.clientGuessChoose;
				    		mora.p1PlayFingers = morraTemp.clientFingerChoose;
				    		mora.p1Submit = morraTemp.clientToServerSent;
				    	}
				    	
				    	// for client 2
				    	else if(count == 2) {
				    		mora.p2Guess = morraTemp.clientGuessChoose;
				    		mora.p2PlayFingers = morraTemp.clientFingerChoose;
				    		mora.p2Submit = morraTemp.clientToServerSent;
				    	}
				    	
				    	// update listView, updateClients 
				    	if((clients.size() == 2) && (mora.p2Submit) && (mora.p1Submit)) {
				    		mora.evalWin(mora.p1PlayFingers, mora.p2PlayFingers);
				    		callback.accept("Number of Players: " + clients.size() +
								mora.info());
				    		
					    	updateClients("Player #1 had " + mora.p1PlayFingers + " fingers " +
				    				 "and guessed " + mora.p1Guess);
					    	updateClients("Player #2 had " + mora.p2PlayFingers + " fingers " +
				    				 "and guessed " + mora.p2Guess);
					    	updateClients("Player 1 Point: " + mora.p1Points + " Player 2 Point: " + mora.p2Points);
					    	
					    	// reset for the next game
					    	mora.p1Submit = false;
					    	mora.p2Submit = false;
				    	}
				    	
				   
				    	// announce who won
				    	if(mora.p1Points == 2) {
				    		callback.accept("Gameover\nPlayer 1 Won\nGoodBye.");
				    		updateClients("Gameover\nPlayer 1 Won\nGoodBye.");
				    	}
				    	
				    	//announce who won
				    	if(mora.p2Points == 2) {
				    		callback.accept("Gameover\nPlayer 2 Won\nGoodBye.");
				    		updateClients("Gameover\nPlayer 2 Won\nGoodBye.");
				    	}
			    	}
				    catch(Exception e) {
				    	whoLeaved = count;
				    	mora.resetMorra();
				    	callback.accept("Player: " + count + " disconnected ... Need 2 Players");
				    	if(count == 2) {
				    		updateClients("Player #"+count+" has left the server!\n"
				    				+ "Waiting for Player 2.");
				    	}
				    	else if(count == 1){
				    		updateClients("Player #"+count+" has left the server!\n"
				    				+ "Waiting for Player 1.");
				    	}
				    	clients.remove(this);
				    	break;
				    }
				}
			}//end of run
			
			
		}//end of client thread
}


	
	

	
