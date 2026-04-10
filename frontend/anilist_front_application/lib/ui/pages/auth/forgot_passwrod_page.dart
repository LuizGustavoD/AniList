import 'package:flutter/material.dart';
import 'package:anilist_front_application/ui/widgets/auth/reset_password_form_widgets.dart';

class ForgotPasswordPage extends StatelessWidget {
  const ForgotPasswordPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Esqueci minha senha')),
      body: const Padding(
        padding: EdgeInsets.all(24.0),
        child: ResetPasswordFormWidgets(),
      ),
    );
  }
}
