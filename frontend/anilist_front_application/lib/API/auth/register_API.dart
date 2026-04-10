import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:anilist_front_application/API/auth/abstract_auth_API.dart';

class AuthApiRegister extends AbstractAuthApi {
  Future<Map<String, dynamic>> register(
      String username, String email, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/register'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'username': username,
        'email': email,
        'password': password,
      }),
    );

    final body = jsonDecode(response.body) as Map<String, dynamic>;

    if (response.statusCode == 200) {
      return body;
    } else {
      throw Exception(body['error'] ?? 'Registration failed');
    }
  }

  Future<Map<String, dynamic>> confirmEmail(String token) async {
    final response = await http.post(
      Uri.parse('$baseUrl/register/confirm-email?token=$token'),
      headers: {'Content-Type': 'application/json'},
    );

    final body = jsonDecode(response.body) as Map<String, dynamic>;

    if (response.statusCode == 200) {
      return body;
    } else {
      throw Exception(body['error'] ?? 'Email confirmation failed');
    }
  }
}
