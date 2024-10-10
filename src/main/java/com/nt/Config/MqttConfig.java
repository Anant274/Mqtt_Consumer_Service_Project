package com.nt.Config;

import com.nt.Entity.User;
import com.nt.Repo.UserRepository;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfig {

    // MQTT Client Factory
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{"tcp://localhost:1883"});  // Replace with your MQTT broker URL
        factory.setConnectionOptions(options);
        return factory;
    }

    // Inbound channel for receiving messages
    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    // MQTT Inbound Adapter (Subscribes to a topic and listens for messages)
    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("consumerClient", mqttClientFactory(), "user_topic");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }

    // Message handler to process messages
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler mqttMessageHandler(UserRepository userRepository) {
        System.out.println("Initial Flow for record storage");
        return message -> {
            String payload = (String) message.getPayload();
            System.out.println("Received MQTT message: " + payload);
            String[] parts = payload.split(",");
            String name = parts[0].split(":")[1].trim();
            String mobileNumber = parts[1].split(":")[1].trim();
            User user = new User(name, mobileNumber);
            userRepository.save(user);

            System.out.println("User data saved in the database: " + user.getName() + ", " + user.getMobileNumber());
        };
    }
}