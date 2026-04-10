import 'dart:convert';
import 'package:http/http.dart' as http;

abstract class AbstractAuthApi {
  final String baseUrl = "http://localhost:8080/api/auth";
}
