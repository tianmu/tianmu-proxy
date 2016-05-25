package org.st.proxy.utils;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.st.proxy.bean.ProxyBean;
import org.st.proxy.service.StringService;

/**
 * http util
 */
public class HttpUtils {

	private static final Integer TIMEOUT = 1000; // 超时时间
	private static final Integer SOCKET_TIMEOUT = TIMEOUT * 3;// socket 超时时间

	private HttpUtils() {
	}

	public static void main(String[] args) {
		try {
			// www.kuaidaili.com www.xicidaili.com
			String content = httpGet("http://www.kuaidaili.com/proxylist/2/?yundun=ce9cdf6cb11b03d2bbac");
			System.out.println(content);
			StringService service = new StringService();
			List<ProxyBean> list = service.transStringToProxy(content);
			for (ProxyBean proxyBean : list) {
				System.out.println(proxyBean);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * http get
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String httpGet(String url) throws ClientProtocolException, IOException {
		String content = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(url);
			RequestConfig config = RequestConfig.custom().setConnectTimeout(TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT)
					.build();
			httpGet.setConfig(config);
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				System.out.println(response.getStatusLine());
				HttpEntity entity1 = response.getEntity();
				content = EntityUtils.toString(entity1);
				EntityUtils.consume(entity1);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return content;
	}

	/**
	 * a get method use proxy
	 * 
	 * @param url
	 * @param proxyUrl
	 * @param proxyPort
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String httpGet(String url, String proxyUrl, Integer proxyPort)
			throws ClientProtocolException, IOException {
		String content = null;
		HttpHost proxy = new HttpHost(proxyUrl, proxyPort);
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		CloseableHttpClient httpclient = HttpClients.custom().setRoutePlanner(routePlanner).build();
		try {
			HttpGet httpGet = new HttpGet(url);
			RequestConfig config = RequestConfig.custom().setConnectTimeout(TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT)
					.build();
			httpGet.setConfig(config);
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				System.out.println(response.getStatusLine());
				HttpEntity entity1 = response.getEntity();
				content = EntityUtils.toString(entity1);
				EntityUtils.consume(entity1);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return content;
	}
}
