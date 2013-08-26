package net.ilx.server.shell.core.server.spring.sshd;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.ilx.server.shell.core.server.spring.conf.SshConfiguration;

import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SshConfiguration.class)
@Documented
public @interface EnableSshd {

}
