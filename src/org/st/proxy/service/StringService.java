package org.st.proxy.service;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.st.proxy.bean.ProxyBean;

public class StringService {

	public static void main(String[] args) {
		StringService service = new StringService();
		service.transStringToProxy("Cn	123.117.152.217	8118	北京	高匿	HTTP	16小时	不到1分钟");
	}

	public List<ProxyBean> transStringToProxy(String content) {
		List<ProxyBean> list = new LinkedList<>();
		Pattern p = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\D+(\\d+)");
		Matcher m = p.matcher(content);
		while (m.find()) {
			ProxyBean proxyBean = new ProxyBean();
			String a = m.group(1);
			String b = m.group(2);
			// System.out.println(a + ":" + b);
			proxyBean.setUrl(a);
			proxyBean.setPort(Integer.valueOf(b));
			list.add(proxyBean);
		}
		return list;
	}
}
