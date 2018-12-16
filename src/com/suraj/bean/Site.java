package com.suraj.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Site {
	
	int siteNo;
	private Integer[] requestArray;
	private boolean hasToken;
	private boolean isExec;
	private boolean isRequesting;
	
	public Site(int siteNo, int noOfSites) {
		super();
		this.siteNo = siteNo;
		this.requestArray = new Integer[noOfSites];
	
		init(noOfSites);
	}
	
	private void init(int noOfSites) {
		for(int i = 0; i<noOfSites; i++){
			requestArray[i] = 0;
		}
	}

	public int getSiteNo() {
		return siteNo;
	}

	public Integer[] getRequestArray() {
		return requestArray;
	}

	public boolean isHasToken() {
		return hasToken;
	}

	public boolean isExec() {
		return isExec;
	}

	public boolean isRequesting() {
		return isRequesting;
	}

	@Override
	public String toString() {
		return "Site [siteNo=" + siteNo + ", requestArray=" + requestArray
				+ ", hasToken=" + hasToken + ", isExec=" + isExec
				+ ", isRequesting=" + isRequesting + "]";
	}
	
	/**
	 * @param tokenAvailableAtStart 
	 * 
	 */
	public void processForExecution(boolean tokenAvailableAtStart){
		if(tokenAvailableAtStart){
			System.out.println(String.format("Site %d already has token, so will enter critical section", (siteNo+1)));			
		}else{
			System.out.println(String.format("Site %d gets the token, so will enter critical section", (siteNo+1)));
		}
		this.isExec = true;
		this.isRequesting = false;
	}

	public void setHasToken(boolean hasToken) {
		this.hasToken = hasToken;
	}

	public void setExec(boolean isExec) {
		this.isExec = isExec;
	}

	public void setRequesting(boolean isRequesting) {
		this.isRequesting = isRequesting;
	}

	public void request(int siteNo, Integer nextSeqNo) {
		this.requestArray[siteNo]  = nextSeqNo;
		if(this.siteNo == siteNo){
			isRequesting = true;
			return;
		}
	}

	/**
	 * Execute request on receiving request from other site
	 * @param token
	 * @param requestingSite
	 * @param nextSeqNo
	 */
	public void executeRequest(Token token, Site requestingSite, Integer nextSeqNo) {
		int reqSiteNo = requestingSite.getSiteNo();
		int maxOfReqSeqNoForSite = Math.max(requestingSite.getRequestArray()[reqSiteNo],nextSeqNo);
		requestArray[reqSiteNo] = maxOfReqSeqNoForSite;
		
		try{
			if(hasToken){
				boolean outstandingCheck = token.getLn()[reqSiteNo] + 1 == requestArray[reqSiteNo];
				if(!isExec && outstandingCheck){
					hasToken = false;
					token.setOwnerSite(requestingSite);
				}else if(outstandingCheck){
					token.getTokenQueue().put(reqSiteNo);
				}
			}
		}catch(Exception e){
			System.out.println("Exception while execution of request"+ e.getMessage());
		}
	}

	/**
	 * release and update token
	 * @param token
	 * @param siteNoToRelease
	 */
	public void releaseAndUpdateToken(Token token, int siteNoToRelease) {
		token.getLn()[siteNoToRelease] = this.getRequestArray()[siteNoToRelease];
		this.setExec(false);
		System.out.println(String.format("Site %d releases the critical section", (siteNoToRelease+1)));
	}

	/**
	 * 
	 * @param token
	 */
	public void holdTokenAndEnterCS(Token token) {
		token.setOwnerSite(this);
		this.isExec = true;
		this.isRequesting = false;
		System.out.println(String.format("Site %d gets token and enters critical section", (this.getSiteNo()+1)));
		
	}
}
