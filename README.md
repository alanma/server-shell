server-shell
============

Simple framework for building java servers.

It has following features:
- interactive ssh console (ssh commands and shell) with tab completion - DONE 
- shell commands which reuse spring shell implementation - DONE

GOAL:

Runtime:
- create lightweight non-exactly-JEE compatible container
- use jetty as web container
- use bitronix as transaction manager
- implement JSR-236 (AND CommonJ) API
- create "application regions" - applications should have parent classloader set as "application region" classloader - something similar to EAR classloader 
- separate container class space from application regions - example: spring from server-shell should not leak into application region scopes
Tooling:
- reuse Eclipse-Jetty integration (http://eclipse-jetty.sourceforge.net) for Eclipse integration
Development:
- integrate spring-loaded or sysdeo devloader in order to support better development experience



TODO:
- write documentation
- add support for groovy commands
- make shell more extensible (similar to OSGI shell scopes, add support for variables, pipes, add simple shell interpeter, investigate if groovy can be reused)
- modules:
 - encryption (should allow use of encrypted values in .properties files) - PARTIAL
 - database integration - PARTIAL
 - JMS integration - PARTIAL
 - simple batch framework - PARTIAL
 - jolokia/JMX - PARTIAL 
 - jolokia console implemented in Vaadin - IDEA
 - zookeeper support - IDEA
 - interactive console implemented in Vaadin - IDEA
 - pluto module - IDEA
- investigate integration with jboss modules OR apache felix - IDEA

