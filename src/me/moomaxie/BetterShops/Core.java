package me.moomaxie.BetterShops;

import BetterShops.Dev.API.Events.ShopDeleteEvent;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import me.moomaxie.BetterShops.Configurations.*;
import me.moomaxie.BetterShops.Configurations.ConfigMenu.ConfigMenu;
import me.moomaxie.BetterShops.Configurations.ConfigMenu.ConfigMenuListener;
import me.moomaxie.BetterShops.Configurations.ConfigMenu.NPCChooser;
import me.moomaxie.BetterShops.Configurations.GUIMessages.*;
import me.moomaxie.BetterShops.Configurations.Permissions.Permissions;
import me.moomaxie.BetterShops.History.HistoryGUI;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.BuyItem;
import me.moomaxie.BetterShops.Listeners.BuyerOptions.OpenShop;
import me.moomaxie.BetterShops.Listeners.Checkout.AmountChooser;
import me.moomaxie.BetterShops.Listeners.Checkout.CheckoutMenu;
import me.moomaxie.BetterShops.Listeners.LayoutArrangement.ShopRearranger;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.*;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.ShopKeeperManager;
import me.moomaxie.BetterShops.Listeners.ManagerOptions.ShopSettings;
import me.moomaxie.BetterShops.Listeners.Misc.ChatMessages;
import me.moomaxie.BetterShops.Listeners.Misc.OpeningChests;
import me.moomaxie.BetterShops.Listeners.OpenShopOptions;
import me.moomaxie.BetterShops.Listeners.OwnerSellingOptions.AddSellingItem;
import me.moomaxie.BetterShops.Listeners.OwnerSellingOptions.OpenSellingOptions;
import me.moomaxie.BetterShops.Listeners.OwnerSellingOptions.SellingItemManager;
import me.moomaxie.BetterShops.Listeners.SearchEngine.OpenEngine;
import me.moomaxie.BetterShops.Listeners.SellerOptions.SellItem;
import me.moomaxie.BetterShops.Listeners.ShopDelete;
import me.moomaxie.BetterShops.Metrics.Metrics;
import me.moomaxie.BetterShops.NPC.*;
import me.moomaxie.BetterShops.NPC.Listeners.HurtNPC;
import me.moomaxie.BetterShops.NPC.Listeners.OpenNPCShop;
import me.moomaxie.BetterShops.Shops.Shop;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class Core extends JavaPlugin {


    //TODO: Manager Admins - maybe
    //TODO: Villager/skeleton/sizes/ types - 1.8 animals - 1.6.0
    //TODO: Add more types of shops: Holographic, Sign, Floating Item - 1.6.0
    //TODO: Shop connections - maybe
    //TODO: Claiming un-owned shops - maybe
    //TODO: Messages/Language Files changing and saving
    //TODO: Change Chat messages in-game

    //Change WG message when changing versions

    //Removed API


    private static Core instance;
    public static Metrics metrics;
    private static Economy economy;
    private static AnvilGUI gui;
    private static TitleManager manager;
    private static boolean aboveEight = false;
    private static boolean wg = false;

    //Beta versions
    private static boolean beta = true;


    @Override
    public void onDisable() {
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {

        //Test for Vault
        if (getVault() != null) {

            try {


                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aEnabling BetterShops §e" + getDescription().getVersion());

                instance = this;

                //Register Listeners
                Bukkit.getPluginManager().registerEvents(new CheckoutMenu(), this);
                Bukkit.getPluginManager().registerEvents(new AmountChooser(), this);
                Bukkit.getPluginManager().registerEvents(new ShopKeeperManager(), this);
                Bukkit.getPluginManager().registerEvents(new AddItemManager(), this);
                Bukkit.getPluginManager().registerEvents(new ItemManager(), this);
                Bukkit.getPluginManager().registerEvents(new NPCMenu(), this);
                Bukkit.getPluginManager().registerEvents(new HistoryGUI(), this);
                Bukkit.getPluginManager().registerEvents(new NPCChooser(), this);
                Bukkit.getPluginManager().registerEvents(new ShopSettings(), this);
                Bukkit.getPluginManager().registerEvents(new GUIMessageListener(), this);
                Bukkit.getPluginManager().registerEvents(new LanguageInventory(), this);
                Bukkit.getPluginManager().registerEvents(new ConfigMenuListener(), this);
                Bukkit.getPluginManager().registerEvents(new OpenShopOptions(), this);
                //Random break in the action
                Bukkit.getPluginManager().registerEvents(new ShopDelete(), this);
                Bukkit.getPluginManager().registerEvents(new OpenShop(), this);
                Bukkit.getPluginManager().registerEvents(new BuyItem(), this);
                Bukkit.getPluginManager().registerEvents(new OwnerPages(), this);
                Bukkit.getPluginManager().registerEvents(new OpenSellingOptions(), this);
                Bukkit.getPluginManager().registerEvents(new AddSellingItem(), this);
                Bukkit.getPluginManager().registerEvents(new SellingItemManager(), this);
                Bukkit.getPluginManager().registerEvents(new SellItem(), this);
                Bukkit.getPluginManager().registerEvents(new OpenEngine(), this);
                Bukkit.getPluginManager().registerEvents(new OpeningChests(), this);
                Bukkit.getPluginManager().registerEvents(new OpenNPCShop(), this);
                Bukkit.getPluginManager().registerEvents(new HurtNPC(), this);
                Bukkit.getPluginManager().registerEvents(new ChatMessages(), this);
                Bukkit.getPluginManager().registerEvents(new ShopRearranger(), this);

                //Find Vault's Economy Hook
                setupEconomy();

                //Switch Config/language files to current version
                Config.changeConfig();
                Messages.changeMessages();
                BuyingAndSelling.changeBuyingAndSellingConfig();
                Checkout.changeCheckoutConfig();
                ItemTexts.changeItemTextsConfig();
                MainGUI.changeMainGUIConfig();
                SearchEngine.changeSearchEngineConfig();
                me.moomaxie.BetterShops.Configurations.GUIMessages.ShopKeeperManager.changeShopKeeperManagerConfig();
                me.moomaxie.BetterShops.Configurations.GUIMessages.ShopSettings.changeShopSettingsConfig();
                History.changeHistoryConfig();

                //Create "Shops" file
                boolean found2 = false;

                if (this.getDataFolder() != null) {

                    if (this.getDataFolder().listFiles() == null) {
                        this.getDataFolder().mkdir();
                    }
                    for (File f : this.getDataFolder().listFiles()) {
                        if (f.getName().equals("Shops")) {
                            found2 = true;
                        }
                    }
                }

                if (!found2) {

                    File fil = new File(this.getDataFolder().getParent(), "BetterShops.jar");

                    java.util.jar.JarFile jar = null;
                    try {
                        jar = new java.util.jar.JarFile(fil);
                    } catch (IOException e) {
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cImproper Jar Name, Rename the .Jar to 'BetterShops.jar'. Plugin Disabling!");
                        this.setEnabled(false);
                    }
                    java.util.Enumeration enumEntries = jar.entries();
                    while (enumEntries.hasMoreElements()) {
                        java.util.jar.JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
                        if (file.getName().equals("Shops")) {
                            java.io.File f = new java.io.File(this.getDataFolder() + java.io.File.separator + file.getName());
                            if (file.isDirectory()) { // if its a directory, create it
                                f.mkdir();
                                continue;
                            }
                            try {
                                java.io.InputStream is = jar.getInputStream(file); // get the input stream
                                java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
                                while (is.available() > 0) {  // write contents of 'is' to 'fos'
                                    fos.write(is.read());
                                }
                                fos.close();
                                is.close();
                            } catch (Exception e) {

                            }
                        }
                    }
                }

                //Get Bukkit Version

                String packageName = this.getServer().getClass().getPackage().getName();
                // Get full package string of CraftServer.
                // org.bukkit.craftbukkit.version
                String version = packageName.substring(packageName.lastIndexOf('.') + 1);
                // Get the last element of the package

                if (Integer.parseInt(version.substring(3, version.length() - 3)) >= 8) {
                    aboveEight = true;
                }


                //Choose Anvil GUI for Version
                try {
                    final Class<?> clazz = Class.forName("BetterShops.Versions." + version + ".AnvilGUI");
                    // Check if we have a NMSHandler class at that location.
                    if (clazz != null) {
                        if (AnvilGUI.class.isAssignableFrom(clazz)) { // Make sure it actually implements NMS
                            gui = (AnvilGUI) clazz.getConstructor().newInstance(); // Set our handler
                        }
                    } else {
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cCould not find support for this CraftBukkit version. Currently Supports CB version 1.7.9 RO.3 - Spigot 1.8.R1, You are using §d" + version + "§c. Plugin Disabling!");
                        this.setEnabled(false);
                        return;
                    }
                } catch (final Exception e) {
//                e.printStackTrace();
                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cCould not find support for this CraftBukkit version. Currently Supports CB version 1.7.9 RO.3 - Spigot 1.8.R1, You are using §d" + version + "§c. Plugin Disabling!");
                    this.setEnabled(false);
                    return;
                }

                //Choose Title Manager for Version
                try {
                    final Class<?> clazz = Class.forName("BetterShops.Versions." + version + ".TitleManager");
                    // Check if we have a NMSHandler class at that location.

                    if (clazz != null) {
                        if (TitleManager.class.isAssignableFrom(clazz)) { // Make sure it actually implements NMS
                            manager = (TitleManager) clazz.getConstructor().newInstance(); // Set our handler
                        }
                    } else {
                        aboveEight = false;

                        return;
                    }

                } catch (final Exception e) {
//                e.printStackTrace();


                }


                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aLoading support for CraftBukkit §e" + version.replaceAll("_", "."));

                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aLoaded §d" + ShopLimits.loadShops() + " §aShops");


                //Register WorldGuard
                if (getWorldGuard() != null) {
                    if (getWorldGuard().getDescription().getVersion().startsWith("\"6") || getWorldGuard().getDescription().getVersion().startsWith("6")) {
                        wg = true;
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aLoading support for §eWorldGuard");
                    } else {
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eWorldGuard §ais detected, but the version §cmust be 5.9 or Lower§a. Current version is not supported.");
                        wg = false;
                    }
                }

                //Load The Shops
                for (Shop shop : ShopLimits.getAllShops()) {
                    if (shop.isNPCShop()) {
                        for (LivingEntity e : shop.getLocation().getWorld().getLivingEntities()) {


                            if (e.getCustomName() != null) {
                                if (e.getCustomName().equals("§a§l" + shop.getName())) {
                                    e.remove();

                                    if (Core.useWorldGuard()) {
                                        CreateNPCWG.createNPC(e.getType(), shop);
                                    } else {
                                        CreateNPC.createNPC(e.getType(), shop);
                                    }

                                }
                            }
                        }
                    }
                }

                //Start NPC Return Policy
                ReturnNPC.startReturnNPC();

                //Register WG Listeners
                if (useWorldGuard()) {
                    Bukkit.getPluginManager().registerEvents(new me.moomaxie.BetterShops.Listeners.ShopCreateWG(), this);
                } else {
                    Bukkit.getPluginManager().registerEvents(new me.moomaxie.BetterShops.Listeners.ShopCreate(), this);
                }

                //Use Metrics
                if (Config.useMetrics()) {
                    metrics = new Metrics(this);
                    setUpMetrics();
                    metrics.start();
                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aUsing §eMetrics §7- http://mcstats.org");
                }

            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cAn error occurred! §cPlease inform the developer @ §ehttp://dev.bukkit.org/bukkit-plugins/better-shops/ §c. Plugin Disabling!");
                e.printStackTrace();
                this.setEnabled(false);
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cI do Not see §dVault §cIn your plugins folder. This means that §bBetter Shops §cis Not able to Function and will now §dDisable");
            this.setEnabled(false);
        }

    }

    //Find Vault Economy Hook
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    //Get Core instance
    public static Core getCore() {
        return instance;
    }

    //Get Vault Economy
    public static Economy getEconomy() {
        return economy;
    }

    //Get Anvil GUI
    public static AnvilGUI getAnvilGUI() {
        return gui;
    }

    //Get Title Manager
    public static TitleManager getTitleManager() {
        return manager;
    }

    //Test if 1.8 is being used
    public static boolean isAboveEight() {
        return aboveEight;
    }

    //Get Metrics
    public static Metrics getMetrics() {
        return metrics;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //Main BetterShops Command
        if (label.equalsIgnoreCase("bs")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {

                    if (args[0].equalsIgnoreCase("info")) {

                        p.sendMessage(Messages.getPrefix() + "You are running version: §e" + getDescription().getVersion());

                        p.sendMessage(Messages.getPrefix() + "Total Shops: §e" + ShopLimits.getAllShops().size());
                        p.sendMessage(Messages.getPrefix() + "Total NPC Shops: §e" + NPCs.getNPCs().size());
                    } else if (args[0].equalsIgnoreCase("config")) {
                        if (Permissions.hasConfigGUIPerm(p)) {
                            ConfigMenu.openConfigMenu(null, p);
                        } else {
                            p.sendMessage(Messages.getPrefix() + Messages.getNoPermission());
                        }
                    } else if (args[0].equalsIgnoreCase("language")) {
                        if (Permissions.hasLanguagePerm(p)) {
                            GUIMessagesInv.openGUIMessagesInv(p);
                        } else {
                            p.sendMessage(Messages.getPrefix() + Messages.getNoPermission());
                        }

                    } else {
                        p.sendMessage("§d<-Better Shops Help->");
                        p.sendMessage("    §a/bs info");
                        p.sendMessage("    §a/bs config");
                        p.sendMessage("    §a/bs language");
                        p.sendMessage("    §a/bs open <Shop>");
                        p.sendMessage("    §a/sremove <Shop>");
                        p.sendMessage("§d<-Better Shops Help->");
                    }
                } else if (args.length >= 2) {
                    if (args[0].equalsIgnoreCase("open")) {
                        if (Permissions.hasOpenCommandPerm(p)) {
                            String name = args[1];

                            for (int i = 2; i < args.length; i++) {
                                name = name + " " + args[i];
                            }

                            Shop shop = ShopLimits.fromString(p, name);

                            if (shop != null) {
                                if (shop.getOwner() != null) {
                                    if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                                        if (shop.isServerShop()) {
                                            OpenShop.openShopItems(null, p, shop, 1);
                                        } else {
                                            OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, 1);
                                        }
                                    } else {
                                        OpenShop.openShopItems(null, p, shop, 1);
                                    }
                                } else {
                                    p.sendMessage(Messages.getPrefix() + "§cThis shop doesn't exist!");
                                }
                            } else {
                                p.sendMessage(Messages.getPrefix() + "§cThis shop doesn't exist!");
                            }
                        } else {
                            p.sendMessage(Messages.getPrefix() + Messages.getNoPermission());
                        }
                    } else {
                        p.sendMessage("§d<-Better Shops Help->");
                        p.sendMessage("    §a/bs info");
                        p.sendMessage("    §a/bs config");
                        p.sendMessage("    §a/bs language");
                        p.sendMessage("    §a/bs open <Shop>");
                        p.sendMessage("    §a/sremove <Shop>");
                        p.sendMessage("§d<-Better Shops Help->");
                    }

                } else {
                    p.sendMessage("§d<-Better Shops Help->");
                    p.sendMessage("    §a/bs info");
                    p.sendMessage("    §a/bs config");
                    p.sendMessage("    §a/bs language");
                    p.sendMessage("    §a/bs open <Shop>");
                    p.sendMessage("    §a/sremove <Shop>");
                    p.sendMessage("§d<-Better Shops Help->");
                }
            }
            return true;
        }

        //Remove a Shop
        if (label.equalsIgnoreCase("sremove")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;

                if (Config.usePerms() && !Permissions.hasRemoveCommandPerm(p)) {
                    p.sendMessage(Messages.getPrefix() + Messages.getNoPermission());
                    return true;
                }

                if (args.length > 0) {
                    String name = args[0];

                    for (int i = 1; i < args.length; i++) {
                        name = name + " " + args[i];
                    }

                    Shop shop = ShopLimits.fromString(p, name);

                    if (shop != null) {

                        if (shop.getOwner() != null) {
                            if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) || p.isOp() || Config.usePerms() && Permissions.hasBreakPerm(p)) {


                                if (!shop.isNPCShop()) {
                                    File file = new File(this.getDataFolder(), "Shops/" + shop.getOwner().getUniqueId() + ".yml");

                                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


                                    String l = config.getConfigurationSection(name).getString("Location");

                                    if (l != null) {

                                        String[] locs = l.split(" ");

                                        World w = Bukkit.getWorld(locs[0]);

                                        double x = Double.parseDouble(locs[1]);
                                        double y = Double.parseDouble(locs[2]);
                                        double z = Double.parseDouble(locs[3]);

                                        Location loc = new Location(w, x, y, z);

                                        Block b = loc.getBlock();

                                        if (b.getState() instanceof Chest) {
                                            Chest chest = (Chest) b.getState();

                                            for (Chunk c : w.getLoadedChunks()) {
                                                for (BlockState bs : c.getTileEntities()) {
                                                    if (bs instanceof Sign) {
                                                        Sign sign = (Sign) bs;

                                                        Block face = sign.getBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());


                                                        if (face.getType() == Material.CHEST) {
                                                            if (face.getState() instanceof Chest) {
                                                                Chest ch = (Chest) face.getState();

                                                                if (ch.getLocation().equals(loc)) {
                                                                    sign.getBlock().setType(Material.AIR);
                                                                    sign.getWorld().dropItem(loc, new ItemStack(Material.SIGN));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            p.sendMessage(Messages.getPrefix() + Messages.getRemoveShop());

                                            ShopDeleteEvent e = new ShopDeleteEvent(shop);

                                            Bukkit.getPluginManager().callEvent(e);


                                            for (ItemStack item : shop.getShopContents(false).keySet()) {
                                                if (chest.getInventory().firstEmpty() >= 0) {
                                                    if (item.getType() != Material.AIR) {
                                                        item.setAmount(shop.getStock(item, false));
                                                        chest.getInventory().addItem(item);
                                                    }
                                                } else {
                                                    if (item.getType() != Material.AIR) {
                                                        item.setAmount(shop.getStock(item, false));

                                                        chest.getWorld().dropItem(chest.getLocation(), item);
                                                    }
                                                }
                                            }

                                            for (ItemStack item : shop.getShopContents(true).keySet()) {
                                                if (chest.getInventory().firstEmpty() >= 0) {
                                                    if (shop.getStock(item, true) > 0) {
                                                        if (item.getType() != Material.AIR) {
                                                            item.setAmount(shop.getStock(item, true));
                                                            chest.getInventory().addItem(item);
                                                        }
                                                    }
                                                } else {
                                                    if (shop.getStock(item, true) > 0) {
                                                        if (item.getType() != Material.AIR) {
                                                            item.setAmount(shop.getStock(item, true));
                                                            chest.getWorld().dropItem(chest.getLocation(), item);
                                                        }
                                                    }
                                                }
                                            }


                                            config.set(name, null);
                                            try {
                                                config.save(file);
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                            ShopLimits.loadShops();

                                            if (Core.isAboveEight() && Config.useTitles()) {


                                                Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                Core.getTitleManager().sendTitle(p, Messages.getRemoveShop());


                                            }
                                        } else {
                                            p.sendMessage(Messages.getPrefix() + "§4ERROR: §cShop is non-existant, please tell a server operator of this problem");
                                        }

                                    } else {
                                        p.sendMessage(Messages.getPrefix() + "§4ERROR: §cCannot find location of shop, please tell a server operator of this problem");
                                    }
                                } else {
                                    File file = new File(this.getDataFolder(), "Shops/" + shop.getOwner().getUniqueId() + ".yml");

                                    boolean can = false;

                                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                                    for (LivingEntity e : shop.getLocation().getWorld().getLivingEntities()) {


                                        if (e.getCustomName() != null && e.getCustomName().equals("§a§l" + shop.getName())) {

                                            for (ShopsNPC npc : NPCs.getNPCs()) {
                                                if (npc.getShop().getName().equals(shop.getName())) {
                                                    NPCs.removeNPC(npc);
                                                    e.remove();
                                                    for (ItemStack item : shop.getShopContents(false).keySet()) {

                                                        if (item.getType() != Material.AIR) {
                                                            item.setAmount(shop.getStock(item, false));

                                                            shop.getLocation().getWorld().dropItem(shop.getLocation(), item);
                                                        }

                                                    }

                                                    for (ItemStack item : shop.getShopContents(true).keySet()) {

                                                        if (item.getType() != Material.AIR) {
                                                            item.setAmount(shop.getStock(item, true));

                                                            shop.getLocation().getWorld().dropItem(shop.getLocation(), item);
                                                        }

                                                    }

                                                    p.sendMessage(Messages.getPrefix() + Messages.getRemoveShop());
                                                    config.set(name, null);
                                                    try {
                                                        config.save(file);
                                                    } catch (IOException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    ShopLimits.loadShops();

                                                    can = true;

                                                    if (Core.isAboveEight() && Config.useTitles()) {

                                                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                        Core.getTitleManager().sendTitle(p, Messages.getRemoveShop());


                                                    }
                                                    break;
                                                }

                                            }
                                        }
                                    }

                                    if (!can) {
                                        for (ItemStack item : shop.getShopContents(false).keySet()) {

                                            if (item.getType() != Material.AIR) {
                                                item.setAmount(shop.getStock(item, false));

                                                shop.getLocation().getWorld().dropItem(shop.getLocation(), item);
                                            }

                                        }

                                        for (ItemStack item : shop.getShopContents(true).keySet()) {

                                            if (item.getType() != Material.AIR) {
                                                item.setAmount(shop.getStock(item, true));

                                                shop.getLocation().getWorld().dropItem(shop.getLocation(), item);
                                            }

                                        }
                                        p.sendMessage(Messages.getPrefix() + Messages.getRemoveShop());
                                        config.set(name, null);
                                        try {
                                            config.save(file);
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        ShopLimits.loadShops();
                                        if (Core.isAboveEight() && Config.useTitles()) {

                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                            Core.getTitleManager().sendTitle(p, Messages.getRemoveShop());


                                        }

                                    }
                                }
                            } else {
                                p.sendMessage(Messages.getPrefix() + Messages.getDenyRemoveShop());
                            }
                        } else {
                            p.sendMessage(Messages.getPrefix() + "§cThis shop doesn't exist!");
                        }
                    } else {
                        p.sendMessage(Messages.getPrefix() + "§cThis shop doesn't exist!");
                    }

                } else {
                    p.sendMessage(Messages.getPrefix() + "§cUsage: §d/sremove <ShopName>");
                }
            } else {
                sender.sendMessage(Messages.getPrefix() + "Only Players Can Perform This Command!");
            }
            return true;
        }

        return false;
    }

    //Test if WG is enabled
    public static boolean useWorldGuard() {
        return wg;
    }

    //Get WG
    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

    //Get Vault
    private Plugin getVault() {
        Plugin plugin = getServer().getPluginManager().getPlugin("Vault");

        // WorldGuard may not be loaded
        if (plugin == null) {
            return null; // Maybe you want throw an exception instead
        }

        return plugin;
    }

    //Get Region Set
    public static ApplicableRegionSet getRegionSet(Location location) {
        if (getWorldGuard() != null) {
            if (getWorldGuard().getDescription().getVersion().startsWith("\"6") || getWorldGuard().getDescription().getVersion().startsWith("6")) {
                return getWorldGuard().getRegionManager(location.getWorld()).getApplicableRegions(location);
            } else {
                return getWorldGuard().getRegionManager(location.getWorld()).getApplicableRegions(location);
            }
        } else {
            return null;
        }
    }

    //Public version of get WG
    public WorldGuardPlugin getWorldGuardPlugin() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

    //Public version of get Vault
    public Plugin getVaultPlugin() {
        Plugin plugin = getServer().getPluginManager().getPlugin("Vault");

        // WorldGuard may not be loaded
        if (plugin == null) {
            return null; // Maybe you want throw an exception instead
        }
        return plugin;
    }

    //Sets up Metrics
    public void setUpMetrics() {
        Metrics.Graph shops = metrics.createGraph("Number of Shops per Server");

        shops.addPlotter(new Metrics.Plotter("" + ShopLimits.getAllShops().size()) {
            @Override
            public int getValue() {
                return 1;
            }
        });

        Metrics.Graph npcshops = metrics.createGraph("Number of NPC Shops per Server");

        npcshops.addPlotter(new Metrics.Plotter("" + NPCs.getNPCs().size()) {
            @Override
            public int getValue() {
                return 1;
            }
        });


        if (beta) {

            Metrics.Graph beta = metrics.createGraph("Beta users");

            beta.addPlotter(new Metrics.Plotter(getDescription().getVersion()) {
                @Override
                public int getValue() {
                    return 1;
                }
            });
        }

        if (useWorldGuard()) {
            Metrics.Graph wg = metrics.createGraph("WorldGuard users");

            wg.addPlotter(new Metrics.Plotter(Core.getWorldGuard().getDescription().getVersion()) {
                @Override
                public int getValue() {
                    return 1;
                }
            });

        }
    }
}
