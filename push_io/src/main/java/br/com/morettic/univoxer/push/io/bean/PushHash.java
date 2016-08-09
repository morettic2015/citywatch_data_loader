/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.morettic.univoxer.push.io.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author LuisAugusto
 */
public class PushHash implements Serializable, Comparable<PushHash>{
    private String deviceId;
    private DeviceType deviceType;
    private int userId;
    private Action action;

    
    public PushHash(com.google.gson.JsonObject js){
        this.deviceId = js.get("deviceId").getAsString();
        this.userId = js.get("userId").getAsInt();
        this.deviceType = DeviceType.valueOf(js.get("deviceType").getAsString());
        this.action = Action.valueOf(js.get("action").getAsString());
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return "{" + "deviceId:'" + deviceId + "', 'deviceType':'" + deviceType.name() + "', 'userId':'" + userId + "'}";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.deviceId);
        hash = 59 * hash + Objects.hashCode(this.deviceType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PushHash other = (PushHash) obj;
        if (this.deviceType != other.deviceType) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(PushHash o) {
        return this.deviceId.compareTo(o.getDeviceId());
    }
    
    
    
}
