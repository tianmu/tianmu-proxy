package org.st.proxy.bean;

public class ProxyBean {
	private String url;
	private Integer port;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return this.getUrl() + ":" + this.getPort();
	}

}
