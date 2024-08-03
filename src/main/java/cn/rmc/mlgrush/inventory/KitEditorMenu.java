package cn.rmc.mlgrush.inventory;

import cn.rmc.mlgrush.Language;
import cn.rmc.mlgrush.MLGRush;
import cn.rmc.mlgrush.util.ItemBuilder;
import cn.rmc.mlgrush.util.game.SlotUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class KitEditorMenu implements Listener {
    private static Inventory Editor;
    private static ItemStack Stick;
    private static ItemStack Block;
    private static ItemStack Pickaxe;
    private static ItemStack Save;
    private static ItemStack Reset;
    private static ItemStack Cancel;
    private static ItemStack BackGuard;


    private static void create(Player p){
        Language lang = Language.get();
        Editor = Bukkit.createInventory(null,18, lang.translate("Gui.editor.header"));
        //背景
        BackGuard = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(byte) 14).setName("§r").toItemStack();
        //击退棒
        Stick =  new ItemBuilder(Material.STICK).setName(lang.translate("Gui.editor.stick")).toItemStack();
        //方块
        Block = new ItemBuilder(Material.SANDSTONE).setName(lang.translate("Gui.editor.block")).toItemStack();
        //稿子
        Pickaxe = new ItemBuilder(Material.DIAMOND_PICKAXE).setName(lang.translate("Gui.editor.pickaxe")).toItemStack();//new ItemStack(Material.DIAMOND_PICKAXE);
        //保存
        Save = new ItemBuilder(Material.WOOL,1,(byte)5).setName(lang.translate("Gui.editor.save")).toItemStack();
        //重置
        Reset = new ItemBuilder(Material.WOOL,1,(byte)4).setName(lang.translate("Gui.editor.reset")).toItemStack();
        //跑路
        Cancel = new ItemBuilder(Material.WOOL,1,(byte)14).setName(lang.translate("Gui.editor.quit")).toItemStack();
        int Stick_slot = SlotUtils.getSlots(p,"stick",0);//String.valueOf(MLGRush.getPlayer().getInt("players."+p.getName()+".inventory.stick")) == null ? 0:MLGRush.getPlayer().getInt("players."+p.getName()+".inventory.stick");
        int Block_slot = SlotUtils.getSlots(p,"block",1);//String.valueOf(MLGRush.getPlayer().getInt("players."+p.getName()+".inventory.block")) == null ? 1:MLGRush.getPlayer().getInt("players."+p.getName()+".inventory.block");
        int Pickaxe_slot = SlotUtils.getSlots(p,"pickaxe",2);//String.valueOf(MLGRush.getPlayer().getInt("players."+p.getName()+".inventory.pickaxe")) == null ? 2:MLGRush.getPlayer().getInt("players."+p.getName()+".inventory.pickaxe");
        //设置物品
        List<Integer> Backguard_slots = Arrays.asList(12,13,14,15,16,17);
        Editor.setItem(9,Save);
        Editor.setItem(10,Reset);
        Editor.setItem(11,Cancel);
        Backguard_slots.forEach(slot -> Editor.setItem((Integer) slot,BackGuard));
        Editor.setItem(Stick_slot,Stick);
        Editor.setItem(Block_slot,Block);
        Editor.setItem(Pickaxe_slot,Pickaxe);
    }
    @EventHandler
    public void onClick(InventoryClickEvent e){
        Language lang = Language.get();
        String prefix = lang.translate("prefix");
        try {
            List<Integer> Inventory_slot = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
            String item = e.getCurrentItem().getItemMeta().getDisplayName();
            if (!e.getInventory().getTitle().equals(lang.translate("Gui.editor.header"))) return;
            if (item.equals(BackGuard.getItemMeta().getDisplayName()))
                e.setCancelled(true);
            if (item.equals(Save.getItemMeta().getDisplayName())) {
                e.setCancelled(true);
                for (int i : Inventory_slot) {
                    if (Editor.getItem(i) == null) continue;
                    String itemname = Editor.getItem(i).getItemMeta().getDisplayName();
                    if (itemname.equals(Stick.getItemMeta().getDisplayName())) {
                        MLGRush.getPlayer().set("players." + e.getWhoClicked().getName() + ".inventory.stick", i);
                    }
                    if (itemname.equals(Block.getItemMeta().getDisplayName())) {
                        MLGRush.getPlayer().set("players." + e.getWhoClicked().getName() + ".inventory.block", i);
                    }
                    if (itemname.equals(Pickaxe.getItemMeta().getDisplayName())) {
                        MLGRush.getPlayer().set("players." + e.getWhoClicked().getName() + ".inventory.pickaxe", i);
                    }
                }
                e.getWhoClicked().sendMessage(prefix + lang.translate("Message.editor.save"));
            }
            if (item.equals(Reset.getItemMeta().getDisplayName())) {
                e.setCancelled(true);
                Inventory_slot.forEach(slot -> Editor.setItem(slot, null));
                Editor.setItem(0, Stick);
                Editor.setItem(1, Block);
                Editor.setItem(2, Pickaxe);
                MLGRush.getPlayer().set("players." + e.getWhoClicked().getName() + ".inventory.stick", 0);
                MLGRush.getPlayer().set("players." + e.getWhoClicked().getName() + ".inventory.block", 1);
                MLGRush.getPlayer().set("players." + e.getWhoClicked().getName() + ".inventory.pickaxe", 2);
                e.getWhoClicked().sendMessage(prefix+lang.translate("Message.editor.reset"));
            }
            if (item.equals(Cancel.getItemMeta().getDisplayName())) {
                e.getWhoClicked().closeInventory();
            }
        }catch (NullPointerException ignored){}
    }
    public static void open(Player p){
        create(p);
        p.openInventory(Editor);
    }
}
