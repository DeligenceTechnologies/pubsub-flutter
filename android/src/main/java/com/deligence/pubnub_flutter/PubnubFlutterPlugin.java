package com.deligence.pubnub_flutter;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.ArrayList;
import java.util.HashMap;

import com.deligence.pubnub_flutter.model.Message;
import com.deligence.pubnub_flutter.util.DateTimeUtil;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** PubnubFlutterPlugin */

public class PubnubFlutterPlugin implements MethodCallHandler {

  public String PUBNUB_PUBLISH_KEY ;
  public  String PUBNUB_SUBSCRIBE_KEY ;
  public PubNub pubnub;
  ArrayList channelNames;
  public static EventChannel.EventSink messageSender;
  public static EventChannel.EventSink statusSender;
  public String uuid = "";
  final private Handler mainHandler = new Handler();


  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "pubnub");
    channel.setMethodCallHandler(new PubnubFlutterPlugin());
    EventChannel messageChannel = new EventChannel(registrar.messenger(), "plugins.flutter.io/pubnub_message");
    messageChannel.setStreamHandler(new EventChannel.StreamHandler()
    {
      @Override
      public void onListen(Object arguments, EventChannel.EventSink events)
      {
        Log.d(getClass().getName(), "messageChannel.onListen");
        messageSender = events;
      }
      @Override
      public void onCancel(Object arguments)
      {
        Log.d(getClass().getName(), "messageChannel.onCancel");
      }
    });

    EventChannel statusChannel = new EventChannel(registrar.messenger(), "plugins.flutter.io/pubnub_status");
    statusChannel.setStreamHandler(new EventChannel.StreamHandler()
    {
      @Override public void onListen(Object o, EventChannel.EventSink eventSink)
      {
        Log.d(getClass().getName(), "statusChannel.onListen");
        statusSender = eventSink;
      }

      @Override public void onCancel(Object o)
      {
        Log.d(getClass().getName(), "statusChannel.onCancel");
      }
    });
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    System.out.println("CALL to the function is "+call.method);
    switch (call.method)
    {
      case "uuid":
        getUuid(call, result);
        break;
      case "initialize":
        initialize(call, result);
        break;
      case "subscribe":
        subscribeToChannel(call, result);
        break;
      case "unsubscribe":
        unSubscribeFromChannel(call, result);
        break;
      case "message":
        sendMessageToChannel(call, result);
        break;
      default:
        result.notImplemented();
    }
  }

  private void getUuid(MethodCall call, Result result){
    String generatedUUID= java.util.UUID.randomUUID().toString();
    result.success(generatedUUID);
  }

  private void initialize(MethodCall call, Result result)
  {
    PUBNUB_PUBLISH_KEY = call.argument("publishKey");
    PUBNUB_SUBSCRIBE_KEY = call.argument("subscribeKey");
    String secretKey = call.argument("secretKey");
    uuid = call.argument("uuid");

    Log.d(getClass().getName(), "Create pubnub with publishKey " + PUBNUB_PUBLISH_KEY + ", subscribeKey " + PUBNUB_SUBSCRIBE_KEY + " uuid" + uuid);

    if ((PUBNUB_PUBLISH_KEY != null && !PUBNUB_PUBLISH_KEY.isEmpty()) && (PUBNUB_SUBSCRIBE_KEY != null && !PUBNUB_SUBSCRIBE_KEY.isEmpty()))
    {
      PNConfiguration pnConfiguration = new PNConfiguration();
      pnConfiguration.setPublishKey(PUBNUB_PUBLISH_KEY);
      pnConfiguration.setSubscribeKey(PUBNUB_SUBSCRIBE_KEY);
      pnConfiguration.setUuid(uuid);
      pnConfiguration.setSecure(true);
      pnConfiguration.setLogVerbosity(PNLogVerbosity.BODY);

      pubnub = new PubNub(pnConfiguration);
      startListener();
      Log.d(getClass().getName(), "PubNub configuration created");
      result.success(true);
    }
    else
    {
      Log.d(getClass().getName(), "Keys should not be null");
      result.success(false);
    }
  }

  public void startListener(){
    pubnub.addListener(new SubscribeCallback()
    {
      @Override public void status(PubNub pubnub, PNStatus status)
      {
        if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
          HashMap res = new HashMap();
          res.put("status",false);
          res.put("type","disconnected");
          res.put("reason","This event happens when radio / connectivity is lost");
          statusSender.success(res);
        }
        else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {
          HashMap res = new HashMap();
          res.put("status",true);
          res.put("type","reconnected");
          res.put("reason","This event happens when radio / connectivity is lost, then regained");
          statusSender.success(res);
        }
        else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {
          HashMap res = new HashMap();
          res.put("status",false);
          res.put("type","error");
          res.put("reason","This event when client configured to encrypt messages and on live data feed it received plain text.");
          statusSender.success(res);
        }
        else if (status.getCategory() == PNStatusCategory.PNConnectedCategory)
        {
          HashMap res = new HashMap();
          res.put("status",true);
          res.put("type","connected");
          res.put("reason","HURRAY -:)   Successfully Connected");
          statusSender.success(res);
        }
        else
        {
          HashMap res = new HashMap();
          res.put("status",false);
          res.put("type","failed");
          res.put("reason","Something went wrong");
          statusSender.success(res);
        }
      }

      @Override public void message(PubNub pubnub, final PNMessageResult message)
      {
        if (message != null)
        {
          try
          {
            mainHandler.post(new Runnable() {
              @Override
              public void run() {
                messageSender.success(message.getMessage().toString());
              }
            });
          }
          catch (Exception e)
          {
            System.out.println("Failed to parse message");
            messageSender.success("Failed to parse message");
            e.printStackTrace();
          }
        }
      }

      @Override public void presence(PubNub pubnub, PNPresenceEventResult presence)
      {
        Log.d(getClass().getName(), "Presence: getChannel " + presence.getChannel() + "getEvent " + presence.getEvent() + "getSubscription " + presence.getSubscription() + "getUuid " + presence.getUuid());
      }
    });
  }


  private void subscribeToChannel(MethodCall call, final Result result)
  {
     channelNames = call.argument("channelNames");

    Log.d(getClass().getName(), "Attempt to Subscribe to channel: " + channelNames.toString());
    try
    {
      pubnub.subscribe().channels(channelNames).execute();
      result.success(true);
    }
    catch (Exception e)
    {
      Log.d(getClass().getName(), e.getMessage());
      result.success(false);
    }
  }


  private void unSubscribeFromChannel(MethodCall call, final Result result)
  {
    try
    {
      pubnub.unsubscribe().channels(channelNames).execute();
    }
    catch (Exception e)
    {
      Log.d(getClass().getName(), e.getMessage());
      result.success(false);
    }
  }

  private void sendMessageToChannel(MethodCall call, final Result result)
  {
    HashMap<String,String> message =call.argument("data");
    String channel=call.argument("channel");

    pubnub.publish().channel(channel).message(message).async(
            new PNCallback()
            {
              @Override public void onResponse(Object object, PNStatus status)
              {
                try
                {
                  if (!status.isError())
                  {
                    Log.v(getClass().getName(), "publish(" + object.toString() + ")");
                    result.success(true);
                  }
                  else
                  {
                    Log.v(getClass().getName(), "publishErr(" + status.getErrorData().toString() + ")");
                    result.success(false);
                  }
                }
                catch (Exception e)
                {
                  e.printStackTrace();
                  result.success(false);
                }
              }
            }
    );
  }
}
