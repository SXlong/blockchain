package com.lancer.blockchain.domain;

public class Message {

	private int vac;

	public int getVac() {
		return vac;
	}

	public void setVac(int vac) {
		this.vac = vac;
	}

	@Override
	public String toString() {
		return "Message [vac=" + vac + "]";
	}
	
}
