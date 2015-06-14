package max.hubbard.bettershops.Shops.Items.Actions;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
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
public class ClickableItem {

    public static HashMap<OfflinePlayer,List<ClickableItem>> items = new HashMap<OfflinePlayer, List<ClickableItem>>();

    private List<LeftClickAction> lcActions = new ArrayList<LeftClickAction>();
    private List<RightClickAction> rcActions = new ArrayList<RightClickAction>();
    private List<ShiftClickAction> sActions = new ArrayList<ShiftClickAction>();

    private ShopItemStack item;
    private Inventory inv;
    private OfflinePlayer p;

    public static HashMap<OfflinePlayer,List<ClickableItem>> getItems(){
        return items;
    }

    public static void clearPlayer(OfflinePlayer p){
        items.remove(p);
    }

    public void addLeftClickAction(LeftClickAction action){
        LeftClickAction.actions.add(action);
        lcActions.add(action);
    }

    public void removeLeftClickAction(LeftClickAction action){
        LeftClickAction.actions.remove(action);
        lcActions.remove(action);
    }

    public List<LeftClickAction> getLeftClickActions(){
        return lcActions;
    }

    public void addRightClickAction(RightClickAction action){
        RightClickAction.actions.add(action);
        rcActions.add(action);
    }

    public void removeRightClickAction(RightClickAction action){
        RightClickAction.actions.remove(action);
        rcActions.remove(action);
    }

    public List<RightClickAction> getRightClickActions(){
        return rcActions;
    }

    public void addShiftClickAction(ShiftClickAction action){
        ShiftClickAction.actions.add(action);
        sActions.add(action);
    }

    public void removeShiftClickAction(ShiftClickAction action){
        ShiftClickAction.actions.remove(action);
        sActions.remove(action);
    }

    public List<ShiftClickAction> getShiftClickActions(){
        return sActions;
    }

    public ClickableItem(ShopItemStack item, Inventory inv, OfflinePlayer p){
        this.item = item;
        this.inv = inv;
        this.p = p;

        List<ClickableItem> c = items.get(p);

        if (c == null){
            c = new ArrayList<ClickableItem>();
        }

        c.add(this);

        items.put(p,c);
    }

    public ShopItemStack getItem(){
        return item;
    }

    public Inventory getInventory(){
        return inv;
    }

    public OfflinePlayer getClicker(){
        return p;
    }

}
