package me.moomaxie.BetterShops;

import BetterShops.Dev.API.Events.ShopDeleteEvent;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import me.moomaxie.BetterShops.Configurations.*;
import me.moomaxie.BetterShops.Configurations.ConfigMenu.ConfigMenu;
import me.moomaxie.BetterShops.Configurations.ConfigMenu.ConfigMenuListener;
import me.moomaxie.BetterShops.Configurations.ConfigMenu.NPCChooser;
import me.moomaxie.BetterShops.Configurations.GUIMessages.*;
import me.moomaxie.BetterShops.Configurations.GUIMessages.NPCs;
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
import me.moomaxie.BetterShops.Listeners.SellerOptions.OpenSellShop;
import me.moomaxie.BetterShops.Listeners.SellerOptions.SellItem;
import me.moomaxie.BetterShops.Listeners.ShopDelete;
import me.moomaxie.BetterShops.Metrics.Metrics;
import me.moomaxie.BetterShops.MySQL.Database;
import me.moomaxie.BetterShops.ShopTypes.Holographic.CreateHologram;
import me.moomaxie.BetterShops.ShopTypes.Holographic.DeleteHoloShop;
import me.moomaxie.BetterShops.ShopTypes.Holographic.HologramManager;
import me.moomaxie.BetterShops.ShopTypes.NPC.Listeners.HurtNPC;
import me.moomaxie.BetterShops.ShopTypes.NPC.Listeners.OpenNPCShop;
import me.moomaxie.BetterShops.ShopTypes.NPC.*;
import me.moomaxie.BetterShops.Shops.Shop;
import me.moomaxie.BetterShops.Shops.ShopItem;
import me.moomaxie.BetterShops.SupplyandDemand.LiveEco;
import me.moomaxie.BetterShops.Updater.Updater;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
    //TODO: Add more types of shops: Sign - 1.7.0
    //TODO: Fix MySQL - 1.7.0
    //TODO: Claiming un-owned shops - maybe

    private static Core instance;
    public static Metrics metrics;
    private static Economy economy;
    private static AnvilGUI gui;
    private static TitleManager manager;
    private static boolean aboveEight = false;
    private static boolean wg = false;
    private static boolean holo = false;
    private static Database sql;
    private static java.sql.Connection c;
    private static boolean useSQL = false;

    //Beta versions
    private static boolean beta = false;


    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eSaving Shops..");
        for (Shop shop : ShopManager.getAllShops()){
            shop.saveConfig();
        }
        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eSaved!");
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {

        //Test for Vault
        if (getVault() != null) {

            try {
                if (setupEconomy()) {

                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aEnabling BetterShops §e" + getDescription().getVersion());

                    instance = this;

                    //Register Listeners
                    Bukkit.getPluginManager().registerEvents(new BuyItem(), this);
                    Bukkit.getPluginManager().registerEvents(new AmountChooser(), this);
                    Bukkit.getPluginManager().registerEvents(new CheckoutMenu(), this);
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
                    Bukkit.getPluginManager().registerEvents(new LiveEco(), this);
                    //Random break in the action
                    Bukkit.getPluginManager().registerEvents(new ShopDelete(), this);
                    Bukkit.getPluginManager().registerEvents(new OpenShop(), this);
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
                    Bukkit.getPluginManager().registerEvents(new Blacklist(), this);


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
                    NPCs.changeNPCsConfig();
                    LiveEconomy.changeLiveEconomyConfig();
                    Blacklist.changeBlacklist();

                    String packageName = this.getServer().getClass().getPackage().getName();

                    String v = this.getServer().getVersion();

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

                        boolean c = false;

                        if (v.contains("Spigot")) {
                            if (version.equals("v1_7_R4") || version.equals("v1_8_R1") || version.equals("v1_8_R2")) {
                                c = true;
                            }
                        }


                        if (c) {
                            final Class<?> clazz2 = Class.forName("BetterShops.Versions." + version + ".TitleManager");
                            // Check if we have a NMSHandler class at that location.

                            if (clazz2 != null) {
                                if (TitleManager.class.isAssignableFrom(clazz2)) { // Make sure it actually implements NMS
                                    manager = (TitleManager) clazz2.getConstructor().newInstance(); // Set our handler
                                }
                            } else {
                                aboveEight = false;

                                return;
                            }
                        }
                    } catch (final Exception e) {
//                e.printStackTrace();
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cCould not find support for this CraftBukkit version. Currently Supports CB version 1.7.9 RO.3 - Spigot 1.8.R1, You are using §d" + version + "§c. Plugin Disabling!");
                        this.setEnabled(false);
                        return;
                    }


                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aLoading support for CraftBukkit §e" + version.replaceAll("_", "."));


                    //Register WorldGuard
                    if (getWorldGuard() != null) {
                        if (getWorldGuard().getDescription().getVersion().startsWith("\"6") || getWorldGuard().getDescription().getVersion().startsWith("6")) {
                            wg = true;
                            Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aLoading support for §eWorldGuard");
                        } else {
                            Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §eWorldGuard §ais detected, but the version §cmust be 6.0 or Higher§a. Current version is not supported.");
                            wg = false;
                        }
                    }

                    //Register Holographic Displays
                    if (getHolographicDisplays() != null) {
                        holo = true;
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aLoading support for §eHolographic Displays");
                    }

                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aLoaded §d" + ShopManager.loadShops() + " §aShops");
                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aLoading §eNPC Shops§a...");
                    //Load The Shops
                    for (World w : Bukkit.getWorlds()) {
                        for (LivingEntity e : w.getLivingEntities()) {

                            if (e.getCustomName() != null) {
                                if (e.getCustomName().contains("§a§l")) {
                                    Shop shop = ShopManager.fromString(e.getCustomName().substring(4));
                                    if (shop != null) {
                                        if (!shop.isNPCShop() || shop.getNPCShop() == null) {
                                            ShopsNPC s = new ShopsNPC(e, shop);
                                            me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.addNPC(s);
                                            s.removeChest();
                                            shop.setNPCShop(true);
                                        } else {
                                            e.remove();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aLoaded §d" + me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.getNPCs().size() + " §aNPC Shops");

                    if (useHolograms()) {
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aLoading §eHologram Shops§a...");
                        for (NamedHologram holo : NamedHologramManager.getHolograms()) {

                            if (holo.getName().startsWith("BS")) {

                                Shop shop =ShopManager.fromString(holo.getName().substring(2).replaceAll("_", " "));

                                if (shop != null) {

                                    if (holo.getLine(0) instanceof TextLine && shop.isHoloShop()) {

                                        if (((TextLine) holo.getLine(0)).getText().equals("§a§l" + shop.getName())) {
                                            if (shop.getHolographicShop() == null) {
                                                holo.delete();
                                                CreateHologram.createHolographicShop(shop);
                                            } else {
                                                holo.delete();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aLoaded §d" + HologramManager.getHolographicShops().size() + " §aHologram Shops");
                    }


                    //Start NPC Return Policy
                    ReturnNPC.startReturnNPC();

                    //Register WG Listeners
                    if (useWorldGuard()) {
                        Bukkit.getPluginManager().registerEvents(new me.moomaxie.BetterShops.Listeners.ShopCreateWG(), this);
                    } else {
                        Bukkit.getPluginManager().registerEvents(new me.moomaxie.BetterShops.Listeners.ShopCreate(), this);
                    }
                    if (aboveEight) {
                        Bukkit.getPluginManager().registerEvents(new TypeSpecifier(), this);
                    } else {
                        Bukkit.getPluginManager().registerEvents(new TypeSpecifier7(), this);
                    }

                    //Use Metrics
                    if (Config.useMetrics()) {
                        metrics = new Metrics(this);
                        setUpMetrics();
                        metrics.start();
                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aUsing §eMetrics §7- http://mcstats.org");
                    }

//                if (Config.config.getBoolean("sql.use")) {
//
//                    String host = Config.config.getString("sql.host", "localhost");
//                    int port = Config.config.getInt("sql.port", 3306);
//                    String db = Config.config.getString("sql.database", "BetterShops");
//                    String user = Config.config.getString("sql.user", "root");
//                    String pass = Config.config.getString("sql.pass", "");
//
//                    sql = new MySQL(this, host, String.valueOf(port), db, user, pass);
//
//
//                    c = sql.openConnection();
//
//                    if (sql.checkConnection()) {
//                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aUsing §eMySQL §afor data storage");
//                        useSQL = true;
//                    } else {
//                        Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §aUsing §cCould not connect to SQL database");
//                    }
//                }

                    Updater.startCheckForUpdate();
                } else {
                    Bukkit.getConsoleSender().sendMessage("§bBetterShops§7 - §cNo Economy Plugin Detected. Plugin Disabling!");
                    this.setEnabled(false);
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

    //Get SQL
    public static Database getSQLDatabase() {
        return sql;
    }

    //Get SQLConnection
    public static Connection getSQLConnection() {
        return c;
    }

    //Get Vault Economy
    public static Economy getEconomy() {
        return economy;
    }

    //Get Anvil GUI
    public static AnvilGUI getAnvilGUI() {
        if (gui != null) {
            try {
                return gui.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return gui;
            }
        }
        return null;
    }

    //Get Title Manager
    public static TitleManager getTitleManager() {
        if (manager != null) {
            try {
                return manager.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return manager;
            }
        }
        return null;
    }

    //Test if 1.8 is being used
    public static boolean isAboveEight() {
        return aboveEight;
    }

    public static boolean useSQL() {
        return useSQL;
    }

    //Test if holograms are being used
    public static boolean useHolograms() {
        return holo;
    }

    //Get Metrics
    public static Metrics getMetrics() {
        return metrics;
    }

    public File getFile() {
        File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
        return file;
    }

    public String getJarName() {
        return getFile().getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //Main BetterShops Command
        if (label.equalsIgnoreCase("bs")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {

                    if (args[0].equalsIgnoreCase("info")) {

                        p.sendMessage(Messages.getString("Prefix") + "You are running version: §e" + getDescription().getVersion());

                        p.sendMessage(Messages.getString("Prefix") + "Total Shops: §e" + ShopManager.getAllShops().size());
                        p.sendMessage(Messages.getString("Prefix") + "Total NPC Shops: §e" + me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.getNPCs().size());
                        p.sendMessage(Messages.getString("Prefix") + "Total Holographic Shops: §e" + HologramManager.getHolographicShops().size());
                    } else if (args[0].equalsIgnoreCase("update")) {
                        if (Permissions.hasUpdatePerm(p)) {
                            Updater.parseFile();
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("Updated"));
                        } else {
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("NoPermission"));
                        }
                    } else if (args[0].equalsIgnoreCase("config")) {
                        if (Permissions.hasConfigGUIPerm(p)) {
                            ConfigMenu.openConfigMenu(null, p, 1);
                        } else {
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("NoPermission"));
                        }
                    } else if (args[0].equalsIgnoreCase("language")) {
                        if (Permissions.hasLanguagePerm(p)) {
                            GUIMessagesInv.openGUIMessagesInv(p);
                        } else {
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("NoPermission"));
                        }

                    } else if (args[0].equalsIgnoreCase("blacklist")) {
                        if (Permissions.hasBlacklistCommandPerm(p)) {
                            Blacklist.openBlacklistInventory(null, p, 1);
                        } else {
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("NoPermission"));
                        }

                    } else if (args[0].equalsIgnoreCase("list")) {
                        if (Permissions.hasListPerm(p)) {
                            List<Shop> shops = ShopManager.getAllShops();
                            p.sendMessage("§d<-Listing Shops (§c" + shops.size() + "§d)->");
                            for (int i = 1; i < shops.size() + 1; i++) {
                                if (shops.get(i - 1).isNPCShop() || shops.get(i - 1).getNPCShop() != null) {
                                    p.sendMessage("§c" + i + ". §a" + shops.get(i - 1).getName() + " §e(NPC)");
                                } else if (shops.get(i - 1).isHoloShop()) {
                                    p.sendMessage("§c" + i + ". §a" + shops.get(i - 1).getName() + " §e(Holo)");
                                } else {
                                    p.sendMessage("§c" + i + ". §a" + shops.get(i - 1).getName());
                                }
                            }
                            p.sendMessage("§d<-Listing Shops (§c" + shops.size() + "§d)->");
                        }
                    } else {
                        p.sendMessage("§d<-Better Shops Help->");
                        p.sendMessage("    §a/bs info");
                        p.sendMessage("    §a/bs update");
                        p.sendMessage("    §a/bs config");
                        p.sendMessage("    §a/bs language");
                        p.sendMessage("    §a/bs blacklist");
                        p.sendMessage("    §a/bs open <Shop>");
                        p.sendMessage("    §a/sremove <Shop>");
                        p.sendMessage("    §a/bs list <Player>");
                        p.sendMessage("§d<-Better Shops Help->");
                    }
                } else if (args.length >= 2) {
                    if (args[0].equalsIgnoreCase("open")) {
                        if (Permissions.hasOpenCommandPerm(p)) {
                            String name = args[1];

                            for (int i = 2; i < args.length; i++) {
                                name = name + " " + args[i];
                            }

                            Shop shop = ShopManager.fromString(p, name);

                            if (shop != null) {
                                if (shop.getOwner() != null) {
                                    if (shop.getOwner().getUniqueId().equals(p.getUniqueId())) {
                                        if (shop.isServerShop()) {
                                            OpenShop.openShopItems(null, p, shop, 1);
                                        } else {
                                            OpenShopOptions.openShopOwnerOptionsInventory(null, p, shop, 1);
                                        }
                                    } else {
                                        if (shop.isOpen()) {
                                            if (shop.getShopItems(false).size() != 0 || shop.getShopItems(false).size() == 0 && shop.getShopItems(true).size() == 0) {
                                                OpenShop.openShopItems(null, p, shop, 1);
                                            } else {
                                                if (Config.useSellingShop()) {
                                                    OpenSellShop.openSellerShop(null, p, shop, 1);
                                                } else {
                                                    OpenShop.openShopItems(null, p, shop, 1);
                                                }
                                            }
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("OpenShop"));
                                        } else {
                                            p.closeInventory();
                                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("ShopClosed"));
                                        }
                                    }
                                } else {
                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("FakeShop"));
                                }
                            } else {
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("FakeShop"));
                            }
                        } else {
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("NoPermission"));
                        }
                    } else if (args[0].equalsIgnoreCase("list")) {
                        if (Permissions.hasListPerm(p)) {
                            String name = args[1];

                            for (int i = 2; i < args.length; i++) {
                                name = name + " " + args[i];
                            }

                            OfflinePlayer pl = Bukkit.getOfflinePlayer(name);

                            if (pl != null) {
                                List<Shop> shops = ShopManager.getShopsForPlayer(pl);
                                if (shops != null && shops.size() > 0) {
                                    p.sendMessage("§d<-Listing §e" + pl.getName() + "'s §dShops (§c" + shops.size() + "§d)->");
                                    for (int i = 1; i < shops.size() + 1; i++) {
                                        if (shops.get(i - 1).isNPCShop() || shops.get(i - 1).getNPCShop() != null) {
                                            p.sendMessage("§c" + i + ". §a" + shops.get(i - 1).getName() + " §e(NPC)");
                                        } else if (shops.get(i - 1).isHoloShop()) {
                                            p.sendMessage("§c" + i + ". §a" + shops.get(i - 1).getName() + " §e(Holo)");
                                        } else {
                                            p.sendMessage("§c" + i + ". §a" + shops.get(i - 1).getName());
                                        }
                                    }
                                    p.sendMessage("§d<-Listing §e" + pl.getName() + "'s §dShops (§c" + shops.size() + "§d)->");
                                } else {
                                    p.sendMessage("§d<-Listing §e" + pl.getName() + "'s §dShops (§c0§d)->");
                                    p.sendMessage("§cNo Shops");
                                    p.sendMessage("§d<-Listing §e" + pl.getName() + "'s §dShops (§c0§d)->");
                                }
                            } else {
                                p.sendMessage("§d<-Listing §e" + name + "'s §dShops (§c0§d)->");
                                p.sendMessage("§cNo Shops");
                                p.sendMessage("§d<-Listing §e" + name + "'s §dShops (§c0§d)->");
                            }
                        } else {
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("NoPermission"));
                        }
                    } else {
                        p.sendMessage("§d<-Better Shops Help->");
                        p.sendMessage("    §a/bs info");
                        p.sendMessage("    §a/bs update");
                        p.sendMessage("    §a/bs config");
                        p.sendMessage("    §a/bs language");
                        p.sendMessage("    §a/bs blacklist");
                        p.sendMessage("    §a/bs open <Shop>");
                        p.sendMessage("    §a/sremove <Shop>");
                        p.sendMessage("    §a/bs list <Player>");
                        p.sendMessage("§d<-Better Shops Help->");
                    }

                } else {
                    p.sendMessage("§d<-Better Shops Help->");
                    p.sendMessage("    §a/bs info");
                    p.sendMessage("    §a/bs update");
                    p.sendMessage("    §a/bs config");
                    p.sendMessage("    §a/bs language");
                    p.sendMessage("    §a/bs blacklist");
                    p.sendMessage("    §a/bs open <Shop>");
                    p.sendMessage("    §a/sremove <Shop>");
                    p.sendMessage("    §a/bs list <Player>");
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
                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("NoPermission"));
                    return true;
                }

                if (args.length > 0) {
                    String name = args[0];

                    for (int i = 1; i < args.length; i++) {
                        name = name + " " + args[i];
                    }

                    Shop shop = ShopManager.fromString(p, name);

                    if (shop != null) {

                        if (shop.getOwner() != null) {
                            if (shop.getOwner().getUniqueId().equals(p.getUniqueId()) || p.isOp() || Config.usePerms() && Permissions.hasBreakPerm(p)) {


                                if (!shop.isNPCShop() && !shop.isHoloShop() || shop.getNPCShop() == null && !shop.isHoloShop()) {

                                    Location loc = shop.getLocation();

                                    Block b = loc.getBlock();

                                    if (b.getState() instanceof Chest) {
                                        Chest chest = (Chest) b.getState();

                                        for (Chunk c : loc.getWorld().getLoadedChunks()) {
                                            for (BlockState bs : c.getTileEntities()) {
                                                if (bs instanceof Sign) {
                                                    Sign sign = (Sign) bs;

                                                    Block face = sign.getBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());


                                                    if (face.getType() == Material.CHEST || face.getType() == Material.TRAPPED_CHEST) {
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

                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("DeleteShop"));

                                        ShopDeleteEvent e = new ShopDeleteEvent(shop);

                                        Bukkit.getPluginManager().callEvent(e);

                                        for (ShopItem item : shop.getShopItems()) {
                                            Stocks.addItemsToInventory(item, p, item.getStock());
                                        }

                                        if (shop.getOwner() != null) {
                                            int amt = ShopManager.getLimits().get(shop.getOwner().getUniqueId());
                                            ShopManager.getLimits().put(shop.getOwner().getUniqueId(), amt - 1);

                                            List<Shop> l = ShopManager.getShopsForPlayer(shop.getOwner());
                                            l.remove(shop);
                                            ShopManager.playerShops.put(shop.getOwner().getUniqueId(), l);
                                        }

                                        YamlConfiguration config = shop.config;
                                        File file = shop.file;

                                        config.set(name, null);
                                        try {
                                            config.save(file);
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        ShopManager.locs.remove(shop.getLocation());
                                        ShopManager.names.remove(shop.getName());
                                        ShopManager.shops.remove(shop);


                                        if (Core.useSQL()) {
                                            try {
                                                Core.getSQLDatabase().getConnection().createStatement().execute("DELETE from Shops where Name='" + shop.getName() + "';");
                                            } catch (SQLException e1) {
                                                e1.printStackTrace();
                                            }
                                        }

                                        if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                            Core.getTitleManager().sendTitle(p, Messages.getString("DeleteShop"));


                                        }
                                    } else {
                                        p.sendMessage(Messages.getString("Prefix") + "§4ERROR: §cShop is non-existant, please tell a server operator of this problem");
                                    }

                                } else if (shop.isNPCShop() || shop.getNPCShop() != null) {
                                    File file = shop.file;

                                    boolean can = false;

                                    YamlConfiguration config = shop.config;
                                    for (LivingEntity e : shop.getLocation().getWorld().getLivingEntities()) {

                                        if (e.getCustomName() != null && e.getCustomName().equals("§a§l" + shop.getName())) {

                                            for (ShopsNPC npc : me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.getNPCs()) {
                                                if (npc.getShop().getName().equals(shop.getName())) {
                                                    me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.removeNPC(npc);
                                                    e.remove();

                                                    for (ShopItem item : shop.getShopItems()) {
                                                        Stocks.addItemsToInventory(item, p, item.getStock());
                                                    }

                                                    p.sendMessage(Messages.getString("Prefix") + Messages.getString("DeleteShop"));

                                                    ShopManager.locs.remove(shop.getLocation());
                                                    ShopManager.names.remove(shop.getName());
                                                    ShopManager.shops.remove(shop);

                                                    if (shop.getOwner() != null) {
                                                        int amt = ShopManager.getLimits().get(shop.getOwner().getUniqueId());
                                                        ShopManager.getLimits().put(shop.getOwner().getUniqueId(), amt - 1);

                                                        List<Shop> l = ShopManager.getShopsForPlayer(shop.getOwner());
                                                        l.remove(shop);
                                                        ShopManager.playerShops.put(shop.getOwner().getUniqueId(), l);
                                                    }

                                                    if (Core.useSQL()) {
                                                        try {
                                                            Core.getSQLDatabase().getConnection().createStatement().execute("DELETE from Shops where Name='" + shop.getName() + "';");
                                                        } catch (SQLException e1) {
                                                            e1.printStackTrace();
                                                        }
                                                    }

                                                    config.set(name, null);
                                                    try {
                                                        config.save(file);
                                                    } catch (IOException e1) {
                                                        e1.printStackTrace();
                                                    }

                                                    can = true;

                                                    if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                                        Core.getTitleManager().setTimes(p, 20, 40, 20);
                                                        Core.getTitleManager().sendTitle(p, Messages.getString("DeleteShop"));


                                                    }
                                                    break;
                                                }

                                            }
                                        }
                                    }

                                    if (!can) {

                                        for (ShopItem item : shop.getShopItems()) {
                                            Stocks.addItemsToInventory(item, p, item.getStock());
                                        }
                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("DeleteShop"));

                                        if (shop.getOwner() != null) {
                                            int amt = ShopManager.getLimits().get(shop.getOwner().getUniqueId());
                                            ShopManager.getLimits().put(shop.getOwner().getUniqueId(), amt - 1);

                                            List<Shop> l = ShopManager.getShopsForPlayer(shop.getOwner());
                                            l.remove(shop);
                                            ShopManager.playerShops.put(shop.getOwner().getUniqueId(), l);
                                        }

                                        config.set(name, null);
                                        try {
                                            config.save(file);
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        ShopManager.locs.remove(shop.getLocation());
                                        ShopManager.names.remove(shop.getName());
                                        ShopManager.shops.remove(shop);


                                        if (Core.useSQL()) {
                                            try {
                                                Core.getSQLDatabase().getConnection().createStatement().execute("DELETE from Shops where Name='" + shop.getName() + "';");
                                            } catch (SQLException e1) {
                                                e1.printStackTrace();
                                            }
                                        }

                                        if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {

                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                            Core.getTitleManager().sendTitle(p, Messages.getString("DeleteShop"));


                                        }

                                    }
                                } else {
                                    DeleteHoloShop.deleteHologramShop(shop.getHolographicShop());

                                    Location loc = shop.getLocation();

                                    Block b = loc.getBlock();

                                    if (b.getState() instanceof Chest) {
                                        Chest chest = (Chest) b.getState();

                                        for (Chunk c : loc.getWorld().getLoadedChunks()) {
                                            for (BlockState bs : c.getTileEntities()) {
                                                if (bs instanceof Sign) {
                                                    Sign sign = (Sign) bs;

                                                    Block face = sign.getBlock().getRelative(((org.bukkit.material.Sign) (sign.getData())).getAttachedFace());


                                                    if (face.getType() == Material.CHEST || face.getType() == Material.TRAPPED_CHEST) {
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

                                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("DeleteShop"));

                                        ShopDeleteEvent e = new ShopDeleteEvent(shop);

                                        Bukkit.getPluginManager().callEvent(e);

                                        for (ShopItem item : shop.getShopItems()) {
                                            Stocks.addItemsToInventory(item, p, item.getStock());
                                        }

                                        if (shop.getOwner() != null) {
                                            int amt = ShopManager.getLimits().get(shop.getOwner().getUniqueId());
                                            ShopManager.getLimits().put(shop.getOwner().getUniqueId(), amt - 1);

                                            List<Shop> l = ShopManager.getShopsForPlayer(shop.getOwner());
                                            l.remove(shop);
                                            ShopManager.playerShops.put(shop.getOwner().getUniqueId(), l);
                                        }

                                        YamlConfiguration config = shop.config;
                                        File file = shop.file;

                                        config.set(name, null);
                                        try {
                                            config.save(file);
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        ShopManager.locs.remove(shop.getLocation());
                                        ShopManager.names.remove(shop.getName());
                                        ShopManager.shops.remove(shop);

                                        if (Core.useSQL()) {
                                            try {
                                                Core.getSQLDatabase().getConnection().createStatement().execute("DELETE from Shops where Name='" + shop.getName() + "';");
                                            } catch (SQLException e1) {
                                                e1.printStackTrace();
                                            }
                                        }

                                        if (Core.isAboveEight() && Config.useTitles() && Core.getTitleManager() != null) {


                                            Core.getTitleManager().setTimes(p, 20, 40, 20);
                                            Core.getTitleManager().sendTitle(p, Messages.getString("DeleteShop"));


                                        }
                                    } else {
                                        p.sendMessage(Messages.getString("Prefix") + "§4ERROR: §cShop is non-existant, please tell a server operator of this problem");
                                    }
                                }
                            } else {
                                p.sendMessage(Messages.getString("Prefix") + Messages.getString("DenyDeleteShop"));
                            }
                        } else {
                            p.sendMessage(Messages.getString("Prefix") + Messages.getString("FakeShop"));
                        }
                    } else {
                        p.sendMessage(Messages.getString("Prefix") + Messages.getString("FakeShop"));
                    }

                } else {
                    p.sendMessage(Messages.getString("Prefix") + "§cUsage: §d/sremove <ShopName>");
                }
            } else {
                sender.sendMessage(Messages.getString("Prefix") + "Only Players Can Perform This Command!");
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
        if (plugin == null || !(plugin instanceof WorldGuardPlugin) || !plugin.isEnabled()) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

    //Get Vault
    private Plugin getVault() {
        Plugin plugin = getServer().getPluginManager().getPlugin("Vault");

        // Vault may not be loaded
        if (plugin == null || !plugin.isEnabled()) {
            return null; // Maybe you want throw an exception instead
        }

        return plugin;
    }

    //Get Holo
    private Plugin getHolographicDisplays() {
        Plugin plugin = getServer().getPluginManager().getPlugin("HolographicDisplays");

        // HD may not be loaded
        if (plugin == null || !plugin.isEnabled()) {
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

        shops.addPlotter(new Metrics.Plotter("" + ShopManager.getAllShops().size()) {
            @Override
            public int getValue() {
                return 1;
            }
        });

        Metrics.Graph npcshops = metrics.createGraph("Number of NPC Shops per Server");

        npcshops.addPlotter(new Metrics.Plotter("" + me.moomaxie.BetterShops.ShopTypes.NPC.NPCs.getNPCs().size()) {
            @Override
            public int getValue() {
                return 1;
            }
        });

        if (useHolograms()) {
            Metrics.Graph holoshops = metrics.createGraph("Number of Holographic Shops per Server");

            holoshops.addPlotter(new Metrics.Plotter("" + HologramManager.getHolographicShops().size()) {
                @Override
                public int getValue() {
                    return 1;
                }
            });
        }

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
