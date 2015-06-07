import org.json.simple.JSONArray;

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
public class Friends extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();
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

        if (db != null){
            try {
                String sql = "SELECT * FROM users" + ";";
                ResultSet rs = db.getRs(sql);
                int[] friends = getArrayFromParameters(req.getParameter("id"));
                int userId;
                JSONArray wrapper = new JSONArray();
                while (rs.next()){
                    userId = rs.getInt(1);
                    for (int i = 0; i < friends.length; i++) {
                        if (friends[i] == userId){
                            wrapper.add(userId);
                        }
                    }
                }
                StringWriter json = new StringWriter();
                wrapper.writeJSONString(json);
                String jsonText = json.toString();
                out.print(jsonText);
                db.closeAll();
//                out.print(wrapper);
//                db.closeAll();
            }
            catch (SQLException e1){
                e1.printStackTrace();
                out.println(e1.getMessage());
                resp.sendError(403, e1.getLocalizedMessage());
            }
        }
        else{
            resp.sendError(400, "Can't connect to database");
        }
    }
    private int[] getArrayFromParameters(String params){
        String[] ids = params.split(",");
        int[] friends = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = ids[i].substring(ids[i].lastIndexOf(" ")+1);
            friends[i] = Integer.parseInt(ids[i]);
        }
        return friends;
    }
}
