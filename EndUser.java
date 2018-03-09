import java.net.DatagramSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

/**
 *
 * Client class
 * 
 * An instance accepts user input 
 *
 */
public class EndUser extends Node {
	
	//Flag = 0 if coming from client, flag = 1 if coming from router
	byte[] flag; 
	boolean isResponse;
	int responseAddr;
	InetSocketAddress routerAddress;
	InetSocketAddress controllerAddress;
	int sourcePortNumber;
	int connectedRouterPort;
	int connectionCount;
	int[] connections;
	ControllerInformPacket controllerPacket;
	
	/**
	 * Constructor
	 * 	 
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 */
	EndUser(String host, int srcPort, int routerPort) {
		try {
			this.sourcePortNumber = srcPort;
			this.connectedRouterPort = routerPort;
			this.routerAddress = new InetSocketAddress(host, routerPort);
			this.controllerAddress = new InetSocketAddress(host, Node.CONTROLLER_PORT);
			
			this.isResponse = false;
			this.responseAddr = 0;
			
			this.connectionCount = 1;
			this.connections = new int[connectionCount];
			this.connections[0] = this.connectedRouterPort;
			this.controllerPacket = null
					;
			//Creates socket at srcPort (EndUser)
			socket= new DatagramSocket(sourcePortNumber);
			
			//Set flag =0, coming from EndUser (entering network)
			this.flag = ByteBuffer.allocate(4).putInt(0).array();
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	
	/**
	 * Assume that incoming packets contain a String and print the string.
	 * @throws Exception 
	 */
	public synchronized void onReceipt(DatagramPacket packet) throws Exception {
		StringContent content= new StringContent(packet);
		StdOut.println("\nPacket received from router...");
		
		StdOut.println("Packet contents = " + content.string);
		
		StdOut.println("\nWould you like to send a response? [y/n]");
		String choice = StdIn.readString();

		if(choice.equalsIgnoreCase("y")) {
			this.isResponse = true;
			this.responseAddr = content.getSource();
			sendMessage();
		}
		
		else {
			StdOut.println("Goodbye.");
			this.notify();
		}
		
	
	}
	
	
	/**
	 * Sender Method - Sends packet from EndUser to connectedRouter
	 * 
	 */
	/**
	 * @throws Exception
	 */
	public synchronized void start() throws Exception, SocketTimeoutException {

		//Inform controller of direct connections
		StdOut.println("Informing controller of connections...");
		informController();
		
		StdOut.println("\nEnter 's' to send a message or 'r' to receive a message:");
		String choice = StdIn.readString();

		if(choice.equalsIgnoreCase("s"))
			sendMessage();
		
		else
			StdOut.println("Waiting for contact at End User (" + this.sourcePortNumber + ")...");
			this.wait();
	}
	
	public synchronized void informController() throws IOException {
		
		this.controllerPacket = new ControllerInformPacket(this.sourcePortNumber, this.connections);
		DatagramPacket packet = controllerPacket.toDatagramPacket();
		packet.setSocketAddress(controllerAddress);
		socket.send(packet);
		
	}
	
	public synchronized void sendMessage() throws IOException {
		
		DatagramPacket packet = null;
		
		byte[] dstAddress = null;
		byte[] srcAddress = null;
		byte[] hopCount = null;
		byte[] payload = null;
		byte[] buffer = null;
		int dst;

		dstAddress = new byte[PacketContent.DST_ADDRESS_LENGTH];
		srcAddress = new byte[PacketContent.SRC_ADDRESS_LENGTH];
		hopCount = new byte[PacketContent.HOP_COUNT_LENGTH];
		
		if(!isResponse) {
		    StdOut.println("\nDestination address of end user: ");
			String dstStr = StdIn.readString();
		    dst = Integer.parseInt ( dstStr );
		}
		
		else
			dst = this.responseAddr;
		
	    dstAddress = ByteBuffer.allocate(PacketContent.DST_ADDRESS_LENGTH).putInt(dst).array();
	    
	    StdOut.println("\nString to send ");
		String stringToSend = StdIn.readString();

		payload = (stringToSend.getBytes());
		StdOut.print(ByteBuffer.wrap(dstAddress).getInt());
		srcAddress = ByteBuffer.allocate(PacketContent.SRC_ADDRESS_LENGTH).putInt(this.sourcePortNumber).array();
		hopCount = ByteBuffer.allocate(PacketContent.HOP_COUNT_LENGTH).putInt(0).array();

		// Creates a buffer to contain the information
		buffer = new byte[PacketContent.HEADER_LENGTH + payload.length];

		// Encloses the above information into a buffer containing an array of bytes
		System.arraycopy(dstAddress, 0, buffer, 0, PacketContent.DST_ADDRESS_LENGTH);
		System.arraycopy(srcAddress, 0, buffer, PacketContent.DST_ADDRESS_LENGTH, PacketContent.SRC_ADDRESS_LENGTH);
		System.arraycopy(hopCount, 0, buffer, (PacketContent.DST_ADDRESS_LENGTH + PacketContent.SRC_ADDRESS_LENGTH), PacketContent.HOP_COUNT_LENGTH);
		System.arraycopy(payload, 0, buffer, PacketContent.HEADER_LENGTH, payload.length);
		

		StdOut.println("\nSending packet to router at : " + this.connectedRouterPort + "...");
		packet = new DatagramPacket(buffer, buffer.length, this.routerAddress);
		socket.send(packet);
		StdOut.println("Packet sent to router\n");
	}
	
	/**
	 * Test method
	 * 
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {	
			//Establish end user details
			StdOut.println("\nEnter the port for end user to be established on: ");
			String inputString = StdIn.readString();
		    int endUserPortNumber = Integer.parseInt ( inputString );
		    
		    //Set connected port for end user
		    int routerPortNumber=0;
		    
		    //Set connected router address
		    switch(endUserPortNumber) {
		    case NET_18_PORT:
		    	routerPortNumber = ROUTER_A_PORT;
		    	break;
		    case NET_15_PORT:
		    	routerPortNumber = ROUTER_B_PORT;
		    	break;
		    case NET_28_PORT:
		    	routerPortNumber = ROUTER_B_PORT;
		    	break;
		    case NET_7_PORT:
		    	routerPortNumber = ROUTER_D_PORT;
		    	break;
		    case NET_5_PORT:
		    	routerPortNumber = ROUTER_F_PORT;
		    	break;
		    case NET_11_PORT:
		    	routerPortNumber = ROUTER_F_PORT;
		    	break;
		    case NET_21_PORT:
		    	routerPortNumber = ROUTER_E_PORT;
		    	break;
		    case NET_2_PORT:
		    	routerPortNumber = ROUTER_E_PORT;
		    	break;
		    case NET_10_PORT:
		    	routerPortNumber = ROUTER_C_PORT;
		    	break;
		    }
		    	
		    StdOut.println("End User (" + endUserPortNumber + ")\n\n");
		    EndUser user = new EndUser(DEFAULT_DST_NODE, endUserPortNumber, routerPortNumber);
		    user.start();
		    
			StdOut.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
