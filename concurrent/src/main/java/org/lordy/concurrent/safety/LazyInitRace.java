package org.lordy.concurrent.safety;

import net.jcip.annotations.NotThreadSafe;

/**
 * 先检查后执行的一种常见情况 - 延迟初始化
 */
@NotThreadSafe
public class LazyInitRace {

    private ExpensiveRace instance = null;

    public ExpensiveRace getInstance() {
        if(instance == null){
            instance = new ExpensiveRace();
        }
        return instance;
    }

    class ExpensiveRace{

    }
}
