package me.moomaxie.BetterShops.ShopTypes.NPC;

import me.moomaxie.BetterShops.Configurations.GUIMessages.Checkout;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.ShopSettings;
import me.moomaxie.BetterShops.Shops.Shop;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
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
public class TypeSpecifier7 implements Listener {

    public static void openVillagerSpecifier(Player p, Shop shop, EntityType type, boolean bb, Villager.Profession prof) {
        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        //Glass
        org.bukkit.inventory.ItemStack glass = new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack ty = new ItemStack(Material.MONSTER_EGG, 1, (byte) type.getTypeId());
        ItemMeta tyMeta = ty.getItemMeta();
        tyMeta.setDisplayName("§a" + type.name());
        if (prof != null) {
            tyMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Profession") + "§7 " + prof.name()));
        }
        ty.setItemMeta(tyMeta);

        ItemStack confirm = new ItemStack(Material.CHEST);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Checkout.getString("Confirm"));
        confirm.setItemMeta(confirmMeta);

        ItemStack baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta babyMeta = baby.getItemMeta();
        if (!bb) {
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOff"));
        } else {
            baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOn"));
        }
        babyMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Toggle")));
        baby.setItemMeta(babyMeta);

        //Villager Stuffs
        if (type == EntityType.VILLAGER) {
            for (Villager.Profession pro : Villager.Profession.values()) {
                ItemStack item = new ItemStack(Material.EMERALD);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Profession") + " §e" + pro.name());
                meta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Choose")));
                item.setItemMeta(meta);

                inv.setItem(inv.firstEmpty(), item);
            }
        }

        inv.setItem(4, ty);
        inv.setItem(45, baby);
        inv.setItem(53, confirm);

