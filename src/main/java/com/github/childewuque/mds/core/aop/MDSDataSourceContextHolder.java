package com.github.childewuque.mds.core.aop;

import org.springframework.core.NamedThreadLocal;
import org.springframework.util.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @description:
 * @author: tanhuiren
 * @date: Created in 2020/3/30 下午2:21
 * @modified By:
 */
public final class MDSDataSourceContextHolder {

    private static final ThreadLocal<Deque<String>> LOOKUP_KEY_HOLDER = new NamedThreadLocal<Deque<String>>("dhgate-datasource") {
        @Override
        protected Deque<String> initialValue() {
            return new ArrayDeque<>();
        }
    };

    private MDSDataSourceContextHolder() {
    }


    public static String peek() {
        return LOOKUP_KEY_HOLDER.get().peek();
    }


    public static void push(String ds) {
        LOOKUP_KEY_HOLDER.get().push(StringUtils.isEmpty(ds) ? "" : ds);
    }


    public static void poll() {
        Deque<String> deque = LOOKUP_KEY_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            LOOKUP_KEY_HOLDER.remove();
        }
    }
}
