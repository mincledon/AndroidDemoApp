package com.example.androiddemoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mparticle.MPEvent;
import com.mparticle.MParticle;
import com.mparticle.MParticleOptions;
import com.mparticle.identity.IdentityApi;
import com.mparticle.identity.IdentityApiRequest;
import com.mparticle.identity.IdentityApiResult;
import com.mparticle.identity.IdentityHttpResponse;
import com.mparticle.identity.MParticleUser;
import com.mparticle.identity.TaskFailureListener;
import com.mparticle.identity.TaskSuccessListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // mParticle SDK initialization
        MParticleOptions options = MParticleOptions.builder(this)
                .credentials("d1f8fc026cbb5747a4982e597258ab88", "WCG05OfpRMCvsAXRkXC6iUKuRDwP8x4JzvR8vfI53MiN9P2ohK3DvxdDJ-mKrCix")
                .environment(MParticle.Environment.Development)
                .logLevel(MParticle.LogLevel.VERBOSE)
                .dataplan("matt_data_plan_android", 1)
                .build();
        MParticle.start(options);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

        // Log event for send message button click, add mPID as an event attribute
        long mpid_temp;
        mpid_temp = MParticle.getInstance().Identity().getCurrentUser().getId();
        String mpid = Long.toString(mpid_temp);

        Map<String, String> button_click_eventInfo = new HashMap<String, String>();
        button_click_eventInfo.put("category", "Button_Click");
        button_click_eventInfo.put("title", "message_send");
        button_click_eventInfo.put("mpid", mpid);

        MPEvent button_click_event = new MPEvent.Builder("Msg_Button_Clicked", MParticle.EventType.Search)
                .customAttributes(button_click_eventInfo)
                .build();

        MParticle.getInstance().logEvent(button_click_event);

        // Log an event with text send in message, add current date as an event attribute
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);

        Map<String, String> message_sent_eventInfo = new HashMap<String, String>();
        message_sent_eventInfo.put("message", message);
        message_sent_eventInfo.put("date_time", strDate);

        MPEvent text_event = new MPEvent.Builder("Text Sent", MParticle.EventType.Other)
                .customAttributes(message_sent_eventInfo)
                .build();

        MParticle.getInstance().logEvent(text_event);

    }


    /** Called when the user taps the Login CustomerId button */
    public void loginCustomeridAction(View view) {

        // pull email address from text field
        EditText customerId = (EditText) findViewById(R.id.editText2);
        // turn input into string
        String customerId_str = customerId.getText().toString();

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);

        // create identity request with email input
        IdentityApiRequest identityRequest = IdentityApiRequest.withEmptyUser()
                .customerId(customerId_str)
                .build();

        MParticle.getInstance().Identity().login(identityRequest)

                .addFailureListener(new TaskFailureListener() {
                    @Override
                    public void onFailure(IdentityHttpResponse identityHttpResponse) {
                        if (identityHttpResponse.getHttpCode() == IdentityApi.UNKNOWN_ERROR) {
                            //device is likely offline and request should be retried
                        } else if (identityHttpResponse.getHttpCode() == IdentityApi.THROTTLE_ERROR) {
                            //on rare occurrences you may receive and retry throttling errors (429)
                        }
                        Log.d("IDSync Error", identityHttpResponse.toString());
                    }
                })
                .addSuccessListener(( new TaskSuccessListener() {
                    @Override
                    public void onSuccess(@NonNull IdentityApiResult identityApiResult) {
                        // Note: may return null if the SDK has yet to acquire a user via IDSync!
                        MParticleUser currentUser = MParticle.getInstance().Identity().getCurrentUser();

                        // Set user attributes associated with the user
                        currentUser.setUserAttribute("Android_Attr1","Matt");
                        currentUser.setUserAttribute("$Country","US");
                        currentUser.setUserAttribute("$Age","21");
                    }
                }));

    }
    /** Called when the user taps the Login Email button */
    public void loginEmailAction(View view) {

        // pull email address from text field
        EditText email = (EditText) findViewById(R.id.editText3);
        // turn input into string
        String email_str = email.getText().toString();

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);

        // create identity request with email input
        IdentityApiRequest identityRequest = IdentityApiRequest.withEmptyUser()
                .email(email_str)
                .build();

        MParticle.getInstance().Identity().login(identityRequest)

                .addFailureListener(new TaskFailureListener() {
                    @Override
                    public void onFailure(IdentityHttpResponse identityHttpResponse) {
                        if (identityHttpResponse.getHttpCode() == IdentityApi.UNKNOWN_ERROR) {
                            //device is likely offline and request should be retried
                        } else if (identityHttpResponse.getHttpCode() == IdentityApi.THROTTLE_ERROR) {
                            //on rare occurrences you may receive and retry throttling errors (429)
                        }
                        Log.d("IDSync Error", identityHttpResponse.toString());
                    }
                })
                .addSuccessListener(( new TaskSuccessListener() {
                    @Override
                    public void onSuccess(@NonNull IdentityApiResult identityApiResult) {
                        // Note: may return null if the SDK has yet to acquire a user via IDSync!
                        MParticleUser currentUser = MParticle.getInstance().Identity().getCurrentUser();

                        // Set user attributes associated with the user
                        currentUser.setUserAttribute("Android_Attr1","Matt");
                        currentUser.setUserAttribute("$Country","US");
                        currentUser.setUserAttribute("$Age","21");
                    }
                }));

    }

    /** Called when the user taps the Logout button */
    public void logoutAction(View view) {

        // create identity object for logout testing where email is sent with logout
        // pull email address from text field
        EditText email = (EditText) findViewById(R.id.editText3);
        // turn input into string
        String email_str = email.getText().toString();
        IdentityApiRequest identityRequest = IdentityApiRequest.withEmptyUser()
                .userIdentity(MParticle.IdentityType.Other, email_str)
                .build();

        MParticle.getInstance().Identity().logout(identityRequest);

    }
}
