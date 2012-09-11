package ress;

import com.scientiamobile.wurflcloud.CloudClientLoader;
import com.scientiamobile.wurflcloud.ICloudClientManager;
import com.scientiamobile.wurflcloud.device.AbstractDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: andersandersen
 * Date: 8/31/12
 * Time: 09:02
 * To change this template use File | Settings | File Templates.
 */
public class CookieFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private ICloudClientManager manager;
    //customize with your cloud service key
    private static final String TEST_API_KEY = "684882:t1548Swi3dvTxUj2LIqm6ngZNXslkz7W";
    private String[] capabilities;

    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            CloudClientLoader loader = new CloudClientLoader(TEST_API_KEY);
            manager = loader.getClientManager();
            capabilities = new String[]{"brand_name", "model_name", "marketing_name", "max_image_width", "ux_full_desktop", "resolution_width", "pointing_method"};
        } catch (Throwable t) {
            throw new ServletException(t);
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        Map<String, String> featureCapabilities = readFeatureCookie((HttpServletRequest) servletRequest);
        Map<String, String> wurflCapabilities = readWurflCapabilities((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);

        Map<String, String> ressCapabilities = new HashMap<String, String>();


        int screenFeature = 0;
        if (featureCapabilities.get("width") != null && featureCapabilities.get("width").matches("\\d.*")) {
            screenFeature = Integer.parseInt(featureCapabilities.get("width"));
        }
        int screenWurfl = Integer.parseInt(wurflCapabilities.get("max_image_width"));

        //calculate RESS capas, width and imageversion
        ressCapabilities.put("width", String.valueOf(screenFeature > 0 ? screenFeature : screenWurfl));
        ressCapabilities.put("imageVersion", String.valueOf(getImageVersion(Integer.parseInt(ressCapabilities.get("width")))));
        ressCapabilities.put("connection",featureCapabilities.get("connection") != null ? featureCapabilities.get("connection")  : "Unknown");

        servletRequest.setAttribute("featureCapabilities", featureCapabilities);
        servletRequest.setAttribute("wurflCapabilities", wurflCapabilities);
        servletRequest.setAttribute("ressCapabilities", ressCapabilities);

        filterChain.doFilter(servletRequest, servletResponse);
    }


    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getImageVersion(int defaultWidth) {
        if (defaultWidth < 320) {
            return 320;
        } else if (defaultWidth < 500) {
            return 500;
        } else if (defaultWidth <= 1024) {
            return 640;
        } else {
            return 770;
        }
    }

    public Map<String, String> readFeatureCookie(HttpServletRequest request) {

        Map<String, String> capabilities = new HashMap<String, String>();

        String ressCookie = readCookieValue(request, "RESS");

        if (ressCookie == null) {
            //add 0 as default width
            capabilities.put("width", "0");

        } else if (ressCookie.contains("|")) {

            String[] valuePair = ressCookie.split("\\|");

            for(String value : valuePair){
                //cookie contains width.1440
                if (value.contains(".")) {
                    String[] cookieValue = value.split("\\.");
                    capabilities.put(cookieValue[0], cookieValue[1]);
                }
            }
        }

        return capabilities;
    }


    private String readCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals(cookieName)) {
                return c.getValue();
            }
        }
        return null;
    }

    public Map<String, String> readWurflCapabilities(HttpServletRequest req, HttpServletResponse resp) {

        String useragent = req.getHeader("User-Agent");
        if (useragent == null) useragent = req.getHeader("user-agent");
        if (logger.isDebugEnabled()) logger.debug("user agent: " + useragent);

        //AbstractDevice device = manager.getDeviceFromRequest(req, resp, capabilities);
        AbstractDevice device = manager.getDeviceFromRequest(req, resp);

        Map<String, String> capabilities = copyMap(device.getCapabilities());
        if (capabilities.containsKey("max_image_width")
                && capabilities.containsKey("ux_full_desktop")
                && capabilities.get("ux_full_desktop").equals("true")) {

            capabilities.put("max_image_width", "1440");
        }


        return capabilities;
    }

    public Map<String, String> copyMap(Map<String, Object> copyMe) {
        Map<String, String> copyTo = new HashMap<String, String>();

        Set<String> s = copyMe.keySet();
        for (String key : s) {
            copyTo.put(key, String.valueOf(copyMe.get(key)));
        }

        return copyTo;

    }


}
