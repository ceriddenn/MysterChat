package com.mystermc.mysterchat.manager;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GeneralEventsHandler {

    private static StaffChatManager manager;

    public GeneralEventsHandler(StaffChatManager manager) {
        this.manager = manager;
    }

    public static void handlePlayerConnect(Player player) {
        // add bukkit scheduler
        String serverName = player.getServer().getServerName();
        String joinMessage = "&8&l[&r&cS&r&8&l]&r &e&l" + serverName + "&8 (&c&l STAFF: &r&b" + player.getName() + " joined the network! &8)";
        manager.sendCustomStaffChatMessage(joinMessage);
        manager.forwardPluginMessage(joinMessage, player.getServer().getServerName(), player, true);
    }

    public void handlerPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String serverName = player.getServer().getServerName();
        String joinMessage = "&8&l[&r&cS&r&8&l]&r &e&l" + serverName + "&8 (&c&l STAFF: &r&b" + player.getName() + " left the network! &8)";
        manager.sendCustomStaffChatMessage(joinMessage);
        manager.forwardPluginMessage(joinMessage, player.getServer().getServerName(), player, true);
    }

}
