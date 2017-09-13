package servlet;

import flexjson.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.*;
import javax.servlet.http.*;

import org.bson.Document;
import org.json.*;
import org.json.JSONException;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


/**
 * Servlet implementation class Account
 */

public class Account extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
            @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Doget");
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB database = mongoClient.getDB("mean");
        DBCollection collection = database.getCollection("users");
        DBCursor cursor = collection.find();
        PrintWriter pw = response.getWriter(); 
        List<DBObject> myList = null;
        myList = cursor.toArray();
        String json = jsonConvert(myList);
        pw.write(myList.toString());
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    	//response.addHeader("Access-Control-Allow-Origin","*");
    	//response.addHeader("Access-Control-Allow-Methods"," GET, POST, OPTIONS");
    	//response.addHeader("Access-Control-Allow-Headers","Content-Type");
        PrintWriter out = response.getWriter();
        
        String data = readRequestBody(request);
        String firstName = "";
        String lastName = "";
        String username = "";
        String password = "";
        String email = "";
        
        try {
            //System.out.println(data);
            //String pass = request.getParameter("pass");
            //Object dataJson = jsonConvert(data);
            JSONObject json = new JSONObject(data);

            firstName = json.getString("firstName");
            lastName = json.getString("lastName");
            username = json.getString("username");
            password = json.getString("password");
            email = json.getString("email");
        } catch (JSONException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }


        System.out.println(firstName);
        System.out.println(lastName);
        System.out.println(username);
        System.out.println(password);
        System.out.println(email);
        
		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoDatabase db = mongo.getDatabase("mean");
        MongoCollection<Document> collection = db.getCollection("users");
        BasicDBObject query = new BasicDBObject();
        BasicDBObject fields = new BasicDBObject();
        //fields.put("name", 1);
        
        Document document = new Document("firstName", firstName)
                .append("lastName", lastName)
                .append("username", username)
                .append("password", password)
                .append("email", email);

        collection.insertOne(document);
        //System.out.println("Success " + id);
           RequestDispatcher rs = request.getRequestDispatcher("index.html");
           //rs.include(request, response);
        PrintWriter pw = response.getWriter();
        pw.write(data);
        //processRequest(request, response);
    }


    
    protected String jsonConvert(Object data) {
        JSONSerializer serializer = new JSONSerializer();
        return serializer.serialize( data );
    }// </editor-fold>

    protected String readRequestBody(HttpServletRequest request) {
    try {
        // Read from request
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        return buffer.toString();
    } catch (Exception e) {
        System.out.println("Failed to read the request body from the request.");

    }
    return null;
}

}
