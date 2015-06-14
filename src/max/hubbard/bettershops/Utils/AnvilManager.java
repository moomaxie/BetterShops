package max.hubbard.bettershops.Utils;

import max.hubbard.bettershops.Configurations.Language;
import max.hubbard.bettershops.Core;
import max.hubbard.bettershops.Versions.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * ***********************************************************************
 * Copyright Max Hubbard (c) 2015. All Rights Reserved.
 * Any code contained within this document, and any associated documents with similar branding
 * are the sole property of Max. Distribution, reproduction, taking snippets, or
 * claiming any contents as your own will break the terms of the license, and void any
 * agreements with you, the third party.
 * ************************************************************************
 */
public class AnvilManager implements Callable {

    String name;
    Player p;

    public AnvilManager(Player player) {
        p = player;
    }

    @Override
    public String call() {
        final AtomicReference<String> result = new AtomicReference<String>();
        final CountDownLatch latch = new CountDownLatch(1);

        AnvilGUI gui = Core.getAnvilGUI();
        ItemStack it = new ItemStack(Material.PAPER);
        ItemMeta meta = it.getItemMeta();
        meta.setDisplayName(Language.getString("SearchEngine", "Name"));
        it.setItemMeta(meta);
        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, it);
        gui.doGUIThing(p, new AnvilGUI.AnvilClickEventHandler() {
            @Override
            public void onAnvilClick(AnvilGUI.AnvilClickEvent ev) {
                if (ev.getSlot() == 2) {
                    ev.setWillClose(true);
                    ev.setWillDestroy(true);

                    if (ev.getCurrentItem().getType() == Material.PAPER) {
                        if (ev.getCurrentItem().hasItemMeta()) {
                            if (ev.getCurrentItem().getItemMeta().getDisplayName() != null) {
                                name = ev.getCurrentItem().getItemMeta().getDisplayName();
                                result.set(name);
                                latch.countDown();
                            }
                        }
                    }

                } else {
                    ev.setWillClose(false);
                    ev.setWillDestroy(false);
                }
            }
        });

        gui.open();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.get();
    }

    private String getValue() {
        return name;
    }

}
