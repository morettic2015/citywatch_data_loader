/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.morettic.univoxer.push.io;

import br.com.morettic.univoxer.push.io.bean.PushHash;
import br.com.morettic.univoxer.push.io.helper.PushCatalog;
import br.com.morettic.univoxer.push.io.task.TimerTask;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author LuisAugusto
 */
@WebServlet(name = "PushServet", urlPatterns = {"/push.io"})
public class PushServlet extends HttpServlet {

    /**
     * SingleTon
     */

    static {
        PushCatalog.newInstance();
        try {
            initPushProcess();
        } catch (SchedulerException ex) {
            Logger.getLogger(PushServlet.class.getName()).log(Level.SEVERE, null, ex);
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("Content-Type: application/json");
        JsonObject js = new JsonObject();
        if (request.getParameter("deviceId") == null || request.getParameter("so") == null || request.getParameter("userId") == null || request.getParameter("action") == null) {
            js.addProperty("status", 500);
        } else {
            js.addProperty("status", 200);
            js.addProperty("deviceId", request.getParameter("deviceId"));
            js.addProperty("deviceType", request.getParameter("so"));
            js.addProperty("action", request.getParameter("action"));
            js.addProperty("userId", Integer.parseInt(request.getParameter("userId")));
            PushCatalog.addPush(new PushHash(js));
        }

        response.getWriter().print(js);

    }

    private static final void initPushProcess() throws SchedulerException {
        // Quartz 1.6.3
       
        JobDetail job = JobBuilder.newJob(TimerTask.class).withIdentity("PUSH_STATUS_JOB", "group1").build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("PUSH_JOB", "group1")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(300000).repeatForever())
                .build();

        // schedule it
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        //scheduler.start();
       // scheduler.scheduleJob(job, trigger);
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
