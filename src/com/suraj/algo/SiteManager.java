package com.suraj.algo;

import java.util.ArrayList;
import java.util.List;

import com.suraj.bean.Site;
import com.suraj.bean.Token;

/**
 * Site Manger, which execute suzuki-algo
 * @author Suraj-Singh
 *
 */
public class SiteManager {

	private static List<Site> sites;
	private int noOfSites;
	private static SiteManager manager;
	private Token token;
	
	public static SiteManager getInstance(){
		if(manager == null){
			synchronized(SiteManager.class){
				if(manager == null){
					manager = new SiteManager();
				}
			}
		}
		return manager;
	}
	
	private SiteManager(){
	}
	
	public void initialize(Token token, int noOfSites){
		sites = new ArrayList<Site>(noOfSites);
		this.token = token;
		this.noOfSites = noOfSites;
		
		for(int i = 0; i< noOfSites; i++){
			Site site = new Site(i, noOfSites);
			sites.add(site);
		}
		
		sites.get(token.getOwnerSite().getSiteNo()).setHasToken(true);
	}
	
	/**
	 * request for token
	 * @param reqSiteNo
	 */
	public void request(int reqSiteNo){
		Site requestingSite = sites.get(reqSiteNo);
		
		int tokenHolderSite = this.token.getOwnerSite().getSiteNo();
		
		if(requestingSite.isRequesting() || requestingSite.isExec()){
			System.out.println(String.format("Site %d is already requesting/executing", (requestingSite.getSiteNo()+1)));
			return;
		}
		
		requestingSite.setRequesting(true);
		if(tokenHolderSite == requestingSite.getSiteNo()){
			requestingSite.processForExecution(true);
			return;
		}
		
		Integer nextSeqNo = requestingSite.getRequestArray()[reqSiteNo] + 1;
		for(int i = 0; i<noOfSites; i++){
			Site innerSite = sites.get(i);
			innerSite.request(reqSiteNo, nextSeqNo);
			if(i != reqSiteNo){
				innerSite.executeRequest(token, requestingSite, nextSeqNo);				
			}
		}
		
		/**
		 * check if requesting site receive the token
		 */
		if(this.token.getOwnerSite().getSiteNo() == requestingSite.getSiteNo()){
			requestingSite.processForExecution(false);
			return;
		}else{
			System.out.println(String.format("Site %d holds token and currently executing, And site %d puts its request on queue.", 
					(token.getOwnerSite().getSiteNo()+1), (reqSiteNo+1)));
		}
	}
	
	/**
	 * 
	 * @param siteNoToRelease
	 */
	public void release(int siteNoToRelease){
		Site siteToRelease = sites.get(siteNoToRelease);
		
		if(!siteToRelease.isExec()){
			System.out.println(String.format("Site %d is not currently executing critical section. so will ignored release command", 
					(siteNoToRelease+1)));
			return;
		}
		
		siteToRelease.releaseAndUpdateToken(token, siteNoToRelease);
		
		/**
		 * check token status
		 */
		if(!token.getTokenQueue().isEmpty()){
			Integer frontRunnerSiteNo = token.getTokenQueue().poll();
			Site frontRunnerSite = sites.get(frontRunnerSiteNo);
			frontRunnerSite.holdTokenAndEnterCS(token);
			return;
		}
		
		System.out.println(String.format("Site %d still holds token as token queue was empty viz no outstanding request in queue", 
				(siteNoToRelease+1)));
	}

	/**
	 * 
	 */
	public void showCurrentStatusOfSystem(){
		System.out.println("============ Token Status ============");
		System.out.println(String.format("Token holder : S%d", (token.getOwnerSite().getSiteNo()+1)));
		System.out.print("Token Queue :");
		if(token.getTokenQueue().isEmpty()){
			System.out.print("[]");
		}
		
		for(Integer queueElement: token.getTokenQueue()){
			System.out.print((queueElement+1) +"-->");
		}
		
		System.out.print("\n");
		
		System.out.print("Token LN array : ");
		for(Integer lnElement : token.getLn()){
			System.out.print(lnElement+" | ");
		}
		
		System.out.print("\n");
		
		/**
		 * show request array (by seeing any site element)
		 */
		System.out.print("Request array : ");
		Integer[] requestArray = token.getOwnerSite().getRequestArray();
		for(int i = 0; i< requestArray.length; i++){
			System.out.print(requestArray[i]+" | ");
		}
		
		System.out.print("\n");
	}
}
