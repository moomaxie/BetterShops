package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Configurations.Config;
import org.bukkit.Material;

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
public class MaterialSearch {

    public static List<Material> closestMaterial(String m) {
        List<Material> mats = new ArrayList<>();

        if (Config.isInEnum(m.toUpperCase().replaceAll(" ", "_"),Material.class)){
            mats.add(Material.valueOf(m.toUpperCase().replaceAll(" ", "_")));
        }

        m = m.replace("_", " ");

        String[] r = m.split("(?=\\p{Upper})");

        m = m.toLowerCase();

        for (Material mat : Material.values()) {

            try {

                int id = Integer.parseInt(m);
                if (mat.getId() == id) {
                    mats.add(mat);
                }
            } catch (Exception e) {

                String name = mat.name().replaceAll("_", " ").toLowerCase();



                if (name.equals(m)) {
                    mats.add(mat);
                }
                if (name.contains(m)) {
                    mats.add(mat);
                }

                if (r.length > 1) {
                    if (mat.name().contains("_")) {
                        if (mat.name().startsWith(r[0])) {
                            String s = mat.name().split("_")[1].toLowerCase();
                            if (s.contains(r[1]) || s.equalsIgnoreCase(r[1])) {
                                mats.add(mat);
                            }
                        }
                    }
                }
            }

        }


        return mats;
    }
}
