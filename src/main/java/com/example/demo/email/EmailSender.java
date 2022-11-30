package com.example.demo.email;

import java.util.Map;

public interface EmailSender {
    void send(String to, Map<String, Object> templateAttributes, String templateForm);
}
