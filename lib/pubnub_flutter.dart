import 'dart:async';

import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

class PubnubFlutter {
  static var messageReceived;
  static var statusReceived;
  String publishKey;
  String subscribeKey;
  String secretKey;
  String uuid;
  List<String> channelName;

  PubnubFlutter();

  var args={
    "test":"test"
  };

  static const MethodChannel channelPubNub = const MethodChannel('pubnub');
  static const EventChannel messageChannel =
      const EventChannel('plugins.flutter.io/pubnub_message');
  static const EventChannel statusChannel =
      const EventChannel('plugins.flutter.io/pubnub_status');

  Future<String> getUuidString() async {
    String result = await channelPubNub.invokeMethod('uuid', args);
    return result;
  }

  /// Initialize  Pubnub with your credential.
  /// Returns TRUE || FALSE based on result
  Future<bool> initialize(
      {
        @required String publishKey,
      @required String subscribeKey,
      String secretKey,
      @required String uuid}) async {

    this.publishKey = publishKey;
    this.subscribeKey = subscribeKey;
    this.secretKey = secretKey;
    this.uuid = uuid;

    args = {
      'publishKey': publishKey,
      'subscribeKey': subscribeKey,
      'secretkey': secretKey,
      'uuid': uuid
    };

    bool result = await channelPubNub.invokeMethod('initialize', args);
    return result;
  }

  unsubscribe() {
    Object result = new Object();
    if (channelPubNub != null) {
      channelPubNub.invokeMethod('unsubscribe', args);
    } else {
      new NullThrownError();
    }
  }

  /// Subscribe it after Initializing the app;
  /// Returns TRUE || FALSE based on result
  Future<bool> subscribe({@required List<String> channelNames}) async {
    var args={
      "channelNames":channelNames
    };
    bool result;
    if (channelPubNub != null) {
      result=await channelPubNub.invokeMethod('subscribe', args);
    } else {
      result=false;
      new NullThrownError();
    }
    return result;
  }

  /// Send message to a specific channel
  /// Returns TRUE || FALSE based on result
  Future<bool> sendMessage({@required Map<String,String> data,@required String channel}) async{
    bool result;
    var args={
      "data":data,
      "channel":channel
    };
    if (channelPubNub != null) {
      result=await channelPubNub.invokeMethod('message', args);
    } else {
      result=false;
      new NullThrownError();
    }
    return result;
  }

  /// Fires whenever the a message is received.
  Stream<dynamic> get onMessageReceived {
    print("stesting the msg stram");
    if (messageReceived == null) {
      messageReceived = messageChannel
          .receiveBroadcastStream()
          .map((dynamic event) => _parseMessage(event));
    }
    return messageReceived;
  }

  /// Fires whenever the status changes.
  Stream<dynamic> get onStatusReceived {
    if (statusReceived == null) {
      statusReceived = statusChannel
          .receiveBroadcastStream()
          .map((dynamic event) => _parseStatus(event));
    }
    return statusReceived;
  }

  dynamic _parseMessage(message) {
    return message;
  }

  dynamic _parseStatus(status) {
    return status;
  }
}


