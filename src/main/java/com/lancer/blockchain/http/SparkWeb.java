package com.lancer.blockchain.http;

import static spark.Spark.get;
import static spark.Spark.post;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lancer.blockchain.domain.Block;
import com.lancer.blockchain.domain.Message;
import com.lancer.blockchain.util.BlockUtils;

/**
 * HTTP相关的内容 创建块 查看链
 * @author Administrator
 *
 */
public class SparkWeb {
	private static final Logger LOGGER = LoggerFactory.getLogger(SparkWeb.class);
	private static List<Block> blockChain = new LinkedList<Block>();
	
	public static void main(String[] args) {
		//创世块
		Block genesisBlock = new Block();
		genesisBlock.setIndex(0);
		genesisBlock.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		genesisBlock.setVac(0);
		genesisBlock.setPrevHash("");
		genesisBlock.setHash(BlockUtils.calculateHash(genesisBlock));
		blockChain.add(genesisBlock);
		
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		final Gson gson = new Gson();
		get("/", (request,response) ->  gson.toJson(blockChain));
		
		post("/",(request,response) -> {
			String body =request.body();
			Message m = gson.fromJson(body, Message.class);
			if(m == null){
				return "vac is null";
			}
			LOGGER.debug("入参"+m.toString());
			int vac = m.getVac();
			Block lastBlock = blockChain.get(blockChain.size()-1);
			Block newBlock = BlockUtils.generateBlock(lastBlock, vac);
			if(BlockUtils.isBlockValid(newBlock, lastBlock)){
				blockChain.add(newBlock);
				LOGGER.info(gson.toJson(blockChain));
			}else{
				return "HTTP 500: Invalid Block Error";
			}
			return "success";
		});
		
		LOGGER.info("初始化启动");
		LOGGER.info(gson.toJson(blockChain));
	}
}
