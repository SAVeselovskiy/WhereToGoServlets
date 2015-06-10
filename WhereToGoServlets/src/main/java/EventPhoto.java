import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by Admin on 07.06.2015.
 */
public class EventPhoto extends HttpServlet {
    int id = 0 ;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RestRequest resourceValues = new RestRequest(req.getRequestURI());
        id = resourceValues.getId();
//        resp.sendError(400,""+id);
        resp.setContentType("image/jpeg");
        String pathToWeb = getServletContext().getRealPath(File.separator);
        try{
            File f = new File(pathToWeb + "/icons/event_"+ id +".jpg");
            if (!f.exists()){
                resp.sendError(404,pathToWeb + "/icons/event_"+ id +".jpg");
                return;
            }
            BufferedImage bi = ImageIO.read(f);
            OutputStream out = resp.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.close();
        }catch (Exception e){
            resp.sendError(400,e.getLocalizedMessage());
        }
    }
}
