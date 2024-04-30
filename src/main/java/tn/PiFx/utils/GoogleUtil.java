package tn.PiFx.utils;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

import java.util.Arrays;

public class GoogleUtil {

    private static final String CLIENT_ID = "655749456436-ov31bkse43uf4mavooelotq021s29dr2.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    private static GoogleAuthorizationCodeFlow flow;

    public static String getAuthorizationUrl(){
        flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                CLIENT_ID,
                CLIENT_SECRET,
                Arrays.asList("https://www.googleapis.com/auth/userinfo.profile", "https://www.googleapis.com/auth/userinfo.email"))
                .setAccessType("offline")
                .build();
        return flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();

    }

    public static Userinfo getUserInfo(String code) throws Exception{
        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
        var credential = flow.createAndStoreCredential(response, null);
        Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(),JacksonFactory.getDefaultInstance(),credential)
                .setApplicationName("NovaFx").build();

        return oauth2.userinfo().get().execute();
    }
}
