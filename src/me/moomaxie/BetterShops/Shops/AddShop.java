package me.moomaxie.BetterShops.Shops;

import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ***********************************************************************
 * Copyright me.moomaxie (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of me.moomaxie. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class AddShop {

    private File file;
    private YamlConfiguration config;
    private String n;
    private Chest chest;

    public AddShop(Player p, Chest c, String name) {
        file = new File(Core.getCore().getDataFolder(), "Shops/" + p.getUniqueId().toString() + ".yml");

        config = YamlConfiguration.loadConfiguration(file);
        n = name;
        chest = c;


        setName(name);
        setOwner(p);
        setDescription("No Description");
        setLocation(chest.getLocation());
        addManager(p);
        removeManager(p);
        setOpen(false);
        setNotify(false);
        setServerShop(false);
        setNPC(false);

        if (!config.getConfigurationSection(n).isConfigurationSection("Contents")) {
            config.getConfigurationSection(n).createSection("Contents");
        }

        if (!config.getConfigurationSection(n).isConfigurationSection("Sell")) {
            config.getConfigurationSection(n).createSection("Sell");
        }

        try {
            config.save(file);
        } catch (IOException e) {

        }

        if (ShopLimits.fromString(name) == null) {
            Shop shop = new Shop(name, config, file);
            ShopLimits.shops.add(shop);
            ShopLimits.locs.put(chest.getLocation(),shop);
            ShopLimits.names.put(name,shop);
        }

    }

    public void setName(String name) {
        config.set(n + ".Name", name);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOpen(boolean open) {
        if (open) {
            config.set(n + ".Open", "True");
        } else {
            config.set(n + ".Open", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNPC(boolean open) {
        if (open) {
            config.set(n + ".NPC", "True");
        } else {
            config.set(n + ".NPC", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setServerShop(boolean open) {
        if (open) {
            config.set(n + ".Server", "True");
        } else {
            config.set(n + ".Server", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNotify(boolean open) {
        if (open) {
            config.set(n + ".Notify", "True");
        } else {
            config.set(n + ".Notify", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return config.getString(n + ".Name");
    }

    public void setOwner(Player p) {
        config.set(n + ".Owner", p.getUniqueId().toString());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getOwner() {
        return Bukkit.getPlayer(UUID.fromString(config.getString(n + ".Owner")));
    }

    public void setDescription(String name) {
        config.set(n + ".Description", name);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDescription() {
        return config.getString(n + ".Description");
    }

    private void setLocation(Location loc) {
        config.set(n + ".Location", loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLocation() {
        Location loc;

        String[] locs = config.getString(n + ".Location").split(" ");

        World w = Bukkit.getWorld(locs[0]);

        double x = Double.parseDouble(locs[1]);
        double y = Double.parseDouble(locs[2]);
        double z = Double.parseDouble(locs[3]);

        loc = new Location(w, x, y, z);

        return loc;
    }

    public List<Player> getManagers() {
        List<Player> mans = new ArrayList<Player>();
        for (String id : config.getConfigurationSection(n).getConfigurationSection("Managers").getKeys(false)) {
            UUID uid = UUID.fromString(id);

            mans.add(Bukkit.getPlayer(uid));
        }
        return mans;
    }

    public void addManager(Player p) {
        if (!config.getConfigurationSection(n).isConfigurationSection("Managers")) {
            config.getConfigurationSection(n).createSection("Managers");
        }
        config.getConfigurationSection(n).getConfigurationSection("Managers").set(p.getUniqueId().toString(), p.getUniqueId().toString());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeManager(Player p) {
        config.getConfigurationSection(n).getConfigurationSection("Managers").set(p.getUniqueId().toString(), null);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
