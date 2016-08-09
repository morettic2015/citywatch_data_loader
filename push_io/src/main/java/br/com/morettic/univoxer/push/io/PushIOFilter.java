/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.morettic.univoxer.push.io;

import br.com.morettic.univoxer.push.io.bean.PushHash;
import br.com.morettic.univoxer.push.io.helper.PushAndroid;
import br.com.morettic.univoxer.push.io.helper.PushCatalog;
import br.com.morettic.univoxer.push.io.helper.PushIOS;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashSet;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import br.com.morettic.univoxer.push.io.bean.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;

/**
 *
 * @author LuisAugusto
 */
@WebFilter(filterName = "SmartProxyFilter",
        urlPatterns = {"/push.io"},
        dispatcherTypes = {
            DispatcherType.FORWARD,
            DispatcherType.ERROR,
            DispatcherType.REQUEST,
            DispatcherType.INCLUDE})
public class PushIOFilter implements Filter {

    private static Logger logger = Logger.getAnonymousLogger();

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    /**
     * Process the Set with pushes;
     *
     */
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {

        HashSet<PushHash> lPushs = (HashSet<PushHash>) PushCatalog.getPushList();

        for (PushHash pushHash : lPushs) {
            //Must send push now!
            if (pushHash.getAction().equals(Action.CALL)) {
                DeviceType dv = pushHash.getDeviceType();
                switch (dv) {
                    case ANDROID:
                        pushAndroid(pushHash);
                        break;
                    case IOS:
                        pushIos(pushHash, request.getServletContext());
                        break;
                    case WP:
                        break;
                    default:
                        break;
                }
            }
        }

    }

    private boolean pushAndroid(PushHash ph) {
        JsonObject js = PushAndroid.sendNotification(ph.getAction().name(), ph.getDeviceId());

        //@todo verificar o tipo da ação......
        if (js.get("status").equals(HttpURLConnection.HTTP_CREATED)) {
            PushCatalog.removePush(ph);
            return true;
        }
        return false;
    }

    private void pushIos(PushHash ph, ServletContext s) {
        String fullPathIos = s.getRealPath("/WEB-INF/certified/univoxer.p12");
        PushIOS pushIOs = new PushIOS();
        try {
            if(pushIOs.sendPushIOs(ph.getDeviceId(), fullPathIos, "Univoxer.com", ph.getAction().name())){
                 Logger.getLogger(PushIOFilter.class.getName()).log(Level.INFO, null, "PUSH SENT");
            }else{
                Logger.getLogger(PushIOFilter.class.getName()).log(Level.WARNING, null, "PUSH NOT SENT");
            }
        } catch (CommunicationException | KeystoreException | InvalidDeviceTokenFormatException ex) {
            Logger.getLogger(PushIOFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        logger.log(Level.WARNING, "EXECUTANDO REQUISI\u00c7\u00c2O DE REGISTRO{0}", new Date().getTime());
        chain.doFilter(request, response);
        logger.log(Level.WARNING, "INICIO DO POS PROCESSAMENTO{0}", new Date().getTime());
        doAfterProcessing(request, response);
        logger.log(Level.WARNING, "FIM DO POS PROCESSAMENTO{0}", new Date().getTime());

    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;

    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("PushIOFilter()");
        }
        StringBuffer sb = new StringBuffer("PushIOFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
