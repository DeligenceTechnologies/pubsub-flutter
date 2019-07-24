import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:pubnub_flutter/pubnub_flutter.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  MyAppState createState() => MyAppState();
}

class MyAppState extends State<MyApp> {
  PubnubFlutter pubNubFlutter;
  String receivedStatus = 'Status: Unknown';
  String receivedMessage = '';
  String sendMessage = '';

  @override
  void initState() {
    super.initState();
    pubNubFlutter = PubnubFlutter(
        publishKey: "pub-c-950ad2a2-27c2-4571-a923-672e76be463e",
        subscribeKey: "sub-c-1519beb8-ad12-11e9-a732-8a2b99383297",
        secretKey: "sec-c-ZTgyN2Q2ZjctOGZjZi00ZmE5LTlhYTYtM2Y4ODU0MTQ1ZmRl",
        channelName: "test234");

    pubNubFlutter.onStatusReceived.listen((status) {
      setState(() {
        receivedStatus = status;
      });
    });
    pubNubFlutter.onMessageReceived.listen((message) {
      print("My msg are " + message.toString());
//      setState(() {
//        receivedMessage = message;
//      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('PubNub'),
        ),
        body: Center(
            child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            new Expanded(
              child: new Text(
                receivedStatus,
                style: new TextStyle(color: Colors.black45),
              ),
            ),
            new Expanded(
              child: new Text(
                receivedMessage,
                style: new TextStyle(color: Colors.black45),
              ),
            ),
            TextField(
              maxLength: 80,
              onChanged: (text) {
                sendMessage = text;
              },
              decoration: InputDecoration(
                  border: OutlineInputBorder(),
                  hintText: "Message to send",
                  hintStyle: TextStyle(
                      fontWeight: FontWeight.w300, color: Colors.grey)),
              style:
                  TextStyle(color: Colors.black, fontWeight: FontWeight.w300),
            ),
            Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[
                  FlatButton(
                      color: Colors.black12,
                      onPressed: () {
                        pubNubFlutter.unsubscribe();
                      },
                      child: Text("Unsubscribe")),
                  FlatButton(
                      color: Colors.black12,
                      onPressed: () {
                        pubNubFlutter.subscribe();
                      },
                      child: Text("Subscribe"))
                ]),
            FlatButton(
                color: Colors.black12,
                onPressed: () {
                  pubNubFlutter.sendMessage(sendMessage);
                },
                child: Text("Send Message")),
          ],
        )),
      ),
    );
  }
}
