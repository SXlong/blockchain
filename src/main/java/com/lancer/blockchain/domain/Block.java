package com.lancer.blockchain.domain;

/**
 * 块 组成区块链的每一个块的数据模型
 * 
<ul>区块
	<li>Index 是这个块在整个链中的位置</li>
	<li>Timestamp 显而易见就是块生成时的时间戳</li>
	<li>Hash 是这个块通过 SHA256 算法生成的散列值</li>
	<li>PrevHash 代表前一个块的 SHA256 散列值</li>
	<li>vac 资产数。我们要记录的数据</li>
</ul>
 */
public class Block {

	/** 是这个块在整个链中的位置 */
	private int index;
	
	/** 显而易见就是块生成时的时间戳 */
	private String timestamp;
	
	/** 虚拟资产。我们要记录的数据 */
	private int vac;
	
	/** 是这个块通过 SHA256 算法生成的散列值 */
	private String hash;
	
	/** 指向前一个块的 SHA256 散列值 */
	private String prevHash;

	/**	难度(前几位是0)*/
	private int difficulty;
	
	/** 现时*/
	private String nonce;
	
	/** getters and setters **/
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getVac() {
		return vac;
	}

	public void setVac(int vac) {
		this.vac = vac;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPrevHash() {
		return prevHash;
	}

	public void setPrevHash(String prevHash) {
		this.prevHash = prevHash;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	@Override
	public String toString() {
		return "Block [index=" + index + ", timestamp=" + timestamp + ", vac="
				+ vac + ", hash=" + hash + ", prevHash=" + prevHash
				+ ", difficulty=" + difficulty + ", nonce=" + nonce + "]";
	}


}
