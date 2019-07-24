import 'dart:async';

import 'package:flutter/services.dart';

class PubnubFlutter {
  static var messageReceived;
  static var statusReceived;
  String publishKey;
  String subscribeKey;
  String secretKey;
  String channelName;

  var args;

  static const MethodChannel channelPubNub = const MethodChannel('pubnub');
  static const EventChannel messageChannel =
      const EventChannel('plugins.flutter.io/pubnub_message');
  static const EventChannel statusChannel =
      const EventChannel('plugins.flutter.io/pubnub_status');

  PubnubFlutter(
      {this.publishKey, this.subscribeKey, this.secretKey, this.channelName}) {
    args = {
      'publishKey': publishKey,
      'subscribeKey': subscribeKey,
      'secretkey': secretKey,
      'channelName': channelName
    };
    channelPubNub.invokeMethod('create', args);
  }

  unsubscribe() {
    Object result = new Object();
    if (channelPubNub != null) {
      channelPubNub.invokeMethod('unsubscribe', args);
    } else {
      new NullThrownError();
    }
  }

  subscribe() {
    if (channelPubNub != null) {
      channelPubNub.invokeMethod('subscribe', args);
    } else {
      new NullThrownError();
    }
  }

  sendMessage(String message) {
    var args = {'sender': 'Flutter', 'message': message};
    if (channelPubNub != null) {
      channelPubNub.invokeMethod('message', args);
    } else {
      new NullThrownError();
    }
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
