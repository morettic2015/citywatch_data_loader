/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.morettic.univoxer.push.io;

import br.com.morettic.univoxer.push.io.bean.PushHash;
import br.com.morettic.univoxer.push.io.helper.PushCatalog;
import br.com.morettic.univoxer.push.io.helper.PushIOS;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;

/**
 *
 * @author LuisAugusto
 */
@WebServlet(name = "PushIOSServer", urlPatterns = {"/apple.io"})
public class PushIOSServer extends HttpServlet {

    private void pushIos(PushHash ph, ServletContext s) {
        String fullPathIos = s.getRealPath("/WEB-INF/certified/voip_cert_notification.p12");
        PushIOS pushIOs = new PushIOS();
        try {
            if (pushIOs.sendPushIOs(ph.getDeviceId(), fullPathIos, "Univoxer.com", ph.getAction().name())) {
                Logger.getLogger(PushIOFilter.class.getName()).log(Level.INFO, null, "PUSH SENT");
            } else {
                Logger.getLogger(PushIOFilter.class.getName()).log(Level.WARNING, null, "PUSH NOT SENT");
            }
        } catch (CommunicationException | KeystoreException | InvalidDeviceTokenFormatException ex) {
            Logger.getLogger(PushIOFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            JsonObject js = new JsonObject();
            js.addProperty("status", 200);
            js.addProperty("deviceId", request.getParameter("deviceId"));
            js.addProperty("deviceType", request.getParameter("so"));
            js.addProperty("action", request.getParameter("action"));
            js.addProperty("userId", Integer.parseInt(request.getParameter("userId")));
           // PushCatalog.addPush(new PushHash(js));
            
            pushIos(new PushHash(js), this.getServletContext());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
