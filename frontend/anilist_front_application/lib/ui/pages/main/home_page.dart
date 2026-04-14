import 'package:flutter/material.dart';
import 'package:anilist_front_application/ui/widgets/other/top_bar_widget.dart';
import 'package:anilist_front_application/ui/pages/main/test_ws_page.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('AnimeList Home'),
      ),
      body: Column(
        children: [
          const TopBarWidget(),
          Expanded(
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Text('Bem-vindo ao AnimeList!'),
                  const SizedBox(height: 24),
                  ElevatedButton(
                    onPressed: () {
                      Navigator.of(context).push(
                        MaterialPageRoute(builder: (context) => const TestWebSocketPage()),
                      );
                    },
                    child: const Text('Testar Mensagens (WebSocket)'),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
