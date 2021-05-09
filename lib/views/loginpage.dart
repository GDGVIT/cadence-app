import 'package:flutter/material.dart';
// import 'package:cadence/main.dart';

class Login extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
        margin: const EdgeInsets.fromLTRB(0, 200, 0, 0),
        child: Column(children: [
          Align(
            alignment: Alignment.topCenter,
            child: Logrow(),
          ),
          Container(
              margin: EdgeInsets.fromLTRB(0, 130, 0, 0),
              child: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    LoginButton(),
                  ])
              // ],
              )
        ])

        //logo

        // ],
        // ),
// ],)

        );
  }
}

class Logrow extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Padding(
          padding: EdgeInsets.fromLTRB(40, 0, 0, 0),
          // children:     [

          child: Text(
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


class LoginButton extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return 
    Center(
    //   // mainAxisSize: MainAxisSize.min,
    //   child: Container(
    //       // width: 330,
    //       margin: EdgeInsets.fromLTRB(50, 0, 50, 0),

    //       // margin: EdgeInsets.fromLTRB(50, 200, 0, 0),
    //       child: RaisedButton(
    //           shape: RoundedRectangleBorder(
    //               borderRadius: BorderRadius.all(Radius.circular(35.0))),
    //           onPressed: () {
    //             print('Button Clicked.');
    //           },
    //           textColor: Colors.white,
    //           color: Colors.green,
    //           padding: EdgeInsets.fromLTRB(15, 15, 0, 15),
    //           child: Padding(
    //               padding: EdgeInsets.fromLTRB(0, 0, 0, 0),
    //               child: Row(
    //                 crossAxisAlignment: CrossAxisAlignment.center,
    //                 children: <Widget>[
    //                   Container(
    //                     color: Colors.green,
    //                     // margin: EdgeInsets,
    //                     padding: EdgeInsets.fromLTRB(25, 0, 25, 0),
    //                     child: Text(
    //                       'LOGIN WITH SPOTIFY',
    //                       style: TextStyle(color: Colors.white, fontSize: 16),
    //                     ),
    //                   ),

    //                   Padding(
    //                     padding: EdgeInsets.fromLTRB(4, 0, 10, 0),
    //                     child: ImageIcon(
    //                       AssetImage('Assets/spotify.png'),
    //                       size: 30,
    //                     ),
    //                     //  color:Colors.white,
    //                   ),
    //                   //  ),
    //                 ],
    //               )))),
    );
  }
}