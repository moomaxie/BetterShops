package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Items.ShopItem;
import max.hubbard.bettershops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Rearrange implements ShopMenu {

    Shop shop;
    Inventory inv;
    public HashMap<UUID, ShopItem> arrange = new HashMap<>();

    public Rearrange(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());
    }


    @Override
    public MenuType getType() {
        return MenuType.REARRANGE;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }

    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        final boolean sell = (boolean) obj[0];

        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, shop.getFrameColor());
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(" ");
        item.setItemMeta(m);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, item);
        }

        ItemStack info = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = info.getItemMeta();
        if (sell) {
            infoMeta.setDisplayName(Language.getString("MainGUI", "ArrangeSelling"));
        } else {
            infoMeta.setDisplayName(Language.getString("MainGUI", "ArrangeBuying"));
        }
        info.setItemMeta(infoMeta);

        ItemStack options = new ItemStack(Material.ENDER_CHEST);
        ItemMeta optionsMeta = options.getItemMeta();

        optionsMeta.setDisplayName(Language.getString("MainGUI", "Arrangement"));

        optionsMeta.setLore(Arrays.asList(Language.getString("MainGUI", "ArrangementLore")));
        options.setItemMeta(optionsMeta);
        ClickableItem opClick = new ClickableItem(new ShopItemStack(options), inv, p);
        opClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                arrange.remove(p.getUniqueId());
                if (sell) {
                    shop.getMenu(MenuType.OWNER_SELLING).draw(p, page);
                } else {
                    shop.getMenu(MenuType.OWNER_BUYING).draw(p, page);
                }
            }
        });

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName(Language.getString("MainGUI", "NextPage"));
        arrow.setItemMeta(arrowMeta);
        ClickableItem arrowClick = new ClickableItem(new ShopItemStack(arrow), inv, p);
        arrowClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page + 1, obj);
            }
        });

        ItemStack barrow = new ItemStack(Material.ARROW);
        ItemMeta barrowMeta = barrow.getItemMeta();
        barrowMeta.setDisplayName(Language.getString("MainGUI", "PreviousPage"));
        barrow.setItemMeta(barrowMeta);
        ClickableItem barrowClick = new ClickableItem(new ShopItemStack(barrow), inv, p);
        barrowClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                draw(p, page - 1, obj);
            }
        });

        ItemStack pg1 = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta pg1Meta = pg1.getItemMeta();
        pg1Meta.setDisplayName(Language.getString("MainGUI", "Page") + " §7" + page);
        pg1.setItemMeta(pg1Meta);


        inv.setItem(3, info);
        inv.setItem(5, options);

        inv.setItem(13, pg1);

        if (page > 1) {
            inv.setItem(0, barrow);
        }

        inv.setItem(8, arrow);

        for (final ShopItem it : shop.getShopItems()) {
            if (it.isSelling() == sell) {
                if (it.getPage() == page) {
                    final ItemStack itemStack = it.getItem().clone();
                    ItemMeta meta = itemStack.getItemMeta();

                    List<String> lore = new ArrayList<>();
                    if (it.getLore() != null) {
                        for (String s : it.getLore()) {
                            lore.add(s);
                        }
                    }

                    if (arrange.containsKey(p.getUniqueId()) && arrange.get(p.getUniqueId()).getItem().isSimilar(itemStack)) {
                        if (!lore.contains(Language.getString("MainGUI", "Selected"))) {
                            lore.add(" ");
                            lore.add(Language.getString("MainGUI", "Selected"));
                        }
                    } else {
                        if (!lore.contains(Language.getString("MainGUI", "Arrange"))) {
                            lore.add(" ");
                            lore.add(Language.getString("MainGUI", "Arrange"));
                        }
                    }
                    meta.setLore(lore);
                    itemStack.setItemMeta(meta);

                    ClickableItem itemClick = new ClickableItem(new ShopItemStack(itemStack), inv, p);
                    itemClick.addLeftClickAction(new LeftClickAction() {
                        @Override
                        public void onAction(InventoryClickEvent e) {
                            if (arrange.containsKey(p.getUniqueId())) {
                                int page = it.getPage();
                                int slot = it.getSlot();
                                int page2 = arrange.get(p.getUniqueId()).getPage();
                                int slot2 = arrange.get(p.getUniqueId()).getSlot();

                                it.setObject("Page", page2);
                                it.setObject("Slot", slot2);

                                arrange.get(p.getUniqueId()).setObject("Page", page);
                                arrange.get(p.getUniqueId()).setObject("Slot", slot);

                                arrange.remove(p.getUniqueId());
                                draw(p, page, obj);
                            } else {

                                arrange.put(p.getUniqueId(), it);
                                draw(p, page, obj);
                            }
                        }
                    });

                    inv.setItem(it.getSlot(), itemStack);
                }
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
