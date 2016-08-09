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
public enum Action {
    CALL(1),
    ONLINE(2),
    OFFLINE(3);
    
    private final int action;
    
    private Action(int i){
        this.action = i;
    }
}
