package com.example.bajajtask;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        System.out.println("App Started");

        // Create RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // STEP 1: Call Generate Webhook API
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        Map<String, String> request = new HashMap<>();
        request.put("name", "Aditya Nalawade");
        request.put("regNo", "REG12347");
        request.put("email", "aditya@example.com");

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map body = response.getBody();

        String webhook = (String) body.get("webhook");
        String token = (String) body.get("accessToken");

        System.out.println("Webhook: " + webhook);
        System.out.println("Token: " + token);

        // STEP 2: Check odd/even
        String regNo = "REG12347";
        String lastTwo = regNo.substring(regNo.length() - 2);
        int num = Integer.parseInt(lastTwo);

        boolean isOdd = num % 2 != 0;

        System.out.println("Is Odd: " + isOdd);

        // STEP 3: Your SQL Query (REPLACE THIS)
        String finalQuery = "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, d.DEPARTMENT_NAME FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID WHERE DAY(p.PAYMENT_TIME) != 1 ORDER BY p.AMOUNT DESC LIMIT 1;";

        // STEP 4: Send Answer to Webhook
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("finalQuery", finalQuery);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(reqBody, headers);

        ResponseEntity<String> result = restTemplate.postForEntity(
                webhook,
                entity,
                String.class
        );

        System.out.println("Final Response: " + result.getBody());
    }
}