package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Shops.Shop;
import max.hubbard.bettershops.Shops.Types.NPC.NPCShop;
import max.hubbard.bettershops.Shops.Types.NPC.ShopsNPC;
import org.bukkit.DyeColor;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.material.Colorable;

import java.lang.reflect.Method;
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
public class NPCInfo {

    public static ShopsNPC createNewShopsNPC(final LivingEntity e, final Shop shop) throws Exception {

        EntityType type = e.getType();
        boolean baby = false;
        boolean sheared = false;
        boolean villager = false;
        List<String> lore = new ArrayList<>();

        if (Ageable.class.isAssignableFrom(type.getEntityClass())) {
            baby = !(boolean) type.getEntityClass().getMethod("isAdult").invoke(e);
        }
        if (type == EntityType.SHEEP) {
            sheared = (boolean) type.getEntityClass().getMethod("isSheared").invoke(e);
        }
        if (type == EntityType.ZOMBIE) {
            villager = (boolean) type.getEntityClass().getMethod("isVillager").invoke(e);
            baby = (boolean) type.getEntityClass().getMethod("isBaby").invoke(e);
        }

        if (Colorable.class.isAssignableFrom(type.getEntityClass())) {
            DyeColor color = (DyeColor) type.getEntityClass().getMethod("getColor").invoke(e);
            lore.add(Language.getString("NPCs", "DyeColor") + " §7" + color.name());
        }

        if (type == EntityType.WOLF) {
            DyeColor color = (DyeColor) type.getEntityClass().getMethod("getCollarColor").invoke(e);
            lore.add(Language.getString("NPCs", "DyeColor") + " §7" + color.name());
        }

        for (int i = 0; i < type.getEntityClass().getDeclaredClasses().length; i++) {
            final Class c = type.getEntityClass().getClasses()[i];
            if (c.isEnum()) {
                for (Method m : type.getEntityClass().getMethods()) {
                    if (m.getParameterTypes().length == 0)
                        if (m.invoke(e).getClass().equals(c)) {
                            final Enum e1 = (Enum) m.invoke(e);
                            if (c.getSimpleName().equals("Type") && type == EntityType.OCELOT) {

                                lore.add(Language.getString("NPCs", "OcelotType") + " §7" + e1.name());
                            } else if (c.getSimpleName().equals("Type") && type == EntityType.RABBIT) {
                                lore.add(Language.getString("NPCs", "RabbitType") + " §7" + e1.name());
                            } else {
                                lore.add(Language.getString("NPCs", c.getSimpleName()) + " §7" + e1.name());
                            }
                            break;
                        }
                }
            }
        }

        if (type == EntityType.SLIME || type == EntityType.MAGMA_CUBE) {
            int i = (int) type.getEntityClass().getMethod("getSize").invoke(e);
            lore.add(Language.getString("NPCs", "Size") + " §7" + i);
        }

        if (type == EntityType.PLAYER) {
            String name = (String) type.getEntityClass().getMethod("getName").invoke(e);
            lore.add(Language.getString("NPCs", "Player") + " §7" + name);
        }

        e.remove();

        return new NPCShop(type, lore, shop, baby, sheared, villager);


    }
}
