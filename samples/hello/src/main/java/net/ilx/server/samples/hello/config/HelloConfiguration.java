package net.ilx.server.samples.hello.config;

import net.ilx.server.shell.modules.jetty.EnableJetty;
import net.ilx.server.shell.modules.jolokia.EnableJolokia;
import net.ilx.server.shell.modules.sshd.EnableSshd;

import org.springframework.context.annotation.Configuration;


@Configuration
@EnableJolokia
@EnableSshd
@EnableJetty
public class HelloConfiguration {

}
