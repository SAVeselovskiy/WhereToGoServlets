import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Admin on 06.06.2015.
 */
public class Invite extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("id"));
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        InitDB db = null;
        try {
            db = new InitDB();
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(401, e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(402, e.getLocalizedMessage());
        }

        if (db != null) {
            int senderId;
            int eventId;
            try {
                String sql = "SELECT * FROM invite where receiver_id = " + userId + ";";
                JSONArray wrapper = new JSONArray();
                ResultSet rs = db.getRs(sql);
                while (rs.next()){
                    senderId = rs.getInt(1);
                    eventId = rs.getInt(3);
                    JSONObject object = new JSONObject();
                    object.put("sender",senderId);
                    object.put("event",eventId);
                    wrapper.add(object);
                }
                JSONObject object = new JSONObject();
                object.put("invites",wrapper);
                StringWriter json = new StringWriter();
                object.writeJSONString(json);
                String jsonText = json.toString();
                out.print(jsonText);
                db.closeAll();

            }catch(SQLException e1){
                e1.printStackTrace();
                out.println(e1.getMessage());
                resp.sendError(400, e1.getLocalizedMessage());
            }
        }
        else{
            resp.sendError(400, "Can't connect to database");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int senderId = Integer.parseInt(req.getParameter("senderId"));
        int receiverId = Integer.parseInt(req.getParameter("receiverId"));
        int eventId = Integer.parseInt(req.getParameter("eventId"));
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/x-www-form-urlencoded");
        InitDB db = null;
        try {
            db = new InitDB();
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(401, e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(402, e.getLocalizedMessage());
        }

        if (db != null) {
            try {
                String sql = "INSERT INTO invite (sender_id,receiver_id,event_id) VALUES(" + senderId + "," + receiverId + "," + eventId + ");";
                JSONObject obj = new JSONObject();
                db.update(sql);
                db.closeAll();
                obj.put("status", "ok");
                out.print(obj);
            } catch (SQLException e1) {
                e1.printStackTrace();
                out.println(e1.getMessage());
                resp.sendError(403, e1.getLocalizedMessage());
            }
        }
        else{
            resp.sendError(400, "Can't connect to database");
        }
}
}
