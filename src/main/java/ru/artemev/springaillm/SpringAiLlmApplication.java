package ru.artemev.springaillm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiLlmApplication {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringAiLlmApplication.class, args);
        ChatClient chatClient = context.getBean(ChatClient.class);

        System.out.println(chatClient.prompt().user("Дай первую строчку из Войны и мир").call().content());

    }

}
