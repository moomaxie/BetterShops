package max.hubbard.bettershops.Utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ChatManager implements Listener{

    public static HashMap<UUID,Chat> calls = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if (calls.containsKey(e.getPlayer().getUniqueId())){
            if (!e.getMessage().equalsIgnoreCase("cancel")) {
                try {
                    calls.get(e.getPlayer().getUniqueId()).call(e.getMessage());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                calls.remove(e.getPlayer().getUniqueId());
            }
            e.setCancelled(true);
        }
    }


}
