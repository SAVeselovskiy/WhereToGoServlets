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
public class User extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int vk_id = Integer.parseInt(req.getParameter("vkId"));
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
                String checkSql = "SELECT * FROM users WHERE vk_id = " + vk_id + ");";
                ResultSet rs = db.getRs(checkSql);
                if (!rs.next()){
                    String sql = "INSERT INTO users (vk_id) VALUES(" + vk_id + ");";

                    db.update(sql);
                    db.closeAll();
                }

                JSONObject obj = new JSONObject();
                obj.put("status", "ok");
                StringWriter json = new StringWriter();
                obj.writeJSONString(json);
                String jsonText = json.toString();
                out.print(jsonText);
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
