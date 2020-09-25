package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.VehiclesUtils;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onLicenseChat(AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();


        if (VehiclesUtils.edit.get(p.getUniqueId() + ".kenteken") == null) {
            return;
        }

        if (VehiclesUtils.edit.get(p.getUniqueId() + ".kenteken") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                if (!(Main.vehicleDataConfig.getConfig().get("vehicle." + e.getMessage() + ".skinItem") == null)) {
                    p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionFailedDupLicense")));
                    VehiclesUtils.menuEdit(p);
                    VehiclesUtils.edit.put(p.getUniqueId() + ".kenteken", false);
                    return;
                }
                for (String s : Main.vehicleDataConfig.getConfig().getConfigurationSection("vehicle." + ken).getKeys(false)) {
                    Main.vehicleDataConfig.getConfig().set("vehicle." + e.getMessage() + "." + s, Main.vehicleDataConfig.getConfig().get("vehicle." + ken + "." + s));
                }
                Main.vehicleDataConfig.save();
                p.getInventory().setItemInMainHand(VehiclesUtils.carItem2(Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".name"), Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), e.getMessage()));

                if (e.isAsynchronous()) {


                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        VehiclesUtils.menuEdit(p);
                    });
                }

                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                VehiclesUtils.edit.put(p.getUniqueId() + ".kenteken", false);
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken, null);
                Main.vehicleDataConfig.save();
                return;
            }
            e.setCancelled(true);

            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    VehiclesUtils.menuEdit(p);
                });
            }

            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            VehiclesUtils.edit.put(p.getUniqueId() + ".kenteken", false);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onNaamChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".naam") == null) {
            return;
        }
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".naam") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".name", e.getMessage());
                Main.vehicleDataConfig.save();
                p.getInventory().setItemInMainHand(VehiclesUtils.carItem2(Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), e.getMessage(), Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), ken));
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                VehiclesUtils.edit.put(p.getUniqueId() + ".naam", false);
                if (e.isAsynchronous()) {

                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        VehiclesUtils.menuEdit(p);
                    });

                }
                return;
            }
            e.setCancelled(true);
            VehiclesUtils.menuEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            VehiclesUtils.edit.put(p.getUniqueId() + ".naam", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    VehiclesUtils.menuEdit(p);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBenzineChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".benzine") == null) {
            return;
        }
        if(isI(e.getMessage(), p) == false) {
            e.setCancelled(true);
            VehiclesUtils.benzineEdit(p);
            VehiclesUtils.edit.put(p.getUniqueId() + ".benzine", false);
            return;
        }
        if (Integer.parseInt(e.getMessage()) > 100){
            e.setCancelled(true);
            VehiclesUtils.benzineEdit(p);
            VehiclesUtils.edit.put(p.getUniqueId() + ".benzine", false);
            p.sendMessage(TextUtils.colorize("&cLetop! Het cijfer moet onder de 100 zijn!"));
            return;
        }
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".benzine") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".benzine", Double.valueOf(e.getMessage()));
                Main.vehicleDataConfig.save();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                VehiclesUtils.edit.put(p.getUniqueId() + ".benzine", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        VehiclesUtils.benzineEdit(p);
                    });
                }
                return;
            }
            e.setCancelled(true);
            VehiclesUtils.benzineEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            VehiclesUtils.edit.put(p.getUniqueId() + ".benzine", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    VehiclesUtils.menuEdit(p);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBenzineVerbruikChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".benzineverbruik") == null) {
            return;
        }
        if(isD(e.getMessage(), p) == false) {
            e.setCancelled(true);
            VehiclesUtils.benzineEdit(p);
            VehiclesUtils.edit.put(p.getUniqueId() + ".benzineverbruik", false);
            return;
        }
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".benzineverbruik") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".benzineVerbruik", Double.valueOf(e.getMessage()));
                Main.vehicleDataConfig.save();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                VehiclesUtils.edit.put(p.getUniqueId() + ".benzineverbruik", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        VehiclesUtils.benzineEdit(p);
                    });
                }
                return;
            }
            e.setCancelled(true);
            VehiclesUtils.benzineEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            VehiclesUtils.edit.put(p.getUniqueId() + ".benzine", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    VehiclesUtils.menuEdit(p);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKofferbakrowsChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".kofferbakRows") == null) {
            return;
        }
        if(isI(e.getMessage(), p) == false) {
            e.setCancelled(true);
            VehiclesUtils.kofferbakEdit(p);
            VehiclesUtils.edit.put(p.getUniqueId() + ".kofferbakRows", false);
            return;
        }
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".kofferbakRows") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".kofferbakRows", Integer.parseInt(e.getMessage()));
                Main.vehicleDataConfig.save();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                VehiclesUtils.edit.put(p.getUniqueId() + ".kofferbakRows", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        VehiclesUtils.kofferbakEdit(p);
                    });
                }
                return;
            }
            e.setCancelled(true);
            VehiclesUtils.benzineEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            VehiclesUtils.edit.put(p.getUniqueId() + ".kofferbakRows", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    VehiclesUtils.kofferbakEdit(p);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAcceleratieSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".acceleratieSpeed") == null) {
            return;
        }
        if(isD(e.getMessage(), p) == false) {
            e.setCancelled(true);
            VehiclesUtils.speedEdit(p);
            VehiclesUtils.edit.put(p.getUniqueId() + ".acceleratieSpeed", false);
            return;
        }
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".acceleratieSpeed") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".acceleratieSpeed", Double.valueOf(e.getMessage()));
                Main.vehicleDataConfig.save();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                VehiclesUtils.edit.put(p.getUniqueId() + ".acceleratieSpeed", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        VehiclesUtils.speedEdit(p);
                    });
                }
                return;
            }
            e.setCancelled(true);
            VehiclesUtils.benzineEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            VehiclesUtils.edit.put(p.getUniqueId() + ".acceleratieSpeed", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    VehiclesUtils.speedEdit(p);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMaxSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".maxSpeed") == null) {
            return;
        }
        if(isD(e.getMessage(), p) == false) {
            e.setCancelled(true);
            VehiclesUtils.speedEdit(p);
            VehiclesUtils.edit.put(p.getUniqueId() + ".maxSpeed", false);
            return;
        }
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".maxSpeed") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".maxSpeed", Double.valueOf(e.getMessage()));
                Main.vehicleDataConfig.save();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                VehiclesUtils.edit.put(p.getUniqueId() + ".maxSpeed", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        VehiclesUtils.speedEdit(p);
                    });
                }
                return;
            }
            e.setCancelled(true);
            VehiclesUtils.benzineEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            VehiclesUtils.edit.put(p.getUniqueId() + ".maxSpeed", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    VehiclesUtils.speedEdit(p);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBrakingSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".brakingSpeed") == null) {
            return;
        }
        if(isD(e.getMessage(), p) == false) {
            e.setCancelled(true);
            VehiclesUtils.speedEdit(p);
            VehiclesUtils.edit.put(p.getUniqueId() + ".brakingSpeed", false);
            return;
        }
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".brakingSpeed") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".brakingSpeed", Double.valueOf(e.getMessage()));
                Main.vehicleDataConfig.save();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                VehiclesUtils.edit.put(p.getUniqueId() + ".brakingSpeed", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        VehiclesUtils.speedEdit(p);
                    });
                }
                return;
            }
            e.setCancelled(true);
            VehiclesUtils.benzineEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            VehiclesUtils.edit.put(p.getUniqueId() + ".brakingSpeed", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    VehiclesUtils.speedEdit(p);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAftrekkenSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".aftrekkenSpeed") == null) {
            return;
        }
        if(isD(e.getMessage(), p) == false) {
            e.setCancelled(true);
            VehiclesUtils.speedEdit(p);
            VehiclesUtils.edit.put(p.getUniqueId() + ".aftrekkenSpeed", false);
            return;
        }
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".aftrekkenSpeed") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".aftrekkenSpeed", Double.valueOf(e.getMessage()));
                Main.vehicleDataConfig.save();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                VehiclesUtils.edit.put(p.getUniqueId() + ".aftrekkenSpeed", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        VehiclesUtils.speedEdit(p);
                    });
                }
                return;
            }
            e.setCancelled(true);
            VehiclesUtils.benzineEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            VehiclesUtils.edit.put(p.getUniqueId() + ".aftrekkenSpeed", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    VehiclesUtils.speedEdit(p);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMaxSpeedBackwardsChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".maxSpeedBackwards") == null) {
            return;
        }
        if(isD(e.getMessage(), p) == false) {
            e.setCancelled(true);
            VehiclesUtils.speedEdit(p);
            VehiclesUtils.edit.put(p.getUniqueId() + ".maxSpeedBackwards", false);
            return;
        }
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".maxSpeedBackwards") == true) {
            if (!e.getMessage().toLowerCase().contains("annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".maxSpeedBackwards", Double.valueOf(e.getMessage()));
                Main.vehicleDataConfig.save();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                VehiclesUtils.edit.put(p.getUniqueId() + ".maxSpeedBackwards", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        VehiclesUtils.speedEdit(p);
                    });
                }
                return;
            }
            e.setCancelled(true);
            VehiclesUtils.benzineEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            VehiclesUtils.edit.put(p.getUniqueId() + ".maxSpeedBackwards", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    VehiclesUtils.speedEdit(p);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRotateSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".rotateSpeed") == null) {
            return;
        }
        if(isI(e.getMessage(), p) == false) {
            e.setCancelled(true);
            VehiclesUtils.speedEdit(p);
            VehiclesUtils.edit.put(p.getUniqueId() + ".rotateSpeed", false);
            return;
        }
        if (VehiclesUtils.edit.get(p.getUniqueId() + ".rotateSpeed") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".rotateSpeed", Integer.parseInt(e.getMessage()));
                Main.vehicleDataConfig.save();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                VehiclesUtils.edit.put(p.getUniqueId() + ".rotateSpeed", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        VehiclesUtils.speedEdit(p);
                    });
                }
                return;
            }
            e.setCancelled(true);
            VehiclesUtils.speedEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            VehiclesUtils.edit.put(p.getUniqueId() + ".rotateSpeed", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    VehiclesUtils.speedEdit(p);
                });
            }
        }
    }

    public boolean isI(String str, Player p) {
        try {
            Integer.parseInt(str);
        } catch (Throwable e) {
            p.sendMessage(TextUtils.colorize("&cLetop! Het moet een cijfer zijn."));
            return false;
        }
        return true;
    }

    public boolean isD(String str, Player p) {
        try {
            Double.valueOf(str);
        } catch (Throwable e) {
            p.sendMessage(TextUtils.colorize("&cLetop! Het moet een double zijn. bv 0.02"));
            return false;
        }
        return true;
    }
}
