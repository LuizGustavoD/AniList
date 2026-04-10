import 'package:flutter/material.dart';
import 'package:anilist_front_application/ui/pages/auth/login_page.dart';

class Main extends StatelessWidget {
  const Main({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'AnimeList',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const LoginPage(),
    );
  }

  
}