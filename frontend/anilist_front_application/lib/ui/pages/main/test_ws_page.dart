import 'package:flutter/material.dart';
import 'package:anilist_front_application/service/message_service.dart';
import 'package:anilist_front_application/service/auth_service.dart';

class TestWebSocketPage extends StatefulWidget {
  const TestWebSocketPage({super.key});

  @override
  State<TestWebSocketPage> createState() => _TestWebSocketPageState();
}

class _TestWebSocketPageState extends State<TestWebSocketPage> {
  final TextEditingController _messageController = TextEditingController();
  final List<String> _messages = [];

  @override
  void initState() {
    super.initState();
    // Connect to WebSocket (adjust URL as needed)
    MessageService.connect('ws://localhost:8080/ws');
    MessageService.stream.listen((event) {
      setState(() {
        _messages.add(event.toString());
      });
    });
  }

  void _sendMessage() async {
    final token = await AuthService.getToken();
    // Example: send message as JSON with token (adjust as needed)
    final msg = _messageController.text;
    MessageService.sendMessage(msg);
    setState(() {
      _messageController.clear();
    });
  }

  @override
  void dispose() {
    MessageService.disconnect();
    _messageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Testar WebSocket')),
      body: Column(
        children: [
          Expanded(
            child: ListView.builder(
              itemCount: _messages.length,
              itemBuilder: (context, index) => ListTile(
                title: Text(_messages[index]),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                Expanded(
                  child: TextField(
                    controller: _messageController,
                    decoration: const InputDecoration(hintText: 'Digite uma mensagem'),
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.send),
                  onPressed: _sendMessage,
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
