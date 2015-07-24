package max.hubbard.bettershops.Shops;

import max.hubbard.bettershops.Configurations.Config;
import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class AddShop {

    private File file;
    private YamlConfiguration config;
    private String n;
    private Chest chest;
    private Shop shop;

    public AddShop(Player p, Chest c, String name) {


        if (!Core.useSQL()) {
            file = new File(Core.getCore().getDataFolder(), "Shops/" + p.getUniqueId().toString() + "/" + name + ".yml");

            config = YamlConfiguration.loadConfiguration(file);
            n = name;
            chest = c;


            setName(name);
            setOwner(p);
            setDescription(Language.getString("MainGUI", "NoDescription"));
            setLocation(chest.getLocation());
            addManager(p);
            removeManager(p);
            setOpen(false);
            setNotify(false);
            setServerShop(false);
            setNPC(false);
            setHoloShop(false);
//        setSignShop(false);

            if (!config.isConfigurationSection("Items")) {
                config.createSection("Items");
            }

            if (!config.isConfigurationSection("Transactions")) {
                config.createSection("Transactions");
            }

            try {
                config.save(file);
            } catch (IOException ignored) {

            }

            if (ShopManager.fromString(name) == null) {
                if (!Core.useSQL()) {
                    shop = new FileShop(config, file, p);
                }
                ShopManager.shops.add(shop);
                ShopManager.locs.put(chest.getLocation(), shop);
                ShopManager.names.put(name, shop);
                List<Shop> l = ShopManager.getShopsForPlayer(p);
                if (l == null) {
                    l = new ArrayList<>();
                }
                l.add(shop);
                ShopManager.playerShops.put(p.getUniqueId(), l);
                ShopManager.getLimits().put(p.getUniqueId(), l.size());

            }
        } else {

            try {
                Statement s = Core.getConnection().createStatement();
                s.executeUpdate("INSERT INTO " + Config.getObject("prefix") + "Shops(`Name`, `Owner`, `Description`, `World`, `X`, `Y`, `Z`, `NextShopId`, `Open`, `Notify`, `Server`, `NPC`, `Holo`, `Frame`) VALUES" +
                        " ('" + name + "', '" + p.getUniqueId().toString() + "', '" + Language.getString("MainGUI", "NoDescription") + "', '" + c.getLocation().getWorld().getName() + "', '" + c.getLocation().getX() + "', '" + c.getLocation().getY() + "', '" + c.getLocation().getZ() + "', '"
                        + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 7 + "');");


                shop = new SQLShop(name);
                ShopManager.shops.add(shop);
                ShopManager.locs.put(c.getLocation(), shop);
                ShopManager.names.put(name, shop);
                List<Shop> l = ShopManager.getShopsForPlayer(p);
                if (l == null) {
                    l = new ArrayList<>();
                }
                l.add(shop);
                ShopManager.playerShops.put(p.getUniqueId(), l);
                ShopManager.getLimits().put(p.getUniqueId(), l.size());

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        if (Core.getMetrics() != null){
            Core.getCore().setUpMetrics();
        }

    }

    public AddShop(OfflinePlayer p, Location l, String name) {

        if (!Core.useSQL()) {
            file = new File(Core.getCore().getDataFolder(), "Shops/" + p.getUniqueId().toString() + "/" + name + ".yml");

            config = YamlConfiguration.loadConfiguration(file);
            n = name;


            setName(name);
            setOwner(p);
            setDescription(Language.getString("MainGUI", "NoDescription"));
            setLocation(l);
            addManager(p);
            removeManager(p);
            setOpen(false);
            setNotify(false);
            setServerShop(false);
            setNPC(false);
            setHoloShop(false);
//        setSignShop(false);

            if (!config.isConfigurationSection("Items")) {
                config.createSection("Items");
            }

            if (!config.isConfigurationSection("Transactions")) {
                config.createSection("Transactions");
            }

            try {
                config.save(file);
            } catch (IOException ignored) {

            }

            if (ShopManager.fromString(name) == null) {
                if (!Core.useSQL()) {
                    shop = new FileShop(config, file, p);
                }
                ShopManager.shops.add(shop);
                ShopManager.locs.put(l, shop);
                ShopManager.names.put(name, shop);
                List<Shop> li = ShopManager.getShopsForPlayer(p);
                if (li == null) {
                    li = new ArrayList<>();
                }
                li.add(shop);
                ShopManager.playerShops.put(p.getUniqueId(), li);
                ShopManager.getLimits().put(p.getUniqueId(), li.size());


            }
        } else {
            try {
                Statement s = Core.getConnection().createStatement();
                s.executeUpdate("INSERT INTO " + Config.getObject("prefix") + "Shops (`Name`, `Owner`, `Description`, `World`, `X`, `Y`, `Z`, `NextShopId`, `Open`, `Notify`, `Server`, `NPC`, `Holo`, `Frame`) VALUES" +
                        " ('" + name + "', '" + p.getUniqueId().toString() + "', '" + Language.getString("MainGUI", "NoDescription") + "', '" + chest.getLocation().getWorld().getName() + "', '" + chest.getLocation().getX() + "', '" + chest.getLocation().getY() + "', '" + chest.getLocation().getZ() + "', '"
                        + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 7 + "');");

                shop = new SQLShop(name);
                ShopManager.shops.add(shop);
                ShopManager.locs.put(l, shop);
                ShopManager.names.put(name, shop);
                List<Shop> li = ShopManager.getShopsForPlayer(p);
                if (li == null) {
                    li = new ArrayList<>();
                }
                li.add(shop);
                ShopManager.playerShops.put(p.getUniqueId(), li);
                ShopManager.getLimits().put(p.getUniqueId(), li.size());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (Core.getMetrics() != null){
            Core.getCore().setUpMetrics();
        }
    }

    public void setName(String name) {
        config.set("Name", name);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOpen(boolean open) {
        if (open) {
            config.set("Open", "True");
        } else {
            config.set("Open", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNPC(boolean open) {
        if (open) {
            config.set("NPC", "True");
        } else {
            config.set("NPC", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHoloShop(boolean open) {
        if (open) {
            config.set("Holo", "True");
        } else {
            config.set("Holo", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setServerShop(boolean open) {
        if (open) {
            config.set("Server", "True");
        } else {
            config.set("Server", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNotify(boolean open) {
        if (open) {
            config.set("Notify", "True");
        } else {
            config.set("Notify", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return config.getString("Name");
    }

    public void setOwner(OfflinePlayer p) {
        config.set("Owner", p.getUniqueId().toString());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(UUID.fromString(config.getString("Owner")));
    }

    public void setDescription(String name) {
        config.set("Description", name);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDescription() {
        return config.getString("Description");
    }

    private void setLocation(Location loc) {
        config.set("Location", loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLocation() {
        Location loc;

        String[] locs = config.getString("Location").split(" ");

        World w = Bukkit.getWorld(locs[0]);

        double x = Double.parseDouble(locs[1]);
        double y = Double.parseDouble(locs[2]);
        double z = Double.parseDouble(locs[3]);

        loc = new Location(w, x, y, z);

        return loc;
    }

    public void addManager(OfflinePlayer p) {
        if (!config.isConfigurationSection("Managers")) {
            config.createSection("Managers");
        }
        config.getConfigurationSection("Managers").set(p.getUniqueId().toString(), p.getUniqueId().toString());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeManager(OfflinePlayer p) {
        if (!config.isConfigurationSection("Managers")) {
            config.createSection("Managers");
        }
        config.getConfigurationSection("Managers").set(p.getUniqueId().toString(), null);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Shop getShop() {
        return shop;
    }
}
