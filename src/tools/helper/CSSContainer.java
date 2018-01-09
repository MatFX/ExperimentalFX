package tools.helper;

import java.net.URL;

public class CSSContainer 
{
	private String forView;
	
	private URL url;
	
	public CSSContainer(String forView, URL url)
	{
		this.forView = forView;
		this.setUrl(url);
	}
	
	public String getForView() {
		return forView;
	}

	public void setForView(String forView) {
		this.forView = forView;
	}
	
	public String toString()
	{
		return forView;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
}