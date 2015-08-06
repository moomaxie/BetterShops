package max.hubbard.bettershops.Menus.ShopMenus;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.Menus.ShopMenu;
import max.hubbard.bettershops.Shops.Items.Actions.ClickableItem;
import max.hubbard.bettershops.Shops.Items.Actions.LeftClickAction;
import max.hubbard.bettershops.Shops.Items.Actions.ShopItemStack;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.NPC.*;
import max.hubbard.bettershops.Utils.AnvilManager;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Colorable;
import org.bukkit.scheduler.BukkitRunnable;

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
public class NPCConfigure implements ShopMenu {

    Shop shop;
    Inventory inv;

    public NPCConfigure(Shop shop) {
        this.shop = shop;
        inv = Bukkit.createInventory(null, 54, Language.getString("MainGUI", "ShopHeader") + shop.getName());
    }


    @Override
    public MenuType getType() {
        return MenuType.NPC_CONFIGURE;
    }

    @Override
    public Shop getAttachedShop() {
        return shop;
    }

    @Override
    public void draw(final Player p, final int page, final Object... obj) {
        inv.clear();

        final EntityType type = (EntityType) obj[0];
        final List<String> lore = (List<String>) obj[1];
        final boolean bb = (boolean) obj[2];
        final boolean vill = (boolean) obj[3];
        final boolean shear = (boolean) obj[4];

//Glass
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, shop.getFrameColor());
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < 18; i++) {
            inv.setItem(i, glass);
        }

        if (type != null) {


            final ItemStack ty = new ItemStack(Material.MONSTER_EGG, 1, (byte) type.getTypeId());
            ItemMeta tyMeta = ty.getItemMeta();
            tyMeta.setDisplayName("§a" + type.name());
            tyMeta.setLore(lore);
            ty.setItemMeta(tyMeta);

            ItemStack confirm = new ItemStack(Material.CHEST);
            ItemMeta confirmMeta = confirm.getItemMeta();
            confirmMeta.setDisplayName(Language.getString("Checkout", "Confirm"));
            confirm.setItemMeta(confirmMeta);
            ClickableItem confirmClick = new ClickableItem(new ShopItemStack(confirm), inv, p);
            confirmClick.addLeftClickAction(new LeftClickAction() {
                @Override
                public void onAction(InventoryClickEvent e) {
                    if (Core.useCitizens()) {
                        ShopsNPC npc = new CitizensShop(new EntityInfo(EntityType.valueOf(ty.getItemMeta().getDisplayName().substring(2)), lore, bb, shear, vill), shop);
                        npc.removeChest();
                        npc.spawn();
                        NPCManager.addNPCShop(npc);
                        shop.setObject("NPC", true);
                        p.closeInventory();
                    } else {
                        ShopsNPC npc = new NPCShop(EntityType.valueOf(ty.getItemMeta().getDisplayName().substring(2)), lore, shop, bb, shear, vill);

                        if (npc.getEntity() != null) {
                            NPCManager.addNPCShop(npc);
                            shop.setObject("NPC", true);
                        }
                        p.closeInventory();
                    }
                }
            });

            if (Ageable.class.isAssignableFrom(type.getEntityClass()) || type == EntityType.ZOMBIE) {

                ItemStack baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
                ItemMeta babyMeta = baby.getItemMeta();
                if (!bb) {
                    babyMeta.setDisplayName(Language.getString("NPCs", "BabyOff"));
                } else {
                    baby = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
                    babyMeta.setDisplayName(Language.getString("NPCs", "BabyOn"));
                }
                babyMeta.setLore(Arrays.asList(Language.getString("NPCs", "Toggle")));
                baby.setItemMeta(babyMeta);
                ClickableItem babyClick = new ClickableItem(new ShopItemStack(baby), inv, p);
                babyClick.addLeftClickAction(new LeftClickAction() {
                    @Override
                    public void onAction(InventoryClickEvent e) {
                        draw(p, page, type, lore, !bb, vill, shear);
                    }
                });


                inv.setItem(45, baby);
            }

            if (type == EntityType.SHEEP) {
                ItemStack shea = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
                ItemMeta shearMeta = shea.getItemMeta();
                if (!shear) {
                    shearMeta.setDisplayName(Language.getString("NPCs", "ShearedOff"));
                } else {
                    shea = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
                    shearMeta.setDisplayName(Language.getString("NPCs", "ShearedOn"));
                }
                shearMeta.setLore(Arrays.asList(Language.getString("NPCs", "Toggle")));
                shea.setItemMeta(shearMeta);
                ClickableItem babyClick = new ClickableItem(new ShopItemStack(shea), inv, p);
                babyClick.addLeftClickAction(new LeftClickAction() {
                    @Override
                    public void onAction(InventoryClickEvent e) {
                        draw(p, page, type, lore, bb, vill, !shear);
                    }
                });
                inv.setItem(46, shea);
            }

            if (type == EntityType.ZOMBIE) {
                ItemStack shea = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
                ItemMeta shearMeta = shea.getItemMeta();
                if (!vill) {
                    shearMeta.setDisplayName(Language.getString("NPCs", "VillagerOff"));
                } else {
                    shea = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
                    shearMeta.setDisplayName(Language.getString("NPCs", "VillagerOn"));
                }
                shearMeta.setLore(Arrays.asList(Language.getString("NPCs", "Toggle")));
                shea.setItemMeta(shearMeta);
                ClickableItem babyClick = new ClickableItem(new ShopItemStack(shea), inv, p);
                babyClick.addLeftClickAction(new LeftClickAction() {
                    @Override
                    public void onAction(InventoryClickEvent e) {
                        draw(p, page, type, lore, bb, !vill, shear);
                    }
                });
                inv.setItem(46, shea);
            }

            //Sheepish Stuffs
            if (Colorable.class.isAssignableFrom(type.getEntityClass()) || type == EntityType.WOLF) {
                for (final DyeColor variant : DyeColor.values()) {
                    ItemStack item = new ItemStack(Material.WOOL, 1, variant.getWoolData());
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(Language.getString("NPCs", "DyeColor") + " §e" + variant.name());
                    meta.setLore(Arrays.asList(Language.getString("NPCs", "Choose")));
                    item.setItemMeta(meta);

                    inv.setItem(inv.firstEmpty(), item);
                    ClickableItem colorClick = new ClickableItem(new ShopItemStack(item), inv, p);
                    colorClick.addLeftClickAction(new LeftClickAction() {
                        @Override
                        public void onAction(InventoryClickEvent e) {

                            String c = null;
                            for (String s : lore) {
                                if (s.contains(Language.getString("NPCs", "DyeColor"))) {
                                    c = s;
                                    break;
                                }
                            }
                            if (c != null) {
                                lore.remove(c);
                            }
                            lore.add(Language.getString("NPCs", "DyeColor") + " §7" + variant.name());

                            draw(p, page, type, lore, bb, vill, shear);
                        }
                    });

                }
            }

            for (int i = 0; i < type.getEntityClass().getDeclaredClasses().length; i++) {
                final Class c = type.getEntityClass().getClasses()[i];
                if (c.isEnum()) {
                    for (Object o : c.getEnumConstants()) {
                        final Enum e1 = (Enum) o;

                        ItemStack item = new ItemStack(Material.WOOL, 1, (byte) i);
                        ItemMeta meta = item.getItemMeta();

                        if (c.getSimpleName().equals("SkeletonType")) {
                            meta.setDisplayName(Language.getString("NPCs", "SkeletonType") + " §e" + e1.name());
                        }
                        if (c.getSimpleName().equals("Type") && type == EntityType.OCELOT) {
                            meta.setDisplayName(Language.getString("NPCs", "OcelotType") + " §e" + e1.name());
                        }
                        if (c.getSimpleName().equals("Type") && type == EntityType.RABBIT) {
                            meta.setDisplayName(Language.getString("NPCs", "RabbitType") + " §e" + e1.name());
                        }
                        if (c.getSimpleName().equals("Style")) {
                            meta.setDisplayName(Language.getString("NPCs", "Style") + " §e" + e1.name());
                        }
                        if (c.getSimpleName().equals("Variant")) {
                            meta.setDisplayName(Language.getString("NPCs", "Variant") + " §e" + e1.name());
                        }
                        if (c.getSimpleName().equals("Color")) {
                            meta.setDisplayName(Language.getString("NPCs", "Color") + " §e" + e1.name());
                        }
                        if (c.getSimpleName().equals("Profession")) {
                            meta.setDisplayName(Language.getString("NPCs", "Profession") + " §e" + e1.name());
                        }
                        meta.setLore(Arrays.asList(Language.getString("NPCs", "Choose")));
                        item.setItemMeta(meta);
                        ClickableItem colorClick = new ClickableItem(new ShopItemStack(item), inv, p);
                        colorClick.addLeftClickAction(new LeftClickAction() {
                            @Override
                            public void onAction(InventoryClickEvent e) {

                                String c1 = null;
                                for (String s : lore) {
                                    if (s.contains(Language.getString("NPCs", c.getSimpleName()))) {
                                        c1 = s;
                                        break;
                                    }
                                }
                                if (c1 != null) {
                                    lore.remove(c1);
                                }
                                if (c.getSimpleName().equals("Type") && type == EntityType.OCELOT) {
                                    lore.add(Language.getString("NPCs", "OcelotType") + " §7" + e1.name());
                                } else if (c.getSimpleName().equals("Type") && type == EntityType.RABBIT) {
                                    lore.add(Language.getString("NPCs", "RabbitType") + " §7" + e1.name());
                                } else {
                                    lore.add(Language.getString("NPCs", c.getSimpleName()) + " §7" + e1.name());
                                }


                                draw(p, page, type, lore, bb, vill, shear);
                            }
                        });
                        inv.setItem(inv.firstEmpty(), item);
                    }
                }
            }

            if (type == EntityType.SLIME || type == EntityType.MAGMA_CUBE) {
                for (int i = 1; i < 10; i++) {
                    final int carl = i;
                    ItemStack item = new ItemStack(Material.WOOL, 1, (byte) 5);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(Language.getString("NPCs", "Size") + " §e" + i);
                    meta.setLore(Arrays.asList(Language.getString("NPCs", "Choose")));
                    item.setItemMeta(meta);

                    inv.setItem(inv.firstEmpty(), item);
                    ClickableItem colorClick = new ClickableItem(new ShopItemStack(item), inv, p);
                    colorClick.addLeftClickAction(new LeftClickAction() {
                        @Override
                        public void onAction(InventoryClickEvent e) {

                            String c = null;
                            for (String s : lore) {
                                if (s.contains(Language.getString("NPCs", "Size"))) {
                                    c = s;
                                    break;
                                }
                            }
                            if (c != null) {
                                lore.remove(c);
                            }
                            lore.add(Language.getString("NPCs", "Size") + " §7" + carl);
                            p.closeInventory();
                            draw(p, page, type, lore, bb, vill, shear);
                        }
                    });
                }
            }

            if (type == EntityType.PLAYER) {
                ItemStack item = new ItemStack(Material.NAME_TAG);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Language.getString("NPCs", "Change"));
                item.setItemMeta(meta);
                inv.setItem(inv.firstEmpty(), item);
                ClickableItem colorClick = new ClickableItem(new ShopItemStack(item), inv, p);
                colorClick.addLeftClickAction(new LeftClickAction() {
                    @Override
                    public void onAction(InventoryClickEvent e) {

                        final AnvilManager man = new AnvilManager(p);
                        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
                            @Override
                            public void run() {
                                String na = man.call();
                                String c = null;
                                for (String s : lore) {
                                    if (s.contains(Language.getString("NPCs", "Player"))) {
                                        c = s;
                                        break;
                                    }
                                }
                                if (c != null) {
                                    lore.remove(c);
                                }
                                lore.add(Language.getString("NPCs", "Player") + " §7" + na);

                                draw(p, page, type, lore, bb, vill, shear);
                            }
                        });


                    }
                });

            }

            inv.setItem(4, ty);

            inv.setItem(53, confirm);

        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(Language.getString("MainGUI", "BackArrow"));
        back.setItemMeta(backMeta);
        ClickableItem backClick = new ClickableItem(new ShopItemStack(back), inv, p);
        backClick.addLeftClickAction(new LeftClickAction() {
            @Override
            public void onAction(InventoryClickEvent e) {
                shop.getMenu(MenuType.NPC_CHOOSE).draw(p, page, obj);
            }
        });

        inv.setItem(0, back);

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
