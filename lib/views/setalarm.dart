import 'package:flutter/material.dart';

class Mg extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      body: SetAlarm(),
    );
  }
}
class SetAlarm extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Head(),
        Inpu(),
        Inpu1(),
        Songcard(),
      ]
    );
  }
}

class Head extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      // color: Colors.red,
      margin: const EdgeInsets.fromLTRB(40, 70, 0, 0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.start,
        children: [
          Text(
      'set alarm',
      style: TextStyle(
        color: Colors.white,
        fontSize: 45,
        fontWeight: FontWeight.w900,
        fontFamily: 'Metropolis',
        decorationColor: Colors.black,
        // fontStyle: FontStyle.italic
      ),
      )
        ]
      )
    );
  }
}

class Inpu extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Form(
      // child: Row(),

      // // key: _formKey,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          TextFormField(
            decoration: const InputDecoration(
              hintText: 'alarm for',
              hintStyle: TextStyle(color: Colors.white),
            ),
            // validator: (String? value) {
            //   if (value == null || value.isEmpty) {
            //     return 'Please enter some text';
            //   }
            //   return null;
            // },
          ),
          // Padding(
          //   padding: const EdgeInsets.symmetric(vertical: 16.0),
          //   child: ElevatedButton(
          //     onPressed: () {
          //       // Validate will return true if the form is valid, or false if
          //       // the form is invalid.
          //       // if (_formKey.currentState!.validate()) {
          //       //   // Process data.
          //       // }
          //     },
          //     child: const Text('Submit'),
          //   ),
          // ),
        ],
      ),
    );
  }
}


class Inpu1 extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Form(
      // child: Row(),

      // // key: _formKey,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          TextFormField(
            decoration: const InputDecoration(
              hintText: 'time',
              hintStyle: TextStyle(color: Colors.white),
            ),
            // validator: (String? value) {
            //   if (value == null || value.isEmpty) {
            //     return 'Please enter some text';
            //   }
            //   return null;
            // },
          ),
          // Padding(
          //   padding: const EdgeInsets.symmetric(vertical: 16.0),
          //   child: ElevatedButton(
          //     onPressed: () {
          //       // Validate will return true if the form is valid, or false if
          //       // the form is invalid.
          //       // if (_formKey.currentState!.validate()) {
          //       //   // Process data.
          //       // }
          //     },
          //     child: const Text('Submit'),
          //   ),
          // ),
        ],
      ),
    );
  }
}
class Songcard extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: Column(
        
        mainAxisAlignment: MainAxisAlignment.end,
        children: [
          Container(
            padding: EdgeInsets.all(30),
            color: Colors.grey[900],
            height: 200,
            width: 300,
          //   decoration: BoxDecoration(
          //   borderRadius: BorderRadius.circular(10),
          //   color: Colors.transparent,
          //   border: Border(
          //       left: BorderSide(
          //           // color: Colors.green,
          //           width: 3,
          //       ),
          //     ),
          // ),
          // child: Cardelements(),
          child: Column(
            children: [
              Text(
            'Select Song                 >',
            style: TextStyle(
              color: Colors.white,
              fontSize: 25,
              fontWeight: FontWeight.w900,
              fontFamily: 'Metropolis',

              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.start,
                children: [Padding(
                padding: EdgeInsets.fromLTRB(0, 20, 0, 0),
                child: Text(
              'Selected:',

              
              style: TextStyle(
                color: Colors.grey[700],
                fontSize: 15,
                fontWeight: FontWeight.w900,
                fontFamily: 'Metropolis',

                ),
              ),
                ),]
            ),







            ],
          )
            // borderRadius:,
            // child: ,
            )
        ],),
    );
  }
}

class Cardelements extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.blue,
      height: 100,
      child: Column(),
    );
  }
}