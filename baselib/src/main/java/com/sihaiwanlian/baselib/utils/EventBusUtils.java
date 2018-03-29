package com.sihaiwanlian.baselib.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * FileName: ${PACKAGE_NAME}.${NAME}.java
 * Author: mouxuefei
 * date: 2017/3/14
 * version: V1.0
 * desc:
 */

public class EventBusUtils {
    
        public static void register(Object context){
            if (!EventBus.getDefault().isRegistered(context)) {
                EventBus.getDefault().register(context);
            }
        }
        public static void unregister(Object context){
            if (EventBus.getDefault().isRegistered(context)) {
                EventBus.getDefault().unregister(context);
            }
        }
        public static void sendEvent(Object object){
            EventBus.getDefault().post(object);
        }
    
}
