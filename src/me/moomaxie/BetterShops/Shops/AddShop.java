package me.moomaxie.BetterShops.Shops;

import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.ShopLimits;
import me.moomaxie.BetterShops.Core;
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
        setDescription(MainGUI.getString("NoDescription"));
        setLocation(chest.getLocation());
        addManager(p);
        removeManager(p);
        setOpen(false);
        setNotify(false);
        setServerShop(false);
        setNPC(false);
        setHoloShop(false);

        if (!config.getConfigurationSection(n).isConfigurationSection("Items")) {
            config.getConfigurationSection(n).createSection("Items");
        }

        if (!config.getConfigurationSection(n).isConfigurationSection("Transactions")) {
            config.getConfigurationSection(n).createSection("Transactions");
        }

        try {
            config.save(file);
        } catch (IOException e) {

        }

        if (ShopLimits.fromString(name) == null) {
            Shop shop = new Shop(name, config, file);
            ShopLimits.shops.add(shop);
            ShopLimits.locs.put(chest.getLocation(), shop);
            ShopLimits.names.put(name, shop);
            List<Shop> l = ShopLimits.getShopsForPlayer(p);
            if (l == null){
                l = new ArrayList<>();
            }
            l.add(shop);
            ShopLimits.playerShops.put(p.getUniqueId(),l);
            ShopLimits.getLimits().put(p.getUniqueId(),l.size());

            if (Core.useSQL()) {
                try {

                    Core.getSQLDatabase().getConnection().createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Shops" +
                            " (Name TEXT, " +
                            "LocationWorld TEXT, " +
                            "LocationX INT, " +
                            "LocationY INT, " +
                            "LocationZ INT, " +
                            "OwnerUUID TEXT, " +
                            "OwnerName TEXT, " +
                            "Description TEXT, " +
                            "Open TEXT, " +
                            "NPCShop TEXT, " +
                            "HoloShop TEXT, " +
                            "ServerShop TEXT, " +
                            "Notify TEXT, " +
                            "NextShopId INT);");


                    Core.getSQLDatabase().updateSQL("INSERT INTO Shops (`Name`, `LocationWorld`, `LocationX`, `LocationY`, `LocationZ`" +
                            ",`OwnerUUID`, `OwnerName`, `Description`, `Open`, `NPCShop`, `HoloShop`, `ServerShop`, `Notify`, `NextShopId`) VALUES ('" + name + "', " +
                            "'" + chest.getLocation().getWorld().getName() + "'," +
                            "'" + chest.getLocation().getX() + "'," +
                            "'" + chest.getLocation().getY() + "'," +
                            "'" + chest.getLocation().getZ() + "'," +
                            "'" + p.getUniqueId().toString() + "'," +
                            "'" + p.getName() + "'," +
                            "'" + MainGUI.getString("NoDescription") + "'," +
                            "'" + false + "'," +
                            "'" + false + "'," +
                            "'" + false + "'," +
                            "'" + false + "'," +
                            "'" + false + "'," +
                            "'" + 0 + "');");

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public AddShop(OfflinePlayer p, Location l, String name) {
        file = new File(Core.getCore().getDataFolder(), "Shops/" + p.getUniqueId().toString() + ".yml");

        config = YamlConfiguration.loadConfiguration(file);
        n = name;


        setName(name);
        setOwner(p);
        setDescription(MainGUI.getString("NoDescription"));
        setLocation(l);
        addManager(p);
        removeManager(p);
        setOpen(false);
        setNotify(false);
        setServerShop(false);
        setNPC(false);
        setHoloShop(false);

        if (!config.getConfigurationSection(n).isConfigurationSection("Items")) {
            config.getConfigurationSection(n).createSection("Items");
        }

        if (!config.getConfigurationSection(n).isConfigurationSection("Transactions")) {
            config.getConfigurationSection(n).createSection("Transactions");
        }

        try {
            config.save(file);
        } catch (IOException e) {

        }

        if (ShopLimits.fromString(name) == null) {
            Shop shop = new Shop(name, config, file);
            ShopLimits.shops.add(shop);
            ShopLimits.locs.put(chest.getLocation(), shop);
            ShopLimits.names.put(name, shop);
            List<Shop> li = ShopLimits.getShopsForPlayer(p);
            if (li == null){
                li = new ArrayList<>();
            }
            li.add(shop);
            ShopLimits.playerShops.put(p.getUniqueId(),li);
            ShopLimits.getLimits().put(p.getUniqueId(), li.size());

            if (Core.useSQL()) {
                try {

                    Core.getSQLDatabase().getConnection().createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Shops" +
                            " (Name TEXT, " +
                            "LocationWorld TEXT, " +
                            "LocationX INT, " +
                            "LocationY INT, " +
                            "LocationZ INT, " +
                            "OwnerUUID TEXT, " +
                            "OwnerName TEXT, " +
                            "Description TEXT, " +
                            "Open TEXT, " +
                            "NPCShop TEXT, " +
                            "HoloShop TEXT, " +
                            "ServerShop TEXT, " +
                            "Notify TEXT, " +
                            "NextShopId INT);");


                    Core.getSQLDatabase().updateSQL("INSERT INTO Shops (`Name`, `LocationWorld`, `LocationX`, `LocationY`, `LocationZ`" +
                            ",`OwnerUUID`, `OwnerName`, `Description`, `Open`, `NPCShop`, `HoloShop`, `ServerShop`, `Notify`, `NextShopId`) VALUES ('" + name + "', " +
                            "'" + l.getWorld().getName() + "'," +
                            "'" + l.getX() + "'," +
                            "'" + l.getY() + "'," +
                            "'" + l.getZ() + "'," +
                            "'" + p.getUniqueId().toString() + "'," +
                            "'" + p.getName() + "'," +
                            "'" + MainGUI.getString("NoDescription") + "'," +
                            "'" + false + "'," +
                            "'" + false + "'," +
                            "'" + false + "'," +
                            "'" + false + "'," +
                            "'" + false + "'," +
                            "'" + 0 + "');");

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
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

    public void setHoloShop(boolean open) {
        if (open) {
            config.set(n + ".Holo", "True");
        } else {
            config.set(n + ".Holo", "False");
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

    public void setOwner(OfflinePlayer p) {
        config.set(n + ".Owner", p.getUniqueId().toString());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(UUID.fromString(config.getString(n + ".Owner")));
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

    public void addManager(OfflinePlayer p) {
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

    public void removeManager(OfflinePlayer p) {
        config.getConfigurationSection(n).getConfigurationSection("Managers").set(p.getUniqueId().toString(), null);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
