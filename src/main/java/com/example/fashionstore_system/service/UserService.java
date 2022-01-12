package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private JavaMailSender javaMailSender;
    public User getCustomerByUserName(String username) {
        return repository.findByUsername(username);
    }

    public void saveUser(User user) {
        repository.save(user);
    }

    public void sendVerificationEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException {

        String subject ="Congratulation";
        String senderName = "Rikkeisoft Java team2";
        String content =  "<table style=\"width: 100% !important\" >\n" +
                "            <tbody>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <div>\n" +
                "                            <h2>Hello "+ user.getUsername() + "</h2>\n" +
                "                        </div>\n" +
                "                        <div>\n" +
                "                            You recently register for your account in our system. We are very happy that you choose our shop.\n" +
                "                        </div>\n" +
                "                        <br>\n" +

                "                        <br>\n" +
                "\n" +
                "                        <div>\n" +
                "                            Enjoy shopping.\n" +
                "                        </div>\n" +
                "\n" +
                "                        <br>\n" +
                "                        <div>\n" +
                "                            Sincerely,\n" +
                "                            <h4>RikkeiSoftFresher Java Team 2</h4>\n" +
                "                        </div>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </tbody>\n" +
                "        </table>";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("EdulanSupport@gmail.com",senderName);
        helper.setTo(user.getCustomer().getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
    }

}
