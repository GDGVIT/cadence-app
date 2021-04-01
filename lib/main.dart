import 'package:flutter/material.dart';

void main() => runApp(
  MyApp()
);

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Welcome to Flutter',
      theme: ThemeData(
        // accentColor: Colors.white,
        primaryColor: Colors.white,
        fontFamily: 'Metropolis',
        // elevatedButtonTheme: ElevatedButtonThemeData(
        //     style: ElevatedButton.styleFrom(
        //       primary: Colors.purple,
        //     ),
        //   ),
        
        ),
      home: Alarmscreen(),
      // home: Login(),
      // home: Playlist(),
    );
  }
}



class Login extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.fromLTRB(0, 200, 0, 0),
      child:
            Column(
              children:[
                  Align(
                        alignment: Alignment.topCenter,
                        child: Logrow(),
                       ),
                  Container(
                    margin:EdgeInsets.fromLTRB(0, 130, 0, 0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        LoginButton(),
                      ]
                    )
                    // ],
                  )
                       ]
            )
            
            
    
            //logo
    
          // ],
  // ),
// ],)
      
          );
  }
}


class LoginButton extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Center(
      // mainAxisSize: MainAxisSize.min,
      child: Container(
      // width: 330,
      margin:EdgeInsets.fromLTRB(50, 0, 50, 0),
  
  // margin: EdgeInsets.fromLTRB(50, 200, 0, 0),
  child: RaisedButton(
  shape: RoundedRectangleBorder(
  borderRadius: BorderRadius.all(Radius.circular(35.0))),
  onPressed: (){ print('Button Clicked.'); },
  textColor: Colors.white,
  color: Colors.green,
  padding: EdgeInsets.fromLTRB(15, 15, 0, 15),
  child: Padding(
  padding: EdgeInsets.fromLTRB(0,0,0,0),
  child: Row(
  crossAxisAlignment: CrossAxisAlignment.center,
  children: <Widget>[
	
  Container(
  color: Colors.green,
  // margin: EdgeInsets,
  padding: EdgeInsets.fromLTRB(25, 0, 25, 0),
  child: Text('LOGIN WITH SPOTIFY', 
	 style: TextStyle(
     color: Colors.white,
     fontSize: 16
     
     ),),
   ),

  Padding(
  padding: EdgeInsets.fromLTRB(4, 0, 10, 0),
  child: ImageIcon(
   AssetImage('Assets/spotify.png'),
     size: 30, 
   ),
	//  color:Colors.white,
	
        ),
      //  ),
      ],
  )))),
    );
  }
}

class Logrow extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
          Padding(
                  padding: EdgeInsets.fromLTRB(30, 0, 0, 0),
                  // children:     [
                  
                          child:Text(
                  
                            'Cadence',
                  
                            style: TextStyle(
                  
                              color: Colors.white,
                  
                              fontFamily: 'Metropolis',
                  
                              fontSize: 60,
                  
                              decorationColor: Colors.black,
                  
                            ),
                  
                          ),
                ),

                Padding(
    
              padding: EdgeInsets.fromLTRB(20, 0, 0, 0),
    
              child: Image(image: AssetImage('Assets/logo.png'))),


      ],
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
          child: Column(children: [

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
          icon: Icon(Icons.cached,
          color: Colors.white,
          
          
          ),
          tooltip: 'Increase volume by 10',
          onPressed: () {
          },
        ),
          ],),

          // search bar
          Padding(
            padding: EdgeInsets.fromLTRB(10, 50, 10, 40),
            child:TextField(

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
                shape: const BeveledRectangleBorder(borderRadius: BorderRadius.all(Radius.circular(5))),
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
          ],)
          ),
      );
  }
}

class Alarmscreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Row(

            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
            Text(
            'Alarm',
              style: TextStyle(
                color: Colors.white,
                fontSize: 40,
                fontWeight: FontWeight.w700,
                fontFamily: 'Metropolis',
                decorationColor: Colors.black,
                // fontStyle: FontStyle.italic
              ),
               
          ),
          Text(
            'ab',
          )
        //   IconButton(
        //   iconSize: 35,
        //   icon: Icon(Icons.cached,
        //   color: Colors.white,
        //   ),
        //   // tooltip: 'Increase volume by 10',
        //   onPressed: () {
        //     // print('ab');
        //   },
        // ),
          ],);
  }
}