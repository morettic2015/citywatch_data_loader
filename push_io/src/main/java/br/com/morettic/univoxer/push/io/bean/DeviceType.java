/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.morettic.univoxer.push.io.bean;

/**
 *
 * @author LuisAugusto
 */
public enum DeviceType {
    IOS(1),
    ANDROID(2),
    WP(3);

    private final int type;
    
    private DeviceType(int mType) {
        type = mType;
    }
    
    public int getValue(){
        return type;
    }
    
    
    
}
