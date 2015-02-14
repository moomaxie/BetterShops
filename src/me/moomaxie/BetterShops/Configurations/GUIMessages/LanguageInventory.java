package me.moomaxie.BetterShops.Configurations.GUIMessages;

import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class LanguageInventory implements Listener {

    private HashMap<HashMap<UUID, String>, String> chat = new HashMap<>();

    public static void openLanguageInventory(String file, Player p, int page) {

        Inventory inv = Bukkit.createInventory(p, 54, "§7[BetterShops] Language");

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 9; i++) {
            inv.setItem(i, glass);
        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(MainGUI.getString("BackArrow"));
        back.setItemMeta(backMeta);

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName(MainGUI.getString("NextPage"));
        arrow.setItemMeta(arrowMeta);

        ItemStack barrow = new ItemStack(Material.ARROW);
        ItemMeta barrowMeta = barrow.getItemMeta();
        barrowMeta.setDisplayName(MainGUI.getString("PreviousPage"));
        barrow.setItemMeta(barrowMeta);

        ItemStack it = new ItemStack(Material.CHEST, 1, (byte) 7);
        ItemMeta itMeta = it.getItemMeta();
        itMeta.setDisplayName("§e" + file);
        itMeta.setLore(Arrays.asList("§e§lClick §7an option to §eChange §7it", "§ePage: §7" + page));
        it.setItemMeta(itMeta);

        inv.setItem(4, it);

        inv.setItem(0, back);

        if (page > 1) {
            inv.setItem(1, barrow);
        }

        YamlConfiguration config = null;

        if (file.equals("BuyingAndSelling")) {
            config = BuyingAndSelling.config;
        }
        if (file.equals("Checkout")) {
            config = Checkout.config;
        }
        if (file.equals("ItemTexts")) {
            config = ItemTexts.config;
        }
        if (file.equals("MainGUI")) {
            config = MainGUI.config;
        }
        if (file.equals("SearchEngine")) {
            config = SearchEngine.config;
        }
        if (file.equals("ShopKeeperManager")) {
            config = ShopKeeperManager.config;
        }
        if (file.equals("ShopSettings")) {
            config = ShopSettings.config;
        }
        if (file.equals("History")) {
            config = History.config;
        }
        if (file.equals("Messages")) {
            config = Messages.config;
        }

        if (config != null) {
            int maxPage = (int) Math.ceil((double) (config.getKeys(false).size() - 1) / 45);

            if (page != maxPage) {
                inv.setItem(8, arrow);
            }

            int j = 0;

            if (page > 1) {
                j = 45 * (page - 1);
            }

            Object[] array = config.getKeys(false).toArray();

            int k = array.length;

            if (page != maxPage) {
                k = k - (j + (array.length - 46));
            }


            for (int i = j; i < k; i++) {
                String s = (String) array[i];

                if (!s.equals("Version")) {
                    ItemStack item = new ItemStack(Material.WOOL, 1, (byte) 5);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("§e" + s);
                    meta.setLore(Arrays.asList("§8'§7" + config.getString(s).replaceAll("&", "§") + "§8'"));
                    item.setItemMeta(meta);

                    inv.setItem(inv.firstEmpty(), item);
                }
            }
        }
        p.openInventory(inv);
    }


    @EventHandler
    public void onChange(InventoryClickEvent e) {
        if (e.getInventory().getName().equals("§7[BetterShops] Language")) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                e.setCancelled(true);
                if (e.getCurrentItem().getType() != Material.STAINED_GLASS_PANE && e.getCurrentItem().getType() != Material.CHEST && e.getCurrentItem().getType() != Material.ARROW && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    String file = e.getInventory().getItem(4).getItemMeta().getDisplayName();

                    String title = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                    HashMap<UUID, String> m = new HashMap<>();
                    m.put(e.getWhoClicked().getUniqueId(), file.substring(2));
                    chat.put(m, title);
                    ((Player) e.getWhoClicked()).sendMessage(Messages.getPrefix() + Messages.getChatMessage());
                    e.getWhoClicked().closeInventory();
                } else if (e.getCurrentItem().getType() == Material.ARROW) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("NextPage"))) {
                            String file = e.getInventory().getItem(4).getItemMeta().getDisplayName();
                            List<String> lore = e.getInventory().getItem(4).getItemMeta().getLore();
                            int page = Integer.parseInt(lore.get(1).substring(10));

                            openLanguageInventory(file.substring(2), (Player) e.getWhoClicked(), page + 1);
                        }
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("BackArrow"))) {
                            GUIMessagesInv.openGUIMessagesInv((Player) e.getWhoClicked());
                        }
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(MainGUI.getString("PreviousPage"))) {
                            String file = e.getInventory().getItem(4).getItemMeta().getDisplayName();
                            List<String> lore = e.getInventory().getItem(4).getItemMeta().getLore();
                            int page = Integer.parseInt(lore.get(1).substring(10));

                            openLanguageInventory(file.substring(2), (Player) e.getWhoClicked(), page - 1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChatChange(AsyncPlayerChatEvent e) {
        for (HashMap<UUID, String> map : chat.keySet()) {
            if (map.containsKey(e.getPlayer().getUniqueId())) {

                if (!e.getMessage().equalsIgnoreCase("cancel")) {
                    String file = map.get(e.getPlayer().getUniqueId());

                    String title = chat.get(map);

                    if (file.equals("BuyingAndSelling")) {
                        BuyingAndSelling.setString(title, e.getMessage());
                    }
                    if (file.equals("Checkout")) {
                        Checkout.setString(title, e.getMessage());
                    }
                    if (file.equals("ItemTexts")) {
                        ItemTexts.setString(title, e.getMessage());
                    }
                    if (file.equals("MainGUI")) {
                        MainGUI.setString(title, e.getMessage());
                    }
                    if (file.equals("SearchEngine")) {
                        SearchEngine.setString(title, e.getMessage());
                    }
                    if (file.equals("ShopKeeperManager")) {
                        ShopKeeperManager.setString(title, e.getMessage());
                    }
                    if (file.equals("ShopSettings")) {
                        ShopSettings.setString(title, e.getMessage());
                    }
                    if (file.equals("History")) {
                        History.setString(title, e.getMessage());
                    }
                    if (file.equals("Messages")) {
                        Messages.setString(title, e.getMessage());
                    }

                    if (title.equals("SignLine1")) {
                        for (Shop shop : ShopLimits.getAllShops()) {
                            Block b = shop.getLocation().getBlock();

                            if (b.getRelative(1, 0, 0).getState() instanceof Sign) {
                                Sign s = (Sign) b.getRelative(1, 0, 0).getState();
                                s.setLine(0, e.getMessage().replaceAll("&", "§"));
                                s.update();
                            }
                            if (b.getRelative(-1, 0, 0).getState() instanceof Sign) {
                                Sign s = (Sign) b.getRelative(-1, 0, 0).getState();
                                s.setLine(0, e.getMessage().replaceAll("&","§"));
                                s.update();
                            }
                            if (b.getRelative(0, 0, 1).getState() instanceof Sign) {
                                Sign s = (Sign) b.getRelative(0, 0, 1).getState();
                                s.setLine(0, e.getMessage().replaceAll("&","§"));
                                s.update();
                            }
                            if (b.getRelative(0, 0, -1).getState() instanceof Sign) {
                                Sign s = (Sign) b.getRelative(0, 0, -1).getState();
                                s.setLine(0, e.getMessage().replaceAll("&","§"));
                                s.update();
                            }
                        }
                    }

                    if (title.equals("SignLine2")) {
                        for (Shop shop : ShopLimits.getAllShops()) {
                            Block b = shop.getLocation().getBlock();

                            if (b.getRelative(1, 0, 0).getState() instanceof Sign) {
                                Sign s = (Sign) b.getRelative(1, 0, 0).getState();
                                s.setLine(1, e.getMessage().replaceAll("&", "§"));
                                s.update();
                            }
                            if (b.getRelative(-1, 0, 0).getState() instanceof Sign) {
                                Sign s = (Sign) b.getRelative(-1, 0, 0).getState();
                                s.setLine(1, e.getMessage().replaceAll("&", "§"));
                                s.update();
                            }
                            if (b.getRelative(0, 0, 1).getState() instanceof Sign) {
                                Sign s = (Sign) b.getRelative(0, 0, 1).getState();
                                s.setLine(1, e.getMessage().replaceAll("&", "§"));
                                s.update();
                            }
                            if (b.getRelative(0, 0, -1).getState() instanceof Sign) {
                                Sign s = (Sign) b.getRelative(0, 0, -1).getState();
                                s.setLine(1, e.getMessage().replaceAll("&", "§"));
                                s.update();
                            }
                        }
                    }

                    if (title.equals("SignLine4")) {
                        for (Shop shop : ShopLimits.getAllShops()) {
                            Block b = shop.getLocation().getBlock();

                            if (b.getRelative(1, 0, 0).getState() instanceof Sign) {
                                Sign s = (Sign) b.getRelative(1, 0, 0).getState();
                                s.setLine(3, e.getMessage().replaceAll("&", "§"));
                                s.update();
                            }
                            if (b.getRelative(-1, 0, 0).getState() instanceof Sign) {
                                Sign s = (Sign) b.getRelative(-1, 0, 0).getState();
                                s.setLine(3, e.getMessage().replaceAll("&","§"));
                                s.update();

                            }
                            if (b.getRelative(0, 0, 1).getState() instanceof Sign) {
                                Sign s = (Sign) b.getRelative(0, 0, 1).getState();
                                s.setLine(3, e.getMessage().replaceAll("&", "§"));
                                s.update();
                            }
                            if (b.getRelative(0, 0, -1).getState() instanceof Sign) {
                                Sign s = (Sign) b.getRelative(0, 0, -1).getState();
                                s.setLine(3, e.getMessage().replaceAll("&","§"));
                                s.update();
                            }
                        }
                    }

                    chat.remove(map);

                    LanguageInventory.openLanguageInventory(file, e.getPlayer(), 1);
                    e.setCancelled(true);
                    break;
                } else {
                    chat.remove(map);
                    String file = map.get(e.getPlayer().getUniqueId());
                    LanguageInventory.openLanguageInventory(file, e.getPlayer(), 1);
                    e.setCancelled(true);
                    break;
                }

            }
        }
    }
}
