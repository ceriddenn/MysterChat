package com.mystermc.mysterchat.listener;

import com.mystermc.mysterchat.manager.GeneralEventsHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private final GeneralEventsHandler handler;

    public PlayerQuit(GeneralEventsHandler handler) {
        this.handler = handler;
    }
    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent event) {
        handler.handlerPlayerDisconnect(event);
        event.setQuitMessage(null);
    }
}
