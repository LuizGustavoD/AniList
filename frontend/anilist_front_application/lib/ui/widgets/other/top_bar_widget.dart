
import 'package:flutter/material.dart';

class TopBarWidget extends StatelessWidget {
  const TopBarWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      
      child: Row(children: [
        Text('AnimeList', style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
        Spacer(),
        TextButton(onPressed: () {
          
        }, child: Text('Login')),
        IconButton(
          icon: Icon(Icons.account_circle),
          onPressed: () {
            // Handle profile action
          },
        ),  
      ],),
    );
  }
}