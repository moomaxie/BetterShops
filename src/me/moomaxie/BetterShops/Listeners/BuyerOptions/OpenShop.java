package me.moomaxie.BetterShops.Listeners.BuyerOptions;

import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class OpenShop implements Listener {


    public static void openShopItems(Inventory inv, Player p, Shop shop, int page) {

        boolean same = true;
        if (inv == null) {
            same = false;
            inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());
        } else {
            inv.clear();
        }

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        ItemStack options = new ItemStack(Material.ENDER_CHEST);
        ItemMeta optionsMeta = options.getItemMeta();

        optionsMeta.setDisplayName("§a§l" + shop.getName());

        if (!shop.isServerShop()) {
            optionsMeta.setLore(Arrays.asList("§7" + shop.getDescription(), " ", MainGUI.getString("Owner") + " §a§l" + shop.getOwner().getName(),
                    MainGUI.getString("Keepers") + " §7" + shop.getManagers().size()));
            options.setItemMeta(optionsMeta);
        } else {
            if (!shop.getOwner().getUniqueId().equals(p.getUniqueId()) || !shop.getOwner().getUniqueId().toString().equals(p.getUniqueId().toString())) {
                optionsMeta.setLore(Arrays.asList("§7" + shop.getDescription(), " ", MainGUI.getString("Owner") + " " + MainGUI.getString("Server"),
                        MainGUI.getString("Keepers") + " §7" + shop.getManagers().size()));
                options.setItemMeta(optionsMeta);
            } else {
                optionsMeta.setLore(Arrays.asList("§7" + shop.getDescription(), " ", MainGUI.getString("BuyingShop"), " ", MainGUI.getString("Owner") + " " + MainGUI.getString("Server"),
                        MainGUI.getString("Keepers") + " §7" + shop.getManagers().size(), " ", MainGUI.getString("OpenShopSettings"), " ", MainGUI.getString("TurnOffServerShop")));
                options.setItemMeta(optionsMeta);
            }
        }

        ItemStack info = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(MainGUI.getString("Buying"));
        infoMeta.setLore(Arrays.asList(MainGUI.getString("ToggleShop"), MainGUI.getString("SearchOptions")));
        info.setItemMeta(infoMeta);

        ItemStack cart = new ItemStack(Material.CHEST);
        ItemMeta cartMeta = cart.getItemMeta();
        cartMeta.setDisplayName(MainGUI.getString("CheckoutDisplayName"));
        cartMeta.setLore(Arrays.asList(MainGUI.getString("CheckoutLore")));
        cart.setItemMeta(cartMeta);

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName(MainGUI.getString("NextPage"));
        arrow.setItemMeta(arrowMeta);

        ItemStack barrow = new ItemStack(Material.ARROW);
        ItemMeta barrowMeta = barrow.getItemMeta();
        barrowMeta.setDisplayName(MainGUI.getString("PreviousPage"));
        barrow.setItemMeta(barrowMeta);

        ItemStack pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta pg1Meta = pg1.getItemMeta();
        pg1Meta.setDisplayName(MainGUI.getString("Page") + " §7" + page);
        pg1.setItemMeta(pg1Meta);

        inv.setItem(3, info);
        inv.setItem(4, cart);
        inv.setItem(5, options);

        inv.setItem(13, pg1);

        if (page > 1) {
            inv.setItem(0, barrow);
        }
        int maxPage = (int) Math.ceil((double) (shop.getShopItems(false).size() - 1) / 36);

        inv.setItem(8, arrow);

        for (ShopItem it : shop.getShopItems(false)) {
            if (it.getPage() == page) {
                ItemStack itemStack = it.getItem().clone();
                List<String> lore = new ArrayList<>();
                ItemMeta meta = itemStack.getItemMeta();
                if (it.getLore() != null) {
                    for (String s : it.getLore()) {
                        lore.add(s);
                    }
                }
                if (it.isInfinite()) {
                    lore.add(MainGUI.getString("Stock") + " §7-");
                } else {
                    lore.add(MainGUI.getString("Stock") + " §7" + it.getStock());
                }
                lore.add(MainGUI.getString("Amount") + " §7" + it.getAmount());
                if (!it.getLiveEco()) {
                    if (it.getPrice() > 0) {
                        lore.add(MainGUI.getString("Price") + " §7" + it.getPriceAsString());
                    } else {
                        lore.add(MainGUI.getString("Price") + " §7" + MainGUI.getString("Free"));
                    }
                } else {
                    if (it.getAdjustedPrice() != it.getOrigPrice()) {
                        lore.add(MainGUI.getString("Price") + " §c§m" + it.getOrigPrice() + "§a " + it.getAdjustedPriceAsString());
                    } else {
                        if (it.getPrice() > 0) {
                            lore.add(MainGUI.getString("Price") + " §7" + it.getPriceAsString());
                        } else {
                            lore.add(MainGUI.getString("Price") + " §7" + MainGUI.getString("Free"));
                        }
                    }
                }
                lore.add(MainGUI.getString("LeftClickToBuy"));
                lore.add(MainGUI.getString("AddToCart"));

                if (shop.getManagers().contains(p)) {
                    lore.add(MainGUI.getString("ShopKeeperManage"));
                }
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                inv.setItem(it.getSlot(), itemStack);
            }
        }

        if (!same)
            p.openInventory(inv);
    }
}
