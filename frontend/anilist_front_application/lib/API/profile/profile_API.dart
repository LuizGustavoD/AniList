import 'dart:convert';
import 'dart:typed_data';
import 'package:http/http.dart' as http;
import 'package:anilist_front_application/service/auth_service.dart';

class ProfileApi {
  final String baseUrl = "http://localhost:8080/api/profile";

  Future<Map<String, String>> _authHeaders() async {
    final token = await AuthService.getToken();
    return {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    };
  }

  Future<Map<String, dynamic>> getMyProfile() async {
    final response = await http.get(
      Uri.parse('$baseUrl/me'),
      headers: await _authHeaders(),
    );

    if (response.statusCode == 200) {
      return jsonDecode(response.body) as Map<String, dynamic>;
    } else {
      throw Exception('Failed to load profile');
    }
  }

  Future<Map<String, dynamic>> uploadProfilePicture(
      Uint8List fileBytes, String filename) async {
    final token = await AuthService.getToken();
    final request = http.MultipartRequest(
      'POST',
      Uri.parse('$baseUrl/picture'),
    );

    request.headers['Authorization'] = 'Bearer $token';
    request.files.add(http.MultipartFile.fromBytes(
      'file',
      fileBytes,
      filename: filename,
    ));

    final streamedResponse = await request.send();
    final response = await http.Response.fromStream(streamedResponse);

    if (response.statusCode == 200) {
      return jsonDecode(response.body) as Map<String, dynamic>;
    } else {
      throw Exception('Failed to upload profile picture');
    }
  }

  Future<Map<String, dynamic>> deleteProfilePicture() async {
    final response = await http.delete(
      Uri.parse('$baseUrl/picture'),
      headers: await _authHeaders(),
    );

    if (response.statusCode == 200) {
      return jsonDecode(response.body) as Map<String, dynamic>;
    } else {
      throw Exception('Failed to delete profile picture');
    }
  }

  String getProfilePictureUrl(String filename) {
    return '$baseUrl/picture/$filename';
  }

  Future<Map<String, dynamic>> updateProfile({
    String? username,
    String? email,
    String? about,
  }) async {
    final response = await http.post(
      Uri.parse('$baseUrl/update'),
      headers: await _authHeaders(),
      body: jsonEncode({
        if (username != null) 'username': username,
        if (email != null) 'email': email,
        if (about != null) 'about': about,
      }),
    );

    if (response.statusCode == 200) {
      return {'message': response.body};
    } else {
      throw Exception(response.body);
    }
  }
}
