package me.moomaxie.BetterShops.Configurations;


import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public interface AnvilGUI {

    public enum AnvilSlot {
        INPUT_LEFT(0),
        INPUT_RIGHT(1),
        OUTPUT(2);

        private int slot;

        private AnvilSlot(int slot){
            this.slot = slot;
        }

        public int getSlot(){
            return slot;
        }

        public static AnvilSlot bySlot(int slot){
            for(AnvilSlot anvilSlot : values()){
                if(anvilSlot.getSlot() == slot){
                    return anvilSlot;
                }
            }

            return null;
        }
    }

    public void doGUIThing(Player player, final AnvilClickEventHandler handler);

    public class AnvilClickEvent {
        private AnvilSlot slot;

        private String name;

        private boolean close = true;
        private boolean destroy = true;

        private ItemStack item;
        private HumanEntity entity;

        public AnvilClickEvent(AnvilSlot slot, String name, ItemStack it, HumanEntity ent){
            this.slot = slot;
            this.name = name;
            this.item = it;
            this.entity = ent;
        }

        public AnvilClickEvent() {

        }

        public int getSlot(){
            return slot.getSlot();
        }

        public ItemStack getCurrentItem(){
            return item;
        }

        public HumanEntity getWhoClicked(){
            return entity;
        }

        public String getName(){
            return name;
        }

        public boolean getWillClose(){
            return close;
        }

        public void setWillClose(boolean close){
            this.close = close;
        }

        public boolean getWillDestroy(){
            return destroy;
        }

        public void setWillDestroy(boolean destroy){
            this.destroy = destroy;
        }
    }

    public interface AnvilClickEventHandler {


        void onAnvilClick(AnvilClickEvent clickEvent);
    }

    Player player = null;

    AnvilClickEventHandler handler = null;

    HashMap<AnvilSlot, ItemStack> items = new HashMap<AnvilSlot, ItemStack>();

    Inventory inv = null;

    Listener listener = null;

    public Player getPlayer();

    public void setSlot(AnvilSlot slot, ItemStack item);

    public void open();

    public void destroy();
}
