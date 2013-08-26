package net.ilx.server.samples.hello.config;

import net.ilx.server.shell.core.server.spring.sshd.EnableSshd;
import net.ilx.server.shell.modules.jolokia.EnableJolokia;

import org.springframework.context.annotation.Configuration;


@Configuration
@EnableJolokia
@EnableSshd
public class HelloConfiguration {

}
