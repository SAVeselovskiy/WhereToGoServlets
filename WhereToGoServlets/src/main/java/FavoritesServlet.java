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
 * Created by Admin on 10.06.2015.
 */
public class FavoritesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String parameter = req.getParameter("events");
        String[] identifires = parameter.split(",");
        PrintWriter out = resp.getWriter();
        InitDB db = null;
        try {
            db = new InitDB();
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(400, e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            resp.sendError(400, e.getLocalizedMessage());
        }

        if (db != null) {
            try {
                JSONArray wrapper = new JSONArray();
                for (int i = 0; i < identifires.length; i++) {
                    String sql = "SELECT * FROM event WHERE id = " + Integer.parseInt(identifires[i]) + ";";
                    ResultSet rs = db.getRs(sql);
                    while(rs.next()) {
                        int id = rs.getInt(6);
                        String name = rs.getString(2);
                        String description = rs.getString(3);
                        int start_date = Integer.parseInt(rs.getString(4));
                        String end_date = rs.getString(5);
                        int typeId = Integer.parseInt(rs.getString(1));
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
                resp.sendError(400, e1.getLocalizedMessage());
            }
        }
    }
}
