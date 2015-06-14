package max.hubbard.bettershops.Menus;

import max.hubbard.bettershops.Shops.Shop;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public interface ShopMenu {

    public MenuType getType();

    public Shop getAttachedShop();

    public void draw(final Player p,final int page, final Object... obj);

    public Inventory getInventory();

}
