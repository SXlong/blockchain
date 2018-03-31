package com.lancer.blockchain.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lancer.blockchain.domain.Block;

public class BlockUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(BlockUtils.class);
	
	public static final int DIFFICULTY = 1;//难度
	
	/**
	 * 计算给定的数据的 SHA256 散列值
	 * @param block
	 * @return
	 */
	public static String calculateHash(Block block) {
	       String record = (block.getIndex()) + block.getTimestamp() + (block.getVac()) + block.getPrevHash();
	       return DigestUtils.sha256Hex(record);
	}
	
	/**
	 * 计算给定的数据的 SHA256 散列值
	 * @param block
	 * @return
	 */
	public static String calculateHashWithPow(Block block) {
	       String record = block.getIndex() + block.getTimestamp() + block.getVac() + block.getPrevHash() + block.getNonce();
	       return DigestUtils.sha256Hex(record);
	}
	
	/**
	 * 区块的生成
	 * @param oldBlock
	 * @param vac
	 * @return
	 */
	public static Block generateBlock(Block oldBlock, int vac) {
	       Block newBlock = new Block();
	       newBlock.setIndex(oldBlock.getIndex() + 1);
	       newBlock.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	       newBlock.setVac(vac);
	       newBlock.setPrevHash(oldBlock.getHash());
	       newBlock.setHash(calculateHash(newBlock));
	       return newBlock;
	}
	
	/**
	 * 区块的生成 基于pow算法(工作证明算法Proof-of-Work)
	 * @param oldBlock
	 * @param vac
	 * @return
	 */
	public static Block generateBlockWithPOW(Block oldBlock, int vac) {
	       Block newBlock = new Block();
	       newBlock.setIndex(oldBlock.getIndex() + 1);
	       newBlock.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	       newBlock.setVac(vac);
	       newBlock.setPrevHash(oldBlock.getHash());
	       newBlock.setDifficulty(DIFFICULTY);
	       
	       /*
	         * 这里的 for 循环很重要： 获得 i 的十六进制表示 ，将 Nonce 设置为这个值，并传入 calculateHash 计算哈希值。
	         * 之后通过上面的 isHashValid 函数判断是否满足难度要求，如果不满足就重复尝试。 这个计算过程会一直持续，直到求得了满足要求的
	         * Nonce 值，之后通过 handleWriteBlock 函数将新块加入到链上。
	         */
	        for (int i = 0;; i++) {
	            String hex = String.format("%x", i);
	            newBlock.setNonce(hex);
	            if (!isHashValid(calculateHashWithPow(newBlock), newBlock.getDifficulty())) {
	                System.out.printf("%s do more work!\n", calculateHashWithPow(newBlock));
	                try {
	                    Thread.sleep(1000);
	                } catch (InterruptedException e) {
	                    LOGGER.error("error:", e);
	                }
	                continue;
	            } else {
	                System.out.printf("%s work done!\n", calculateHashWithPow(newBlock));
	                newBlock.setHash(calculateHashWithPow(newBlock));
	                break;
	            }
	        }
	       
	       return newBlock;
	}
	
	/**
	 * 校验区块的合法性（有效性）
	 * @param newBlock
	 * @param oldBlock
	 * @return
	 */
	public static boolean isBlockValid(Block newBlock, Block oldBlock) {
	       if (oldBlock.getIndex() + 1 != newBlock.getIndex()) {
	           return false;
	       }
	       if (!oldBlock.getHash().equals(newBlock.getPrevHash())) {
	           return false;
	       }
	       if (!calculateHash(newBlock).equals(newBlock.getHash())) {
	           return false;
	       }
	       return true;
	}
	
	/**
	 * 校验区块的合法性（有效性）
	 * @param newBlock
	 * @param oldBlock
	 * @return
	 */
	public static boolean isBlockValidWithPow(Block newBlock, Block oldBlock) {
	       if (oldBlock.getIndex() + 1 != newBlock.getIndex()) {
	           return false;
	       }
	       if (!oldBlock.getHash().equals(newBlock.getPrevHash())) {
	           return false;
	       }
	       if (!calculateHashWithPow(newBlock).equals(newBlock.getHash())) {
	           return false;
	       }
	       return true;
	}
	
	/**
	 * 如果有别的链比你长，就用比你长的链作为区块链
	 * @param newBlocks
	 * @param oldBlocks
	 * @return
	 */
	public List<Block> replaceChain(List<Block> newBlocks,List<Block> oldBlocks) {
	       if (newBlocks.size() > oldBlocks.size()) {
	           return newBlocks;
	       }else{
	    	   return oldBlocks;
	       }
	}
	
	/**
     * 校验HASH的合法性
     * 
     * @param hash
     * @param difficulty 难度(有几位0)
     * @return
     */
    public static boolean isHashValid(String hash, int difficulty) {
        String prefix = repeat("0", difficulty);
        return hash.startsWith(prefix);
    }
    
    /**
     * 制作相同的字符串
     * @param str	字符
     * @param repeat 个数
     * @return
     */
    private static String repeat(String str, int repeat) {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < repeat; i++) {
            buf.append(str);
        }
        return buf.toString();
    }
}
