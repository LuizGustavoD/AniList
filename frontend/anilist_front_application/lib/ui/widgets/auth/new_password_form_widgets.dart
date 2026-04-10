
import 'package:flutter/material.dart';
import 'package:anilist_front_application/API/auth/reset_password_API.dart';

class NewPasswordFormWidgets extends StatefulWidget {
  final String? token;

  const NewPasswordFormWidgets({super.key, this.token});

  @override
  State<NewPasswordFormWidgets> createState() => _NewPasswordFormWidgetsState();
}

class _NewPasswordFormWidgetsState extends State<NewPasswordFormWidgets> {
  final TextEditingController _tokenController = TextEditingController();
  final TextEditingController _newPasswordController = TextEditingController();
  final TextEditingController _confirmPasswordController = TextEditingController();
  final AuthApiResetPassword _resetApi = AuthApiResetPassword();
  bool _isLoading = false;
  String? _errorMessage;
  String? _successMessage;

  @override
  void initState() {
    super.initState();
    if (widget.token != null) {
      _tokenController.text = widget.token!;
    }
  }

  @override
  void dispose() {
    _tokenController.dispose();
    _newPasswordController.dispose();
    _confirmPasswordController.dispose();
    super.dispose();
  }

  Future<void> _handleResetPassword() async {
    final token = _tokenController.text.trim();
    final password = _newPasswordController.text;
    final confirmPassword = _confirmPasswordController.text;

    if (token.isEmpty) {
      setState(() => _errorMessage = 'Token é obrigatório.');
      return;
    }
    if (password.isEmpty) {
      setState(() => _errorMessage = 'Nova senha é obrigatória.');
      return;
    }
    if (password.length < 8) {
      setState(() => _errorMessage = 'A senha deve ter pelo menos 8 caracteres.');
      return;
    }
    if (password != confirmPassword) {
      setState(() => _errorMessage = 'As senhas não coincidem.');
      return;
    }

    setState(() {
      _isLoading = true;
      _errorMessage = null;
      _successMessage = null;
    });

    try {
      final message = await _resetApi.confirmReset(token, password);
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
        TextField(
          controller: _tokenController,
          decoration: const InputDecoration(
            labelText: 'Token',
            hintText: 'Cole o token recebido por e-mail',
          ),
          maxLines: 1,
        ),
        const SizedBox(height: 16),
        TextField(
          controller: _newPasswordController,
          decoration: const InputDecoration(labelText: 'Nova senha'),
          obscureText: true,
        ),
        const SizedBox(height: 16),
        TextField(
          controller: _confirmPasswordController,
          decoration: const InputDecoration(labelText: 'Confirmar nova senha'),
          obscureText: true,
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
                onPressed: _handleResetPassword,
                child: const Text('Redefinir senha'),
              ),
        const SizedBox(height: 16),
        if (_successMessage != null)
          ElevatedButton(
            onPressed: () {
              Navigator.popUntil(context, (route) => route.isFirst);
            },
            child: const Text('Voltar ao login'),
          ),
      ],
    );
  }
}
