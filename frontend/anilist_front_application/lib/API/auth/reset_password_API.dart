import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:anilist_front_application/API/auth/abstract_auth_API.dart';

class AuthApiResetPassword extends AbstractAuthApi {
  Future<String> requestReset(String email) async {
    final response = await http.post(
      Uri.parse('$baseUrl/reset-password/request'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'email': email}),
    );

    if (response.statusCode == 200) {
      return response.body;
    } else {
      throw Exception(response.body.isNotEmpty ? response.body : 'Failed to send reset email');
    }
  }

  Future<String> confirmReset(String token, String newPassword) async {
    final response = await http.post(
      Uri.parse('$baseUrl/reset-password/confirm?token=$token'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'newPassword': newPassword}),
    );

    if (response.statusCode == 200) {
      return response.body;
    } else {
      throw Exception(response.body.isNotEmpty ? response.body : 'Failed to reset password');
    }
  }
}