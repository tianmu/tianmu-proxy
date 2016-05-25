package org.st.proxy.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.st.proxy.bean.ProxyBean;
import org.st.proxy.service.StringService;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * use htmlunit
 */
public class HtmlUnitUtils {
	public static void main(String[] args) {
		String content = html("http://www.kuaidaili.com");
		System.out.println(content);
		StringService service = new StringService();
		List<ProxyBean> list = service.transStringToProxy(content);
		for (ProxyBean proxyBean : list) {
			System.out.println(proxyBean);
		}
	}

	/**
	 * get html from url after js already run.
	 * @param url
	 * @return
	 */
	public static String html(String url) {
		WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setTimeout(10000);
		try {
			HtmlPage page = webClient.getPage(url);
			page.initialize();
			String html = page.asXml();
			return html;
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			webClient.close();
		}
		return null;
	}
}
