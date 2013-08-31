package net.ilx.server.samples.hello.config;

import net.ilx.server.shell.modules.jolokia.EnableJolokia;
import net.ilx.server.shell.modules.sshd.EnableSshd;

import org.springframework.context.annotation.Configuration;


@Configuration
@EnableJolokia
@EnableSshd
public class HelloConfiguration {

}
