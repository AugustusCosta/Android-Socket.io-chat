package com.otg.socket.io;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AckCallback {
	
	private JSONObject requestData;
	
	public abstract void callback(JSONArray data) throws JSONException;

	public JSONObject getRequestData() {
		return requestData;
	}

	public void setRequestData(JSONObject requestData) {
		this.requestData = requestData;
	}
}
