package cn.rmc.mlgrush.inventory;

import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.data.Game;
import cn.rmc.mlgrush.util.ItemBuilder;
import cn.rmc.mlgrush.util.inventory.InventoryUI;
import cn.rmc.mlgrush.util.inventory.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class SpecMenu {
    private InventoryUI specmenu;
    public static HashMap<UUID,InventoryUI> inventories = new HashMap<>();

    public SpecMenu(){
        Setup();
        Bukkit.getScheduler().runTaskTimerAsynchronously(MLGRush.getInstance(),()->{
            update();
        },0L,60L);
    }
    public void Setup(){
        specmenu = new InventoryUI("Spectate",6);
    }
    public static void open(Player p){
        p.openInventory(MLGRush.getInstance().getSpecMenu().specmenu.getCurrentPage());
    }
    public void update(){
        for(int i=0 ; i<=53 ;i++){
            int size = MLGRush.getInstance().getGameManager().getData().size();
            if(i+1 <= size){
                Game g = MLGRush.getInstance().getGameManager().getData().get(i);
                ItemBuilder ib = new ItemBuilder(Material.BED);
                ib.setName("§r");
                ib.addLoreLine("§cRed: "+g.getRed().getPlayer().getDisplayName());
                ib.addLoreLine("§bBlue: "+g.getBlue().getPlayer().getDisplayName());
                ib.addLoreLine("§fID: "+g.getId());
                g.getFighter().forEach(playerData ->{
                    if(playerData.getPlayer().hasPermission("group.streamer")){
                        ib.addEnchant(Enchantment.DURABILITY,1);
                        ib.setType(Material.NETHER_STAR);
                    }
                });
                specmenu.setItem(i, new InventoryUI.AbstractClickableItem(ItemUtil.hideEnchants(ib.toItemStack())) {
                    @Override
                    public void onClick(InventoryClickEvent e) {
                        Player p = (Player) e.getWhoClicked();
                        g.addwatcher(MLGRush.getInstance().getPlayerManager().get(p));
                    }
                });
            }else{
                specmenu.setItem(i, new InventoryUI.EmptyClickableItem(new ItemStack(Material.AIR)));
            }
        }
    }
}
