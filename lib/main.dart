import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Welcome to Flutter',
      theme: ThemeData(
        // accentColor: Colors.white,
        // primaryColor: Colors.white,
        fontFamily: 'Metropolis',
        
        ),
      home: Scaffold(
        // appBar: AppBar(
        //   title: Text('Welcome to Flutter'),
        // ),



        backgroundColor: Colors.black,
        body: Container(
          padding: EdgeInsets.all(60),
          child: Column(children: [

            Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
            Text(
            'Synced \nplaylists',
              style: TextStyle(
                color: Colors.white,
                fontSize: 36,
                fontWeight: FontWeight.bold,
                fontFamily: 'Metropolis',
              ),
               
          ),
          IconButton(
          iconSize: 35,
          icon: Icon(Icons.cached,
          color: Colors.white,
          
          
          ),
          tooltip: 'Increase volume by 10',
          onPressed: () {
          },
        ),
          ],),

          // search bar

          TextField(
              decoration: InputDecoration(
                // border: InputBorder.,  
                hintText: 'Enter a search term',
                hintStyle: TextStyle(
                  color: Colors.grey,
                  // fontStyle: FontStyle,
                ),
              ),
            ),



          ],)        
          ),
      ),
    );
  }
}