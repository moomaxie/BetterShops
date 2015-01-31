package me.moomaxie.BetterShops.Shops;

import me.moomaxie.BetterShops.Configurations.GUIMessages.ItemTexts;
import me.moomaxie.BetterShops.Configurations.GUIMessages.MainGUI;
import me.moomaxie.BetterShops.Configurations.Messages;
import me.moomaxie.BetterShops.Core;
import me.moomaxie.BetterShops.History.History;
import me.moomaxie.BetterShops.History.Transaction;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * ***********************************************************************
 * Copyright moomaxie (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of moomaxie. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Shop {

    private OfflinePlayer p;
    private Location l;
    private String name;
    private File file;
    private YamlConfiguration config;
    private History history = new History(this);

    public boolean transLoaded = false;

    public Shop(Player owner, String shopName) {
        name = shopName;

        if (name != null) {
            File file = new File(Core.getCore().getDataFolder(), "Shops");

            if (file == null) {
                file.mkdir();
            }

            if (file.listFiles() != null) {

                for (File f : file.listFiles()) {
                    if (f.getName().contains(".yml")) {
                        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                        if (config != null) {


                            for (String s : config.getKeys(false)) {
                                if (name.equals(s)) {
                                    s = s + ".Location";
                                    if (s.contains("Location")) {
                                        String c = config.getString(s);

                                        if (c != null) {

                                            String[] locs = c.split(" ");

                                            World w = Bukkit.getWorld(locs[0]);

                                            double x = Double.parseDouble(locs[1]);
                                            double y = Double.parseDouble(locs[2]);
                                            double z = Double.parseDouble(locs[3]);

                                            p = Bukkit.getOfflinePlayer(UUID.fromString(config.getConfigurationSection(name).getString("Owner")));

                                            l = new Location(w, x, y, z);

                                            this.config = config;

                                            this.file = f;

                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Shop(String shopName) {
        name = shopName;

        if (name != null) {
            File file = new File(Core.getCore().getDataFolder(), "Shops");

            if (file == null) {
                file.mkdir();
            }

            if (file.listFiles() != null) {

                for (File f : file.listFiles()) {
                    if (f.getName().contains(".yml")) {
                        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                        if (config != null) {


                            for (String s : config.getKeys(false)) {
                                if (name.equals(s)) {
                                    s = s + ".Location";
                                    if (s.contains("Location")) {
                                        String c = config.getString(s);

                                        if (c != null) {

                                            String[] locs = c.split(" ");

                                            World w = Bukkit.getWorld(locs[0]);

                                            double x = Double.parseDouble(locs[1]);
                                            double y = Double.parseDouble(locs[2]);
                                            double z = Double.parseDouble(locs[3]);

                                            p = Bukkit.getOfflinePlayer(UUID.fromString(config.getConfigurationSection(name).getString("Owner")));

                                            l = new Location(w, x, y, z);

                                            this.config = config;

                                            this.file = f;

                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Shop(Location loc) {

        File file = new File(Core.getCore().getDataFolder(), "Shops");

        if (file == null) {
            file.mkdir();
        }

        if (file.listFiles() != null) {

            for (File f : file.listFiles()) {
                if (f.getName().contains(".yml")) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                    for (String s : config.getKeys(true)) {
                        if (s.contains("Location")) {
                            String c = config.getString(s);

                            String[] locs = c.split(" ");

                            World w = Bukkit.getWorld(locs[0]);

                            double x = Double.parseDouble(locs[1]);
                            double y = Double.parseDouble(locs[2]);
                            double z = Double.parseDouble(locs[3]);

                            if (w != null) {

                                if (loc.getWorld().getName().equals(w.getName())) {
                                    if (loc.getX() == x && loc.getY() == y && loc.getZ() == z) {
                                        l = loc;

                                        String n = s.substring(0, s.length() - 9);

                                        this.file = f;

                                        name = n;

                                        this.config = config;

                                        p = Bukkit.getOfflinePlayer(UUID.fromString(config.getString(name + ".Owner")));

                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        config.set(name + ".Name", Name);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLocation(Location l){
        this.l = l;

        config.set(name + ".Location",l.getWorld().getName() + " " + l.getX() + " " + l.getY() + " " + l.getZ());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDescription() {
        return config.getString(name + ".Description");
    }

    public void setDescription(String desc) {
        config.set(name + ".Description", desc);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<OfflinePlayer> getManagers() {
        List<OfflinePlayer> mans = new ArrayList<OfflinePlayer>();

        if (config != null && name != null) {
            if (config.isConfigurationSection(name) && config.getConfigurationSection(name).isConfigurationSection("Managers")) {
                for (String id : config.getConfigurationSection(name).getConfigurationSection("Managers").getKeys(false)) {
                    UUID uid = UUID.fromString(id);

                    mans.add(Bukkit.getOfflinePlayer(uid));
                }
            }
        }
        return mans;
    }

    public void addManager(OfflinePlayer p) {

        if (config != null && name != null) {
            if (!config.getConfigurationSection(name).isConfigurationSection("Managers")) {
                config.getConfigurationSection(name).createSection("Managers");
            }
            if (config.isConfigurationSection(name) && config.getConfigurationSection(name).isConfigurationSection("Managers")) {

                config.getConfigurationSection(name).getConfigurationSection("Managers").set(p.getUniqueId().toString(), p.getUniqueId().toString());

                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public History getHistory(){
        return history;
    }

    public void removeManager(OfflinePlayer p) {
        if (getManagers().contains(p)) {
            config.getConfigurationSection(name).getConfigurationSection("Managers").set(p.getUniqueId().toString(), null);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen() {

        return config != null && config.isString(name + ".Open") && config.getConfigurationSection(name).getString("Open").equals("True");
    }

    public boolean isServerShop() {

        try {
            if (config != null) {
                if (config.isString(name + ".Server")) {
                    return config.getConfigurationSection(name).getString("Server").equals("True");
                } else {
                    setServerShop(false);
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            setServerShop(false);
            return false;
        }
    }

    public boolean isNPCShop() {

        try {
            if (config != null) {
                if (config.isString(name + ".NPC")) {
                    return config.getConfigurationSection(name).getString("NPC").equals("True");
                } else {
                    setNPCShop(false);
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            setNPCShop(false);
            return false;
        }
    }

    public boolean isNotify() {

        return config != null && config.isString(name + ".Notify") && config.getConfigurationSection(name).getString("Notify").equals("True");

    }

    public Location getLocation() {
        return l;
    }

    public OfflinePlayer getOwner() {
        return p;
    }

    public boolean alreadyBeingSold(ItemStack item, boolean sell) {

        boolean exists = false;

        int a = item.getAmount();

        item.setAmount(1);

        if (item.getType() != Material.SKULL_ITEM) {

            ItemMeta meta = item.getItemMeta();
            List<String> lore = getLore(item, sell);


            meta.setLore(lore);
            item.setItemMeta(meta);
        } else {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            List<String> lore = getLore(item, sell);


            meta.setLore(lore);
            item.setItemMeta(meta);
        }


        if (getShopContents(sell).keySet().contains(item)) {
            exists = true;
        }


        for (ItemStack it : getShopContents(sell).keySet()) {
            ItemMeta meta = it.getItemMeta();
            meta.setLore(getLore(it,sell));
            it.setItemMeta(meta);
            if (item.equals(it) || item.toString().equals(it.toString()) && item.getData().getData() == it.getData().getData() && item.getDurability() == it.getDurability()) {
                exists = true;
            }

//            if (item.hasItemMeta()){
//                if (item.getItemMeta().getDisplayName() != null){
//                    if (item.getItemMeta().getLore() != null){
//                        if (it.getType() == item.getType() && item.getAmount())
//                    }
//                }
//            }
        }



        if (!exists) {
            int amt = item.getAmount();
            item.setAmount(1);

            if (getShopContents(sell).containsKey(item)) {
                exists = true;
            }
            for (ItemStack it : getShopContents(sell).keySet()) {
                int am = it.getAmount();
                it.setAmount(1);
                if (item.equals(it)) {
                    exists = true;
                }

                if (!item.toString().equals(it.toString())) {
                    exists = false;
                }

                it.setAmount(am);
            }


            item.setAmount(amt);
        }

        for (ItemStack it : getShopContents(sell).keySet()) {
            int am = it.getAmount();
            it.setAmount(1);

            if (!item.toString().equals(it.toString())) {
                exists = false;
            }

            if (item.equals(it) || item.toString().equals(it.toString())) {
                if (it.getData().getData() != item.getData().getData()) {
                    exists = false;

                } else {

                    if (it.getDurability() != item.getDurability()) {
                        exists = false;

                    } else {
                        exists = true;
                        break;
                    }
                    break;
                }


            }

            it.setAmount(am);
        }

        item.setAmount(a);

        return exists;
    }

    public void clearTransactions(){
        if (config.getConfigurationSection(name).isConfigurationSection("Transactions")){
            config.getConfigurationSection(name).set("Transactions",null);
        } else {
            config.getConfigurationSection(name).createSection("Transactions");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTransactions(){
        if (config.getConfigurationSection(name).isConfigurationSection("Transactions")){

            if (history != null) {
                history.clearAllTransactions();
            }


            for (String s : config.getConfigurationSection(name).getConfigurationSection("Transactions").getKeys(false)){

                Date d = new Date(config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getLong("Date"));
                OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getString("Player")));
                ItemStack item = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getItemStack("Item");
                double price = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getDouble("Price");
                int amount = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getInt("Amount");
                boolean sell = config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection(s).getBoolean("Selling");


                history.addTransaction(p,d,item,price,amount,sell,false);

            }

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            config.getConfigurationSection(name).createSection("Transactions");

        }

        transLoaded = true;
    }

    public void saveTransaction(Transaction t, boolean save){

        if (!config.getConfigurationSection(name).isConfigurationSection("Transactions")){
            config.getConfigurationSection(name).createSection("Transactions");
        }

        int amt = config.getConfigurationSection(name).getConfigurationSection("Transactions").getKeys(false).size();

        config.getConfigurationSection(name).getConfigurationSection("Transactions").createSection("" + (amt + 1));

        config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection("" + (amt + 1)).set("Date",t.getDate().getTime());
        config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection("" + (amt + 1)).set("Player",t.getPlayer().getUniqueId().toString());
        config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection("" + (amt + 1)).set("Item",t.getItem());
        config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection("" + (amt + 1)).set("Price",t.getPrice());
        config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection("" + (amt + 1)).set("Amount",t.getAmount());
        config.getConfigurationSection(name).getConfigurationSection("Transactions").getConfigurationSection("" + (amt + 1)).set("Selling",t.isSell());

        if (save) {
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addItem(ItemStack item, Inventory inv, boolean sell) {

        if (!sell) {
            if (!config.getConfigurationSection(name).isConfigurationSection("Contents")) {
                config.getConfigurationSection(name).createSection("Contents");
            }
        } else {
            if (!config.getConfigurationSection(name).isConfigurationSection("Sell")) {
                config.getConfigurationSection(name).createSection("Sell");
            }
        }

        if (alreadyBeingSold(item, sell)) {

            if (getOwner().isOnline()) {
                getOwner().getPlayer().sendMessage(Messages.getPrefix() + "§aYou can add more of an item by going into the item settings!");
            }

            int amt1 = item.getAmount();

            item.setAmount(1);

            if (!sell) {

                int amt = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock");

                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Stock", amt + 1);
            }

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            item.setAmount(amt1);
        } else {

            int amt = item.getAmount();

            item.setAmount(1);

            if (!sell) {

                config.getConfigurationSection(name).getConfigurationSection("Contents").createSection(inv.firstEmpty() + "");
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("ItemStack", item);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("Name", item.getItemMeta().getDisplayName());

                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("Stock", 1);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("Price", 1);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("Amount", 1);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("Infinite", "False");
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(inv.firstEmpty() + "").set("Slot", inv.firstEmpty());
            } else {
                config.getConfigurationSection(name).getConfigurationSection("Sell").createSection(inv.firstEmpty() + "");
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(inv.firstEmpty() + "").set("ItemStack", item);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(inv.firstEmpty() + "").set("Name", item.getItemMeta().getDisplayName());

                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(inv.firstEmpty() + "").set("Stock", 0);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(inv.firstEmpty() + "").set("Price", 1);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(inv.firstEmpty() + "").set("Amount", 1);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(inv.firstEmpty() + "").set("Slot", inv.firstEmpty());
            }

            item.setAmount(amt);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    public void addItem(ItemStack item, int slot, boolean sell) {

        if (item != null && item.getType() != Material.AIR) {

            if (!sell) {
                if (!config.getConfigurationSection(name).isConfigurationSection("Contents")) {
                    config.getConfigurationSection(name).createSection("Contents");
                }
            } else {
                if (!config.getConfigurationSection(name).isConfigurationSection("Sell")) {
                    config.getConfigurationSection(name).createSection("Sell");
                }
            }

            if (alreadyBeingSold(item, sell)) {

                if (getOwner().isOnline()) {
                    getOwner().getPlayer().sendMessage(Messages.getPrefix() + "§aYou can add more of an item by going into the item settings!");
                }

                int amt1 = item.getAmount();

                item.setAmount(1);

                if (!sell) {

                    int amt = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock");

                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Stock", amt + 1);

                }

                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                item.setAmount(amt1);
            } else {

                int amt = item.getAmount();

                item.setAmount(1);

                if (!sell) {

                    config.getConfigurationSection(name).getConfigurationSection("Contents").createSection(slot + "");
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("ItemStack", item);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("Name", item.getItemMeta().getDisplayName());

                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("Stock", 1);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("Price", 1);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("Amount", 1);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("Infinite", "False");
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").set("Slot", slot);
                } else {
                    config.getConfigurationSection(name).getConfigurationSection("Sell").createSection(slot + "");
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").set("ItemStack", item);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").set("Name", item.getItemMeta().getDisplayName());

                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").set("Stock", 0);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").set("Price", 1);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").set("Amount", 1);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").set("Slot", slot);
                }


                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                item.setAmount(amt);
            }
        }
    }

    public void setOpen(boolean open) {
        if (open) {
            config.set(name + ".Open", "True");
        } else {
            config.set(name + ".Open", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setServerShop(boolean open) {
        if (open) {
            config.set(name + ".Server", "True");
        } else {
            config.set(name + ".Server", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNPCShop(boolean open) {
        if (open) {
            config.set(name + ".NPC", "True");
        } else {
            config.set(name + ".NPC", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNotification(boolean notify) {
        if (notify) {
            config.set(name + ".Notify", "True");
        } else {
            config.set(name + ".Notify", "False");
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrice(ItemStack item, double amt, boolean sell) {

        if (!sell) {
            config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Price", amt);
        } else {
            config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").set("Price", amt);
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setStock(ItemStack item, int amt, boolean sell) {

        if (!sell) {
            config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Stock", amt);
        } else {
            config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").set("Stock", amt);
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Deprecated
    public void setSlot(ItemStack item, int amt, boolean sell) {
        if (alreadyBeingSold(item, sell)) {
            if (!sell) {
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Slot", amt);
            } else {
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").set("Slot", amt);
            }
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setInfinite(ItemStack item, boolean in, boolean sell) {

        if (!sell) {
            if (in) {
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Infinite", "True");
            } else {
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Infinite", "False");
            }
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setAmount(ItemStack item, int amt, boolean sell) {
        if (!sell) {
            config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").set("Amount", amt);
        } else {
            config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").set("Amount", amt);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean compareItems(ItemStack item1, ItemStack item2, boolean sell) {
        boolean exists = false;


        if (item1 != null && item2 != null) {

            if (item1.getType() != Material.SKULL_ITEM && item2.getType() != Material.SKULL_ITEM && !item1.getType().name().contains("LEATHER_") && !item2.getType().name().contains("LEATHER_")) {

                ItemMeta meta = item1.getItemMeta();



                meta.setLore(getLore(item1,sell));
                item1.setItemMeta(meta);


                ItemMeta meta2 = item2.getItemMeta();



                meta2.setLore(getLore(item2,sell));
                item2.setItemMeta(meta2);

                int amt = item2.getAmount();

                item2.setAmount(1);

                if (item1.equals(item2) || item2.equals(item1)) {
                    exists = true;
                }

                item2.setAmount(amt);
            }

        }

        if (item1 != null && item2 != null) {

            if (item1.getType() == Material.SKULL_ITEM && item2.getType() == Material.SKULL_ITEM) {


                SkullMeta meta = (SkullMeta) item1.getItemMeta();



                meta.setLore(getLore(item1,sell));
                item1.setItemMeta(meta);


                SkullMeta meta2 = (SkullMeta) item2.getItemMeta();



                meta2.setLore(getLore(item2,sell));
                item2.setItemMeta(meta2);

                int amt = item2.getAmount();

                item2.setAmount(1);

                if (item1.equals(item2) || item2.equals(item1) || item1 == item2 || item1.toString().equals(item2.toString())) {
                    exists = true;
                }

                item2.setAmount(amt);
            }

        }

        if (item1 != null && item2 != null) {

            if (item1.getType().name().contains("LEATHER_") && item2.getType().name().contains("LEATHER_")) {

                LeatherArmorMeta meta = (LeatherArmorMeta) item1.getItemMeta();




                meta.setLore(getLore(item1,sell));
                item1.setItemMeta(meta);


                LeatherArmorMeta meta2 = (LeatherArmorMeta) item2.getItemMeta();



                meta2.setLore(getLore(item2,sell));
                item2.setItemMeta(meta2);

                int amt = item2.getAmount();

                item2.setAmount(1);

                if (item1.equals(item2) || item2.equals(item1)) {
                    exists = true;
                }

                item2.setAmount(amt);
            }

        }

        return exists;
    }

    public Double getPrice(ItemStack item, boolean sell) {
        double price = 1.00;
        if (item != null) {
            if (!sell) {
                if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").isDouble("Price")) {
                    price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getDouble("Price");
                } else {
                    price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Price");
                }
            } else {
                if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").isDouble("Price")) {
                    price = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getDouble("Price");
                } else {
                    price = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Price");
                }
            }
        }

        return price;
    }

    public Integer getHighestSlot(boolean sell) {
        int higest = 18;
        if (!sell) {

            for (String s : config.getConfigurationSection(name).getConfigurationSection("Contents").getKeys(false)) {
                int h = Integer.parseInt(s);

                if (h > higest) {
                    higest = h;
                }
            }

        } else {
            for (String s : config.getConfigurationSection(name).getConfigurationSection("Sell").getKeys(false)) {
                int h = Integer.parseInt(s);

                if (h > higest) {
                    higest = h;
                }
            }
        }
        return higest;
    }

    public List<String> getLore(ItemStack item, boolean sell) {

        List<String> lore = null;
        ItemMeta meta = item.getItemMeta();
        List<String> l = meta.getLore();


        if (l != null) {
            if (l.contains(MainGUI.getString("ManageItem"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 6; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(ItemTexts.getString("RemoveItemLore"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 5; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }



            if (l.contains(MainGUI.getString("SellItem"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 5; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(MainGUI.getString("SellOptions"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 6; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(MainGUI.getString("Stock")) && !l.contains(MainGUI.getString("ShopKeeperManage")) && !l.contains(MainGUI.getString("LeftClickToBuy")) && !l.contains(MainGUI.getString("AddToCart")) && !l.contains(MainGUI.getString("Amount")) && !l.contains(MainGUI.getString("Price"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 2; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }
            if (l.contains(MainGUI.getString("AddToCart")) && !l.contains(MainGUI.getString("ShopKeeperManage"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 7; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(MainGUI.getString("AddToCart")) && l.contains(MainGUI.getString("ShopKeeperManage"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 8; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }

            if (l.contains(MainGUI.getString("Arrange"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 3; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }
            if (l.contains(MainGUI.getString("Selected"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 3; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }
            if (l.contains(MainGUI.getString("LeftClickBuy"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 6; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }
            if (l.contains(MainGUI.getString("ShopKeeperManage"))) {
                int s = l.size();
                for (int i = s - 1; i > s - 7; i--) {
                    if (i > -1) {
                        l.remove(i);
                    } else break;
                }
            }
        }
        lore = l;

        return lore;
    }

    @Deprecated
    public Integer getSlot(ItemStack item, boolean sell) {
        int price = 18;
        if (alreadyBeingSold(item, sell)) {

            price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Slot");
        }
        return price;
    }

    public Integer getStock(ItemStack item, boolean sell) {
        int price = 1;

        if (item != null) {

            if (!sell) {
                if (config.getConfigurationSection(name).getConfigurationSection("Contents").isConfigurationSection(getSlotForItem(item, sell) + ""))
                    price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock");
            } else {

                if (config.getConfigurationSection(name).getConfigurationSection("Sell").isConfigurationSection(getSlotForItem(item, sell) + ""))
                    price = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock");
            }
        }
        return price;
    }

    public boolean isInfinite(ItemStack item, boolean sell) {
        if (alreadyBeingSold(item, sell)) {

            if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getString("Infinite").equals("True")) {
                return true;
            } else {
                return false;
            }
        } else {
            setInfinite(item, false, sell);
            return false;
        }

    }

    public Integer getAmount(ItemStack item, boolean sell) {
        int price = 1;

        if (!sell) {
            price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Amount");
        } else {
            price = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Amount");
        }


        return price;
    }

    public void exchangeItems(ItemStack item1, int slot1, ItemStack item2, int slot2, boolean sell) {
        if (!sell && item2 != null && item1 != null) {
            if (config.getConfigurationSection(name).getConfigurationSection("Contents").isConfigurationSection(slot2 + "")) {

                double price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").getDouble("Price");
                int amt = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").getInt("Amount");
                int stock = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").getInt("Stock");

                String inf = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").getString("Infinite");

                double price2 = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").getDouble("Price");
                int amt2 = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").getInt("Amount");
                int stock2 = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").getInt("Stock");

                String inf2 = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").getString("Infinite");


                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").set("ItemStack", item1);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").set("Stock", stock);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").set("Price", price);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").set("Amount", amt);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").set("Infinite", inf);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot2 + "").set("Slot", slot2);

                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").set("ItemStack", item2);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").set("Stock", stock2);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").set("Price", price2);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").set("Amount", amt2);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").set("Infinite", inf2);
                config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot1 + "").set("Slot", slot1);

                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (config.getConfigurationSection(name).getConfigurationSection("Sell").isConfigurationSection(slot2 + "")) {

                double price = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").getDouble("Price");
                int amt = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").getInt("Amount");
                int stock = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").getInt("Stock");

                String inf = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").getString("Infinite");

                double price2 = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").getDouble("Price");
                int amt2 = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").getInt("Amount");
                int stock2 = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").getInt("Stock");

                String inf2 = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").getString("Infinite");


                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").set("ItemStack", item1);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").set("Stock", stock);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").set("Price", price);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").set("Amount", amt);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").set("Infinite", inf);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot2 + "").set("Slot", slot2);

                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").set("ItemStack", item2);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").set("Stock", stock2);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").set("Price", price2);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").set("Amount", amt2);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").set("Infinite", inf2);
                config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot1 + "").set("Slot", slot1);

                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void changePlaces(ItemStack item, int newSlot, boolean sell) {

        if (!sell) {

            if (!config.getConfigurationSection(name).getConfigurationSection("Contents").isConfigurationSection(newSlot + "")) {

                if (config.getConfigurationSection(name).getConfigurationSection("Contents").isConfigurationSection(getSlotForItem(item, sell) + "")) {
                    double price = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getDouble("Price");
                    int amt = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Amount");
                    int stock = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock");

                    String inf = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getString("Infinite");

                    config.getConfigurationSection(name).getConfigurationSection("Contents").set(getSlotForItem(item, sell) + "", null);

                    config.getConfigurationSection(name).getConfigurationSection("Contents").createSection(newSlot + "");

                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(newSlot + "").set("ItemStack", item);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(newSlot + "").set("Stock", stock);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(newSlot + "").set("Price", price);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(newSlot + "").set("Amount", amt);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(newSlot + "").set("Infinite", inf);
                    config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(newSlot + "").set("Slot", newSlot);

                    try {
                        config.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            if (!config.getConfigurationSection(name).getConfigurationSection("Sell").isConfigurationSection(newSlot + "")) {
                if (config.getConfigurationSection(name).getConfigurationSection("Sell").isConfigurationSection(getSlotForItem(item, sell) + "")) {

                    double price = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getDouble("Price");
                    int amt = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Amount");
                    int stock = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock");

                    String inf = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getString("Infinite");

                    config.getConfigurationSection(name).getConfigurationSection("Sell").set(getSlotForItem(item, sell) + "", null);

                    config.getConfigurationSection(name).getConfigurationSection("Sell").createSection(newSlot + "");

                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(newSlot + "").set("ItemStack", item);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(newSlot + "").set("Stock", stock);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(newSlot + "").set("Price", price);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(newSlot + "").set("Amount", amt);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(newSlot + "").set("Infinite", inf);
                    config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(newSlot + "").set("Slot", newSlot);

                    try {
                        config.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void deleteItem(ItemStack item, boolean sell) {

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();



        meta.setLore(getLore(item,sell));
        item.setItemMeta(meta);

        int slot;

        if (!sell) {

            slot = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Slot");

            config.getConfigurationSection(name).getConfigurationSection("Contents").set(slot + "", null);
        } else {
            slot = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Slot");

            config.getConfigurationSection(name).getConfigurationSection("Sell").set(slot + "", null);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {


            for (int i = 18; i < 162; i++) {

                if (i > slot && i < 54) {

                    ItemStack it = getItemForSlot(i, sell);


                    if (it != null) {
                        changePlaces(it, i - 1, sell);
                    }
                }
                if (i > slot && i >= 72 && i < 108) {
                    ItemStack it = getItemForSlot(i, sell);

                    if (it != null) {

                        if (i == 72) {
                            changePlaces(it, i - 19, sell);
                        } else {
                            changePlaces(it, i - 1, sell);
                        }

                    }
                }


                if (i > slot && i >= 126 && i < 162) {
                    ItemStack it = getItemForSlot(i, sell);

                    if (it != null) {
                        if (i == 126) {
                            changePlaces(it, i - 19, sell);
                        } else {
                            changePlaces(it, i - 1, sell);
                        }
                    }
                }
            }
        }


    }

    public void removeItem(ItemStack item, int amount, boolean sell) {

        ItemMeta meta = item.getItemMeta();



        meta.setLore(getLore(item,sell));
        item.setItemMeta(meta);

        if (alreadyBeingSold(item, sell)) {

            if (!sell) {

                if (!isInfinite(item, sell)) {

                    if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock") > amount) {

                        setStock(item, getStock(item, sell) - amount, sell);

                    } else if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock") == amount) {
                        deleteItem(item, sell);
                    }
                }
            } else {
                if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock") > amount) {

                    setStock(item, getStock(item, sell) - amount, sell);

                } else if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(getSlotForItem(item, sell) + "").getInt("Stock") == amount) {
                    deleteItem(item, sell);
                }
            }
        }
    }


    public HashMap<ItemStack, Integer> getShopContents(boolean sell) {
        HashMap<ItemStack, Integer> items = new HashMap<ItemStack, Integer>();

        if (!sell) {


            if (!config.getConfigurationSection(name).isConfigurationSection("Contents")) {
                config.getConfigurationSection(name).createSection("Contents");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (String s : config.getConfigurationSection(name).getConfigurationSection("Contents").getKeys(false)) {


                if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).isItemStack("ItemStack")) {
                    ItemStack item = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).getItemStack("ItemStack");
                    int amt = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).getInt("Slot");
                    items.put(item, amt);

                }
            }

        } else {

            if (!config.getConfigurationSection(name).isConfigurationSection("Sell")) {
                config.getConfigurationSection(name).createSection("Sell");
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (String s : config.getConfigurationSection(name).getConfigurationSection("Sell").getKeys(false)) {


                if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).isItemStack("ItemStack")) {
                    ItemStack item = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).getItemStack("ItemStack");
                    int amt = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).getInt("Slot");
                    items.put(item, amt);
                }
            }
        }


        return items;
    }

    public Integer getSlotForItem(ItemStack item, boolean sell) {
        int slot = 18;
        if (item != null) {
            if (item.getType() != Material.SKULL_ITEM) {
                ItemMeta meta = item.getItemMeta();

                List<String> lore = getLore(item, sell);

                meta.setLore(lore);
                item.setItemMeta(meta);
            } else {
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                List<String> lore = getLore(item, sell);
                meta.setLore(lore);
                item.setItemMeta(meta);
            }


            if (!sell) {
                for (String s : config.getConfigurationSection(name).getConfigurationSection("Contents").getKeys(false)) {
                    if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).isItemStack("ItemStack")) {
                        ItemStack it = config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(s).getItemStack("ItemStack");

                        if (it != null) {
                            if (it.getType() != Material.SKULL_ITEM) {
                                ItemMeta meta = it.getItemMeta();

                                List<String> lore = getLore(it, sell);

                                meta.setLore(lore);
                                it.setItemMeta(meta);
                            } else {
                                SkullMeta meta = (SkullMeta) it.getItemMeta();
                                List<String> lore = getLore(it, sell);
                                meta.setLore(lore);
                                it.setItemMeta(meta);
                            }

                            int am = item.getAmount();
                            int at = it.getAmount();

                            item.setAmount(1);
                            it.setAmount(1);

                            if (item.equals(it) || item.toString().equals(it.toString()) && item.getData().getData() == it.getData().getData() && item.getDurability() == it.getDurability()) {
                                slot = Integer.parseInt(s);
                            }

                            item.setAmount(am);
                            it.setAmount(at);
                        }
                    }
                }


            } else {
                for (String s : config.getConfigurationSection(name).getConfigurationSection("Sell").getKeys(false)) {
                    if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).isItemStack("ItemStack")) {
                        ItemStack it = config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(s).getItemStack("ItemStack");

                        if (it != null) {

                            if (it.getType() != Material.SKULL_ITEM) {
                                ItemMeta meta = it.getItemMeta();

                                List<String> lore = getLore(it, sell);

                                meta.setLore(lore);
                                it.setItemMeta(meta);
                            } else {
                                SkullMeta meta = (SkullMeta) it.getItemMeta();
                                List<String> lore = getLore(it, sell);
                                meta.setLore(lore);
                                it.setItemMeta(meta);
                            }

                            int am = item.getAmount();
                            int at = it.getAmount();

                            item.setAmount(1);
                            it.setAmount(1);

                            if (item.equals(it) || item.toString().equals(it.toString()) && item.getData().getData() == it.getData().getData() && item.getDurability() == it.getDurability()) {
                                slot = Integer.parseInt(s);
                            }

                            item.setAmount(am);
                            it.setAmount(at);
                        }
                    }
                }
            }
        }
        return slot;
    }


    public ItemStack getItemForSlot(int slot, boolean sell) {

        if (!sell) {

            if (config.getConfigurationSection(name).getConfigurationSection("Contents").isConfigurationSection(slot + "")) {

                if (config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").isItemStack("ItemStack")) {
                    return config.getConfigurationSection(name).getConfigurationSection("Contents").getConfigurationSection(slot + "").getItemStack("ItemStack");
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            if (config.getConfigurationSection(name).getConfigurationSection("Sell").isConfigurationSection(slot + "")) {
                if (config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").isItemStack("ItemStack")) {
                    return config.getConfigurationSection(name).getConfigurationSection("Sell").getConfigurationSection(slot + "").getItemStack("ItemStack");
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }


    public static boolean isShopOwner(Player pl, Location chestLocation) {
        boolean owner = false;

        File file = new File(Core.getCore().getDataFolder(), "Shops");

        if (file != null) {

            for (File f : file.listFiles()) {
                if (f.getName().contains(".yml")) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                    for (String s : config.getKeys(true)) {
                        if (s.contains("Location")) {
                            String c = config.getString(s);

                            String[] locs = c.split(" ");

                            World w = Bukkit.getWorld(locs[0]);

                            double x = Double.parseDouble(locs[1]);
                            double y = Double.parseDouble(locs[2]);
                            double z = Double.parseDouble(locs[3]);

                            if (chestLocation.getWorld().getName().equals(w.getName())) {
                                if (chestLocation.getX() == x && chestLocation.getY() == y && chestLocation.getZ() == z) {

                                    String n = s.substring(0, s.length() - 9);


                                    Player p = Bukkit.getPlayer(UUID.fromString(config.getString(n + ".Owner")));

                                    if (p == pl) {
                                        owner = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return owner;
    }

    public static boolean isShopOwner(Player pl, String name) {
        boolean owner = false;

        File file = new File(Core.getCore().getDataFolder(), "Shops");

        if (file != null) {

            for (File f : file.listFiles()) {
                if (f.getName().contains(".yml")) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

                    for (String s : config.getKeys(true)) {
                        if (s.contains("Location")) {
                            String c = config.getString(s);

                            String[] locs = c.split(" ");

                            World w = Bukkit.getWorld(locs[0]);

                            double x = Double.parseDouble(locs[1]);
                            double y = Double.parseDouble(locs[2]);
                            double z = Double.parseDouble(locs[3]);

                            if (isShopOwner(pl, new Location(w, x, y, z))) {

                                String n = s.substring(0, s.length() - 9);

                                Player p = Bukkit.getPlayer(UUID.fromString(config.getString(n + ".Owner")));

                                if (p == pl) {
                                    if (n.equals(name)) {
                                        owner = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return owner;
    }
}
