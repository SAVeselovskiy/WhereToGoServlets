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
public class Events extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        int typeId = Integer.parseInt(req.getParameter("type"));
        PrintWriter out = response.getWriter();
        InitDB db = null;
        try {
            db = new InitDB();
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(400, e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(400, e.getLocalizedMessage());
        }
        if (db != null){
            try {
                String sql = "SELECT * FROM event WHERE type_id = " + typeId + ";";
                JSONArray wrapper = new JSONArray();
                ResultSet rs = db.getRs(sql);
                while(rs.next()) {
                    int id = rs.getInt(6);
                    String name = rs.getString(2);
                    String description = rs.getString(3);
                    int start_date = Integer.parseInt(rs.getString(4));
                    String end_date = rs.getString(5);

                    String pathToWeb = getServletContext().getRealPath(File.separator) + "icons/event_" + id + ".jpg";

                    JSONObject obj=new JSONObject();
                    obj.put("id",id);
                    obj.put("typeId",typeId);
                    obj.put("name",name);
                    obj.put("description",description);
                    obj.put("start_date", start_date);
                    if (end_date != null) {
                        obj.put("end_date",Integer.parseInt(end_date));
                    }
                    if ((new File(pathToWeb)).exists()){
                        obj.put("hasPhoto",true);
                    }
                    else{
                        obj.put("hasPhoto",false);
                    }

                    wrapper.add(obj);
                }
                JSONObject obj = new JSONObject();
                obj.put("events",wrapper);
                StringWriter json = new StringWriter();
                obj.writeJSONString(json);
                String jsonText = json.toString();
                out.print(jsonText);
                db.closeAll();
            } catch (SQLException e1) {
                e1.printStackTrace();
                out.println(e1.getMessage());
                response.sendError(400, e1.getLocalizedMessage());
            }

        }
        else
            response.sendError(400, "Can't connect to database");
    }
}

