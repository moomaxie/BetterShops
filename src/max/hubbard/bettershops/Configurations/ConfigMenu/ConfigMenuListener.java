package max.hubbard.bettershops.Configurations.ConfigMenu;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Utils.AnvilManager;
import max.hubbard.bettershops.Utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ConfigMenuListener implements Listener {

    @EventHandler
    public void onConfig(final InventoryClickEvent e) {
        if (e.getInventory().getName().equals("§7[BetterShops] §dConfig")) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                e.setCancelled(true);

                Player p = (Player) e.getWhoClicked();

                int page = Integer.parseInt(e.getInventory().getItem(4).getItemMeta().getLore().get(0).substring(Language.getString("MainGUI", "Page").length() + 3));
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) {
                    return;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eChoose NPCs")) {
                    NPCChooser.openNPCConfigChooser((Player) e.getWhoClicked());
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Language.getString("MainGUI", "NextPage"))) {
                    ConfigMenu.openConfigMenu(e.getInventory(), (Player) e.getWhoClicked(), page + 1);
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Language.getString("MainGUI", "PreviousPage"))) {
                    ConfigMenu.openConfigMenu(e.getInventory(), (Player) e.getWhoClicked(), page - 1);
                } else {

                    String s = e.getCurrentItem().getItemMeta().getDisplayName().substring(4);
                    if (Config.getObject(s) instanceof Boolean) {
                        Config.setObject(s, !(boolean) Config.getObject(s));
                        ConfigMenu.openConfigMenu(e.getInventory(), (Player) e.getWhoClicked(), page);
                    } else {
                        if (s.equalsIgnoreCase("CostForShops")) {
                            setCost(p);
                        }
                        if (s.equalsIgnoreCase("Limit")) {
                            setLimit(p);
                        }
                        if (s.equalsIgnoreCase("DefaultPrice")) {
                            setDefaultPrice(p);
                        }
                        if (s.equalsIgnoreCase("MaxPrice")) {
                            setMaxPrice(p);
                        }
                        if (s.equalsIgnoreCase("StockLimit")) {
                            setStockLimit(p);
                        }
                        if (s.equalsIgnoreCase("RemoveAfter")) {
                            setRemoveAfter(p);
                        }
                    }

                    if (s.contains("Metrics")) {
                        if (Core.getMetrics() != null) {
                            try {
                                Core.getMetrics().disable();
                            } catch (IOException e1) {
                                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §4Error: §cCould not disable §eMetrics");
                            }
                        } else {
                            try {
                                Core.metrics = new Metrics(Core.getCore());
                                Core.getCore().setUpMetrics();
                                Core.getMetrics().start();
                            } catch (Exception ie) {
                                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §4Error: §cCould not enable §eMetrics");
                            }
                        }
                    }

                }
            }
        }
    }

    public void setLimit(final Player p) {
        final AnvilManager man = new AnvilManager(p);
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {
                final String name = man.call();

                int amt = 0;
                boolean can = true;
                try {
                    amt = Integer.parseInt(name);
                    can = false;


                } catch (Exception ex) {
                }

                if (!can) {
                    Config.setObject("Limit", amt);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        }
                    }, 1L);
                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        }
                    }, 1L);
                }
            }
        });


    }

    public void setRemoveAfter(final Player p) {
        final AnvilManager man = new AnvilManager(p);
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {
                final String name = man.call();

                int amt = 0;
                boolean can = true;
                try {
                    amt = Integer.parseInt(name);
                    can = false;


                } catch (Exception ex) {
                }

                if (!can) {
                    Config.setObject("RemoveAfter", amt);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        }
                    }, 1L);
                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        }
                    }, 1L);
                }
            }
        });


    }

    public void setStockLimit(final Player p) {
        final AnvilManager man = new AnvilManager(p);
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {
                final String name = man.call();

                int amt = 0;
                boolean can = true;
                try {
                    amt = Integer.parseInt(name);
                    can = false;


                } catch (Exception ex) {
                }

                if (!can) {
                    Config.setObject("StockLimit", amt);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        }
                    }, 1L);
                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        }
                    }, 1L);
                }
            }
        });


    }

    public void setDefaultPrice(final Player p) {
        final AnvilManager man = new AnvilManager(p);
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {
                final String name = man.call();

                double amt = 0;
                boolean can = true;
                try {
                    amt = Double.parseDouble(name);
                    amt = Double.valueOf(new DecimalFormat("#.00").format(amt));
                    can = false;


                } catch (Exception ex) {
                }

                if (!can) {
                    Config.setObject("DefaultPrice", amt);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        }
                    }, 1L);
                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        }
                    }, 1L);
                }
            }
        });

    }

    public void setMaxPrice(final Player p) {
        final AnvilManager man = new AnvilManager(p);
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {
                final String name = man.call();

                double amt = 0;
                boolean can = true;
                try {
                    amt = Double.parseDouble(name);
                    amt = Double.valueOf(new DecimalFormat("#.00").format(amt));
                    can = false;


                } catch (Exception ex) {
                }

                if (!can) {
                    Config.setObject("MaxPrice", amt);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        }
                    }, 1L);
                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        }
                    }, 1L);
                }
            }
        });

    }

    public void setCost(final Player p) {
        final AnvilManager man = new AnvilManager(p);
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {
                final String name = man.call();

                double amt = 0;
                boolean can = true;
                try {
                    amt = Double.parseDouble(name);
                    can = false;

                } catch (Exception ex) {
                }

                if (!can) {
                    Config.setObject("CostForShops", amt);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        }
                    }, 1L);
                } else {
                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidNumber"));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                        public void run() {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        }
                    }, 1L);
                }
            }
        });
    }
}
