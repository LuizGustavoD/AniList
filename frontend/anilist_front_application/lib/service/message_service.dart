import 'package:anilist_front_application/API/message_API.dart';

class MessageService {
  static final MessageAPI _api = MessageAPI();

  static void connect(String url) {
    _api.connect(url);
  }

  static void sendMessage(String message) {
    _api.sendMessage(message);
  }

  static Stream get stream => _api.stream;

  static void disconnect() {
    _api.disconnect();
  }
}
