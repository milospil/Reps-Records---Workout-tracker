module com.core {
  requires transitive com.fasterxml.jackson.databind;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.datatype.jsr310;

  exports com.core;
  exports com.core.enums;
  exports com.json;

  opens com.core to com.fasterxml.jackson.databind;

}
