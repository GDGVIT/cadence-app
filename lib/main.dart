import 'package:flutter/material.dart';
import 'package:custom_switch/custom_switch.dart';
import 'data.dart';
import 'package:cadence/views/loginpage.dart';
// import 'views/setalarm.dart';
// import 'spotify.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // const MyApp({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Welcome to Flutter',
      theme: ThemeData(
        // accentColor: Colors.white,
        // primaryColor: Colors.white,
        fontFamily: 'Metropolis',
        // elevatedButtonTheme: ElevatedButtonThemeData(
        //     style: ElevatedButton.styleFrom(
        //       primary: Colors.purple,
        //     ),
        //   ),
      ),
      // home: Alarmscreen(),
      home: Login(),
      // home: Playlist(),
      // home: Mg(),
      // home: Spot(),
    );
  }
}
class Playlist extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // appBar: AppBar(
      //   title: Text('Welcome to Flutter'),
      // ),
      backgroundColor: Colors.black,
      body: Container(
          padding: EdgeInsets.all(50),
          child: Column(
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    'Synced \nplaylists',
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 36,
                      fontWeight: FontWeight.w700,
                      fontFamily: 'Metropolis',
                      // fontStyle: FontStyle.italic
                    ),
                  ),
                  IconButton(
                    iconSize: 35,
                    icon: Icon(
                      Icons.cached,
                      color: Colors.white,
                    ),
                    tooltip: 'Increase volume by 10',
                    onPressed: () {},
                  ),
                ],
              ),

              // search bar
              Padding(
                padding: EdgeInsets.fromLTRB(10, 50, 10, 40),
                child: TextField(
                  decoration: InputDecoration(
                    // border: InputBorder.,
                    hintText: 'Enter a search term',
                    hintStyle: TextStyle(
                      color: Colors.grey,
                      fontFamily: 'Metropolis',
                      // fontStyle: FontStyle,
                    ),
                  ),
                ),
              ),

            Expanded(
              
              child: ListView(
                children: [
                   Container(
                    height: 90,
                    decoration: BoxDecoration(
                    border: Border(
                      bottom: BorderSide(width: 0.5, color: Colors.grey[900]),
                    ),
                    color: Colors.transparent,
                    ),
                    
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      children: [
                        Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children:[
                            Radio(
                              activeColor: Colors.blue,
                              onChanged:null,
                              value:0,
                              groupValue:null,
                              ),
                          ]
                        ),
                        
                        Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Image.network('https://i.scdn.co/image/ab67616d0000b273bec74947ee43d003b6d8ee9f',
                            height:55,
                            )
                          ],
                        ),
                        
                        
                        Container(
                            margin: EdgeInsets.fromLTRB(20, 0, 0, 0),
                            child: Column(
                            crossAxisAlignment:CrossAxisAlignment.start,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Text("Name of Song",
                              style: TextStyle(
                                color: Colors.grey[100],
                                fontSize: 17,
                                fontWeight: FontWeight.w800,
                                ),
                              ),
                              Padding(
                                padding: EdgeInsets.fromLTRB(0, 20, 0, 0),
                                child: Text("Artist",
                                style: TextStyle(
                                color: Colors.grey[700],
                                fontSize: 15,
                                fontWeight: FontWeight.w900,
                                ),
                                )),
                            ]
                          ),
                        )
                      ],
                    ),
                  ), 
                ]
              ),
            ),


              //list
              // ListView(
              //   children: const <Widget>[
              //     Card(child: ListTile(title: Text('One-line ListTile'))),
              //     Card(
              //       child: ListTile(
              //         leading: FlutterLogo(),
              //         title: Text('One-line with leading widget'),
              //       ),
              //     ),
              //     Card(
              //       child: ListTile(
              //         title: Text('One-line with trailing widget'),
              //         trailing: Icon(Icons.more_vert),
              //       ),
              //     ),
              //     Card(
              //       child: ListTile(
              //         leading: FlutterLogo(),
              //         title: Text('One-line with both widgets'),
              //         trailing: Icon(Icons.more_vert),
              //       ),
              //     ),
              //     Card(
              //       child: ListTile(
              //         title: Text('One-line dense ListTile'),
              //         dense: true,
              //       ),
              //     ),
              //     Card(
              //       child: ListTile(
              //         leading: FlutterLogo(size: 56.0),
              //         title: Text('Two-line ListTile'),
              //         subtitle: Text('Here is a second line'),
              //         trailing: Icon(Icons.more_vert),
              //       ),
              //     ),
              //     Card(
              //       child: ListTile(
              //         leading: FlutterLogo(size: 72.0),
              //         title: Text('Three-line ListTile'),
              //         subtitle: Text(
              //           'A sufficiently long subtitle warrants three lines.'
              //         ),
              //         trailing: Icon(Icons.more_vert),
              //         isThreeLine: true,
              //       ),
              //     ),
              //   ],
              // ),

              ElevatedButton(
                child: Text('Confirm'),
                style: ElevatedButton.styleFrom(
                  primary: Colors.blue,
                  padding: EdgeInsets.fromLTRB(60, 15, 60, 15),
                  shape: const BeveledRectangleBorder(
                      borderRadius: BorderRadius.all(Radius.circular(5))),
                  elevation: 5,
                  textStyle: TextStyle(
                    color: Colors.black,
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                    fontFamily: 'Metropolis',
                    // fontStyle: FontStyle.italic
                  ),
                ),
                onPressed: () {
                  print('Pressed');
                },
              )
            ],
          )),
    );
  }
}

class Alarmscreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      body: Container(
          margin: const EdgeInsets.fromLTRB(40, 80, 40, 0),
          child: Column(children: [
            Align(
                // alignment: Alignment.topCenter,
                child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                  PlusRow(),
                  PlusIcon(),
                ])),
            Expanded(
                // margin: EdgeInsets.fromLTRB(0, 40, 0, 0),
                child: ListView(
                        children: alarms.map((e){
                          return Container(
                            height: 90,
                            margin: EdgeInsets.fromLTRB(0, 10, 0, 10),
                            // color:Colors.red,
                             decoration: BoxDecoration(
                              border: Border(
                                // top: BorderSide(width: 16.0, color: Colors.lightBlue.shade600),
                                bottom: BorderSide(width: 1.0, color: Colors.grey[900]),
                              ),
                              color: Colors.transparent,
                            ),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                  Text(
                                    'Yoga',
                                    style: TextStyle(
                                      color: Colors.grey,
                                      fontSize: 20,
                                      fontWeight: FontWeight.bold,
                                      fontFamily: 'Metropolis',
                                      // fontStyle: FontStyle.italic
                                    ),
                                    ),
                                  Container(
                                      padding: EdgeInsets.fromLTRB(0, 20, 0, 0),
                                      child: Text(
                                      '10:30',
                                      style: TextStyle(
                                        color: Colors.grey[50],
                                        fontSize: 40,                                        
                                        fontWeight: FontWeight.w900,
                                        fontFamily: 'Metropolis',
                                        // fontStyle: FontStyle.italic
                                      ),
                                      ),
                                  ),
                                ],),
                                // Switch(
                                //   value: true, 
                                //   onChanged: (bool value){},
                                //   activeColor: Colors.blue,
                                //   )
                                // 
                                CustomSwitch(
                                    activeColor: Colors.pinkAccent,
                                    value: true,
                                    onChanged: (value) {
                                      print("VALUE : $value");
                                      // setState(() {
                                      //   status = value;
                                      // });
                                    },
                                  ),
                              ]
                            ),
                          );
                        }).toList(),
                      ),
                // ],
                )
          ])),
    );
  }
}

class PlusRow extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Text(
      'alarm',
      style: TextStyle(
        color: Colors.white,
        fontSize: 45,
        fontWeight: FontWeight.w900,
        fontFamily: 'Metropolis',
        decorationColor: Colors.black,
        // fontStyle: FontStyle.italic
      ),
    );
    // ],);
  }
}

class PlusIcon extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return IconButton(
      iconSize: 30,
      icon: Icon(
        Icons.add,
        color: Colors.white,
      ),
      onPressed: () {
        print("alarm add");
      },
    );
  }
}


