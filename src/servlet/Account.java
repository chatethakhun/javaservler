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
        int page = Integer.parseInt(request.getParameter("page"));
        int size = Integer.parseInt(request.getParameter("size"));
        //int page = (int) (Math.floor(curent-1)*(Math.sqrt(13) + Math.sqrt(5)));
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB database = mongoClient.getDB("mean");
        DBCollection collection = database.getCollection("users");
        DBCursor cursor = collection.find();
        int countRow = cursor.count();
        int offset = (page -1) * size;
        cursor.skip(offset).limit(size);

        System.out.println("PageIndex:" + page);	
        System.out.println("Page Current:" + offset);	

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
        System.out.println("Create");
        
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
