package com.poly.greeen.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServerUriProvider {

    private final ServletWebServerApplicationContext webServerAppContext;

    public String getServerUri() {
        String scheme = "http"; // Default scheme
        int port = webServerAppContext.getWebServer().getPort();

        String host = "localhost"; // Default host
        return scheme + "://" + host + ":" + port; // Construct the URI
    }
}
