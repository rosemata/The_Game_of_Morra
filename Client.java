import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{

	Socket socketClient;
	ObjectOutputStream out;
	ObjectInputStream in;
	private Consumer<Serializable> callback;
	String ip;
	int port;
	MorraInfo mora = new MorraInfo();
	
	Client(Consumer<Serializable> call, String ipNum, int portNum){
		ip = ipNum;
		port = portNum;
		callback = call;
	}
	
	public void run() {
		try {
			// used ip and port from user input
			socketClient= new Socket(ip,port);
		    out = new ObjectOutputStream(socketClient.getOutputStream());
		    in = new ObjectInputStream(socketClient.getInputStream());
		    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}
		
		while(true) {
			try {
				String message = in.readObject().toString();
				callback.accept(message);
			}
			catch(Exception e) {}
		}
    }
	
	public void send(MorraInfo info) {
		try {
			out.writeObject(info);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
