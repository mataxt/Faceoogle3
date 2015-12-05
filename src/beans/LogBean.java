package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;

import com.google.gson.Gson;

import vm.LogViewModel;

@ViewScoped
@ManagedBean(name = "logBean")
public class LogBean implements Serializable {
	private String path = "http://130.237.84.211:8080/Faceoogle2/rest/";
	private static final long serialVersionUID = 1L;
	private List<LogViewModel> myLogs;
	private List<LogViewModel> myFeed;
	private String paramUser = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
			.getRequest()).getParameter("user");;
	private String messageLogs;
	@ManagedProperty(value = "#{userBean}")
	private UserBean userBean;

	public String getParamUser() {
		return paramUser;
	}

	@SuppressWarnings("unchecked")
	public List<LogViewModel> getLogs() {
		RestClient client = new RestClient();
		Resource res = client.resource(path + "log/logs?recv=" + paramUser);
		String jsonLogs = res.accept("application/json").get(String.class);
		Gson gson = new Gson();
		myLogs = gson.fromJson(jsonLogs, ArrayList.class);
		return myLogs;
	}

	@SuppressWarnings("unchecked")
	public List<LogViewModel> getFeed() {
		RestClient client = new RestClient();
		Resource res = client.resource(path + "log/feed?user=" + userBean.getUsername());
		String jsonLogs = res.accept("application/json").get(String.class);
		Gson gson = new Gson();
		myFeed = gson.fromJson(jsonLogs, ArrayList.class);
		return myFeed;
	}

	public void setMyFeed(List<LogViewModel> myFeed) {
		this.myFeed = myFeed;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public String getMessageLogs() {
		return messageLogs;
	}

	public void setMessageLogs(String messageLogs) {
		this.messageLogs = messageLogs;
	}

	public String sendWriteLog() {
		Map<String, String> log = new HashMap<String, String>();
		log.put("writer", userBean.getUsername());
		log.put("receiver", paramUser);
		log.put("body", messageLogs);
		Gson gson = new Gson();
		String json = gson.toJson(log);
		RestClient client = new RestClient();
		Resource resource = client.resource(path + "log/writelog");
		resource.contentType("application/json").accept("text/plain").post(String.class, json);
		return null;
	}

	public String sendFeedLog() {
		Map<String, String> log = new HashMap<String, String>();
		log.put("writer", userBean.getUsername());
		log.put("receiver", userBean.getUsername());
		log.put("body", messageLogs);
		Gson gson = new Gson();
		String json = gson.toJson(log);
		RestClient client = new RestClient();
		Resource resource = client.resource(path + "log/writelog");
		resource.contentType("application/json").accept("text/plain").post(String.class, json);
		return null;
	}

}