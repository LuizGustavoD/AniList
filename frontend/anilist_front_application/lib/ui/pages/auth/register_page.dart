import 'package:flutter/material.dart';
import 'package:anilist_front_application/ui/widgets/auth/register_form_widgets.dart';

class RegisterPage extends StatelessWidget {
  const RegisterPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Registrar')),
      body: const Padding(
        padding: EdgeInsets.all(24.0),
        child: RegisterFormWidgets(),
      ),
    );
  }
}
