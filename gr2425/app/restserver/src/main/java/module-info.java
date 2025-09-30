module com.restserver {
  requires java.naming;

  requires spring.core;
  requires spring.context;
  requires spring.beans;
  requires spring.boot;
  requires spring.boot.autoconfigure;

  requires spring.web;

  requires lombok;

  requires com.fasterxml.jackson.databind;

  requires transitive com.core;

  exports com.restserver;

  opens com.restserver to spring.core, spring.beans;
}
