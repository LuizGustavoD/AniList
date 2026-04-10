import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:anilist_front_application/API/auth/abstract_auth_API.dart';

class AuthApiLogin extends AbstractAuthApi {
  Future<Map<String, dynamic>> login(String username, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/login'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'username': username,
        'password': password,
      }),
    );

    final body = jsonDecode(response.body) as Map<String, dynamic>;

    if (response.statusCode == 200) {
      return body;
    } else {
      throw Exception(body['error'] ?? 'Login failed');
    }
  }
}
