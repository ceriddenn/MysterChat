package com.mystermc.mysterchat.utils;

import org.bukkit.ChatColor;

public class Util {

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
