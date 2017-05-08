package edu.Brandeis.cs131.Common.HuTimothy;

import java.util.HashMap;
import java.util.LinkedList;

import edu.Brandeis.cs131.Common.Abstract.Client;
import edu.Brandeis.cs131.Common.Abstract.Log.Log;
import edu.Brandeis.cs131.Common.Abstract.Server;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MasterServer extends Server {

	private final Map<Integer, List<Client>> mapQueues = new HashMap<Integer, List<Client>>();
	private final Map<Integer, Server> mapServers = new HashMap<Integer, Server>();
	
	//each server has its own conditions in order to prevent clients from notifying clients 
	//in other server
	final Lock lock = new ReentrantLock();
	private final Map<Integer, Condition> mapNotFirstInQueue = new HashMap<Integer, Condition>();
	private final Map<Integer, Condition> mapFirstInQueue = new HashMap<Integer, Condition>();
	
	public MasterServer(String name, Collection<Server> servers, Log log) {
		super(name, log);
		Iterator<Server> iter = servers.iterator();
		while (iter.hasNext()) {
			this.addServer(iter.next());
		}
	}
	
	//
	public void addServer(Server server) {
		int location = mapQueues.size();
		this.mapServers.put(location, server);
		this.mapQueues.put(location, new LinkedList<Client>());
		this.mapFirstInQueue.put(location, lock.newCondition());
		this.mapNotFirstInQueue.put(location, lock.newCondition());
	}

	@Override
	public boolean connectInner(Client client) {
		int key = getKey(client);
		List<Client> q = mapQueues.get(key);
		Server serv = mapServers.get(key);
		lock.lock();
		try{
			q.add(client);
			if (!q.isEmpty()){
				while(!q.get(0).equals(client)){ //if it is not first in queue then wait until it gets to that position
					mapNotFirstInQueue.get(key).await();
				}
			}else{ //if queue is empty, this is to prevent errors
				return false;
			}
			//if first in queue, test if can connect, and if not wait and context switch to another thread
			while(serv.connect(client)==false){
				mapFirstInQueue.get(key).await();
			}
			q.remove(0);
			//signal clients attached to server
			mapNotFirstInQueue.get(key).signal();
			return true;
		} catch (InterruptedException e){

		} finally {
			lock.unlock();
		}
		return false;
		// TODO Auto-generated method stub
	}

	@Override
	public void disconnectInner(Client client) {
		int key = getKey(client);
		Server serv = mapServers.get(key);
		lock.lock();

		try{
			//signals all clients in server, first front in queue and then clients in back to move them through lock
			serv.disconnect(client);
			mapFirstInQueue.get(key).signal();
			mapNotFirstInQueue.get(key).signalAll();
		} finally{
			lock.unlock();
		}
	}

	//returns a number from 0- mapServers.size -1
	// MUST be used when calling get() on mapServers or mapQueues
	private int getKey(Client client) {
		return client.getSpeed() % mapServers.size();
	}
}
