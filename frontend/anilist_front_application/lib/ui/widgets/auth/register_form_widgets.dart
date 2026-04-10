

import 'package:flutter/material.dart';
import 'package:anilist_front_application/API/auth/register_API.dart';

class RegisterFormWidgets extends StatefulWidget{
  const RegisterFormWidgets({super.key});

  @override
  State<RegisterFormWidgets> createState() => _RegisterFormWidgetsState();

}

class _RegisterFormWidgetsState extends State<RegisterFormWidgets> {

  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final AuthApiRegister _authApiRegister = AuthApiRegister();
  bool _isLoading = false;
  String? _errorMessage;
  String? _successMessage;

  @override
  void dispose() {
    _usernameController.dispose();
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  Future<void> _handleRegister() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
      _successMessage = null;
    });

    try {
      final result = await _authApiRegister.register(
        _usernameController.text,
        _emailController.text,
        _passwordController.text,
      );
      setState(() {
        _successMessage = result['message'] ?? 'Registro realizado com sucesso!';
      });
    } catch (e) {
      setState(() {
        _errorMessage = e.toString().replaceFirst('Exception: ', '');
      });
    } finally {
      if (mounted) {
        setState(() => _isLoading = false);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        children: [
          TextField(
            controller: _usernameController,
            decoration: InputDecoration(labelText: 'Username'),
          ),
          TextField(
            controller: _emailController,
            decoration: InputDecoration(labelText: 'Email'),
          ),
          TextField(
            controller: _passwordController,
            decoration: InputDecoration(labelText: 'Password'),
            obscureText: true,
          ),
          _isLoading
              ? const CircularProgressIndicator()
              : ElevatedButton(
                  onPressed: _handleRegister,
                  child: const Text('Register'),
                ),
          if (_errorMessage != null)
            Padding(
              padding: const EdgeInsets.only(top: 8),
              child: Text(
                _errorMessage!,
                style: const TextStyle(color: Colors.red),
              ),
            ),
          if (_successMessage != null)
            Padding(
              padding: const EdgeInsets.only(top: 8),
              child: Text(
                _successMessage!,
                style: const TextStyle(color: Colors.green),
              ),
            ),
          TextButton(
            onPressed: () {
              // Handle navigation to login screen
            },
            child: Text('Already have an account? Login'),
          ),
        ],
      ),
    );
  }
}