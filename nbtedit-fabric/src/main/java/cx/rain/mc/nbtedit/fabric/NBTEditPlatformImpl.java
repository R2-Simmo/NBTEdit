package cx.rain.mc.nbtedit.fabric;

import cx.rain.mc.nbtedit.api.command.IModPermission;
import cx.rain.mc.nbtedit.api.config.IModConfig;
import cx.rain.mc.nbtedit.api.netowrking.IModNetworking;
import cx.rain.mc.nbtedit.fabric.command.ModPermissionImpl;
import cx.rain.mc.nbtedit.fabric.config.ModConfigImpl;
import cx.rain.mc.nbtedit.fabric.networking.ModNetworkingImpl;
import net.fabricmc.loader.api.FabricLoader;

public class NBTEditPlatformImpl {
    private static final ModNetworkingImpl NETWORKING = new ModNetworkingImpl();
    private static final ModConfigImpl CONFIG = new ModConfigImpl(FabricLoader.getInstance().getGameDir().toFile());
    private static final ModPermissionImpl PERMISSION = new ModPermissionImpl(CONFIG.getPermissionsLevel());

    public static IModNetworking getNetworking() {
        return NETWORKING;
    }

    public static IModConfig getConfig() {
        return CONFIG;
    }

    public static IModPermission getPermission() {
        return PERMISSION;
    }
}
