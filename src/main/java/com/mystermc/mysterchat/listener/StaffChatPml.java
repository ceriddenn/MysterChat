package com.mystermc.mysterchat.listener;

import com.mystermc.mysterchat.manager.StaffChatManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class StaffChatPml implements PluginMessageListener {
    private final StaffChatManager manager;
    public StaffChatPml(StaffChatManager manager) {
        this.manager = manager;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        manager.onPluginMessageReceived(channel, message);
    }
}
