package com.trendyol.deeplink.repository;

import java.net.URL;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service("DeepLinkService")
public class DeepLinkImpl implements DeepLinkRepository {

	public static Logger logger = Logger.getLogger(DeepLinkImpl.class.getName());

	public static final boolean isDebug = false;
	
	public String getDeepLinkRequest(String urlString) {
		urlString = urlString.toLowerCase();
		URL url = null;
		String path;

		try {
			url = new URL(urlString);
			if (isDebug)
				logger.info(DeepLinkMessages.msg0005.format(url.toString()));
		} catch (Exception ex) {
			if (isDebug)
				logger.warning(ex.toString());
			System.out.print(ex.toString());
		}

		if (url != null && url.getHost().equals(DeepLinkConstants.HOST)) {
			path = url.getFile();
			String result = deepLinkConverter(path);
			// write the data into ElasticSearch: { index: deepLinkRequests, dateTime, urlString, result }
			return result;
		} else {
			if (isDebug) 
				logger.warning(DeepLinkConstants.INVALID);
			// write the data into ElasticSearch: { index: invalid, dateTime, urlString }
			return DeepLinkConstants.INVALID;
		}
	}

	public String deepLinkConverter(String path) {

		// If it contains product or search prefix, it should be divided through
		// elements
		if (path.contains(DeepLinkConstants.PRODUCT_PREFIX) || path.contains(DeepLinkConstants.SEARCH_PREFIX)) {

			// If it is product page, we should define possible sub items and retrieve
			// appropriate data
			if (path.contains(DeepLinkConstants.PRODUCT_PREFIX)) {
				String[] splitByProductPrefix = path.split(DeepLinkConstants.PRODUCT_PREFIX);
				String productUrl = splitByProductPrefix[1];
				String productId = "", boutiqueId = "", merchantId = "";

				// Every product has product detail page, it is located before -p-
				productId = productUrl;
				if (productUrl.contains("?")) {
					productId = DeepLinkHelpers.before(productUrl, "?");
				}

				// If there is boutiqueId, we should retrieve appropriate data
				if (productUrl.contains(DeepLinkConstants.PRODUCT_BOUTIQUEID)) {
					boutiqueId = DeepLinkHelpers.after(productUrl, DeepLinkConstants.PRODUCT_URL_BOUTIQUEID);

					if (boutiqueId.contains(DeepLinkConstants.AND_PREFIX)) {
						boutiqueId = DeepLinkHelpers.between(productUrl, DeepLinkConstants.PRODUCT_URL_BOUTIQUEID,
								DeepLinkConstants.AND_PREFIX);
					}
				}

				// If there is merchantId, we should retrieve appropriate data
				if (productUrl.contains(DeepLinkConstants.PRODUCT_MERCHANTID)) {
					merchantId = DeepLinkHelpers.after(productUrl, DeepLinkConstants.PRODUCT_URL_MERCHANTID);

					if (merchantId.contains(DeepLinkConstants.AND_PREFIX)) {
						merchantId = DeepLinkHelpers.between(productUrl, DeepLinkConstants.PRODUCT_URL_MERCHANTID,
								DeepLinkConstants.AND_PREFIX);
					}
				}
				return createDeepUrl(productId, boutiqueId, merchantId);
			}
			if (path.contains(DeepLinkConstants.SEARCH_PREFIX)) {
				String[] splitBySearchPrefix = path.split(DeepLinkConstants.SEARCH_PREFIX);
				String query = splitBySearchPrefix[1];
				return createDeepUrl(query);
			}
		} else {
			return createDeepUrl();
		}
		return DeepLinkConstants.INVALID;
	}

	public String createDeepUrl() {
		StringBuilder result = new StringBuilder();
		result.append(DeepLinkConstants.DEEP_LINK_HOME_PAGE);
		if (isDebug)
			logger.info(DeepLinkMessages.msg0004.format(result.toString()));
		return result.toString();
	}

	public String createDeepUrl(String query) {
		StringBuilder result = new StringBuilder();
		result.append(DeepLinkConstants.DEEP_LINK_SEARCH_PAGE);
		result.append(query);
		if (isDebug)
			logger.info(DeepLinkMessages.msg0004.format(result.toString()));
		return result.toString();
	}

	public String createDeepUrl(String productId, String boutiqueId, String merchantId) {
		StringBuilder result = new StringBuilder();
		result.append(DeepLinkConstants.DEEP_LINK_PRODUCT_PAGE);

		if (!productId.isEmpty()) {
			result.append(DeepLinkConstants.DEEP_LINK_TAG_CONTENT_ID + productId);
		}
		if (!boutiqueId.isEmpty() && merchantId.isEmpty()) {
			result.append(DeepLinkConstants.AND_PREFIX);
			result.append(DeepLinkConstants.DEEP_LINK_TAG_CAMPAIGN_ID + boutiqueId);
		}
		if (!merchantId.isEmpty() && boutiqueId.isEmpty()) {
			result.append(DeepLinkConstants.AND_PREFIX);
			result.append(DeepLinkConstants.DEEP_LINK_TAG_MERCHANT_ID + merchantId);
		}
		if(!merchantId.isEmpty() && !boutiqueId.isEmpty()) {
			result.append(DeepLinkConstants.AND_PREFIX);
			result.append(DeepLinkConstants.DEEP_LINK_TAG_CAMPAIGN_ID + boutiqueId);
			result.append(DeepLinkConstants.AND_PREFIX);
			result.append(DeepLinkConstants.DEEP_LINK_TAG_MERCHANT_ID + merchantId);
		}
		if (isDebug)
			logger.info(DeepLinkMessages.msg0004.format(result.toString()));
		return result.toString();
	}

