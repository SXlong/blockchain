package com.lancer.blockchain.p2p;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lancer.blockchain.domain.Block;

public class BroadcastThread extends Thread{
	private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastThread.class);
	final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private Socket socket = null;
	private List<Block> blockChain;

	public BroadcastThread(Socket socket, List<Block> blockChain) {
		this.socket = socket;
		this.blockChain = blockChain;
	}

	@Override
	public void run() {
		while (true) {
			PrintWriter pw = null;
			try {
				Thread.sleep(3000);	//3秒
				LOGGER.info("\n------------broadcast-------------\n");
				LOGGER.info(gson.toJson(blockChain));
				pw = new PrintWriter(socket.getOutputStream());
				// 发送到其他结点
				pw.write("------------broadcast-------------\n");
				pw.write(gson.toJson(blockChain));
				pw.flush();
			} catch (InterruptedException e) {
				LOGGER.error("error:", e);
				if(socket.isClosed()){
					break;
				}
			} catch (IOException e) {
				LOGGER.error("error:", e);
				if(socket.isClosed()){
					break;
				}
			} 
		}
	}
	
	
}
