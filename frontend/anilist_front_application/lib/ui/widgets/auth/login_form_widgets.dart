
import 'package:flutter/material.dart';
import 'package:anilist_front_application/API/auth/login_API.dart';
import 'package:anilist_front_application/service/auth_service.dart';
import 'package:anilist_front_application/ui/pages/auth/forgot_passwrod_page.dart';

class LoginFormWidgets extends StatefulWidget {
  const LoginFormWidgets({super.key});

  @override
  State<LoginFormWidgets> createState() => _LoginFormWidgetsState();
}

class _LoginFormWidgetsState extends State<LoginFormWidgets> {

  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final AuthApiLogin _authApiLogin = AuthApiLogin();
  bool _isLoading = false;
  String? _errorMessage;

  @override
  void dispose() {
    _usernameController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  Future<void> _handleLogin() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      final result = await _authApiLogin.login(
        _usernameController.text,
        _passwordController.text,
      );
      await AuthService.saveToken(result['token']!);
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Login realizado com sucesso!')),
        );
      }
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
            controller: _passwordController,
            decoration: InputDecoration(labelText: 'Password'),
            obscureText: true,
          ),
          _isLoading
              ? const CircularProgressIndicator()
              : ElevatedButton(
                  onPressed: _handleLogin,
                  child: const Text('Login'),
                ),
          if (_errorMessage != null)
            Padding(
              padding: const EdgeInsets.only(top: 8),
              child: Text(
                _errorMessage!,
                style: const TextStyle(color: Colors.red),
              ),
            ),
        Container(
          margin: EdgeInsets.only(top: 20),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              TextButton(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (_) => const ForgotPasswordPage(),
                    ),
                  );
                },
                child: Text('Forgot Password?'),
              ),
              TextButton(
                onPressed: () {
                  // Handle sign up logic
                },
                child: Text('Sign Up'),
              ),
            ],
          ),

        )],
      ),
    );
  }
}