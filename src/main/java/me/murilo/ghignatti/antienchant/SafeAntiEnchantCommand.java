package me.murilo.ghignatti.antienchant;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SafeAntiEnchantCommand implements CommandExecutor {

    private final SafeAntiEnchant mainInstance;
    private FileConfiguration config;
    private final BlockedMaterialsHandler blockedMaterialsHandler;

    public SafeAntiEnchantCommand(SafeAntiEnchant mainInstance, FileConfiguration config, BlockedMaterialsHandler blockedMaterialsHandler) {
        this.mainInstance = mainInstance;
        this.config = config;
        this.blockedMaterialsHandler = blockedMaterialsHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args[0].toLowerCase()) {
            case "add":
                if (!sender.hasPermission(Constants.ADD_PERMISSION)) {
                    return false;
                }
                if (args.length == 1) {
                    return addHandItem((Player) sender);
                } else {
                    return addItem(Arrays.stream(args).skip(1).collect(Collectors.toList()), sender);
                }
            case "remove":
                if (!sender.hasPermission(Constants.REMOVE_PERMISSION)) {
                    return false;
                }
                if (args.length == 1) {
                    return removeHandItem((Player) sender);
                } else {
                    return removeItem(Arrays.stream(args).skip(1).collect(Collectors.toList()), sender);
                }
            case "list":
                if (!sender.hasPermission(Constants.LIST_PERMISSION)) {
                    return false;
                }
                return listBlockedItems(sender);
            case "reload":
                if (!sender.hasPermission(Constants.RELOAD_PERMISSION)) {
                    return false;
                }
                blockedMaterialsHandler.loadMaterials(mainInstance.getConfig());
                mainInstance.config = mainInstance.getConfig();
                config = mainInstance.getConfig();
                mainInstance.saveConfig();
                return true;
            default:
                return false;
        }
    }

    private boolean removeHandItem(Player p) {
        return removeItem(Collections.singletonList(p.getInventory().getItemInMainHand().getType().name()), p);
    }

    private boolean addHandItem(Player p) {
        return addItem(Collections.singletonList(p.getInventory().getItemInMainHand().getType().name()), p);
    }

    private boolean listBlockedItems(CommandSender sender) {
        sender.sendMessage(Constants.PLUGIN_PREFIX + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString(Constants.MESSAGE_LIST_BLOCKED_ITEMS))).replace("{items}", String.join(", ", blockedMaterialsHandler.getBlockedMaterials())));
        return true;
    }

    private boolean addItem(List<String> args, CommandSender sender) {
        if (args.isEmpty()) {
            return false;
        }
        args.stream().map(Material::getMaterial).forEach(blockedMaterialsHandler::blockMaterial);
        mainInstance.forceSaveConfig();
        sender.sendMessage(Constants.PLUGIN_PREFIX + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString(Constants.MESSAGE_ADDING_ITEM_PATH))).replace("{item}", String.join(", ", args)));
        return true;
    }

    private boolean removeItem(List<String> args, CommandSender sender) {
        if (args.isEmpty()) {
            return false;
        }
        args.stream().map(Material::getMaterial).forEach(blockedMaterialsHandler::unblockMaterial);
        mainInstance.forceSaveConfig();
        sender.sendMessage(Constants.PLUGIN_PREFIX + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString(Constants.MESSAGE_REMOVING_ITEM_PATH))).replace("{item}", String.join(", ", args)));
        return true;
    }
}
