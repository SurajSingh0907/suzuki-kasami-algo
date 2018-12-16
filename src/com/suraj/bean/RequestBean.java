package com.suraj.bean;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestBean {
	
	private Site site;
	private AtomicInteger requestSeqNo = new AtomicInteger(0);
	
	public RequestBean(Site site) {
		super();
		this.site = site;
	}

	public Site getSite() {
		return site;
	}
	
	public int addAndGetRequestSeqNo(){
		return requestSeqNo.incrementAndGet();
	}
	
	public int getRequestSeqNo() {
		return requestSeqNo.get();
	}

	@Override
	public String toString() {
		return "RequestBean [site=" + site + ", requestNo=" + requestSeqNo.get() + "]";
	}	

}
