package com.trendyol.deeplink;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.*;
import org.junit.Assert;
//import org.junit.jupiter.api.Test;

import org.junit.Test;

import com.trendyol.deeplink.repository.DeepLinkImpl;
import com.trendyol.deeplink.repository.DeepLinkRepository;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
 
@RunWith(JUnit4.class)
public class DeeplinkApplicationTests {
	
	public String [][] webUrlList = new String[8][2];
	public String [][] deepLinkList = new String[7][2];

	@Test
	public void testWebUrlToDeepLink() {
		
		DeepLinkImpl dp = new DeepLinkImpl();
		webUrlList[0][0] = "https://www.trendyol.com/caso/saat-p-1925865?boutiqueId=439892&merchantId=105064"; 
		webUrlList[0][1] = "ty://?Page=Product&ContentId=1925865&CampaignId=439892&MerchantId=105064";
		webUrlList[1][0] = "https://www.trendyol.com/caso/erkek-kol-saat-p-1925865";
		webUrlList[1][1] = "ty://?Page=Product&ContentId=1925865";
		webUrlList[2][0] = "https://www.trendyol.com/caso/erkek-kol-saat-p-1925865?boutiqueId=439892";
		webUrlList[2][1] = "ty://?Page=Product&ContentId=1925865&CampaignId=439892";
		webUrlList[3][0] = "https://www.trendyol.com/caso/erkek-kol-saat-p-1925865?MerchantId=105064";
		webUrlList[3][1] = "ty://?Page=Product&ContentId=1925865&MerchantId=105064";
		webUrlList[4][0] = "https://www.trendyol.com/tum--urunler?q=elbse";
		webUrlList[4][1] = "ty://?Page=Search&Query=elbse";
		webUrlList[5][0] = "https://www.trendyol.com/tum--urunler?q=C3%BCt%C3%BC";
		webUrlList[5][1] = "ty://?Page=Search&Query=C3%BCt%C3%BC";
		webUrlList[6][0] = "https://www.trendyol.com/Hesabm/Favorler";
		webUrlList[6][1] = "ty://?Page=Home";
		webUrlList[7][0] = "https://www.trendyol.com/Hesabm/#/Siparislerim";
		webUrlList[7][1] = "ty://?Page=Home";
		
		int testCase = 0;
		
		// testCase=5 iken veriler dogru geliyor, fakat "t" != "T" olmadigi icin false der
		// debug edilmesi lazim
		
		String expected = dp.getDeepLinkRequest(webUrlList[testCase][0]);
		String actual = webUrlList[testCase][1];
		Assert.assertEquals(expected,actual);
	}

	@Test
	public void testDeepLinkToWebUrl() {
		DeepLinkImpl dp = new DeepLinkImpl();
		deepLinkList[0][0] = "ty://?Page=Product&ContentId=1925865&CampaignId=439892&MerchantId=105064";
		deepLinkList[0][1] = "https://www.trendyol.com/brand/name-p-1925865?boutiqueId=439892&merchantId=105064";
		deepLinkList[1][0] = "ty://?Page=Product&ContentId=1925865";
		deepLinkList[1][1] = "https://www.trendyol.com/brand/name-p-1925865";
		deepLinkList[2][0] = "ty://?Page=Product&ContentId=1925865&CampaignId=439892";
		deepLinkList[2][1] = "https://www.trendyol.com/brand/name-p-1925865?boutiqueId=439892";
		deepLinkList[3][0] = "ty://?Page=Product&ContentId=1925865&MerchantId=105064";
		deepLinkList[3][1] = "https://www.trendyol.com/brand/name-p-1925865?merchantId=105064";
		deepLinkList[4][0] = "ty://?Page=Search&Query=elbise";
		deepLinkList[4][1] = "https://www.trendyol.com/tum--urunler?q=elbise";
		deepLinkList[5][0] = "ty://?Page=Favortes";
		deepLinkList[5][1] = "https://www.trendyol.com";
		deepLinkList[6][0] = "ty://?Page=Orders";
		deepLinkList[6][1] = "https://www.trendyol.com";
		
		int testCase = 0;
	
		String expected = dp.getWebUrlRequest(deepLinkList[testCase][0]);
		String actual = deepLinkList[testCase][1];
		Assert.assertEquals(expected.toLowerCase(), actual.toLowerCase());
	}
	


}
