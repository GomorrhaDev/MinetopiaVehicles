package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.utils.PluginUpdater;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.Bukkit;

import static nl.mtvehicles.core.infrastructure.enums.PluginVersion.getPluginVersion;

/**
 * <b>/vehicle version</b> - get information about the plugin and server version.
 */
public class VehicleVersion extends MTVSubCommand {
    public VehicleVersion() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.admin")) return true;

        String pluginVersion = VersionModule.pluginVersionString;
        String isLatest = (PluginUpdater.isLatestVersion() && !getPluginVersion().isDev()) ? " (latest)" : "";
        String serverVersion = Bukkit.getVersion();

        sender.sendMessage(String.format("§2Running §aMTVehicles v%s§2%s.", pluginVersion, isLatest));
        sender.sendMessage(String.format("§2Your server is running §a%s§2.", serverVersion));
        if (!DependencyModule.loadedDependencies.isEmpty()) {
            String dependencies = "";
            int numberOfDependencies = 0;
            for (SoftDependency dependency: DependencyModule.loadedDependencies) {
                if (numberOfDependencies == 0) dependencies += dependency.getName();
                else dependencies += ", " + dependency.getName();
                numberOfDependencies++;
            }
            if (DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) {
                if (!DependencyModule.vault.isEconomySetUp()) dependencies = dependencies.replace("Vault", "§a§mVault§a");
            }
            sender.sendMessage(String.format("§2Loaded dependencies (%s§2): §a%s§2.", numberOfDependencies, dependencies));
        } else {
            sender.sendMessage(String.format("§2There are no loaded dependencies."));
        }

        if (VersionModule.isPreRelease) {
            sender.sendMessage("§e-----");
            if (getPluginVersion().isDev()) sender.sendMessage(TextUtils.colorize("&cWarning: You're using a dev-version. Auto-updater is disabled."));
            sendMessage(Message.USING_PRE_RELEASE);
        }

        return true;
    }
}
