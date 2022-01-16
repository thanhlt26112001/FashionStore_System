package com.example.fashionstore_system.service;


import com.example.fashionstore_system.dto.UserDto;
import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.repository.CustomerRepository;
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
    private CustomerRepository customerRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    public User getById(int id) {
        return userRepository.getById(id);
    }

    public UserDto getUserByUserName(final String userName) {
        User user = userRepository.findByUsername(userName);
        if (user != null) {
            UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getCustomer().getEmail());
            return userDto;
        }
        throw new RuntimeException("No user available for the given user name");
    }

    public UserDto getUserByEmail(final String email) {
        Customer user = customerRepository.findByEmail(email);
        if (user != null) {
            UserDto userDto = new UserDto(user.getId(), user.getUser().getUsername(), user.getEmail());
            return userDto;
        }
        throw new RuntimeException("No user available for the given user name");
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void sendVerificationEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        String subject = "Congratulation";
        String senderName = "Rikkeisoft Java team2";
        String content = "<table style=\"width: 100% !important\" >\n" +
                "            <tbody>\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <div>\n" +
                "                            <h2>Hello " + user.getUsername() + "</h2>\n" +
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
        helper.setFrom("EdulanSupport@gmail.com", senderName);
        helper.setTo(user.getCustomer().getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
    }
    public void deleteUser(int id){
        userRepository.deleteById(id);
    }
}