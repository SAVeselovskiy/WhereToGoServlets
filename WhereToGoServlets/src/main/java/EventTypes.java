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
import org.postgresql.Driver;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by Admin on 04.06.2015.
 */
public class EventTypes extends HttpServlet{
    int id = 0;
    String name = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

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
                String sql = "SELECT * FROM event_type" + ";";
                JSONArray wrapper = new JSONArray();
                ResultSet rs = db.getRs(sql);
                while(rs.next()) {
                    id = rs.getInt(2);
                    name = rs.getString(1);
                    JSONObject obj=new JSONObject();
                    obj.put("id",id);
                    obj.put("name",name);
                    wrapper.add(obj);
                }
                JSONObject obj = new JSONObject();
                obj.put("types",wrapper);
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
