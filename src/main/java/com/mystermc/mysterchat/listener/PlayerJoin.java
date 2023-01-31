package com.mystermc.mysterchat.listener;

import com.mystermc.mysterchat.manager.GeneralEventsHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private final GeneralEventsHandler handler;

    public PlayerJoin(GeneralEventsHandler handler) {
        this.handler = handler;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        handler.handlePlayerConnect(event.getPlayer());
        event.setJoinMessage(null);
    }
}
