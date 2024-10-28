package com.example.nestapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PingController {
    private static boolean isReachable(String addr, int openPort, int timeOutMillis) {
        // Any Open port on other machine
        // openPort =  22 - ssh, 80 or 443 - webserver, 25 - mailserver etc.
        try {
            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress(addr, openPort), timeOutMillis);
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    @GetMapping("/ping")
    public Map<String, Object> ping(@RequestParam("ip") String ip) {
        Map<String, Object> response = new HashMap<>();
        try {
            InetAddress address = InetAddress.getByName(ip);
            System.out.println("Sprawdzany adres: " + address.getHostAddress());

            boolean reachable = isReachable(ip, 443, 5000); // Timeout 2 sekund
            response.put("ip", ip);
            response.put("reachable", reachable);
            response.put("status", reachable ? "Host jest osiągalny" : "Host jest nieosiągalny");

        } catch (IOException e) {
            response.put("error", "Wystąpił problem z połączeniem z podanym adresem IP.");
            response.put("exception", e.getMessage());
            System.err.println("Błąd: " + e.getMessage());
        }
        return response;
    }
}