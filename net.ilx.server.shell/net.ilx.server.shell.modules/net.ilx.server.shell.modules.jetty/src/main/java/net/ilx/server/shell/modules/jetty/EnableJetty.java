package net.ilx.server.shell.modules.jetty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.ilx.server.shell.modules.jetty.conf.JettyConfiguration;

import org.springframework.context.annotation.Import;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(JettyConfiguration.class)
@Documented
public @interface EnableJetty {

   
    
}
