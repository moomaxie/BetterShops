package me.moomaxie.BetterShops.Configurations.GUIMessages;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class GUIMessagesInv {

    public static void openGUIMessagesInv(Player p){
        Inventory inv = Bukkit.createInventory(p, 54, "§7[BetterShops] §dLanguages");

        //Glass
        org.bukkit.inventory.ItemStack glass = new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS_PANE,1,(byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 9;i++) {
            inv.setItem(i,glass);
        }

        for (int i = 8; i < 54;i= i+9) {
            inv.setItem(i,glass);
        }

        for (int i = 0; i < 54;i= i+9) {
            inv.setItem(i,glass);
        }

        ItemStack main = new ItemStack(Material.ENDER_CHEST);
        ItemMeta mainMeta = main.getItemMeta();
        mainMeta.setDisplayName("§eMain GUI Messages");
        main.setItemMeta(mainMeta);

        ItemStack checkout = new ItemStack(Material.CHEST);
        ItemMeta checkoutMeta = checkout.getItemMeta();
        checkoutMeta.setDisplayName("§eCheckout Messages");
        checkout.setItemMeta(checkoutMeta);

        ItemStack BAS = new ItemStack(Material.EMERALD);
        ItemMeta BASMeta = BAS.getItemMeta();
        BASMeta.setDisplayName("§eBuying and Selling Messages");
        BAS.setItemMeta(BASMeta);

        ItemStack itemTexts = new ItemStack(Material.DIAMOND);
        ItemMeta itemTextsMeta = itemTexts.getItemMeta();
        itemTextsMeta.setDisplayName("§eItem Messages");
        itemTexts.setItemMeta(itemTextsMeta);

        ItemStack searchEngine = new ItemStack(Material.NAME_TAG);
        ItemMeta searchEngineMeta = searchEngine.getItemMeta();
        searchEngineMeta.setDisplayName("§eSearch Engine Messages");
        searchEngine.setItemMeta(searchEngineMeta);

        ItemStack shopKeeper = new ItemStack(Material.SIGN);
        ItemMeta shopKeeperMeta = shopKeeper.getItemMeta();
        shopKeeperMeta.setDisplayName("§eShop Keeper Manager Messages");
        shopKeeper.setItemMeta(shopKeeperMeta);

        ItemStack shopSettings = new ItemStack(Material.REDSTONE);
        ItemMeta shopSettingsMeta = shopSettings.getItemMeta();
        shopSettingsMeta.setDisplayName("§eShop Settings Messages");
        shopSettings.setItemMeta(shopSettingsMeta);

        ItemStack history = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta historyMeta = history.getItemMeta();
        historyMeta.setDisplayName("§eShop History Messages");
        history.setItemMeta(historyMeta);

        ItemStack chat = new ItemStack(Material.SNOW_BALL);
        ItemMeta chatMeta = chat.getItemMeta();
        chatMeta.setDisplayName("§eChat Messages");
        chat.setItemMeta(chatMeta);

        inv.setItem(10,main);
        inv.setItem(11,checkout);
        inv.setItem(12,BAS);
        inv.setItem(13,itemTexts);
        inv.setItem(14,searchEngine);
        inv.setItem(15,shopKeeper);
        inv.setItem(16,shopSettings);
        inv.setItem(19,history);
        inv.setItem(20, chat);

        p.openInventory(inv);
    }
}
