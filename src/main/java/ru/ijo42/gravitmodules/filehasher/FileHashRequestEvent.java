package ru.ijo42.gravitmodules.filehasher;

import com.google.gson.JsonArray;
import pro.gravit.launcher.events.RequestEvent;
import pro.gravit.launcher.hasher.HashedDir;
import pro.gravit.launcher.hasher.HashedFile;

public class FileHashRequestEvent extends RequestEvent {
    public final String jsonHDir;

    public FileHashRequestEvent(HashedDir hDir) {
        JsonArray n = new JsonArray();
        hDir.map().values().parallelStream().filter(hashedEntry -> hashedEntry instanceof HashedDir).map(x -> (HashedDir) x).map(FileHasherModule.gsonManager.gson::toJsonTree).forEach(n::add);
        hDir.map().values().parallelStream().filter(hashedEntry -> hashedEntry instanceof HashedFile).map(x -> (HashedFile) x).map(FileHasherModule.gsonManager.gson::toJsonTree).forEach(n::add);
        this.jsonHDir = n.toString();
    }

    public FileHashRequestEvent(byte[] binaryDigest) {
        HashedFile hFile = new HashedFile(0, binaryDigest);
        this.jsonHDir = FileHasherModule.gsonManager.gson.toJsonTree(hFile).toString();
    }

    @Override
    public String getType() {
        return "filehash";
    }

}
