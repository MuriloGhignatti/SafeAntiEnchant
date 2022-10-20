package me.murilo.ghignatti.antienchant;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class SafeAntiEnchatTabCompleter implements TabCompleter {
    private final BlockedMaterialsHandler blockedMaterialsHandler;
    List<String> arguments = Arrays.asList("add", "remove", "reload");
    List<String> materials = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList());

    public SafeAntiEnchatTabCompleter(BlockedMaterialsHandler blockedMaterialsHandler) {
        this.blockedMaterialsHandler = blockedMaterialsHandler;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        Player p = (Player) sender;

        if (args.length == 1) {
            if (p.hasPermission(Constants.ADD_PERMISSION)) {
                commands.add("add");
            }
            if (p.hasPermission(Constants.REMOVE_PERMISSION)) {
                commands.add("remove");
            }
            if (p.hasPermission(Constants.RELOAD_PERMISSION)) {
                commands.add("reload");
            }
            if (p.hasPermission(Constants.LIST_PERMISSION)) {
                commands.add("list");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            if (args[0].equals("add")) {
                commands.addAll(materials.stream().filter(it -> it.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList()));
            } else if (args[0].equals("remove")) {
                commands.addAll(blockedMaterialsHandler.getBlockedMaterials());
            }
            StringUtil.copyPartialMatches(args[1], commands, completions);
        } else if (args.length >= 3) {
            Set<String> alreadyAddedItems = new HashSet<>(Arrays.stream(args).map(String::toLowerCase).collect(Collectors.toList()).subList(1, args.length));
            if (args[0].equals("add")){
                alreadyAddedItems.addAll(blockedMaterialsHandler.getBlockedMaterials().stream().map(String::toLowerCase).collect(Collectors.toList()));
                commands.addAll(materials.stream().filter(it -> !alreadyAddedItems.contains(it)).collect(Collectors.toList()));
            }
            else if (args[0].equals("remove")){
                commands.addAll(blockedMaterialsHandler.getBlockedMaterials().stream().filter(it -> !alreadyAddedItems.contains(it.toLowerCase())).collect(Collectors.toList()));
            }
            StringUtil.copyPartialMatches(args[args.length - 1], commands, completions);
        }
        return completions;
    }
}
