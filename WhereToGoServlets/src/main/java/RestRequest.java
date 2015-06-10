import javax.servlet.ServletException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 07.06.2015.
 */
public class RestRequest {
    private Pattern regExEventPhoto = Pattern.compile("/events/([0-9]+)/photo");
    private  Pattern regExPhoto = Pattern.compile("/photo/([0-9]+)");
    private Integer id;
    private boolean isPhoto = false;

    public RestRequest(String pathInfo) throws ServletException {
        Matcher matcher;

        matcher = regExPhoto.matcher(pathInfo);
        if (matcher.find()){
            id = Integer.parseInt(matcher.group(1));
            isPhoto = true;
            return;
        }

        throw new ServletException("Invalid URI" + pathInfo);
    }
    public int getId(){
        return id;
    }
    public boolean isPhoto(){
        return isPhoto;
    }
}
