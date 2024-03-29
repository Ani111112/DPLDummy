package com.example.demo.service;

import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UserLoginDTO;
import com.example.demo.exception.*;
import com.example.demo.model.Address;
import com.example.demo.model.User;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;
    @Value("${spring.secrets.key}")
    private String secretKey;
    private final Random random = new Random();
    HashMap<String, String> otpStorage = new HashMap<>(); // for now it is a temporary storage for otp
    public User signup(UserDTO user) {
        if (user == null) throw new UserNullException("Fields Must Be Filled");
        Optional<User> savedUserByUserId = Optional.ofNullable(userRepository.findByUserId(user.getUserId()));
        if (savedUserByUserId.isPresent()) throw new UserIdAlreadyUserdException("User Id is Already User Try Some Other User Id");
        Optional<User> savedUserByEmailId = Optional.ofNullable(userRepository.findByEmailId(user.getEmailId()));
        if (savedUserByEmailId.isPresent()) throw new EmailIdAlreadyUsedException("Email Id is Already User Try Some Other User Id");
        Optional<Address> savedUserByMobileNumber = Optional.ofNullable(addressRepository.findByPrimaryMobileNumber(user.getAddress().get(0).getPrimaryMobileNumber()));
        if (savedUserByMobileNumber.isPresent()) throw new MobileNumberAlreadyUsedException("Primary Mobile Number is Already Used Try Some Other Number");
        String password = user.getPassword();
        String newPassword = password.concat(secretKey);
        String encodedPassword = hashPassword(newPassword);
        user.setPassword(encodedPassword);
        User userFromUserDto = UserTransformer.UserDtoToUser(user);
        userFromUserDto.getAddressList().forEach(address -> address.setUser(userFromUserDto));
        User savedUser = userRepository.save(userFromUserDto);
        String otp = generateOtp();
        verifyEmail(savedUser.getEmailId(), otp);
        otpStorage.put(savedUser.getUserId(), otp);
        return savedUser;
    }

    private void verifyEmail(String emailId, String otp) {
        String body = getBodyForSms(otp);
        String senderEmail = "aniruddhamukherjee232@gmail.com";
        String appPassword = "bite okmm lwac pyoz";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });


        try {
            // Create a message
            javax.mail.Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(emailId));
            message.setSubject("Application for Java Backend Developer Position");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText(body);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String generateOtp() {
        int length = 4;
        // Possible characters in the OTP
        String numbers = "0123456789";
        // StringBuilder to store generated OTP
        StringBuilder sb = new StringBuilder();

        // Generate OTP of specified length
        for (int i = 0; i < length; i++) {
            // Generate a random index between 0 and length of numbers string
            int index = random.nextInt(numbers.length());
            // Append the character at the randomly generated index to the OTP
            sb.append(numbers.charAt(index));
        }

        return sb.toString();
    }

    private String hashPassword(String newPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(newPassword);
    }

    private String getBodyForSms(String otp) {
        return "Your OTP is: " + otp + ". Please use this OTP to complete your verification.";
    }

    public Boolean verifyOtp(String userId, String enteredOtp) {
        if (!otpStorage.containsKey(userId)) throw new UserIdNotExitsException("User id is not valid");
        String originalOtp = otpStorage.get(userId);
        return originalOtp.equals(enteredOtp);
    }

    public User login(UserLoginDTO userLogin) {
        String userId = userLogin.getUserId();
        String enteredPassword = userLogin.getPassword();
//        Optional<User>
    }
}
