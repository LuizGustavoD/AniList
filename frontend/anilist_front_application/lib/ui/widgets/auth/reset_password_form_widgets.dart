import 'package:flutter/material.dart';
import 'package:anilist_front_application/API/auth/reset_password_API.dart';
import 'package:anilist_front_application/ui/pages/auth/reset_password_page.dart';

class ResetPasswordFormWidgets extends StatefulWidget {
  const ResetPasswordFormWidgets({super.key});

  @override
  State<ResetPasswordFormWidgets> createState() => _ResetPasswordFormWidgetsState();
}

class _ResetPasswordFormWidgetsState extends State<ResetPasswordFormWidgets> {
  final TextEditingController _emailController = TextEditingController();
  final AuthApiResetPassword _resetApi = AuthApiResetPassword();
  bool _isLoading = false;
  String? _errorMessage;
  String? _successMessage;

  @override
  void dispose() {
    _emailController.dispose();
    super.dispose();
  }

  Future<void> _handleSendResetEmail() async {
    final email = _emailController.text.trim();
    if (email.isEmpty) {
      setState(() => _errorMessage = 'Por favor, insira seu e-mail.');
      return;
    }

    setState(() {
      _isLoading = true;
      _errorMessage = null;
      _successMessage = null;
    });

    try {
      final message = await _resetApi.requestReset(email);
      setState(() => _successMessage = message);
    } catch (e) {
      setState(() {
        _errorMessage = e.toString().replaceFirst('Exception: ', '');
      });
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        const Text(
          'Insira seu e-mail para receber o link de redefinição de senha.',
          style: TextStyle(fontSize: 16),
        ),
        const SizedBox(height: 20),
        TextField(
          controller: _emailController,
          decoration: const InputDecoration(labelText: 'E-mail'),
          keyboardType: TextInputType.emailAddress,
        ),
        const SizedBox(height: 20),
        if (_errorMessage != null)
          Padding(
            padding: const EdgeInsets.only(bottom: 12),
            child: Text(
              _errorMessage!,
              style: const TextStyle(color: Colors.red),
            ),
          ),
        if (_successMessage != null)
          Padding(
            padding: const EdgeInsets.only(bottom: 12),
            child: Text(
              _successMessage!,
              style: const TextStyle(color: Colors.green),
            ),
          ),
        _isLoading
            ? const Center(child: CircularProgressIndicator())
            : ElevatedButton(
                onPressed: _handleSendResetEmail,
                child: const Text('Enviar e-mail de redefinição'),
              ),
        const SizedBox(height: 16),
        TextButton(
          onPressed: () {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (_) => const ResetPasswordPage(),
              ),
            );
          },
          child: const Text('Já tenho o token? Redefinir senha'),
        ),
      ],
    );
  }
}