package com.mystermc.mysterchat.main;

import com.mystermc.mysterchat.listener.Chat;
import com.mystermc.mysterchat.listener.PlayerJoin;
import com.mystermc.mysterchat.listener.PlayerQuit;
import com.mystermc.mysterchat.listener.StaffChatPml;
import com.mystermc.mysterchat.manager.GeneralEventsHandler;
import com.mystermc.mysterchat.manager.StaffChatManager;
import com.mystermc.mystercore.utils.CoreAPI;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MysterChat extends JavaPlugin {
    private MysterChat instance;
    private final StaffChatManager staffChatManager = new StaffChatManager(this);
    private final GeneralEventsHandler generalEventsHandler = new GeneralEventsHandler(staffChatManager);
    private final StaffChatPml staffChatPml = new StaffChatPml(staffChatManager);
    @Override
    public void onEnable() {
        instance = this;
        // register plugin msg channels
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", staffChatPml);
        getServer().getPluginManager().registerEvents(new Chat(staffChatManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(generalEventsHandler), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(generalEventsHandler), this);
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord", staffChatPml);
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
    }

}
