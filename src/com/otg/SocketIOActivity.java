package com.otg;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.otg.socket.io.IOSocket;
import com.otg.socket.io.MessageCallback;

public class SocketIOActivity extends Activity {
	
	EditText inputTextView;
	TextView chatTextView;
	IOSocket socket;
	Button button;
	Handler mHandler;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        inputTextView = (EditText) findViewById(R.id.inputTextView);
        chatTextView = (TextView) findViewById(R.id.chatTextView);
        button = (Button) findViewById(R.id.button);
        mHandler = new Handler();
        
        socket = new IOSocket("http://192.168.0.146:8080", new MessageCallback() {
        //socket = new IOSocket("http://10.0.2.5:8080", new MessageCallback() {
        //socket = new IOSocket("http://10.0.2.2:8080", new MessageCallback() {
        	  @Override
        	  public void on(final String event, final JSONObject... data) {
        		  mHandler.post(new Runnable() { 
        			    public void run() { 
        			    	try {
	        			    	if(event.equals("updatechat")){
	        			    		
	        			    			
										updateChat(event + " - " + data[0].getString("username") + ": " + data[0].getString("data"));
									
	        			    	}
	        			    	if(event.equals("updateusers")){
	        			    		updateChat(event);
	        			    		JSONArray names = data[0].names();
	        			    		for(int i = 0; i < names.length(); i++){
	        			    			updateChat(names.get(i).toString());
	        			    		}
	        			    	}
        			    	
        			    	} catch (JSONException e) {
								updateChat("ERRO: " + e.getMessage());
							}
        			    	 
        			    	
        			    } 
        			  }); 
        	    
        	  }

        	  @Override
        	  public void onMessage(final String message) {
        		  mHandler.post(new Runnable() { 
      			    public void run() { updateChat(message); } 
      			  });
        		  
        	  }

        	  @Override
        	  public void onMessage(final JSONObject message) {
        		  mHandler.post(new Runnable() { 
        			    public void run() { updateChat(message.toString()); } 
        			  });
        		  
        	  }

        	  @Override
        	  public void onConnect() {
        		  mHandler.post(new Runnable() { 
      			    public void run() { updateChat("CONNECT"); button.setEnabled(true); adduser("ANDROID"); } 
      			  });
        		  
        	  }

        	  @Override
        	  public void onDisconnect() {
        		  mHandler.post(new Runnable() { 
        			    public void run() { updateChat("DISCONNECT"); button.setEnabled(true); } 
        			  });
        		  
        	  }

			  @Override
			  public void onConnectFailure() {
				  mHandler.post(new Runnable() { 
      			    public void run() { updateChat("CONNECT_FAILURE"); } 
      			  });
				  
			  }
        	});

        	socket.connect();
    }
    
    private void updateChat(String msg){
    	String text = chatTextView.getText().toString();
    	text += "\n";
    	text += msg;
    	chatTextView.setText(text);
    }
    
    private void sendMsg(String msg){
    	// simple message
//    	try {
//			socket.send("Hello world");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	try {
			socket.emit("sendchat", new JSONObject().put("menssagem", msg));
		} catch (IOException e) {
			chatTextView.setText(e.getMessage());
		} catch (JSONException e) {
			chatTextView.setText(e.getMessage());
		}

    	
    }
    
    private void adduser(String name){
    	// event with a json message
    	try {
    		socket.emit("adduser", new JSONObject().put("username", name));
		} catch (IOException e) {
			chatTextView.setText(e.getMessage());
		} catch (JSONException e) {
			chatTextView.setText(e.getMessage());
		}
    }
    
    public void sendButonClick(View v){
    	String msg = inputTextView.getText().toString();
    	if(msg!= null && msg.length() > 0){
    		sendMsg(msg);
    		inputTextView.setText("");
    	}
    }
}