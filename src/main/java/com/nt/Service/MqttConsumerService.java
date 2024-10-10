package com.nt.Service;

import com.nt.Entity.User;
import com.nt.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
public class MqttConsumerService {

    /*@Autowired
    private UserRepository userRepository;

    @MqttListener(topics = "user_topic")
    public void consumeMessage(String message) {
        // Parse the message (assuming it's a simple string format here)
        System.out.println("Received message from MQTT broker: " + message);

        // You might need to parse the message properly into a User object, depending on your format
        // For simplicity, assume the message format is 'User: {name}, Mobile: {mobileNumber}'
        String[] parts = message.split(",");
        String name = parts[0].split(":")[1].trim();
        String mobileNumber = parts[1].split(":")[1].trim();

        // Save user to the database
        User user = new User(name, mobileNumber);
        userRepository.save(user);

        System.out.println("User data saved in database: " + user.getName() + ", " + user.getMobileNumber());
    }*/
}
