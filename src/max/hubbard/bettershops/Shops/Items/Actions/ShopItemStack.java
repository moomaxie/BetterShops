package max.hubbard.bettershops.Shops.Items.Actions;

import org.bukkit.inventory.ItemStack;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class ShopItemStack {

    ItemStack item;

    public ShopItemStack(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public boolean compare(ItemStack it) {
        return item.isSimilar(it) || item.toString().equals(it.toString()) && item.getData().getData() == it.getData().getData() && item.getDurability() == it.getDurability();
    }
}
