import 'package:flutter/material.dart';
import 'package:pubnub_flutter/pubnub_flutter.dart';
//
//class Chat extends StatefulWidget {
//  final PubnubFlutter pubNubFlutter;
//  Chat({this.pubNubFlutter});
//
//  @override
//  _ChatState createState() => _ChatState();
//}
//
//class _ChatState extends State<Chat> {
//  String receivedMessage = "";
//  String msg = "";
//  final myController = TextEditingController();
//
//  @override
//  void initState() {
//    widget.pubNubFlutter.onMessageReceived.listen((message) {
//      print("My msg are " + message.toString());
//      setState(() {
//        receivedMessage = message;
//        myController.clear();
//        msg = "";
//      });
//    });
//    super.initState();
//  }
//
//  @override
//  Widget build(BuildContext context) {
//    return Scaffold(
//      appBar: AppBar(
//        backgroundColor: Colors.red,
//        title: Text(
//          "Chat",
//          style: TextStyle(color: Colors.white),
//        ),
//      ),
//      body: Column(
//        children: <Widget>[
//          Expanded(
//            child: Container(
//              child: ListView(
//                children: <Widget>[
//                  Container(
//                    height: 100.0,
//                    color: Colors.red,
//                  ),
//                  Container(
//                    height: 100.0,
//                    color: Colors.blue,
//                    child: Center(
//                      child: Text("Data $receivedMessage"),
//                    ),
//                  ),
//                  Container(
//                    height: 100.0,
//                    color: Colors.green,
//                  )
//                ],
//              ),
//            ),
//          ),
//          Row(
//            children: <Widget>[
//              Expanded(
//                child: Padding(
//                  padding: const EdgeInsets.all(8.0),
//                  child: TextField(
//                    controller: myController,
//                    onChanged: (_) {
//                      msg = _;
//                    },
//                    decoration: InputDecoration(border: OutlineInputBorder()),
//                  ),
//                ),
//              ),
//              SizedBox(
//                width: 20.0,
//              ),
//              InkWell(
//                  onTap: () {
//                    widget.pubNubFlutter.sendMessage(msg);
//                  },
//                  child: Icon(
//                    Icons.send,
//                    color: Colors.red,
//                  )),
//              SizedBox(
//                width: 20.0,
//              ),
//            ],
//          )
//        ],
//      ),
//    );
//  }
//
//  @override
//  void dispose() {
//    print("-----------------------------------_DISPOSE-----------------------");
//    myController.dispose();
//    widget.pubNubFlutter.unsubscribe();
//    super.dispose();
//  }
//}


class Chat extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container();
  }
}
