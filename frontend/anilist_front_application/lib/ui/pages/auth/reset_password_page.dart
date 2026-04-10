import 'package:flutter/material.dart';
import 'package:anilist_front_application/ui/widgets/auth/new_password_form_widgets.dart';

class ResetPasswordPage extends StatelessWidget {
  final String? token;

  const ResetPasswordPage({super.key, this.token});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Redefinir senha')),
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: NewPasswordFormWidgets(token: token),
      ),
    );
  }
}