	public String getWebUrlRequest(String urlString) {
		if (isDebug)
			logger.info(DeepLinkMessages.msg0002.format(urlString));
		if (urlString != null && urlString.contains(DeepLinkConstants.DEEP_LINK_HOST)) {
			String result = webUrlConverter(urlString);
			// write the data into ElasticSearch: { index: webUrlRequests, dateTime, urlString, result }
			return result;
		} else {
			if (isDebug)
				logger.warning(DeepLinkMessages.msg0003.format(urlString));
			// write the data into ElasticSearch: { index: invalid, dateTime, urlString }
			return DeepLinkConstants.INVALID;
		}
	}

	public String webUrlConverter(String path) {

		// Request= ty://?Page=Search&Query=elb
		// Response= https://www.trendyol.com/tum--urunler?q=elb
		if (path.contains(DeepLinkConstants.DEEP_LINK_SEARCH_PAGE)) {
			String query = DeepLinkHelpers.after(path, DeepLinkConstants.DEEP_LINK_SEARCH_PAGE);
			StringBuilder result = new StringBuilder();
			result.append(DeepLinkConstants.WEB_URL_SEARCH_PAGE);
			result.append(query);
			if (isDebug)
				logger.info(DeepLinkMessages.msg0001.format(result.toString()));
			return result.toString();
		}

		// Request=
		// ty://?Page=Product&ContentId=1925865&CampaignId=45448&MerchantId=154845
		else if (path.contains(DeepLinkConstants.DEEP_LINK_PRODUCT_PAGE)) {
			String contentId = DeepLinkHelpers.after(path, DeepLinkConstants.DEEP_LINK_TAG_CONTENT_ID);
			String merchantId = "", campaignId = "";
			if (contentId.contains(DeepLinkConstants.AND_PREFIX)) {
				contentId = DeepLinkHelpers.before(contentId, DeepLinkConstants.AND_PREFIX);
			}

			if (path.contains(DeepLinkConstants.DEEP_LINK_TAG_CAMPAIGN_ID)) {
				campaignId = DeepLinkHelpers.after(path, (DeepLinkConstants.DEEP_LINK_TAG_CAMPAIGN_ID));
				if (campaignId.contains(DeepLinkConstants.AND_PREFIX)) {
					campaignId = DeepLinkHelpers.before(campaignId, DeepLinkConstants.AND_PREFIX);
				}
			}
			if (path.contains(DeepLinkConstants.DEEP_LINK_TAG_MERCHANT_ID)) {
				merchantId = DeepLinkHelpers.after(path, DeepLinkConstants.DEEP_LINK_TAG_MERCHANT_ID);
				if (merchantId.contains(DeepLinkConstants.AND_PREFIX)) {
					merchantId = DeepLinkHelpers.before(merchantId, DeepLinkConstants.AND_PREFIX);
				}
			}
			return createWebUrl(contentId, campaignId, merchantId);
		}

		// Request= ty://?Page=Favoriler
		// Response= https://www.trendyol.com
		else {
			if (isDebug)
				logger.info(DeepLinkMessages.msg0001.format(DeepLinkConstants.FULLY_HOST));
			return DeepLinkConstants.FULLY_HOST;
		}
	}

	public String createWebUrl(String contentId, String boutiqueId, String merchantId) {
		StringBuilder result = new StringBuilder();
		result.append(DeepLinkConstants.WEB_URL_PRODUCT_PAGE);

		if (!contentId.isEmpty()) {
			result.append(contentId);
		}
		if (!boutiqueId.isEmpty() && merchantId.isEmpty()) {
			result.append(DeepLinkConstants.PRODUCT_URL_PRODUCTID);
			result.append(DeepLinkConstants.WEB_URL_TAG_CAMPAIGN_ID + boutiqueId);
		}
		if (!merchantId.isEmpty() && boutiqueId.isEmpty()) {
			result.append(DeepLinkConstants.PRODUCT_URL_PRODUCTID);
			result.append(DeepLinkConstants.WEB_URL_TAG_MERCHANT_ID + merchantId);
		}
		if (!merchantId.isEmpty() && !boutiqueId.isEmpty()) {
			result.append(DeepLinkConstants.PRODUCT_URL_PRODUCTID);
			result.append(DeepLinkConstants.WEB_URL_TAG_CAMPAIGN_ID + boutiqueId);
			result.append(DeepLinkConstants.AND_PREFIX);
			result.append(DeepLinkConstants.WEB_URL_TAG_MERCHANT_ID + merchantId);
		}
		if (isDebug)
			logger.info(DeepLinkMessages.msg0001.format(result.toString()));
		return result.toString();
	}
}
