import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:pubnub_flutter/pubnub_flutter.dart';
import 'package:pubnub_flutter_example/chat.dart';

class Connect extends StatefulWidget {
  @override
  _ConnectState createState() => _ConnectState();
}

class _ConnectState extends State<Connect> {
  String channel = "";
  PubnubFlutter pubNubFlutter=PubnubFlutter();
  String receivedStatus = "";

  @override
  void initState() {
//    getuuid();
    // pubNubFlutter = PubnubFlutter(
    //     publishKey: "pub-c-950ad2a2-27c2-4571-a923-672e76be463e",
    //     subscribeKey: "sub-c-1519beb8-ad12-11e9-a732-8a2b99383297",
    //     secretKey: "sec-c-ZTgyN2Q2ZjctOGZjZi00ZmE5LTlhYTYtM2Y4ODU0MTQ1ZmRl",
    //     channelName: "test234");

    // pubNubFlutter.onStatusReceived.listen((status) {
    //   if (status == "connected") {
    //     Navigator.push(
    //         context,
    //         CupertinoPageRoute(
    //             builder: (context) => Chat(
    //                   pubNubFlutter: pubNubFlutter,
    //                 )));
    //   }
    // });

    super.initState();
  }

  getuuid() async {
    String myId =await  pubNubFlutter.getUuidString();
    bool ini=await pubNubFlutter.initialize(
           publishKey: "pub-c-950ad2a2-27c2-4571-a923-672e76be463e",
           subscribeKey: "sub-c-1519beb8-ad12-11e9-a732-8a2b99383297",
           uuid: myId,
           secretKey: "sec-c-ZTgyN2Q2ZjctOGZjZi00ZmE5LTlhYTYtM2Y4ODU0MTQ1ZmRl");
    print("\n\n\n Initialize : $ini \n\n\n");
    if(ini){
       pubNubFlutter.onStatusReceived.listen((status) {
         print("check your status $status");
         if (status['status'] == true && status['type']=="connected") {
              print("\n\n CONNECTED \n\n");
         }
       });

       pubNubFlutter.onMessageReceived.listen((status) {
           print("\n\n RECEIVED MESSAGE IS $status \n\n");
       });

       bool sub = await pubNubFlutter.subscribe(channelNames: ['test@234']);

       print("\n\n\n Subscrile : $sub \n\n\n");
    }
  }

  sendMessage({String channel, Map<String,String> data}) async {
    bool msg=await pubNubFlutter.sendMessage(data: data, channel: channel);
    print("\n\n MESSAGE SENT : $msg \n\n");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(18.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            TextField(
              decoration: InputDecoration(border: OutlineInputBorder()),
              onChanged: (_) {
                channel = _;
              },
            ),
            SizedBox(
              height: 30.0,
            ),
            CupertinoButton(
              color: Colors.red,
              onPressed: () {
                getuuid();
//                subscribeToChannel();
              },
              child: Text(
                "Connect",
                style: TextStyle(color: Colors.white),
              ),
            ),
            SizedBox(
              height: 30.0,
            ),
            CupertinoButton(
              color: Colors.green,
              onPressed: () {
                sendMessage(data: {
                  "msg":"Something something something ...."

                },channel: "test@234");
              },
              child: Text(
                "Channel test@234",
                style: TextStyle(color: Colors.white),
              ),
            ),
            SizedBox(
              height: 30.0,
            ),
            CupertinoButton(
              color: Colors.blue,
              onPressed: () {
                sendMessage(data: {
                  "msg":"Something something something ...."

                },channel: "wrong");
              },
              child: Text(
                "Channel wrong",
                style: TextStyle(color: Colors.white),
              ),
            ),
            SizedBox(
              height: 30.0,
            ),
            CupertinoButton(
              color: Colors.orange,
              onPressed: () {
                sendMessage(data: {
                  "msg":"Something something something ...."

                },channel: "test@234");
              },
              child: Text(
                "Channel test@234",
                style: TextStyle(color: Colors.white),
              ),
            )

          ],
        ),
      ),
    );
  }

  @override
  void dispose() {
    super.dispose();
  }
}
