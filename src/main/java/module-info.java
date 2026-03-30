module Schedulify {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires javafx.base;
    requires javafx.web;
    requires com.sun.jna;
    requires de.mkammerer.argon2.nolibs;
    requires org.junit.jupiter.api;
    requires java.desktop;
    requires jdk.compiler;
    requires google.genai;
    requires com.google.api.client;
    requires json.simple;
    requires org.checkerframework.checker.qual;

    exports schedulify.authentication;
    opens schedulify.authentication to javafx.fxml;
    exports schedulify.service;
    opens schedulify.service to javafx.fxml;
    exports schedulify.ui;
    opens schedulify.ui to javafx.fxml;
    exports schedulify.models;
    opens schedulify.models to javafx.fxml;
}