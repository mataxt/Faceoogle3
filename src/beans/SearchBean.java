package beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.primefaces.event.SelectEvent;

import com.google.gson.Gson;


@ViewScoped
@ManagedBean(name = "searchBean")
public class SearchBean implements Serializable {
	private String path = "http://130.237.84.211:8080/Faceoogle2/rest/";
	private static final long serialVersionUID = 1L;
	private String searchName;
	private ArrayList<String> result;

	@SuppressWarnings("unchecked")
	public ArrayList<String> getResult(String searchName) {
		RestClient client = new RestClient();
		Resource res = client.resource(path + "user/usernames?search=" + searchName);
		String jsonNames= res.accept("application/json").get(String.class);
		Gson gson = new Gson(); 
		result = gson.fromJson(jsonNames, ArrayList.class);
		return result;
	}
	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	public void setResult(ArrayList<String> result) {
		this.result = result;
	}
	
	public void onSelect(SelectEvent event) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("profile.xhtml?faces-redirect=true" + "&user=" + searchName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}