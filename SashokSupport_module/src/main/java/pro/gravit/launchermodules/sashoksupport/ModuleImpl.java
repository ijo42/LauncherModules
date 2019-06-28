package pro.gravit.launchermodules.sashoksupport;

import pro.gravit.launcher.modules.Module;
import pro.gravit.launcher.modules.ModuleContext;
import pro.gravit.launchermodules.sashoksupport.socket.LegacyServerComponent;
import pro.gravit.launchermodules.sashoksupport.socket.ServerSocketHandler;
import pro.gravit.launchserver.auth.handler.AuthHandler;
import pro.gravit.launchserver.auth.provider.AuthProvider;
import pro.gravit.launchserver.auth.texture.TextureProvider;
import pro.gravit.launchserver.components.Component;
import pro.gravit.utils.Version;

public class ModuleImpl implements Module {
    private static boolean registred = false;
    public static final Version version = new Version(1, 0, 0, 0, Version.Type.LTS);

    @Override
    public void close() {

    }

    @Override
    public String getName() {
        return "DepcreatedFunctions";
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public void init(ModuleContext context1) {
    }

    @Override
    public void preInit(ModuleContext context1) {
        if (!registred) {
            AuthHandler.providers.register("binaryFile", BinaryFileAuthHandler.class);
            Component.providers.register("legacyServer", LegacyServerComponent.class);
            registred = true;
        }
    }

    @Override
    public void postInit(ModuleContext context1) {

    }

    public static void main(String[] args) {
        System.err.println("This is module, use with GravitLauncher`s LaunchServer.");
    }
}