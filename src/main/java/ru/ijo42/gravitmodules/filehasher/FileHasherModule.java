package ru.ijo42.gravitmodules.filehasher;

import pro.gravit.launcher.managers.GsonManager;
import pro.gravit.launcher.modules.LauncherInitContext;
import pro.gravit.launcher.modules.LauncherModule;
import pro.gravit.launcher.modules.LauncherModuleInfo;
import pro.gravit.launchserver.modules.events.LaunchServerPostInitPhase;
import pro.gravit.launchserver.socket.WebSocketService;
import pro.gravit.utils.Version;

public class FileHasherModule extends LauncherModule {
    public static final Version version = new Version(1, 0, 0, 1, Version.Type.STABLE);
    protected static final GsonManager gsonManager = new GsonManager();

    public FileHasherModule() {
        super(new LauncherModuleInfo("FileHasherModule", version));
        gsonManager.initGson();
        gsonManager.registerAdapters(gsonManager.gsonBuilder);
    }

    @Override
    public void init(LauncherInitContext initContext) {
        registerEvent(this::finish, LaunchServerPostInitPhase.class);
    }

    public void finish(LaunchServerPostInitPhase context) {
        WebSocketService.providers.register("filehash", FileHashResponse.class);
    }
}
