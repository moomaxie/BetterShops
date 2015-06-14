package max.hubbard.bettershops.Shops.Types.Sign;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Utils.Stocks;
import max.hubbard.bettershops.Versions.SignChange;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Purchase implements Listener {

    @EventHandler
    public void onPurchase(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getState() instanceof Sign && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Sign s = (Sign) e.getClickedBlock().getState();

                if (SignShopManager.isShopSign(s)) {
                    if (SignShopManager.isSell(s)) {
                        if (SignShopManager.isAdmin(s)) {
                            if (Stocks.getNumberInInventory(SignShopManager.getItem(s), p) >= SignShopManager.getAmounts().get(s)) {
                                Stocks.removeItemsFromInventory(SignShopManager.getItem(s), p, SignShopManager.getAmounts().get(s));
                                Core.getEconomy().depositPlayer(p, SignShopManager.getPrices().get(s));
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "SellItem"));
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ReceivedAmount").replaceAll("<Amount>", "" + SignShopManager.getPrices().get(s)));
                                SignChange.updateSigns(p);
                                p.updateInventory();
                            } else {
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NotEnough"));
                                SignChange.updateSigns(p);
                            }
                        } else {
                            Block face = s.getBlock().getRelative(((org.bukkit.material.Sign) (s.getData())).getAttachedFace());
                            Chest chest = (Chest) face.getState();

                            if (Stocks.canAdd(SignShopManager.getItem(s), chest.getInventory(), SignShopManager.getAmounts().get(s))) {
                                if (Stocks.getNumberInInventory(SignShopManager.getItem(s), p) >= SignShopManager.getAmounts().get(s)) {
                                    Stocks.removeItemsFromInventory(SignShopManager.getItem(s), p, SignShopManager.getAmounts().get(s));
                                    Core.getEconomy().depositPlayer(p, SignShopManager.getPrices().get(s));
                                    Stocks.addItemsToInventory(SignShopManager.getItem(s), chest, SignShopManager.getAmounts().get(s));

                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "SellItem"));
                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "ReceivedAmount").replaceAll("<Amount>", "" + SignShopManager.getPrices().get(s)));
                                    SignChange.updateSigns(p);
                                    p.updateInventory();
                                } else {
                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NotEnough"));
                                    SignChange.updateSigns(p);
                                }
                            } else {
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoRoom"));
                                SignChange.updateSigns(p);
                            }
                        }
                    } else {
                        if (SignShopManager.isAdmin(s)) {
                            if (Core.getEconomy().getBalance(p) >= SignShopManager.getPrices().get(s)) {
                                Stocks.addItemsToInventory(SignShopManager.getItem(s), p, SignShopManager.getAmounts().get(s));
                                Core.getEconomy().withdrawPlayer(p, SignShopManager.getPrices().get(s));

                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "BuyItem"));
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "TakenAmount").replaceAll("<Amount>", "" + SignShopManager.getPrices().get(s)));
                                SignChange.updateSigns(p);
                                p.updateInventory();
                            } else {
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoMoney"));
                            }
                        } else {
                            Block face = s.getBlock().getRelative(((org.bukkit.material.Sign) (s.getData())).getAttachedFace());
                            Chest chest = (Chest) face.getState();

                            if (Stocks.getNumberInInventory(SignShopManager.getItem(s), chest) >= SignShopManager.getAmounts().get(s)) {
                                if (Core.getEconomy().getBalance(p) >= SignShopManager.getPrices().get(s)) {
                                    Stocks.addItemsToInventory(SignShopManager.getItem(s), p, SignShopManager.getAmounts().get(s));
                                    Core.getEconomy().withdrawPlayer(p, SignShopManager.getPrices().get(s));
                                    Stocks.removeItemsFromInventory(SignShopManager.getItem(s), chest, SignShopManager.getAmounts().get(s));

                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "BuyItem"));
                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "TakenAmount").replaceAll("<Amount>", "" + SignShopManager.getPrices().get(s)));
                                    SignChange.updateSigns(p);
                                    p.updateInventory();
                                } else {
                                    p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "NoMoney"));
                                }
                            } else {
                                p.sendMessage(Language.getString("Messages", "Prefix") + Language.getString("Messages", "LowStock"));
                                SignChange.updateSigns(p);
                            }
                        }
                    }
                }
            }
        }
    }
}