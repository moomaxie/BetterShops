package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Configurations.Permissions;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.Holo.CreateHologram;
import max.hubbard.bettershops.Shops.Types.Holo.DeleteHoloShop;
import max.hubbard.bettershops.Shops.Types.NPC.DeleteNPC;
import max.hubbard.bettershops.Utils.AnvilManager;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
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
public class ShopSettings implements ShopMenu {

    Shop shop;
    Inventory inv;

    public ShopSettings(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());
    }


    @Override
    public MenuType getType() {
        return MenuType.SHOP_SETTINGS;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }

    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, shop.getFrameColor());
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);
        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        ItemStack nam = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta namMeta = nam.getItemMeta();
        if (shop.isNotify()) {
            nam = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            namMeta.setDisplayName(Language.getString("ShopSettings", "NotificationsOn"));
        } else {
            namMeta.setDisplayName(Language.getString("ShopSettings", "NotificationsOff"));
        }
        namMeta.setLore(Arrays.asList(Language.getString("ShopSettings", "NotificationsLore")));
        nam.setItemMeta(namMeta);
        ClickableItem notifyClick = new ClickableItem(new ShopItemStack(nam), inv, p);
        notifyClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.setObject("Notify", !shop.isNotify());
                draw(p, page, obj);
            }
        });

        ItemStack desc = new ItemStack(Material.NAME_TAG);
        ItemMeta descMeta = desc.getItemMeta();
        descMeta.setDisplayName(Language.getString("ShopSettings", "ChangeDescription"));
        descMeta.setLore(Arrays.asList(Language.getString("ShopSettings", "ChangeDescriptionLore")));
        desc.setItemMeta(descMeta);
        ClickableItem descClick = new ClickableItem(new ShopItemStack(desc), inv, p);
        descClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                final AnvilManager man = new AnvilManager(p);

                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String name = man.call();
                        shop.setObject("Description", name);
                    }
                });

            }
        });

        ItemStack owner = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta ownerMeta = (SkullMeta) owner.getItemMeta();
        ownerMeta.setDisplayName(Language.getString("ShopSettings", "ChangeOwner"));
        ownerMeta.setLore(Arrays.asList(Language.getString("ShopSettings", "ChangeOwnerLore")));
        ownerMeta.setOwner(shop.getOwner().getName());
        owner.setItemMeta(ownerMeta);
        ClickableItem ownerClick = new ClickableItem(new ShopItemStack(owner), inv, p);
        ownerClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String na = man.call();
                        OfflinePlayer pl = Bukkit.getOfflinePlayer(na);
                        if (pl.hasPlayedBefore()) {
                            if (shop.setOwner(pl)) {
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ChangeOwner"));
                            } else {
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "AtLimit"));
                            }
                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidKeeper"));
                        }
                    }
                });
            }
        });

        ItemStack blacklist = new ItemStack(Material.IRON_FENCE);
        ItemMeta blacklistMeta = blacklist.getItemMeta();
        blacklistMeta.setDisplayName(Language.getString("ShopSettings", "Blacklist"));
        blacklistMeta.setLore(Arrays.asList(Language.getString("ShopSettings", "BlacklistLore")));
        blacklist.setItemMeta(blacklistMeta);
        ClickableItem blacklistClick = new ClickableItem(new ShopItemStack(blacklist), inv, p);
        blacklistClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.PLAYER_BLACKLIST).draw(p, page);
            }
        });

        ItemStack name = new ItemStack(Material.NAME_TAG);
        ItemMeta nameMeta = name.getItemMeta();
        nameMeta.setDisplayName(Language.getString("ShopSettings", "ChangeName"));
        nameMeta.setLore(Arrays.asList(Language.getString("ShopSettings", "ChangeNameLore")));
        name.setItemMeta(nameMeta);
        ClickableItem nameClick = new ClickableItem(new ShopItemStack(name), inv, p);
        nameClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                final AnvilManager man = new AnvilManager(p);
                Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        String na = man.call();
                        if (shop.setName(na)) {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ChangeName"));
                        } else {
                            p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NameTaken"));
                        }
                    }
                });
            }
        });

        ItemStack ownuse = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta ownuseMeta = ownuse.getItemMeta();
        if (shop.isServerShop()) {
            ownuse = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            ownuseMeta.setDisplayName(Language.getString("ShopSettings", "ServerShopOn"));
        } else {
            ownuseMeta.setDisplayName(Language.getString("ShopSettings", "ServerShopOff"));
        }
        ownuseMeta.setLore(Arrays.asList(Language.getString("ShopSettings", "ServerShopLore")));
        ownuse.setItemMeta(ownuseMeta);
        ClickableItem ownClick = new ClickableItem(new ShopItemStack(ownuse), inv, p);
        ownClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.setObject("Server", !shop.isServerShop());
                draw(p, page, obj);
            }
        });

        ItemStack npc = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta npcMeta = npc.getItemMeta();
        if (shop.isNPCShop() || shop.getNPCShop() != null) {
            npc = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            npcMeta.setDisplayName(Language.getString("ShopSettings", "NPCShopOn"));
        } else {
            npcMeta.setDisplayName(Language.getString("ShopSettings", "NPCShopOff"));
        }
        npcMeta.setLore(Arrays.asList(Language.getString("ShopSettings", "Warning"),
                Language.getString("ShopSettings", "DeletedChest"),
                " ",
                Language.getString("ShopSettings", "NPCLore")));
        npc.setItemMeta(npcMeta);
        ClickableItem npcClick = new ClickableItem(new ShopItemStack(npc), inv, p);
        npcClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {

                if (shop.isHoloShop()) {
                    DeleteHoloShop.deleteHologramShop(shop.getHolographicShop());

                }
                if (shop.isNPCShop()) {
                    DeleteNPC.deleteNPC(shop.getNPCShop());
                    shop.setObject("NPC",false);
                    draw(p,page);
                } else {
                    shop.getMenu(MenuType.NPC_CHOOSE).draw(p, page, obj);
                }

            }
        });

        ItemStack holo = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta holoMeta = holo.getItemMeta();
        if (shop.isHoloShop()) {
            holo = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            holoMeta.setDisplayName(Language.getString("ShopSettings", "HolographicOn"));
        } else {
            holoMeta.setDisplayName(Language.getString("ShopSettings", "HolographicOff"));
        }
        holoMeta.setLore(Arrays.asList(Language.getString("ShopSettings", "Warning"),
                Language.getString("ShopSettings", "DeletedChest"),
                " ",
                Language.getString("ShopSettings", "HolographicLore")));
        holo.setItemMeta(holoMeta);
        ClickableItem holoClick = new ClickableItem(new ShopItemStack(holo), inv, p);
        holoClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {

                if (shop.isNPCShop()) {
                    DeleteNPC.deleteNPC(shop.getNPCShop());
                }

                if (shop.isHoloShop()) {
                    DeleteHoloShop.deleteHologramShop(shop.getHolographicShop());
                } else {

                    if (shop.getShopItems().size() > 0) {
                        CreateHologram.createHolographicShop(shop);
                        p.closeInventory();
                    } else {
                        p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "TooFew"));
                    }
                }
            }
        });

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(Language.getString("MainGUI", "BackArrow"));
        back.setItemMeta(backMeta);
        ClickableItem backClick = new ClickableItem(new ShopItemStack(back), inv, p);
        backClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                if (shop.isServerShop()) {
                    shop.getMenu(MenuType.MAIN_BUYING).draw(p, page);
                } else {
                    shop.getMenu(MenuType.OWNER_BUYING).draw(p, page);
                }
            }
        });

        for (int i = 0; i < DyeColor.values().length; i++) {
            final DyeColor c = DyeColor.getByData((byte) i);

            ItemStack ite = new ItemStack(Material.STAINED_GLASS_PANE, 1, c.getDyeData());
            ItemMeta m1 = ite.getItemMeta();
            m1.setDisplayName(Language.getString("ShopSettings", "Theme"));
            ite.setItemMeta(m1);
            ClickableItem themeClick = new ClickableItem(new ShopItemStack(ite), inv, p);
            themeClick.addLeftClickAction(new LeftClickAction() {
                @Override
                public void onAction(InventoryClickEvent e) {
                    shop.setObject("Frame", c.getDyeData());
                    draw(p, page, obj);
                }
            });

            inv.setItem(36 + i, ite);

        }

        inv.setItem(inv.firstEmpty(), nam);
        inv.setItem(3, desc);
        inv.setItem(4, owner);
        inv.setItem(5, name);


        if (Permissions.hasPlayerBlacklistPerm(p))
            inv.setItem(8, blacklist);
        if (Permissions.hasUsePerm(p)) {
            inv.setItem(inv.firstEmpty(), ownuse);
        }
        if ((boolean) Config.getObject("EnableNPC") && Permissions.hasNPCPerm(p)) {
            inv.setItem(inv.firstEmpty(), npc);
        }
        if (Core.useHolograms() && (boolean) Config.getObject("HoloShops") && Permissions.hasHoloPerm(p)) {
            inv.setItem(inv.firstEmpty(), holo);
        }

        inv.setItem(0, back);

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
