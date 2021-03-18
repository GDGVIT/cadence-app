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
      home: Login(),
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
                  Row(
                    children: [
                      LoginButton(),
                    ],
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
      width: 230,
  
  // margin: EdgeInsets.fromLTRB(50, 200, 0, 0),
  child: RaisedButton(
  shape: RoundedRectangleBorder(
         borderRadius: BorderRadius.all(Radius.circular(10.0))),
  onPressed: (){ print('Button Clicked.'); },
  textColor: Colors.white,
  color: Colors.pink,
  padding: EdgeInsets.fromLTRB(5, 0, 5, 0),
  child: Padding(
  padding: EdgeInsets.fromLTRB(0,0,0,0),
  child: Row(
  crossAxisAlignment: CrossAxisAlignment.center,
  children: <Widget>[
	
  Container(
  color: Colors.pink,
  padding: EdgeInsets.fromLTRB(10, 4, 4, 4),
  child: Text('Button With Right Icon', 
	 style: TextStyle(color: Colors.white),),
   ),

  Padding(
  padding: EdgeInsets.fromLTRB(4, 0, 10, 0),
  child: Icon(
         Icons.backup,
	 color:Colors.white,
	 size: 30,
        ),
       ),
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