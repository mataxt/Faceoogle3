package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.gson.Gson;

import database.LogDB;
import model.Log;
import model.User;
import vm.LogViewModel;


@Path("/log")
public class LogLogic {
	
	@GET
	@Path("/logs")
	@Produces("application/json")
	public String getLogs(@QueryParam("recv") String receiver) {
		List<LogViewModel> lvm = new ArrayList<LogViewModel>();
		List<Log> original = LogDB.listUserLogs(receiver);
		
		for (Log log : original) {
			log.setBody(log.getBody().replaceAll("(.{60})", "$1\n"));
			lvm.add(new LogViewModel(log));
		}
		String json = null;
		if(lvm.size() > 0){
		 Gson gson = new Gson();
		 json = gson.toJson(lvm, ArrayList.class);
		}
		return json;
	}

	@GET
	@Path("/feed")
	@Produces("application/json")
	public String getFeed(@QueryParam("user") String username) {
		User usr = new User();
		usr.setUsername(username);
		List<LogViewModel> lvm = new ArrayList<LogViewModel>();
		List<Log> original = LogDB.listUserFeed(usr);
		
		for (Log log : original) {
			log.setBody(log.getBody().replaceAll("(.{60})", "$1\n"));
			lvm.add(new LogViewModel(log));
		}
		String json = null;
		if(lvm.size() > 0){
		 Gson gson = new Gson();
		 json = gson.toJson(lvm, ArrayList.class);
		}
		return json;
	}
	@SuppressWarnings("unchecked")
	@POST
	@Path("/writelog")
	@Consumes("application/json")
	public Integer writeLog(String json) {
		Gson gson = new Gson();
		Map<String,String> jsonLog = gson.fromJson(json, HashMap.class);
		Log log = new Log(jsonLog.get("writer"),jsonLog.get("receiver"),jsonLog.get("body"));
		return LogDB.addLog(log);
	}
}
