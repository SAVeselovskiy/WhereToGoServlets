import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Admin on 07.06.2015.
 */
public class UrlFilterForEvents implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            String url = ((HttpServletRequest)request).getRequestURL().toString();
            RestRequest restRequest = new RestRequest(url);
            if (restRequest.isPhoto()) {
                EventPhoto eventPhoto = new EventPhoto();
                eventPhoto.doGet((HttpServletRequest)request,(HttpServletResponse)response);
            }
            else {
                EventTypes eventTypes = new EventTypes();
                eventTypes.doGet((HttpServletRequest) request, (HttpServletResponse) response);
            }
        }
    }

    public void destroy() {}
}
