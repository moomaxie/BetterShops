package max.hubbard.bettershops.Shops.Items.Actions;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
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
public interface RightClickAction {

    public static List<RightClickAction> actions = new ArrayList<RightClickAction>();

    public void onAction(InventoryClickEvent e);
}
