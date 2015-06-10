import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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

        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        InitDB db = null;
        InitDB db2 = null;
        InitDB db3 = null;
        try {
            db = new InitDB();
            db2 = new InitDB();
            db3 = new InitDB();
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(401, e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(402, e.getLocalizedMessage());
        }

        if (db != null && db2!=null && db3!=null) {
            int senderId;
            int eventId;
            String senderName;
            try {
                String sql = "SELECT * FROM invite where receiver_id = " + userId + ";";
                JSONArray wrapper = new JSONArray();
                ResultSet rs = db.getRs(sql);
                while (rs.next()){
                    senderId = rs.getInt(1);
                    eventId = rs.getInt(3);
                    String sql3 = "SELECT * FROM users WHERE vk_id = " + senderId + ";";
                    ResultSet rs3 = db3.getRs(sql3);
                    rs3.next();
                    senderName = rs3.getString(2);

                    String sql2 = "SELECT * FROM event WHERE id = " + eventId + ";";
                    ResultSet rs2 = db2.getRs(sql2);

                    JSONObject eventObject = new JSONObject();
                    while(rs2.next()) {
                        int id = rs2.getInt(6);
                        String name = rs2.getString(2);
                        String description = rs2.getString(3);
                        int start_date = Integer.parseInt(rs2.getString(4));
                        String end_date = rs2.getString(5);
                        int typeId = rs2.getInt(1);
                        String pathToWeb = getServletContext().getRealPath(File.separator) + "icons/event_" + id + ".jpg";


                        eventObject.put("id", id);
                        eventObject.put("typeId", typeId);
                        eventObject.put("name", name);
                        eventObject.put("description", description);
                        eventObject.put("start_date", start_date);
                        if (end_date != null) {
                            eventObject.put("end_date", Integer.parseInt(end_date));
                        }
                        if ((new File(pathToWeb)).exists()) {
                            eventObject.put("hasPhoto", true);
                        } else {
                            eventObject.put("hasPhoto", false);
                        }
                    }

                    JSONObject object = new JSONObject();
                    object.put("sender",senderId);
                    object.put("senderName", senderName);
                    object.put("event",eventObject);
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
        String receiverId = req.getParameter("receiverId");
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

        String[] identifires = receiverId.split(",");

        if (db != null) {
            try {
                for (int i = 0; i < identifires.length; i++) {
                    int receiver = Integer.parseInt(identifires[i]);
//                    VALUES(" + senderId + "," + receiver + "," + eventId + ");";
                    String sql = "INSERT INTO invite (sender_id,receiver_id,event_id) SELECT " + senderId + "," + receiver + "," + eventId +
                            " WHERE NOT EXISTS (SELECT * FROM invite " +
                            "WHERE invite.sender_id="+senderId+" AND invite.receiver_id="+receiver+" AND invite.event_id="+eventId+");";
                    db.update(sql);
                }
                JSONObject obj = new JSONObject();
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
