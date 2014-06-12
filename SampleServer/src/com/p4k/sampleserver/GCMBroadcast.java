package com.p4k.sampleserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

/**
 * Servlet implementation class GCMBroadcast
 */
@WebServlet("/GCMBroadcast")
public class GCMBroadcast extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// The SENDER_ID here is the "Browser Key" that was generated when I
	// created the API keys for my Google APIs project.
	private static final String SENDER_ID = "AIzaSyCzF73Xl9TKSc6f4CcNws5uG9MbIjgZnE0";
	
	// This is a *cheat*  It is a hard-coded registration ID from an Android device
	// that registered itself with GCM using the same project id shown above.
	// ASUS Nexus 7 
	// private static final String ANDROID_DEVICE = "APA91bGUZwIdNbhRnB2wfF7SsmOgrv0lweqq_-ehOvi_cLwDlyhJrV29qwrdC98Ekh--kKzRbsAcFYd7oG__odAc_vJxepToougQGBsKC_zulYaJTe5SW_e2wPPgRyZkWgGJGPIKiE3oSytreBc_cN2ADbZxse20AGi0FJGgmPc2EoE_M8Tg-gA";
	private static final String ANDROID_DEVICE1 = "APA91bGUZwIdNbhRnB2wfF7SsmOgrv0lweqq_-ehOvi_cLwDlyhJrV29qwrdC98Ekh--kKzRbsAcFYd7oG__odAc_vJxepToougQGBsKC_zulYaJTe5SW_e2wPPgRyZkWgGJGPIKiE3oSytreBc_cN2ADbZxse20AGi0FJGgmPc2EoE_M8Tg-gA";
		
	//Auxus Nuclea N1 Key
	private static final String ANDROID_DEVICE2 = "APA91bHriUEUF1U7DNw9j4pjHr_StThMSM8sBvvKeyhBVuoJzvQfJyUE7LeB2mna3c4Pp-ZP-0ndpoDIvaGA88CHWMJl0GVxC46oBrCdzC1KR1M2LSMKx_Vui5KsIi6hCDZAKoZIKVH7iZ7Zrbe8NUzKdj9vWIjaAR3tOrhvEitNAMQTOsZz6MY";
	
	// This array will hold all the registration ids used to broadcast a message.
	// for this demo, it will only have the ANDROID_DEVICE id that was captured 
	// when we ran the Android client app through Eclipse.
	private List<String> androidTargets = new ArrayList<String>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GCMBroadcast() {
    	
        super();

        // we'll only add the hard-coded *cheat* target device registration id 
        // for this demo.
        androidTargets.add(ANDROID_DEVICE1);
        androidTargets.add(ANDROID_DEVICE2);
        
    }
    
    // This doPost() method is called from the form in our index.jsp file.
    // It will broadcast the passed "Message" value.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// We'll collect the "CollapseKey" and "Message" values from our JSP page
		String collapseKey = "";
		String userMessage = "";
		
		try {
			userMessage = request.getParameter("Message");
			collapseKey = request.getParameter("CollapseKey");
			System.out.println( "Message and CollapseKey are " + userMessage + "--" + collapseKey );
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		//p4k - Demo GCM Server code took from internet. 

		// Instance of com.android.gcm.server.Sender, that does the
		// transmission of a Message to the Google Cloud Messaging service.
		Sender sender = new Sender(SENDER_ID);
		
		// This Message object will hold the data that is being transmitted
		// to the Android client devices.  For this demo, it is a simple text
		// string, but could certainly be a JSON object.
		Message message = new Message.Builder()
		
		// If multiple messages are sent using the same .collapseKey()
		// the android target device, if it was offline during earlier message
		// transmissions, will only receive the latest message for that key when
		// it goes back on-line.
		.collapseKey(collapseKey)
		.timeToLive(30)
		.delayWhileIdle(true)
		.addData("message", userMessage)
		.build();
		
		try {
			// use this for multicast messages.  The second parameter
			// of sender.send() will need to be an array of register ids.
			MulticastResult result = sender.send(message, androidTargets, 1);
			
			if (result.getResults() != null) {
				int canonicalRegId = result.getCanonicalIds();
				if (canonicalRegId != 0) {
					
				}
			} else {
				int error = result.getFailure();
				System.out.println("Broadcast failure: " + error);
			}
			
		} catch (Exception e) {
			System.out.println( "Exception while trying to broadcast messages" + e.getMessage() );
			e.printStackTrace();
		}
		
		// We'll pass the CollapseKey and Message values back to index.jsp, only so
		// we can display it in our form again.
		request.setAttribute("CollapseKey", collapseKey);
		request.setAttribute("Message", userMessage);
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
				
	}

}
