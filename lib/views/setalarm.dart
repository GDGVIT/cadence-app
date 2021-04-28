import 'package:flutter/material.dart';

class SetAlarm extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Head(),
        Inpu(),
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
    return Container(
      // input
    );
  }
}