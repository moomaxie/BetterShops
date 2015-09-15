package max.hubbard.bettershops.Shops.Types.Sign;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Configurations.Permissions;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.NPC.*;
import max.hubbard.bettershops.Utils.MaterialSearch;
import max.hubbard.bettershops.Utils.WordsCapitalizer;
import max.hubbard.bettershops.Versions.SignChange;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class CreateSign implements Listener {

    @EventHandler
    public void onCreate(SignChangeEvent e) {
        final Player p = e.getPlayer();
        boolean can = false;
        boolean sell = false;
        boolean admin;
        if (e.getLine(0).equalsIgnoreCase(Language.getString("MainGUI", "SignShopBuy"))) {
            can = true;
        }
        if (e.getLine(0).equalsIgnoreCase(Language.getString("MainGUI", "SignShopSell"))) {
            can = true;
            sell = true;
        }


        if (can) {
            if (((boolean) Config.getObject("Permissions")) && !Permissions.hasCreateSignShopPerm(p)) {
                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoPermission"));
                return;
            }

            Sign s = (Sign) e.getBlock().getState();

            Block face = e.getBlock().getRelative(((org.bukkit.material.Sign) (s.getData())).getAttachedFace());

            admin = !(face.getType() == Material.CHEST || face.getType() == Material.TRAPPED_CHEST) && (!((boolean) Config.getObject("Permissions")) || Permissions.hasCreateAdminSignShopPerm(p));

            Chest chest = null;

            if (!admin) {
                if (face.getState() instanceof Chest) {
                    chest = (Chest) face.getState();
                }
            }

            String n = e.getLine(1);
            String pr = e.getLine(2);
            String am = e.getLine(3);

            double price;
            int amt;

            try {
                price = Double.parseDouble(pr);
            } catch (Exception ex) {
                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidPrice"));
                return;
            }

            try {
                amt = Integer.parseInt(am);
            } catch (Exception ex) {
                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidAmount"));
                return;
            }

            int d = 0;
            if (n.contains(":")) {
                try {
                    d = Integer.parseInt(n.split(":")[1]);
                } catch (Exception ignored) {

                } finally {
                    n = n.split(":")[0];
                }
            }
            List<Material> m = MaterialSearch.closestMaterial(n);

            if (m.size() > 0) {

                List<String> parts = WordsCapitalizer.getParts(p.getUniqueId().toString().replaceAll("-", ""), 15);

                String mat = WordsCapitalizer.capitalizeEveryWord(m.get(0).name().replaceAll("_", " "));
                mat = mat.replaceAll(" ", "");

                for (int i = 0; i < parts.size(); i++) {
                    e.setLine(i, parts.get(i));

                    if (i == 2) {
                        if (m.get(0).name().length() > 12) {
                            e.setLine(2, e.getLine(2) + ":" + mat.substring(0, 12));
                        } else {
                            e.setLine(2, e.getLine(2) + ":" + mat);
                        }
                    }
                }


                if (mat.length() > 12) {
                    parts = WordsCapitalizer.getParts(mat.substring(12), 15);

                    for (int i = 0; i < parts.size(); i++) {
                        e.setLine(i + 3, parts.get(i));
                    }
                } else {
                    e.setLine(3, "");
                }
                if (sell)
                    e.setLine(3, e.getLine(3) + ":S:" + d + ":" + pr + ":" + am);
                else
                    e.setLine(3, e.getLine(3) + ":B:" + d + ":" + pr + ":" + am);


                SignShopManager.addSign(s, new ItemStack(m.get(0), 1, (byte) d), price, amt, p.getUniqueId(), sell, admin, chest);

                Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                    @Override
                    public void run() {
                        SignChange.updateSigns(p);
                    }
                }, 3L);

            } else {
                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "InvalidItem"));
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {

        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {
                SignChange.updateSigns(e.getPlayer());
            }
        }, 20L);
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {
                for (final Shop s : ShopManager.getShops()) {
                    if (s.isHoloShop()) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                            @Override
                            public void run() {
                                s.getHolographicShop().getHologram().refreshAll();
                            }
                        });
                    }
                    if (s.useIcon()) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                            @Override
                            public void run() {
                                s.getShopIcon().getHologram().refreshAll();
                            }
                        });
                    }
                    if (!Core.useCitizens())
                        if (s.isNPCShop()) {
                            if (s.getNPCShop() == null) {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                    @Override
                                    public void run() {

                                        if (s.getObject("NPCInfo") != null) {
                                            if (!Core.useCitizens()) {

                                                ShopsNPC s1 = new NPCShop(EntityInfo.fromString((String) s.getObject("NPCInfo")), s);
                                                NPCManager.addNPCShop(s1);
                                                s1.removeChest();
                                                s.setObject("NPC", true);
                                            } else {

                                                CitizensShop s1 = new CitizensShop(EntityInfo.fromString((String) s.getObject("NPCInfo")), s);
                                                s1.spawn();
                                                NPCManager.addNPCShop(s1);
                                                s1.removeChest();
                                                s.setObject("NPC", true);

                                            }
                                        } else {

                                            s.setObject("NPC", false);
                                            DeleteNPC.addChest(s);
                                        }

                                    }
                                });

                            } else {
                                for (LivingEntity ent : s.getLocation().getWorld().getLivingEntities()) {
                                    if (ent.getCustomName() != null && ent.getCustomName().equals("§a§l" + s.getName())) {
                                        if (!ent.equals(s.getNPCShop().getEntity())) {
                                            ent.remove();
                                        }
                                    }
                                }

                            }
                        }
                }
            }
        });


    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!e.getInventory().getName().contains(Language.getString("MainGUI", "ShopHeader"))) {
            Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                @Override
                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        SignChange.updateSigns(p);
                    }
                }
            });
        }
    }

    @EventHandler
    public void onTp(final PlayerTeleportEvent e) {

        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {
                SignChange.updateSigns(e.getPlayer());
            }
        }, 20L);

        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {
                for (final Shop s : ShopManager.getShops()) {
                    if (s.isHoloShop()) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                            @Override
                            public void run() {
                                s.getHolographicShop().getHologram().refreshAll();
                            }
                        });
                    }
                    if (s.useIcon()) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                            @Override
                            public void run() {
                                s.getShopIcon().getHologram().refreshAll();
                            }
                        });
                    }

                    if (!Core.useCitizens())
                        if (s.isNPCShop())

                        {
                            if (s.getNPCShop() == null) {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                    @Override
                                    public void run() {

                                        if (s.getObject("NPCInfo") != null) {
                                            if (!Core.useCitizens()) {

                                                ShopsNPC s1 = new NPCShop(EntityInfo.fromString((String) s.getObject("NPCInfo")), s);
                                                NPCManager.addNPCShop(s1);
                                                s1.removeChest();
                                                s.setObject("NPC", true);
                                            } else {

                                                CitizensShop s1 = new CitizensShop(EntityInfo.fromString((String) s.getObject("NPCInfo")), s);
                                                s1.spawn();
                                                NPCManager.addNPCShop(s1);
                                                s1.removeChest();
                                                s.setObject("NPC", true);

                                            }
                                        } else {

                                            s.setObject("NPC", false);
                                            DeleteNPC.addChest(s);
                                        }

                                    }
                                });

                            } else {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                                    @Override
                                    public void run() {
                                        for (LivingEntity ent : s.getLocation().getWorld().getLivingEntities()) {
                                            if (ent.getCustomName() != null && ent.getCustomName().equals("§a§l" + s.getName())) {
                                                if (!ent.equals(s.getNPCShop().getEntity())) {
                                                    ent.remove();
                                                }
                                            }
                                        }
                                    }
                                });

                            }
                        }
                }
            }
        });
    }
}
