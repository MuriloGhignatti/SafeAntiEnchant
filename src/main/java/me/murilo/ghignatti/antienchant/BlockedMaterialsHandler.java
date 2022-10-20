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

    public void blockMaterial(Material material){
        blockedMaterials.add(material);
    }

    public void unblockMaterial(Material material){
        blockedMaterials.remove(material);
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
        blockedMaterials = config.getStringList(Constants.BLOCKED_ITEMS_PATH).stream().map(Material::getMaterial).collect(Collectors.toSet());
    }
}
