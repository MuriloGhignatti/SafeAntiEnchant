package me.murilo.ghignatti.antienchant;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SafeAntiEnchant extends JavaPlugin {

    FileConfiguration config;
    BlockedMaterialsHandler blockedMaterialsHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getServer().getConsoleSender().sendMessage(
            Constants.PLUGIN_PREFIX +
            ChatColor.translateAlternateColorCodes('&', " &aLoaded"));
        generateConfig();
        blockedMaterialsHandler = new BlockedMaterialsHandler(config);
        getServer().getPluginManager().registerEvents(
            new BlockEnchantEvent(blockedMaterialsHandler), this);
        Objects.requireNonNull(getCommand("SafeAntiEnchant"))
            .setExecutor(new SafeAntiEnchantCommand(this, config,
                                                    blockedMaterialsHandler));
        Objects.requireNonNull(getCommand("SafeAntiEnchant"))
            .setTabCompleter(
                new SafeAntiEnchatTabCompleter(blockedMaterialsHandler));
    }

    @Override
    public void onDisable() {
        forceSaveConfig();
        config = null;
        blockedMaterialsHandler = null;
        Bukkit.getServer().getConsoleSender().sendMessage(
            Constants.PLUGIN_PREFIX +
            ChatColor.translateAlternateColorCodes('&', " &cUnloaded"));
    }

    public void forceSaveConfig() {
        config.set(Constants.BLOCKED_ITEMS_PATH,
                   blockedMaterialsHandler.getBlockedMaterials());
        saveConfig();
    }

    private void generateConfig() {
        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
        config.addDefault(Constants.BLOCKED_ITEMS_PATH, new String[] {});
        config.addDefault(Constants.MESSAGE_ADDING_ITEM_PATH,
                          "&aAdding &fItem(s) &b{item} &fto blocked list");
        config.addDefault(Constants.MESSAGE_REMOVING_ITEM_PATH,
                          "&cRemoving &fItem(s) &b{item} &fto blocked list");
        config.addDefault(Constants.MESSAGE_LIST_BLOCKED_ITEMS,
                          "Items &cBlocked &f: {items}");
        saveConfig();
    }
}
