package org.lordy.concurrent.shared;

/**
 * 发布内部类实例
 *
 * 当我们实例化ThisEscape对象时，
 * 会调用source的registerListener方法，
 * 这时便启动了一个线程，
 * 而且这个线程持有了ThisEscape对象（调用了对象的doSomething方法），
 * 但此时ThisEscape对象却没有实例化完成（还没有返回一个引用），
 * 所以我们说，此时造成了一个this引用逸出，
 * 即还没有完成的实例化ThisEscape对象的动作，却已经暴露了对象的引用
 */
public class ThisEscape {

    private class EventSource{

        private void registerListener(EventListener eventListener){}
    }

    private class EventListener{
        void onEvent(Event e){}

    }

    private class Event{

    }

    public ThisEscape(EventSource eventSource){
        eventSource.registerListener(new EventListener(){
            public void onEvent(Event e){
                doSomething(e);
            }
        });

    }

    private void doSomething(Event e){}
}
