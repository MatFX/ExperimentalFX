package networkinterfaces;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkIFaces {

	public static void main(String[] args) {
		List<NetworkInterface> interfaces = new ArrayList<NetworkInterface>();

		try
		{
	    	Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
	    	
	    	while (ifaces.hasMoreElements())
	    	{
	    		NetworkInterface networkInterface = ifaces.nextElement();
	    	
	    		if (!networkInterface.isLoopback() && networkInterface.isUp() && !networkInterface.isPointToPoint())
	    		{
	    			System.out.println("networkInterface " + networkInterface.toString());
	    			interfaces.add(networkInterface);
	    			//TODO evtl. noch unterscheiden nach Ethernet-Adresse
	    			byte[] mac = networkInterface.getHardwareAddress();
	    			
	    			if (mac != null) {
                        // MAC-Adresse in hexadezimalen String umwandeln
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < mac.length; i++) {
                            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                        }
                        System.out.println("Ethernet MAC-Adresse: " + sb.toString());
                    }
	    			 
	    			
	    		}
	    	}
		}
		catch (Exception e)
		{
		}

	}

}
