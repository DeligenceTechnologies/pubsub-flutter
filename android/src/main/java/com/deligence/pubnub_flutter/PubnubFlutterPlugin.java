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

import java.util.Arrays;
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
  /** Plugin registration. */
  
  public static final String PUBNUB_PUBLISH_KEY = "pub-c-950ad2a2-27c2-4571-a923-672e76be463e";
  public static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-1519beb8-ad12-11e9-a732-8a2b99383297";
  public PubNub pubnub;
  public String channelName = "test";
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
    switch (call.method)
    {
      case "create":
        createChannel(call, result);
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

  private void createChannel(MethodCall call, Result result)
  {
    String publishKey = call.argument("publishKey");
    String subscribeKey = call.argument("subscribeKey");
    String secretKey = call.argument("secretKey");
    uuid = java.util.UUID.randomUUID().toString();

    Log.d(getClass().getName(), "Create pubnub with publishKey " + publishKey + ", subscribeKey " + subscribeKey + " uuid" + uuid);

    if ((publishKey != null && !publishKey.isEmpty()) && (subscribeKey != null && !subscribeKey.isEmpty()))
    {
      PNConfiguration pnConfiguration = new PNConfiguration();
      pnConfiguration.setPublishKey(publishKey);
      pnConfiguration.setSubscribeKey(subscribeKey);
      pnConfiguration.setUuid(uuid);
      pnConfiguration.setSecretKey(secretKey);
      pnConfiguration.setSecure(true);
      pnConfiguration.setLogVerbosity(PNLogVerbosity.BODY);

      pubnub = new PubNub(pnConfiguration);
      Log.d(getClass().getName(), "PubNub configuration created");
      result.success("PubNub configuration created");
    }
    else
    {
      Log.d(getClass().getName(), "Keys should not be null");
      result.success("Keys should not be null");
    }
  }

  private void subscribeToChannel(MethodCall call, final Result result)
  {
    channelName = call.argument("channelName");
    String publishKey = call.argument("publishKey");
    String subscribeKey = call.argument("subscribeKey");
    String secretKey = call.argument("secretKey");

    Log.d(getClass().getName(), "Attempt to Subscribe to channel: " + channelName);
    try
    {
      pubnub.addListener(new SubscribeCallback()
      {
        @Override public void status(PubNub pubnub, PNStatus status)
        {
//          statusSender.success("Ready to listen messages"+pubnub.fetchMessages());
          notifyListner(status.toString());
//          if (status.getCategory() == PNStatusCategory.PNConnectedCategory)
//          {
//            Log.d(getClass().getName(), "Subscription was successful at channel " + channelName);
//            statusSender.success("Subscription was successful at channel " + channelName);
//            messageSender.success("Ready to listen messages");
//            result.success(true);
//          }
//          if (status.getCategory() == PNStatusCategory.P)
//          {
//            Log.d(getClass().getName(), "Subscription was successful at channel " + channelName);
//            statusSender.success("Subscription was successful at channel " + channelName);
//            messageSender.success("Ready to listen messages");
//            result.success(true);
//          }
//          else
//          {
//            Log.d(getClass().getName(), "Subscription failed at channel" + channelName);
////            statusSender.success("Subscription failed at channel " + channelName );
////            result.success(false);
//          }
        }

        @Override public void message(PubNub pubnub, final PNMessageResult message)
        {
          //If is not your message
          if (message != null)
          {
            try
            {
              mainHandler.post(new Runnable() {
                @Override
                public void run() {
                  statusSender.success("Help given to the thread"+message.getMessage().toString());
                }
              });
//              notifyListner(message.getMessage().toString());
//              mainHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                  result.success(finalPath);
//                }
//              });
//              Message receivedMessage = new Gson().fromJson(message.getMessage(), Message.class);
//              receivedMessage.setChannel(message.getChannel());
//              receivedMessage.setPublisher(message.getPublisher());
//              messageSender.success(receivedMessage.getMessage());
            }
            catch (Exception e)
            {
//              messageSender.success("Failed to parse message");
              e.printStackTrace();
            }
          }
        }

        @Override public void presence(PubNub pubnub, PNPresenceEventResult presence)
        {
          Log.d(getClass().getName(), "Presence: getChannel " + presence.getChannel() + "getEvent " + presence.getEvent() + "getSubscription " + presence.getSubscription() + "getUuid " + presence.getUuid());
        }
      });

      pubnub.subscribe().channels(Arrays.asList(channelName)).execute();
    }
    catch (Exception e)
    {
      Log.d(getClass().getName(), e.getMessage());
      result.success(false);
    }
  }

  public void notifyListner(String msg){
    statusSender.success("RESULT"+msg);
  }

  private void unSubscribeFromChannel(MethodCall call, final Result result)
  {
    channelName = call.argument("channelName");
    Log.d(getClass().getName(), "^%^%&^%&^%&^%&^%&^%&^%&^%&^%&^%&^%&^%^&Attempt to Unsubscribe to channel: " + channelName);

    try
    {
      pubnub.unsubscribe().channels(Arrays.asList(channelName)).execute();
    }
    catch (Exception e)
    {
      Log.d(getClass().getName(), e.getMessage());
      result.success(false);
    }
  }

  private void sendMessageToChannel(MethodCall call, final Result result)
  {
    HashMap<String, String> message = new HashMap<>();
    message.put("sender", (String)call.argument("sender"));
    message.put("message", (String)call.argument("message"));
    message.put("timestamp", DateTimeUtil.getTimeStampUtc());

    pubnub.publish().channel(channelName).message(message).async(
            new PNCallback()
            {
              @Override public void onResponse(Object object, PNStatus status)
              {
                try
                {
                  if (!status.isError())
                  {
                    Log.v(getClass().getName(), "publish(" + object + ")");
//                    result.success(true);
                  }
                  else
                  {
                    Log.v(getClass().getName(), "publishErr(" + status.getErrorData() + ")");
//                    result.success(false);
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
