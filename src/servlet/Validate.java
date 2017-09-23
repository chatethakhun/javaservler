package servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import tools.*;

/**
 *
 * @author chate
 */
public class Validate extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Validate</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Validate at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");
            request.setCharacterEncoding("UTF-8");

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");
            request.setCharacterEncoding("UTF-8");
            String header = request.getHeader("Authorization");
            response.setContentType("application/json");
            boolean status = false;
            String message = "";

            BodyRequest getBody = new BodyRequest();
            String data = getBody.readRequestBody(request);
            System.out.println("Data is :" + data);
            String username = "";
            String password = "";
            String name = "";
            String lastname = "";



        
        try {
            JSONObject json = new JSONObject(data);
            username = json.getString("username");
            password = json.getString("password");
            MD5 md5 = new MD5();
            String token = "";
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB database = mongoClient.getDB("mean");
            DBCollection collection = database.getCollection("users");
            BasicDBObject whereQuery = new BasicDBObject();
            List<BasicDBObject> objDB = new ArrayList<BasicDBObject>();
            objDB.add(new BasicDBObject("username", username));
            objDB.add(new BasicDBObject("password", password));
            whereQuery.put("$and", objDB);
          
            System.out.println(whereQuery.toString());
          
            DBCursor cursor = collection.find(whereQuery);
            JSONObject returnJson = new JSONObject();
                if(cursor.count() == 0) {
                    System.out.println("not found");
                    //status = true;
                    message = "Login unsuccess";
                    returnJson.put("status", status);
                    returnJson.put("message", message);
                    PrintWriter out = response.getWriter();
                    out.print(returnJson);
                
            }else {
                    while (cursor.hasNext()) {
                        BasicDBObject obj = (BasicDBObject) cursor.next();
                        token = obj.getString("token");
                        name = obj.getString("firstName");
                        lastname = obj.getString("lastName");
                    }
                    
                    status = true;
                    message = "Login success";
                    returnJson.put("status", status);
                    returnJson.put("name", name);
                    returnJson.put("lastname", lastname);
                    returnJson.put("message", message);
                    returnJson.put("token", token);
                    PrintWriter out = response.getWriter();
                    out.print(returnJson);
                }


        } catch (JSONException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
