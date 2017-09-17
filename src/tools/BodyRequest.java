/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.BufferedReader;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author chate
 */
public class BodyRequest {
    
    public String readRequestBody(HttpServletRequest request) {
    try {
        // Read from request

        
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;

        while ((line = reader.readLine()) != null) {
            buffer.append(line);

        }
                                    System.out.println(buffer.toString());
        return buffer.toString();
    } catch (Exception e) {
        System.out.println("Failed to read the request body from the request.");

    }
    return null;
}
    
}
