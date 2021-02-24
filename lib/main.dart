import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Welcome to Flutter',
      theme: ThemeData(
        accentColor: Colors.white,
        primaryColor: Colors.white,
        fontFamily: 'Metropolis',
        
        ),
      home: Scaffold(
        // appBar: AppBar(
        //   title: Text('Welcome to Flutter'),
        // ),



        backgroundColor: Colors.black,
        body: Container(
          padding: EdgeInsets.all(60),
          child: Row(children: [
          //   Text(
          //   'Synced \nplaylists',
          //     style: TextStyle(
          //       color: Colors.white,
          //       fontSize: 40,
          //       fontFamily: 'Metropolis',
          //     ),
               
          // ),
        //   IconButton(
        //   icon: Icon(Icons.volume_up),
        //   tooltip: 'Increase volume by 10',
        //   onPressed: () {
        //   },
        // ),
          ],)
        ),
      ),
    );
  }
}