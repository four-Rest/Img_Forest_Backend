package com.ll.demo.global.util;

public class Ut {
    public static class match {
        public static boolean isTrue(Boolean bool) {
            return bool != null && bool;
        }

        public static boolean isFalse(Boolean bool) {
            return bool != null && !bool;
        }
    }
}
