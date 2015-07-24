package max.hubbard.bettershops.Shops.Types.NPC;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Menus.MenuType;
import max.hubbard.bettershops.ShopManager;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Utils.CitizensStuff;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class DeleteNPC {
    public static final BlockFace[] axis = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

    public static void deleteNPC(final ShopsNPC npc) {


        if (npc != null) {
            for (Entity e : npc.getShop().getLocation().getWorld().getLivingEntities()) {
                if (e.getType() == npc.getEntity().getType()) {

                    if (npc.getEntity().getCustomName() != null && npc.getEntity().getCustomName().equals("§a§l" + npc.getShop().getName())) {

//                    ShopManager.loadShops();

                        final Shop shop = ShopManager.fromString(npc.getEntity().getCustomName().substring(4));
                        shop.setObject("NPC", false);
                        NPCManager.removeNPCShop(npc);
                        if (Core.useCitizens()) {
                            CitizensStuff.deleteCitizensNPC(npc.getEntity());
                        } else {
                            npc.getEntity().remove();
                        }

                        addChest(shop);
                        shop.getMenu(MenuType.SHOP_SETTINGS).draw(shop.getOwner().getPlayer(), 1);
                        break;
                    }
                }
            }
        }

    }

    public static BlockFace yawToFace(float yaw) {

        return axis[Math.round(yaw / 90f) & 0x3];

    }

    public static void addChest(final Shop shop) {
        BlockFace fa = null;

        if (shop.getOwner().isOnline()) {
            fa = yawToFace(shop.getOwner().getPlayer().getLocation().getYaw()).getOppositeFace();
        }

        final BlockFace f = fa;

        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterShops"), new Runnable() {
            @Override
            public void run() {


                shop.getLocation().getBlock().setType(Material.CHEST);

                Chest chest = (Chest) shop.getLocation().getBlock().getState();

                org.bukkit.material.Chest c = (org.bukkit.material.Chest) chest.getData();
                BlockFace fa;
                if (f == null) {
                    fa = c.getFacing();
                } else {
                    fa = f;
                }

                final BlockFace face = fa;

                c.setFacingDirection(face.getOppositeFace());

                chest.setData(c);

                chest.update();

                final Block b = chest.getBlock().getRelative(face.getOppositeFace());

                if (b != null) {


                    b.setType(Material.WALL_SIGN);


                    if (b.getState() instanceof Sign) {
                        Sign s = (Sign) b.getState();

                        org.bukkit.material.Sign sign = (org.bukkit.material.Sign) s.getData();

                        sign.setFacingDirection(face.getOppositeFace());

                        s.setData(sign);

                        s.setLine(0, Language.getString("MainGUI", "SignLine1"));
                        s.setLine(1, Language.getString("MainGUI", "SignLine2"));
                        if (shop.isOpen()) {
                            s.setLine(2, Language.getString("MainGUI", "SignLine3Open"));
                        } else {
                            s.setLine(2, Language.getString("MainGUI", "SignLine3Closed"));
                        }
                        s.setLine(3, Language.getString("MainGUI", "SignLine4"));

                        s.update();
                        ShopManager.signLocs.values().remove(shop);
                        ShopManager.signLocs.put(s.getLocation(), shop);
                    }

                }
            }

        });
    }
}
