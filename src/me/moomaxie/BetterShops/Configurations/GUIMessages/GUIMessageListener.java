package me.moomaxie.BetterShops.Configurations.GUIMessages;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class GUIMessageListener implements Listener{

    @EventHandler
    public void onGUIClick(InventoryClickEvent e){

        if (e.getInventory().getName().equals("§7[BetterShops] §dLanguages")){
            Player p = (Player) e.getWhoClicked();

            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR){
                e.setCancelled(true);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eMain GUI Messages")){
                    LanguageInventory.openLanguageInventory("MainGUI",p,1);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eCheckout Messages")){
                    LanguageInventory.openLanguageInventory("Checkout",p,1);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eBuying and Selling Messages")){
                    LanguageInventory.openLanguageInventory("BuyingAndSelling",p,1);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eItem Messages")){
                    LanguageInventory.openLanguageInventory("ItemTexts",p,1);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eSearch Engine Messages")){
                    LanguageInventory.openLanguageInventory("SearchEngine",p,1);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eShop Keeper Manager Messages")){
                    LanguageInventory.openLanguageInventory("ShopKeeperManager",p,1);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eShop Settings Messages")){
                    LanguageInventory.openLanguageInventory("ShopSettings",p,1);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eShop History Messages")){
                    LanguageInventory.openLanguageInventory("History",p,1);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eChat Messages")){
                    LanguageInventory.openLanguageInventory("Messages",p,1);
                }
            }
        }
    }
}
