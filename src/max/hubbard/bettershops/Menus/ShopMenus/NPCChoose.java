package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Configurations.Permissions;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class NPCChoose implements ShopMenu{

    Shop shop;
    Inventory inv;

    public NPCChoose(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());

    }


    @Override
    public MenuType getType() {
        return MenuType.NPC_CHOOSE;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }

    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, shop.getFrameColor());
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 9; i++) {
            inv.setItem(i, glass);
        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(Language.getString("MainGUI","BackArrow"));
        back.setItemMeta(backMeta);
        ClickableItem backClick = new ClickableItem(new ShopItemStack(back), inv, p);
        backClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.SHOP_SETTINGS).draw(p, page, obj);
            }
        });

        inv.setItem(0, back);

        for (EntityType type : Config.getNPCs()) {
            boolean can = true;

            if ((boolean)Config.getObject("Permissions")) {
                can = Permissions.hasNPCTypePerm(type, p);
            }

            if (can) {
                final ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, type.getTypeId());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§e" + type.name().replaceAll("_", " "));
                meta.setLore(Arrays.asList(Language.getString("MainGUI","ChooseNPC")));
                item.setItemMeta(meta);
                ClickableItem npcClick = new ClickableItem(new ShopItemStack(item), inv, p);
                npcClick.addLeftClickAction(new LeftClickAction() {
                    @Override
                    public void onAction(InventoryClickEvent e) {
                        shop.getMenu(MenuType.NPC_CONFIGURE).draw(p, page, EntityType.valueOf(item.getItemMeta().getDisplayName().substring(2).replaceAll(" ","_")),new ArrayList<String>(),false,false,false);
                    }
                });

                inv.setItem(inv.firstEmpty(), item);
            }
        }

       new BukkitRunnable() {

            @Override
            public void run() {
                p.openInventory(inv);
            }

        }.runTask(Bukkit.getPluginManager().getPlugin("BetterShops"));

    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
