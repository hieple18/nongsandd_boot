package com.nongsandd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import com.nongsandd.constant.Constant;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

/**
 * @author: HiepLe
 * @version: Sep 6, 2018
 */

@Service
public class TwilloService {
	public int createVerifyCode(){
		Random r = new Random();
		int code = 1000 + r.nextInt(8900);
		return code;
	}
	
	public String sendVerifyCode(int code, String phoneNum){
		return  sendMessage("NongSanDD thong bao ma xac thuc cua ban la: " + code, phoneNum);
    }
	
	public String sendMessage(String content, String phoneNum){
		String phone = "+84" + phoneNum.substring(1);
		
    	try {
            TwilioRestClient client = new TwilioRestClient(Constant.ACCOUNT_SID, Constant.AUTH_TOKEN);
     
            // Build a filter for the MessageList
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Body", content));
            params.add(new BasicNameValuePair("To", phone)); //Add real number here
            params.add(new BasicNameValuePair("From", Constant.TWILIO_NUMBER));
     
            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            Message message = messageFactory.create(params);
            System.out.println(message.getSid());
            return "good";
        }
        catch (TwilioRestException e) {
            return e.toString();
        }
    }
}
