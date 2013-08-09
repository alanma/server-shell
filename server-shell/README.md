server-shell
============

Simple framework for building java servers similar to spring boot.

It has following features:
- interactive ssh console (ssh commands and shell) with tab completion
- shell commands which reuse spring shell implementation


TODO:
- write documentation
- add more modules and samples
- add support for groovy commands
- make shell extensible (similar to OSGI shell scopes, add support for variables, pipes, add simple shell interpeter, investigate if groovy can be reused)
- modules:
-- encryption (should allow use of encrypted values in .properties files)
-- database integration
-- JMS integration
-- simple batch framework
-- jolokia/JMX
-- tomcat module
-- jolokia console implemented in Vaadin
-- zookeeper support
-- interactive console implemented in Vaadin
-- pluto module
- investigate integration with jboss modules OR apache felix


