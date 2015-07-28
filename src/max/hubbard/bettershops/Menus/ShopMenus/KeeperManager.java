package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.AnvilManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

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
public class KeeperManager implements ShopMenu {

    Shop shop;
    Inventory inv;

    public KeeperManager(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());
    }

    @Override
    public MenuType getType() {
        return MenuType.KEEPER_MANAGER;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }

    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, shop.getFrameColor());
        ItemMeta m = glass.getItemMeta();
        m.setDisplayName(" ");
        glass.setItemMeta(m);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack nam = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta namMeta = nam.getItemMeta();
        namMeta.setDisplayName(Language.getString("ShopKeeperManager", "AddKeeperDisplayName"));
        namMeta.setLore(Arrays.asList(Language.getString("ShopKeeperManager", "AddKeeperLore")));
        nam.setItemMeta(namMeta);
        ClickableItem addClick = new ClickableItem(new ShopItemStack(nam), inv, p);
        addClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String desc = man.call();
                        OfflinePlayer player = Bukkit.getOfflinePlayer(desc);
                        if (player != null && player.hasPlayedBefore()) {
                            shop.addKeeper(player);
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "AddedKeeper"));
                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidKeeper"));
                        }
                        draw(p, page, shop);
                    }
                });
            }
        });

        ItemStack desc = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta descMeta = desc.getItemMeta();
        descMeta.setDisplayName(Language.getString("ShopKeeperManager", "RemoveKeeperDisplayName"));
        descMeta.setLore(Arrays.asList(Language.getString("ShopKeeperManager", "RemoveKeeperLore")));
        desc.setItemMeta(descMeta);
        ClickableItem removeClick = new ClickableItem(new ShopItemStack(desc), inv, p);
        removeClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String desc = man.call();
                        OfflinePlayer player = Bukkit.getOfflinePlayer(desc);
                        if (player != null) {
                            shop.removeKeeper(player);
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "RemovedKeeper"));
                        }
                        draw(p, page, shop);
                    }
                });
            }
        });

        ItemStack info = new ItemStack(Material.ENDER_CHEST);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(Language.getString("ShopKeeperManager", "KeeperInformationDisplayName"));
        infoMeta.setLore(Arrays.asList(Language.getString("ShopKeeperManager", "KeeperManage"), Language.getString("ShopKeeperManager", "KeeperDeposit"), Language.getString("ShopKeeperManager", "KeeperWithdraw")));
        info.setItemMeta(infoMeta);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(Language.getString("MainGUI", "BackArrow"));
        back.setItemMeta(backMeta);
        ClickableItem backClick = new ClickableItem(new ShopItemStack(back), inv, p);
        backClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.OWNER_BUYING).draw(p, page, obj);
            }
        });

        inv.setItem(0, back);

        inv.setItem(5, nam);
        inv.setItem(6, nam);
        inv.setItem(4, info);
        inv.setItem(3, desc);
        inv.setItem(2, desc);

        for (final OfflinePlayer pl : shop.getKeepers()) {
            ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            SkullMeta sk = (SkullMeta) it.getItemMeta();
            sk.setDisplayName("§a" + pl.getName());
            sk.setLore(Arrays.asList(Language.getString("ShopKeeperManager", "ClickRemoveKeeper")));
            sk.setOwner(pl.getName());
            it.setItemMeta(sk);

            if (inv.firstEmpty() > 0)
                inv.setItem(inv.firstEmpty(), it);

            ClickableItem itClick = new ClickableItem(new ShopItemStack(it), inv, p);
            itClick.addLeftClickAction(new LeftClickAction() {
                @Override
                public void onAction(InventoryClickEvent e) {
                    shop.removeKeeper(pl);
                    draw(p, page, shop);
                }
            });
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
