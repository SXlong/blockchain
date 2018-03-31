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
 * 工作量证明算法 Proof-of-work
 * 
 * 创建新块并加入到链上之前需要完成“工作量证明”过程。我们先写一个简单的函数来检查给定的哈希值是否满足要求。 哈希值必须具有给定位的“前导0”
 * “前导0”的位数是由难度（difficulty）决定的 可以动态调整难度（difficulty）来确保 Proof-of-Work 更难解
 * 
 * @author Administrator
 *
 */
public class SparkWebPow {
	private static final Logger LOGGER = LoggerFactory.getLogger(SparkWebPow.class);
	private static List<Block> blockChain = new LinkedList<Block>();
	
	public static void main(String[] args) {
		//创世块
		Block genesisBlock = new Block();
		genesisBlock.setIndex(0);
		genesisBlock.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		genesisBlock.setVac(0);
		genesisBlock.setPrevHash("");
		genesisBlock.setHash(BlockUtils.calculateHashWithPow(genesisBlock));
		genesisBlock.setDifficulty(BlockUtils.DIFFICULTY);
		genesisBlock.setNonce("");
		blockChain.add(genesisBlock);
		
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		final Gson gson = new Gson();
		get("/", (request,response) ->  gson.toJson(blockChain));
		
		/***
		 * post / {"vac":75} 
		 * curl -X POST -i http://localhost:4567/ --data {"vac":75}
		 */
		post("/",(request,response) -> {
			String body =request.body();
			Message m = gson.fromJson(body, Message.class);
			if(m == null){
				return "vac is null";
			}
			LOGGER.debug("入参"+m.toString());
			int vac = m.getVac();
			Block lastBlock = blockChain.get(blockChain.size()-1);
			Block newBlock = BlockUtils.generateBlockWithPOW(lastBlock, vac);
			if(BlockUtils.isBlockValidWithPow(newBlock, lastBlock)){
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
