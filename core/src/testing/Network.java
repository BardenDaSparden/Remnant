package testing;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
	
	public static final int UDP_PORT = 33333;
	public static final int TCP_PORT = 33333;
	public static final String HOST_IP = "192.168.0.150";
	
	public static void register(EndPoint endpoint){
		Kryo kryo = endpoint.getKryo();
		kryo.register(Message.class);
	}
	
}

class Message {
	
	public String text;
	
}