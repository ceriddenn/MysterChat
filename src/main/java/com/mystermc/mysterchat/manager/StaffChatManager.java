package com.mystermc.mysterchat.manager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mystermc.mysterchat.main.MysterChat;
import com.mystermc.mysterchat.utils.Util;
import com.mystermc.mystercore.utils.CoreAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StaffChatManager {
    private final MysterChat instance;
    private String onlinePName;
    private final List<UUID> toggledPlayers = new ArrayList<>();
    private final List<UUID> alertsOff = new ArrayList<>();

    public StaffChatManager(MysterChat instance) {
        this.instance = instance;
    }

    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (event.getMessage().equalsIgnoreCase("@toggle")) {
            if (player.hasPermission("mystermc.staff.sc.toggle"))
            if (toggledPlayers.contains(player.getUniqueId())) {
                toggledPlayers.remove(player.getUniqueId());
                event.setCancelled(true);
                player.sendMessage(Util.color("&8&l[&r&cS&r&8&l]&r &b&l" + "Turned your staff chat toggle status to off!"));
            } else {
                toggledPlayers.add(player.getUniqueId());
                event.setCancelled(true);
                player.sendMessage(Util.color("&8&l[&r&cS&r&8&l]&r &b&l" + "Turned your staff chat toggle status to on!"));
            }

        } else if(event.getMessage().equalsIgnoreCase("@alerts")) {
            if (player.hasPermission("mystermc.staff.sc.alerts")) {
                if (alertsOff.contains(player.getUniqueId())) {
                    alertsOff.remove(player.getUniqueId());
                    event.setCancelled(true);
                    player.sendMessage(Util.color("&8&l[&r&cS&r&8&l]&r &b&l" + "Turned on staff chat alerts"));
                } else {
                    alertsOff.add(player.getUniqueId());
                    event.setCancelled(true);
                    player.sendMessage(Util.color("&8&l[&r&cS&r&8&l]&r &b&l" + "Turned off staff chat alerts"));
                }
            }
        } else if (event.getMessage().startsWith("@")) {
            event.setCancelled(true);
            if (alertsOff.contains(player.getUniqueId())) {
                player.sendMessage(Util.color("&8&l[&r&cS&r&8&l]&r &c" + "Your staff chat alerts are off. Please turn them on to send a message."));
                return;
            }
            sendStaffChatMessage(event.getMessage(), player.getName());
            forwardPluginMessage(event.getMessage(), event.getPlayer().getServer().getServerName(), event.getPlayer(), false);
        }  else if (toggledPlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            if (alertsOff.contains(player.getUniqueId())) {
                player.sendMessage(Util.color( "Your staff chat alerts are off. Please turn them on to send a message."));
                return;
            }
            sendStaffChatMessage(event.getMessage(), player.getName());
            forwardPluginMessage(event.getMessage(), event.getPlayer().getServer().getServerName(), event.getPlayer(), false);
        }
    }

    public void sendCustomStaffChatMessage(String msg) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("mystermc.staff.sc.chat")) {
                if (alertsOff.contains(p.getUniqueId())) {
                    return;
                } else {
                    p.sendMessage(Util.color(msg));
                }
            }
        }
    }

    public void sendStaffChatMessage(String msg, String playerName) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("mystermc.staff.sc.chat")) {
                if (alertsOff.contains(p.getUniqueId())) {
                    return;
                } else {
                    p.sendMessage(Util.color("&8&l[&r&cS&r&8&l]&r &c&l" + playerName + "&o&e: " + p.getServer().getServerName() + "&8 > &b" + msg.substring(1)));
                }
            }
        }
    }

    public void sendStaffChatMessage(String msg, String server, String playerName) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("mystermc.staff.sc.chat")) {
                p.sendMessage(Util.color("&8&l[&r&cS&r&8&l]&r &c&l" + playerName + "&o&e: " + server + "&8 > &b" + msg.substring(1)));
            }
        }
    }

    public void forwardPluginMessage(String msg, String server, Player player, boolean custom) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ONLINE");
        out.writeUTF("StaffChat");
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        try {
            dataStream.writeUTF(server);
            dataStream.writeUTF(msg);
            dataStream.writeUTF(player.getName());
            dataStream.writeBoolean(custom);
        } catch (IOException e) {
            e.printStackTrace();
            instance.getLogger().severe("Failed to send plugin message to bungeecord");
            return;
        }
        byte[] data = byteStream.toByteArray();
        out.writeShort(data.length);
        out.write(data);
        player.sendPluginMessage(instance, "BungeeCord", out.toByteArray());
    }
    public void onPluginMessageReceived(String channel, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        if (!subChannel.equals("StaffChat")) {
            return;
        }
        short len = in.readShort();
        byte[] msgbytes = new byte[len];
        in.readFully(msgbytes);

        DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
        String serverName;
        String actualMessage;
        String playerName;
        boolean custom;
        try {
            serverName = msgin.readUTF();
            actualMessage = msgin.readUTF();
            playerName = msgin.readUTF();
            custom = msgin.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
            instance.getLogger().severe("Failed to read plugin message from bungeecord");
            return;
        }
        if (custom) {
            sendCustomStaffChatMessage(actualMessage);
        } else {
            sendStaffChatMessage(actualMessage, serverName, playerName);
        }
    }
}
