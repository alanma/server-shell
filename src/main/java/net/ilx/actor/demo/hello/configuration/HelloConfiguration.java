package net.ilx.actor.demo.hello.configuration;

import net.ilx.actor.demo.hello.commands.HelloCommands;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HelloConfiguration {

    @Bean
    public HelloCommands helloCommands() {
        return new HelloCommands();
    }
}
