// import 'package:flutter/material.dart';
// import 'package:logger/logger.dart';
// import 'package:spotify_sdk/models/connection_status.dart';
// // import 'package:spotify_sdk/models/crossfade_state.dart';
// import 'package:spotify_sdk/spotify_sdk.dart';

// class Spot extends StatefulWidget {
//   @override
//   _SpotState createState() => _SpotState();
// }

// class _SpotState extends State<Spot> {
//   bool _loading = false;
//   bool _connected = false;
//   final Logger _logger = Logger();

//   // CrossfadeState? crossfadeState;

//   @override
//   Widget build(BuildContext context) {
//     return MaterialApp(
//       home: StreamBuilder<ConnectionStatus>(
//         stream: SpotifySdk.subscribeConnectionStatus(),
//         builder: (context, snapshot) {
//           _connected = false;
//           var data = snapshot.data;
//           if (data != null) {
//             _connected = data.connected;
//           }
//           return Scaffold(
//             appBar: AppBar(
//               title: const Text('SpotifySdk Example'),
//               actions: [
//                 _connected
//                     ? IconButton(
//                         onPressed: null,
//                         icon: Icon(Icons.exit_to_app),
//                       )
//                     : Container()
//               ],
//             ),
//             // body: _sampleFlowWidget(context),
//           );
//         },
//       ),
//     );
//   }
// }



  