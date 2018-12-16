package com.suraj.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * @author Suraj-Singh
 *
 */
public class Token {

	private Site ownerSite;
	private BlockingQueue<Integer> tokenQueue;
	private Integer[] ln;
	
	public Token(Site ownerSite, int noOfSites) {
		super();
		setOwnerSite(ownerSite);
		tokenQueue = new ArrayBlockingQueue<Integer>(noOfSites);
		ln = new Integer[noOfSites];
		
		init(noOfSites);
	}

	private void init(int noOfSites) {
		for(int i = 0; i<noOfSites; i++){
			ln[i] = 0;
		}
	}

	public Site getOwnerSite() {
		return ownerSite;
	}
	
	public BlockingQueue<Integer> getTokenQueue() {
		return tokenQueue;
	}
	
	public void setOwnerSite(Site ownerSite) {
		this.ownerSite = ownerSite;
		ownerSite.setHasToken(true);
	}

	public Integer[] getLn() {
		return ln;
	}

	@Override
	public String toString() {
		return "Token [ownerSite=" + ownerSite.getSiteNo() + "]";
	}
}
