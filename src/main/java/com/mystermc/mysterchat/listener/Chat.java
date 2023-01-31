package com.mystermc.mysterchat.listener;

import com.mystermc.mysterchat.manager.StaffChatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {

    private final StaffChatManager manager;

    public Chat(StaffChatManager manager) {
        this.manager = manager;
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        manager.onChat(event);
    }

}
