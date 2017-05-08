package edu.Brandeis.cs131.Common.HuTimothy;

import java.util.ArrayList;
import edu.Brandeis.cs131.Common.Abstract.Client;
import edu.Brandeis.cs131.Common.Abstract.Server;

public class BasicServer extends Server {
	ArrayList<Client> clients = new ArrayList<Client>();
	   
	public BasicServer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public synchronized boolean connectInner(Client client){
		// TODO Auto-generated method stub
		//if there are no clients connected to the server
			if (clients.size()==0){
				clients.add(client); 
				return true;
			}
			
			
			//if there is an existing client connected to the server
			if ((clients.size()==1)){
				//if one is basic
				if(client.toString().contains("BASIC") || clients.get(0).toString().contains("BASIC")){
					return false;
				}else{
					//if its shared
					//looks at industry of shared client
					if(client.getIndustry().equals(clients.get(0).getIndustry())){
						return false;
					}else{
						clients.add(client);
						return true;
					}
				}
			}

			//if 2 clients already connected
			return false;
	}

	@Override
	public synchronized void disconnectInner(Client client) {
		// TODO Auto-generated method stub
		clients.remove(client);
	}
}
