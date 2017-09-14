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
            	String dataObject = readRequestBody(request);
            	int curent = Integer.parseInt(request.getParameter("page"));
                int page = (int) (Math.floor(curent-1)*(Math.sqrt(13) + Math.sqrt(5)));
	
                String sort = request.getParameter("sort");
                String order = request.getParameter("order");
                int size = Integer.parseInt(request.getParameter("size"));
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB database = mongoClient.getDB("mean");
        DBCollection collection = database.getCollection("users");
        DBCursor cursor = collection.find();
        int countRow = cursor.count();
        cursor.skip(page).limit(size);

        System.out.println("PageIndex" + page);	
        System.out.println("CountRow" + size);	

        PrintWriter pw = response.getWriter(); 
        List<DBObject> myList = null;
        myList = cursor.toArray();
        //String json = jsonConvert(cursor);
                //System.out.println(json);


         pw.write("{\"total_count\":" + countRow + ",\"pageIndex\":" + page +  ",\"items\":" + myList + "}");
         pw.close();
    }  
    
      //  pw.write(json);
    


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

        Users users = new Users();
        users.setFirstName(firstName);
        users.setLastName(lastName);
        users.setUsername(username);
        users.setPassword(password);
        users.setEmail(email);
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        DB db = mongoClient.getDB( "mean" );
        DBCollection collection = null ;
        collection = db.getCollection("users");
        collection.save(users);
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
        System.out.println("Buffer" + buffer.toString());
        return buffer.toString();
    } catch (Exception e) {
        System.out.println("Failed to read the request body from the request.");

    }
    return null;
}

}
