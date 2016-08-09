/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.morettic.univoxer.push.io.task;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author LuisAugusto
 */
public class TimerTask implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {

        System.out.println("Hello Quartz!"+new Date().toLocaleString());

    }
}
