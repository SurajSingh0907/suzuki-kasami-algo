package com.suraj.exec;

import java.util.Scanner;

import com.suraj.algo.SiteManager;
import com.suraj.bean.Site;
import com.suraj.bean.Token;

/**
 * 
 * @author Suraj-Singh
 *
 */
public class Exec {

	public static void main(String[] args) {
		System.out.println("======== Welcome to Suzuki-Kasami Algorithm @written by Suraj Singh =========");
		System.out.println("");
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter no. of sites :");
		int noOfSites = scan.nextInt();
		
		int tokenHolderSite = -1;
		while(tokenHolderSite < 1 || tokenHolderSite > (noOfSites-1)){
			System.out.println("\nEnter token holder site :" );
			tokenHolderSite = scan.nextInt();
			tokenHolderSite = tokenHolderSite - 1 ; //because using it as index
		}
		
		//initialize token instance
		Token token = new Token(new Site(tokenHolderSite, noOfSites), noOfSites);
		SiteManager siteManager = SiteManager.getInstance();
		siteManager.initialize(token, noOfSites);
		
		boolean breakFlag = true;
		while(breakFlag){
			System.out.println("::::: Options:<code> ::::");
			System.out.println("1. Request: rqt");
			System.out.println("2. Release: rls");
			System.out.println("3. System Status: sts");
			System.out.println("4. End: end");
			
			System.out.print("Enter your choice :");
			String choice = scan.next().toLowerCase();
			
			switch(choice){
			case "rqt":
				System.out.println("Enter requesting site no. :");
				int requestSiteNo = scan.nextInt();
				siteManager.request(requestSiteNo-1);
				break;
			case "rls":
				System.out.println("Enter release site no.:");
				int releaseSiteNo = scan.nextInt();
				siteManager.release(releaseSiteNo-1);
				break;
			case "sts":
				siteManager.showCurrentStatusOfSystem();
				break;
			case "end":
				breakFlag = false;
				break;
			default:
				System.out.println("Not a valid choice. Please reselect your choice");
			}
		}
		
		System.out.println("==== Thank You ===");
	}

}
