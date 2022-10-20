package me.murilo.ghignatti.antienchant;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockedMaterialsHandler {
    private Set<Material> blockedMaterials;

    public BlockedMaterialsHandler(FileConfiguration configuration) {
        loadMaterials(configuration);
    }

    public void blockItem(Material item){
        blockedMaterials.add(item);
    }

    public void unblockItem(Material item){
        blockedMaterials.remove(item);
    }

    public List<String> getBlockedMaterials(){
        return blockedMaterials.stream().map(Material::name).collect(Collectors.toList());
    }

    public boolean isMaterialBlocked(Material materialToCheck){
        return blockedMaterials.contains(materialToCheck);
    }

    public void reloadMaterials(FileConfiguration configuration){
        loadMaterials(configuration);
    }

    public void loadMaterials(FileConfiguration config){
        blockedMaterials = config.getStringList("Block.Items").stream().map(Material::getMaterial).collect(Collectors.toSet());
    }
}
