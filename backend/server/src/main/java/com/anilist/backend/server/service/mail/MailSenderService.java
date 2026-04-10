package com.anilist.backend.server.service.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MailSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
    }

    public void sendConfirmationEmail(String to, String username, String confirmationLink) throws MessagingException {
        String subject = "Confirme sua conta — AniList";
        String html = buildConfirmationHtml(username, confirmationLink);
        sendEmail(to, subject, html);
    }

    public void sendPasswordResetEmail(String to, String username, String confirmationLink) throws MessagingException {
        String subject = "Redefinição de senha — AniList";
        String html = buildPasswordResetVerifyAccount(username, confirmationLink);
        sendEmail(to, subject, html);
    }

    private String buildConfirmationHtml(String username, String confirmationLink) {
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin:0; padding:0; background-color:#0f0f1a; font-family:'Segoe UI',Roboto,Arial,sans-serif;">
                <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#0f0f1a; padding:40px 0;">
                    <tr>
                        <td align="center">
                            <table role="presentation" width="520" cellpadding="0" cellspacing="0" style="background-color:#1a1a2e; border-radius:16px; overflow:hidden; box-shadow:0 8px 32px rgba(0,0,0,0.4);">
                                <!-- Header -->
                                <tr>
                                    <td style="background:linear-gradient(135deg,#6c5ce7,#a855f7,#ec4899); padding:40px 40px 30px; text-align:center;">
                                        <h1 style="margin:0; font-size:32px; font-weight:800; color:#ffffff; letter-spacing:1px;">
                                            &#127752; AniList
                                        </h1>
                                        <p style="margin:8px 0 0; font-size:14px; color:rgba(255,255,255,0.85);">
                                            Sua lista de animes favoritos
                                        </p>
                                    </td>
                                </tr>
                                <!-- Body -->
                                <tr>
                                    <td style="padding:36px 40px;">
                                        <h2 style="margin:0 0 8px; font-size:22px; color:#e2e8f0;">
                                            Olá, %s! 👋
                                        </h2>
                                        <p style="margin:0 0 24px; font-size:15px; color:#94a3b8; line-height:1.6;">
                                            Obrigado por se cadastrar! Para ativar sua conta e começar a montar sua lista de animes, confirme seu e-mail clicando no botão abaixo.
                                        </p>
                                        <!-- Button -->
                                        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td align="center" style="padding:8px 0 28px;">
                                                    <a href="%s"
                                                       style="display:inline-block; padding:14px 48px; background:linear-gradient(135deg,#6c5ce7,#a855f7); color:#ffffff; font-size:16px; font-weight:700; text-decoration:none; border-radius:50px; letter-spacing:0.5px;">
                                                        Confirmar E-mail
                                                    </a>
                                                </td>
                                            </tr>
                                        </table>
                                        <p style="margin:0 0 16px; font-size:13px; color:#64748b; line-height:1.5;">
                                            Se o botão não funcionar, copie e cole o link abaixo no navegador:
                                        </p>
                                        <p style="margin:0 0 24px; font-size:13px; word-break:break-all;">
                                            <a href="%s" style="color:#a855f7; text-decoration:underline;">%s</a>
                                        </p>
                                        <hr style="border:none; border-top:1px solid #2d2d44; margin:24px 0;">
                                        <p style="margin:0; font-size:12px; color:#475569; line-height:1.5;">
                                             Este link expira em <strong style="color:#94a3b8;">24 horas</strong>.<br>
                                            Se você não criou uma conta, pode ignorar este e-mail com segurança.
                                        </p>
                                    </td>
                                </tr>
                                <!-- Footer -->
                                <tr>
                                    <td style="background-color:#141425; padding:24px 40px; text-align:center;">
                                        <p style="margin:0; font-size:12px; color:#475569;">
                                            &copy; 2026 AniList — Todos os direitos reservados.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(username, confirmationLink, confirmationLink, confirmationLink);
    }

    private String buildPasswordResetVerifyAccount(String userName, String confirmationURL){
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin:0; padding:0; background-color:#0f0f1a; font-family:'Segoe UI',Roboto,Arial,sans-serif;">
                <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#0f0f1a; padding:40px 0;">
                    <tr>
                        <td align="center">
                            <table role="presentation" width="520" cellpadding="0" cellspacing="0" style="background-color:#1a1a2e; border-radius:16px; overflow:hidden; box-shadow:0 8px 32px rgba(0,0,0,0.4);">
                                <!-- Header -->
                                <tr>
                                    <td style="background:linear-gradient(135deg,#6c5ce7,#a855f7,#ec4899); padding:40px 40px 30px; text-align:center;">
                                        <h1 style="margin:0; font-size:32px; font-weight:800; color:#ffffff; letter-spacing:1px;">
                                            &#127752; AniList
                                        </h1>
                                        <p style="margin:8px 0 0; font-size:14px; color:rgba(255,255,255,0.85);">
                                            Sua lista de animes favoritos
                                        </p>
                                    </td>
                                </tr>
                                <!-- Body -->
                                <tr>
                                    <td style="padding:36px 40px;">
                                        <h2 style="margin:0 0 8px; font-size:22px; color:#e2e8f0;">
                                            Olá, %s! 👋
                                        </h2>
                                        <p style="margin:0 0 24px; font-size:15px; color:#94a3b8; line-height:1.6;">
                                            Recebemos uma solicitação para redefinir a senha da sua conta. Para criar uma nova senha, clique no botão abaixo.
                                        </p>
                                        <!-- Button -->
                                        <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td align="center" style="padding:8px 0 28px;">
                                                    <a href="%s"
                                                       style="display:inline-block; padding:14px 48px; background:linear-gradient(135deg,#6c5ce7,#a855f7); color:#ffffff; font-size:16px; font-weight:700; text-decoration:none; border-radius:50px; letter-spacing:0.5px;">
                                                        Redefinir Senha
                                                    </a>
                                                </td>
                                            </tr>
                                        </table>
                                        <p style="margin:0 0 16px; font-size:13px; color:#64748b; line-height:1.5;">
                                            Se o botão não funcionar, copie e cole o link abaixo no navegador:
                                        </p>
                                        <p style="margin:0 0 24px; font-size:13px; word-break:break-all;">
                                            <a href="%s" style="color:#a855f7; text-decoration:underline;">%s</a>
                                        </p>
                                        <hr style="border:none; border-top:1px solid #2d2d44; margin:24px 0;">
                                        <p style="margin:0; font-size:12px; color:#475569; line-height:1.5;">
                                            Este link expira em <strong style="color:#94a3b8;">24 horas</strong>.<br>
                                            Se você não solicitou esta redefinição, pode ignorar este e-mail com segurança.
                                        </p>
                                    </td>
                                </tr>
                                <!-- Footer -->
                                <tr>
                                    <td style="background-color:#141425; padding:24px 40px; text-align:center;">
                                        <p style="margin:0; font-size:12px; color:#475569;">
                                            &copy; 2026 AniList — Todos os direitos reservados.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(userName, confirmationURL, confirmationURL, confirmationURL);
    }
}
