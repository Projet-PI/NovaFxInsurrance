package tn.esprit.controllers.DEVIS;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class Example {
    // Find your Account SID and Auth Token at twilio.com/console
    // and set the environment variables. See http://twil.io/secure
    public static final String ACCOUNT_SID = "ACc4e7579ae71752b1382aadb852bf525d";
    public static final String AUTH_TOKEN = "c3740f22310f186f6a7846594e83c509";
    public static final String TWILIO_PHONE_NUMBER = "+17575126258";
    String text;
    public void send_sms(String text) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+21696014188"),
                        new com.twilio.type.PhoneNumber("+216 "),
                        text)
                .create();

        System.out.println(message.getSid());
    }

    public void main(String[] args) {


        Example main=new Example();
        main.send_sms(text);
    }
}