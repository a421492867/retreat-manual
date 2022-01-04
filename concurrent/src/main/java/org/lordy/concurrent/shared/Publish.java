package org.lordy.concurrent.shared;

import java.util.HashSet;
import java.util.Set;

/**
 * 发布一个新对象
 */
public class Publish {

    public static Set<Secret> knownSecrets;

    public void initialize(){
        knownSecrets = new HashSet<Secret>();
    }


    private class Secret{}
}
