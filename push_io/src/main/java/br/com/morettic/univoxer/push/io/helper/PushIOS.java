/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.morettic.univoxer.push.io.helper;

import br.com.morettic.univoxer.push.io.PushIOFilter;
import java.io.File;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.Devices;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.Payload;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import javapns.Push;
import javapns.notification.PushNotificationManager;
import javapns.notification.ResponsePacket;

/**
 *
 * @author LuisAugusto
 */
public class PushIOS {

    private static final String password = "Euvs$2016";
    private static final Logger log = Logger.getLogger(PushIOS.class.getName());

    public boolean sendPushIOs(String token, String fullPath, String title, String action) throws CommunicationException, KeystoreException, InvalidDeviceTokenFormatException {

        PushNotificationPayload payload = PushNotificationPayload.complex();

        boolean returnVal = false;

        payload.addAlert(title);
        payload.addBadge(1);
        payload.addSound("default");
        payload.addCustomDictionary("action", action);

        String tokenDevice = token.replaceAll("\\s+", "").replace("<", "").replace(">", "");
        BasicDevice device = new BasicDevice(tokenDevice);

        List<PushedNotification> pushNotificationsList = Push.payload(payload, fullPath, password, true, device.getToken());

        for (PushedNotification pushNotificationPojo : pushNotificationsList) {
            if (pushNotificationPojo.isSuccessful()) {

                /* APPLE ACCEPTED THE pushNotificationPojo AND SHOULD DELIVER IT */
                Logger.getLogger(PushIOS.class.getName()).log(Level.INFO, null, "PUSH pushNotificationPojo SENT SUCCESSFULLY TO: " + pushNotificationPojo.getDevice().getToken());

                /* STILL NEED TO QUERY THE FEEDBACK SERVICE REGULARLY */
                ResponsePacket responsePush = pushNotificationPojo.getResponse();
                if (responsePush != null) {
                    Logger.getLogger(PushIOS.class.getName()).log(Level.INFO, null, "PUSH STATUS: " + responsePush.getStatus());
                    Logger.getLogger(PushIOS.class.getName()).log(Level.INFO, null, "PUSH ID: " + responsePush.getIdentifier());
                }
                returnVal = true;
            } else {
                String invalidToken = pushNotificationPojo.getDevice().getToken();

                /* ADD CODE HERE TO REMOVE invalidToken FROM YOUR DATABASE */
                Logger.getLogger(PushIOS.class.getName()).log(Level.SEVERE, null, "ERROR: " + invalidToken);

                /* FIND OUT MORE ABOUT WHAT THE PROBLEM WAS */
                Exception theProblem = pushNotificationPojo.getException();
                Logger.getLogger(PushIOS.class.getName()).log(Level.SEVERE, null, "ERROR: " + theProblem);

                /* IF THE PROBLEM WAS AN ERROR-RESPONSE PACKET RETURNED BY APPLE, GET IT */
                ResponsePacket THEERRORRESPONSE = pushNotificationPojo.getResponse();
                if (THEERRORRESPONSE != null) {
                    Logger.getLogger(PushIOS.class.getName()).log(Level.SEVERE, null, "ERROR: " + THEERRORRESPONSE.getMessage());
                    returnVal = false;
                }
            }
        }
        pushNotificationsList.clear();
        device = null;
        tokenDevice = null;

        return returnVal;
    }
}