        p.openInventory(inv);
    }

    //For:
    //Chicken
    //Cows
    //Mooshrooms
    //Pig Zombies
    //Pig

    public static void openBabySpecifier(Player p, Shop shop, EntityType type, boolean bb) {
        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        //Glass
        org.bukkit.inventory.ItemStack glass = new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack ty = new ItemStack(Material.MONSTER_EGG, 1, (byte) type.getTypeId());
        ItemMeta tyMeta = ty.getItemMeta();
        tyMeta.setDisplayName("§a" + type.name());
        ty.setItemMeta(tyMeta);

        ItemStack confirm = new ItemStack(Material.CHEST);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Checkout.getString("Confirm"));
        confirm.setItemMeta(confirmMeta);

        ItemStack baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta babyMeta = baby.getItemMeta();
        if (!bb) {
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOff"));
        } else {
            baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOn"));
        }
        babyMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Toggle")));
        baby.setItemMeta(babyMeta);

        inv.setItem(4, ty);
        inv.setItem(45, baby);
        inv.setItem(53, confirm);

        p.openInventory(inv);
    }

    public static void openHorseSpecifier(Player p, Shop shop, EntityType type, boolean bb, Horse.Variant var, Horse.Style style, Horse.Color color) {
        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        //Glass
        org.bukkit.inventory.ItemStack glass = new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack ty = new ItemStack(Material.MONSTER_EGG, 1, (byte) type.getTypeId());
        ItemMeta tyMeta = ty.getItemMeta();
        tyMeta.setDisplayName("§a" + type.name());
        List<String> lore = new ArrayList<>();
        if (var != null) {
            lore.add(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Variant") + " §7" + var.name());
        }
        if (style != null) {
            lore.add(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Style") + " §7" + style.name());
        }
        if (color != null) {
            lore.add(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Color") + " §7" + color.name());
        }
        tyMeta.setLore(lore);
        ty.setItemMeta(tyMeta);

        ItemStack confirm = new ItemStack(Material.CHEST);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Checkout.getString("Confirm"));
        confirm.setItemMeta(confirmMeta);

        ItemStack baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta babyMeta = baby.getItemMeta();
        if (!bb) {
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOff"));
        } else {
            baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOn"));
        }
        babyMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Toggle")));
        baby.setItemMeta(babyMeta);

        //Horse Stuffs
        if (type == EntityType.HORSE) {
            for (Horse.Variant variant : Horse.Variant.values()) {
                ItemStack item = new ItemStack(Material.WOOL, 1, (byte) 1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Variant") + " §e" + variant.name());
                meta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Choose")));
                item.setItemMeta(meta);

                inv.setItem(inv.firstEmpty(), item);
            }

            for (Horse.Style variant : Horse.Style.values()) {
                ItemStack item = new ItemStack(Material.WOOL, 1, (byte) 2);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Style") + " §e" + variant.name());
                meta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Choose")));
                item.setItemMeta(meta);

                inv.setItem(inv.firstEmpty(), item);
            }

            for (Horse.Color variant : Horse.Color.values()) {
                ItemStack item = new ItemStack(Material.WOOL, 1, (byte) 3);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Color") + " §e" + variant.name());
                meta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Choose")));
                item.setItemMeta(meta);

                inv.setItem(inv.firstEmpty(), item);
            }
        }

        inv.setItem(4, ty);
        inv.setItem(45, baby);
        inv.setItem(53, confirm);

        p.openInventory(inv);
    }

    public static void openCatSpecifier(Player p, Shop shop, EntityType type, boolean bb, Ocelot.Type typ) {
        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        //Glass
        org.bukkit.inventory.ItemStack glass = new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack ty = new ItemStack(Material.MONSTER_EGG, 1, (byte) type.getTypeId());
        ItemMeta tyMeta = ty.getItemMeta();
        tyMeta.setDisplayName("§a" + type.name());
        if (typ != null) {
            tyMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("OcelotType") + "§7 " + typ.name()));
        }
        ty.setItemMeta(tyMeta);

        ItemStack confirm = new ItemStack(Material.CHEST);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Checkout.getString("Confirm"));
        confirm.setItemMeta(confirmMeta);

        ItemStack baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta babyMeta = baby.getItemMeta();
        if (!bb) {
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOff"));
        } else {
            baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOn"));
        }
        babyMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Toggle")));
        baby.setItemMeta(babyMeta);

        //Cat Stuffs
        if (type == EntityType.OCELOT) {
            for (Ocelot.Type pro : Ocelot.Type.values()) {
                ItemStack item = new ItemStack(Material.EMERALD);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("OcelotType") + " §e" + pro.name());
                meta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Choose")));
                item.setItemMeta(meta);

                inv.setItem(inv.firstEmpty(), item);
            }
        }

        inv.setItem(4, ty);
        inv.setItem(45, baby);
        inv.setItem(53, confirm);

        p.openInventory(inv);
    }

    public static void openSkeletonSpecifier(Player p, Shop shop, EntityType type, Skeleton.SkeletonType typ) {
        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        //Glass
        org.bukkit.inventory.ItemStack glass = new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack ty = new ItemStack(Material.MONSTER_EGG, 1, (byte) type.getTypeId());
        ItemMeta tyMeta = ty.getItemMeta();
        tyMeta.setDisplayName("§a" + type.name());
        if (typ != null) {
            tyMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("SkeletonType") + "§7 " + typ.name()));
        }
        ty.setItemMeta(tyMeta);

        ItemStack confirm = new ItemStack(Material.CHEST);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Checkout.getString("Confirm"));
        confirm.setItemMeta(confirmMeta);

        //Skeleton Stuffs
        if (type == EntityType.SKELETON) {
            for (Skeleton.SkeletonType pro : Skeleton.SkeletonType.values()) {
                ItemStack item = new ItemStack(Material.EMERALD);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("SkeletonType") + " §e" + pro.name());
                meta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Choose")));
                item.setItemMeta(meta);

                inv.setItem(inv.firstEmpty(), item);
            }
        }

        inv.setItem(4, ty);
        inv.setItem(53, confirm);

        p.openInventory(inv);
    }

    public static void openSheepSpecifier(Player p, Shop shop, EntityType type, boolean bb, boolean sheared, DyeColor color) {
        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        //Glass
        org.bukkit.inventory.ItemStack glass = new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack ty = new ItemStack(Material.MONSTER_EGG, 1, (byte) type.getTypeId());
        ItemMeta tyMeta = ty.getItemMeta();
        tyMeta.setDisplayName("§a" + type.name());
        List<String> lore = new ArrayList<>();
        if (color != null) {
            lore.add(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("DyeColor") + " §7" + color.name());
        }
        tyMeta.setLore(lore);
        ty.setItemMeta(tyMeta);

        ItemStack confirm = new ItemStack(Material.CHEST);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Checkout.getString("Confirm"));
        confirm.setItemMeta(confirmMeta);

        ItemStack baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta babyMeta = baby.getItemMeta();
        if (!bb) {
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOff"));
        } else {
            baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOn"));
        }
        babyMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Toggle")));
        baby.setItemMeta(babyMeta);

        ItemStack shear = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta shearMeta = shear.getItemMeta();
        if (!sheared) {
            shearMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("ShearedOff"));
        } else {
            shear = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            shearMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("ShearedOn"));
        }
        shearMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Toggle")));
        shear.setItemMeta(shearMeta);

        //Sheepish Stuffs
        if (type == EntityType.SHEEP) {
            for (DyeColor variant : DyeColor.values()) {
                ItemStack item = new ItemStack(Material.WOOL, 1, variant.getWoolData());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("DyeColor") + " §e" + variant.name());
                meta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Choose")));
                item.setItemMeta(meta);

                inv.setItem(inv.firstEmpty(), item);
            }
        }

        inv.setItem(4, ty);
        inv.setItem(45, baby);
        inv.setItem(46, shear);
        inv.setItem(53, confirm);

        p.openInventory(inv);
    }

    public static void openDogSpecifier(Player p, Shop shop, EntityType type, boolean bb, DyeColor color) {
        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        //Glass
        org.bukkit.inventory.ItemStack glass = new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack ty = new ItemStack(Material.MONSTER_EGG, 1, (byte) type.getTypeId());
        ItemMeta tyMeta = ty.getItemMeta();
        tyMeta.setDisplayName("§a" + type.name());
        List<String> lore = new ArrayList<>();
        if (color != null) {
            lore.add(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("DyeColor") + " §7" + color.name());
        }
        tyMeta.setLore(lore);
        ty.setItemMeta(tyMeta);

        ItemStack confirm = new ItemStack(Material.CHEST);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Checkout.getString("Confirm"));
        confirm.setItemMeta(confirmMeta);

        ItemStack baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta babyMeta = baby.getItemMeta();
        if (!bb) {
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOff"));
        } else {
            baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOn"));
        }
        babyMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Toggle")));
        baby.setItemMeta(babyMeta);

        //Sheepish Stuffs
        if (type == EntityType.WOLF) {
            for (DyeColor variant : DyeColor.values()) {
                ItemStack item = new ItemStack(Material.WOOL, 1, variant.getWoolData());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("DyeColor") + " §e" + variant.name());
                meta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Choose")));
                item.setItemMeta(meta);

                inv.setItem(inv.firstEmpty(), item);
            }
        }

        inv.setItem(4, ty);
        inv.setItem(45, baby);
        inv.setItem(53, confirm);

        p.openInventory(inv);
    }

    public static void openSlimeSpecifier(Player p, Shop shop, EntityType type, int size) {
        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        //Glass
        org.bukkit.inventory.ItemStack glass = new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack ty = new ItemStack(Material.MONSTER_EGG, 1, (byte) type.getTypeId());
        ItemMeta tyMeta = ty.getItemMeta();
        tyMeta.setDisplayName("§a" + type.name());
        List<String> lore = new ArrayList<>();
        lore.add(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Size") + " §7" + size);

        tyMeta.setLore(lore);
        ty.setItemMeta(tyMeta);

        ItemStack confirm = new ItemStack(Material.CHEST);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Checkout.getString("Confirm"));
        confirm.setItemMeta(confirmMeta);

        //Slime Stuffs
        if (type == EntityType.SLIME || type == EntityType.MAGMA_CUBE) {
            for (int i = 1; i < 28; i++) {
                ItemStack item = new ItemStack(Material.WOOL, 1, (byte) 5);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Size") + " §e" + i);
                meta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Choose")));
                item.setItemMeta(meta);

                inv.setItem(inv.firstEmpty(), item);
            }

            ItemStack item = new ItemStack(Material.WOOL, 1, (byte) 5);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Size") + " §e" + 100);
            meta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Choose")));
            item.setItemMeta(meta);

            inv.setItem(inv.firstEmpty(), item);
        }

        inv.setItem(4, ty);
        inv.setItem(53, confirm);

        p.openInventory(inv);
    }

    public static void openZombieSpecifier(Player p, Shop shop, EntityType type, boolean bb, boolean village) {
        Inventory inv = Bukkit.createInventory(p, 54, MainGUI.getString("ShopHeader") + shop.getName());

        //Glass
        org.bukkit.inventory.ItemStack glass = new org.bukkit.inventory.ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        ItemStack ty = new ItemStack(Material.MONSTER_EGG, 1, (byte) type.getTypeId());
        ItemMeta tyMeta = ty.getItemMeta();
        tyMeta.setDisplayName("§a" + type.name());
        List<String> lore = new ArrayList<>();
        tyMeta.setLore(lore);
        ty.setItemMeta(tyMeta);

        ItemStack confirm = new ItemStack(Material.CHEST);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(Checkout.getString("Confirm"));
        confirm.setItemMeta(confirmMeta);

        ItemStack baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta babyMeta = baby.getItemMeta();
        if (!bb) {
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOff"));
        } else {
            baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            babyMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOn"));
        }
        babyMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Toggle")));
        baby.setItemMeta(babyMeta);

        //Zombie Stuffs
        ItemStack villager = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta villagerMeta = villager.getItemMeta();
        if (!village) {
            villagerMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("VillagerOff"));
        } else {
            villager = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
            villagerMeta.setDisplayName(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("VillagerOn"));
        }
        villagerMeta.setLore(Arrays.asList(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Toggle")));
        villager.setItemMeta(villagerMeta);


        inv.setItem(4, ty);
        inv.setItem(45, baby);
        inv.setItem(46, villager);
        inv.setItem(53, confirm);

        p.openInventory(inv);
    }

    @EventHandler
    public void onTypeChoose(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().contains(MainGUI.getString("ShopHeader"))) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {

                    String name = e.getInventory().getName();
                    name = name.substring(11);

                    Shop shop = ShopLimits.fromString(name);

                    if (e.getInventory().getItem(4) != null && e.getInventory().getItem(4).getType() == Material.MONSTER_EGG && e.getInventory().getItem(53) != null && e.getInventory().getItem(53).getItemMeta().getDisplayName() != null && e.getInventory().getItem(53).getItemMeta().getDisplayName().equals(Checkout.getString("Confirm")) && e.getInventory().getItem(53).getType() == Material.CHEST) {

                        if (EntityType.valueOf(e.getInventory().getItem(4).getItemMeta().getDisplayName().substring(2)) != null) {
                            EntityType type = EntityType.valueOf(e.getInventory().getItem(4).getItemMeta().getDisplayName().substring(2));

                            boolean baby = false;
                            if (e.getInventory().getItem(45) != null) {
                                if (e.getInventory().getItem(45).getItemMeta().getDisplayName() != null && e.getInventory().getItem(45).getItemMeta().getDisplayName().equals(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOn"))) {
                                    baby = true;
                                }
                            }

                            boolean shear = false;
                            boolean villager = false;
                            if (e.getInventory().getItem(46) != null) {
                                if (e.getInventory().getItem(46).getItemMeta().getDisplayName() != null && e.getInventory().getItem(46).getItemMeta().getDisplayName().equals(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("ShearedOn"))) {
                                    shear = true;
                                }
                                if (e.getInventory().getItem(46).getItemMeta().getDisplayName() != null && e.getInventory().getItem(46).getItemMeta().getDisplayName().equals(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("VillagerOn"))) {
                                    villager = true;
                                }
                            }

                            int size = 1;
                            DyeColor dyeColor = null;
                            Horse.Color horseColor = null;
                            Horse.Variant horseVar = null;
                            Horse.Style horseStyle = null;
                            Ocelot.Type catType = null;
                            Skeleton.SkeletonType skeletonType = Skeleton.SkeletonType.NORMAL;
                            Villager.Profession prof = null;

                            if (e.getInventory().getItem(4).getItemMeta().getLore() != null) {
                                for (String s : e.getInventory().getItem(4).getItemMeta().getLore()) {
                                    if (s.contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Profession"))) {
                                        prof = Villager.Profession.valueOf(s.substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Profession").length() + 3));
                                    }
                                    if (s.contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Size"))) {
                                        size = Integer.parseInt(s.substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Size").length() + 3));
                                    }
                                    if (s.contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("SkeletonType"))) {
                                        skeletonType = Skeleton.SkeletonType.valueOf(s.substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("SkeletonType").length() + 3));
                                    }
                                    if (s.contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("OcelotType"))) {
                                        catType = Ocelot.Type.valueOf(s.substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("OcelotType").length() + 3));
                                    }
                                    if (s.contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("DyeColor"))) {
                                        dyeColor = DyeColor.valueOf(s.substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("DyeColor").length() + 3));
                                    }
                                    if (s.contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Color"))) {
                                        horseColor = Horse.Color.valueOf(s.substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Color").length() + 3));
                                    }
                                    if (s.contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Style"))) {
                                        horseStyle = Horse.Style.valueOf(s.substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Style").length() + 3));
                                    }
                                    if (s.contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Variant"))) {
                                        horseVar = Horse.Variant.valueOf(s.substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Variant").length() + 3));
                                    }

                                }
                            }

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals(Checkout.getString("Confirm"))) {
                                if (type == EntityType.VILLAGER) {
                                    me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby, prof));
                                } else if (type == EntityType.OCELOT) {
                                    me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby, catType));
                                } else if (type == EntityType.WOLF) {
                                    me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby, dyeColor));
                                } else if (type == EntityType.ZOMBIE) {
                                    me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby, villager));
                                } else if (type == EntityType.SLIME || type == EntityType.MAGMA_CUBE) {
                                    me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, size));
                                } else if (type == EntityType.HORSE) {
                                    me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby, horseVar, horseColor, horseStyle, false));
                                } else if (type == EntityType.SHEEP) {
                                    me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, shear, baby, dyeColor));
                                } else if (type == EntityType.SKELETON) {
                                    me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, skeletonType));
                                } else if (type == EntityType.COW || type == EntityType.CHICKEN || type == EntityType.MUSHROOM_COW || type == EntityType.PIG_ZOMBIE || type == EntityType.PIG) {
                                    me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop, baby));
                                } else {
                                    me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(new ShopsNPC(type, shop));
                                }

                                p.closeInventory();
                                shop.setNPCShop(true);
                                shop.setOpen(true);
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("NPCShop").replaceAll("<Value>", "§aOn"));
                                ShopSettings.removeChest(shop);
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Profession"))) {
                                openVillagerSpecifier(p, shop, type, baby, Villager.Profession.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Profession").length() + 3)));
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Size"))) {
                                openSlimeSpecifier(p, shop, type, Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Size").length() + 3)));
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("SkeletonType"))) {
                                openSkeletonSpecifier(p, shop, type, Skeleton.SkeletonType.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("SkeletonType").length() + 3)));
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("OcelotType"))) {
                                openCatSpecifier(p, shop, type, baby, Ocelot.Type.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("OcelotType").length() + 3)));
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("DyeColor"))) {
                                if (type == EntityType.SHEEP) {
                                    openSheepSpecifier(p, shop, type, baby, shear, DyeColor.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("DyeColor").length() + 3)));
                                }
                                if (type == EntityType.WOLF) {
                                    openDogSpecifier(p, shop, type, baby, DyeColor.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("DyeColor").length() + 3)));
                                }
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Color"))) {
                                openHorseSpecifier(p, shop, type, baby, horseVar, horseStyle, Horse.Color.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Color").length() + 3)));
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Style"))) {
                                openHorseSpecifier(p, shop, type, baby, horseVar, Horse.Style.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Style").length() + 3)), horseColor);
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Variant"))) {
                                openHorseSpecifier(p, shop, type, baby, Horse.Variant.valueOf(e.getCurrentItem().getItemMeta().getDisplayName().substring(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("Variant").length() + 3)), horseStyle, horseColor);
                            }

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("ShearedOn"))) {
                                openSheepSpecifier(p, shop, type, baby, false, dyeColor);
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("ShearedOff"))) {
                                openSheepSpecifier(p, shop, type, baby, true, dyeColor);
                            }

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("VillagerOn"))) {
                                openZombieSpecifier(p, shop, type, baby, false);
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("VillagerOff"))) {
                                openZombieSpecifier(p, shop, type, baby, true);
                            }

                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOn"))) {

                                if (type == EntityType.VILLAGER) {
                                    TypeSpecifier.openVillagerSpecifier(p, shop, type, false, prof);
                                } else if (type == EntityType.OCELOT) {
                                    TypeSpecifier.openCatSpecifier(p, shop, type, false, catType);
                                } else if (type == EntityType.WOLF) {
                                    TypeSpecifier.openDogSpecifier(p, shop, type, false, dyeColor);
                                } else if (type == EntityType.ZOMBIE) {
                                    TypeSpecifier.openZombieSpecifier(p, shop, type, false, villager);
                                } else if (type == EntityType.SLIME || type == EntityType.MAGMA_CUBE) {
                                    TypeSpecifier.openSlimeSpecifier(p, shop, type, size);
                                } else if (type == EntityType.HORSE) {
                                    TypeSpecifier.openHorseSpecifier(p, shop, type, false, horseVar, horseStyle, horseColor);
                                } else if (type == EntityType.SHEEP) {
                                    TypeSpecifier.openSheepSpecifier(p, shop, type, false, shear, dyeColor);
                                } else if (type == EntityType.SKELETON) {
                                    TypeSpecifier.openSkeletonSpecifier(p, shop, type, skeletonType);
                                } else if (type == EntityType.COW || type == EntityType.CHICKEN || type == EntityType.MUSHROOM_COW || type == EntityType.PIG_ZOMBIE || type == EntityType.PIG) {
                                    TypeSpecifier.openBabySpecifier(p, shop, type, false);
                                }
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().contains(me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs.getString("BabyOff"))) {

                                if (type == EntityType.VILLAGER) {
                                    TypeSpecifier.openVillagerSpecifier(p, shop, type, true, prof);
                                } else if (type == EntityType.OCELOT) {
                                    TypeSpecifier.openCatSpecifier(p, shop, type, true, catType);
                                } else if (type == EntityType.WOLF) {
                                    TypeSpecifier.openDogSpecifier(p, shop, type, true, dyeColor);
                                } else if (type == EntityType.ZOMBIE) {
                                    TypeSpecifier.openZombieSpecifier(p, shop, type, true, villager);
                                } else if (type == EntityType.SLIME || type == EntityType.MAGMA_CUBE) {
                                    TypeSpecifier.openSlimeSpecifier(p, shop, type, size);
                                } else if (type == EntityType.HORSE) {
                                    TypeSpecifier.openHorseSpecifier(p, shop, type, true, horseVar, horseStyle, horseColor);
                                } else if (type == EntityType.SHEEP) {
                                    TypeSpecifier.openSheepSpecifier(p, shop, type, true, shear, dyeColor);
                                } else if (type == EntityType.SKELETON) {
                                    TypeSpecifier.openSkeletonSpecifier(p, shop, type, skeletonType);
                                } else if (type == EntityType.COW || type == EntityType.CHICKEN || type == EntityType.MUSHROOM_COW || type == EntityType.PIG_ZOMBIE || type == EntityType.PIG) {
                                    TypeSpecifier.openBabySpecifier(p, shop, type, true);
                                }
                            }


                        }
                    }
                }
            }
        }
    }
}
