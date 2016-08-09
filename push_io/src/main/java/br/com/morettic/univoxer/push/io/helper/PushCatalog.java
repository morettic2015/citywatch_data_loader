/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.morettic.univoxer.push.io.helper;

import br.com.morettic.univoxer.push.io.bean.PushHash;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author LuisAugusto
 */
public final class PushCatalog {

    private static Set<PushHash> lPushes;

    public static void newInstance(){
        if(lPushes==null)
            lPushes = new HashSet<PushHash>();
    }
    
    static {
        lPushes = new HashSet<PushHash>();
    }

    public static void addPush(PushHash pPush) {
        if (lPushes == null) {
            lPushes = new  HashSet<PushHash>();
        }

        lPushes.add(pPush);
    }

    public static void removePush(PushHash pPush) {
        if (lPushes == null) {
            lPushes = new  HashSet<PushHash>();
        }

        lPushes.remove(pPush);
    }

    public static Set<PushHash> getPushList() {
        if (lPushes == null) {
            lPushes = new  HashSet<PushHash>();
        }

        return lPushes;
    }

}
