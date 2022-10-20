package me.murilo.ghignatti.antienchant;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

public class BlockEnchantEvent implements Listener {

    private final BlockedMaterialsHandler blockedMaterialsHandler;

    public BlockEnchantEvent(BlockedMaterialsHandler blockedMaterialsHandler){
        this.blockedMaterialsHandler = blockedMaterialsHandler;
    }

    @EventHandler
    public void onPrepareItemEnchant(PrepareItemEnchantEvent e){
        if (blockedMaterialsHandler.isMaterialBlocked(e.getItem().getType())){
            e.setCancelled(true);
        }
    }
}
