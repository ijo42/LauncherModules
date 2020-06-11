package ru.ijo42.gravitmodules.filehasher;

import io.netty.channel.ChannelHandlerContext;
import pro.gravit.launcher.hasher.HashedDir;
import pro.gravit.launcher.hasher.HashedFile;
import pro.gravit.launchserver.socket.Client;
import pro.gravit.launchserver.socket.response.SimpleResponse;
import pro.gravit.utils.helper.SecurityHelper;

import java.io.IOException;
import java.nio.file.Path;

public class FileHashResponse extends SimpleResponse {
    public String path;

    @Override

    public String getType() {
        return "filehash";
    }

    @Override
    public void execute(ChannelHandlerContext ctx, Client client) throws IOException {
        byte[] hBinary = new byte[ 0 ];
        boolean isFile = path.contains(".");
        HashedDir dir = server.getUpdateDir(path);
        if (!isFile) {
            if (dir == null) {
                sendError(String.format("Directory %s not found", path));
                return;
            }
        } else {
            Path absFile = server.updatesDir.resolve(path);
            if (server.launcherBinary.syncBinaryFile.compareTo(absFile) == 0)
                hBinary = SecurityHelper.digest(HashedFile.DIGEST_ALGO, absFile);
            else if (server.launcherEXEBinary.syncBinaryFile.compareTo(absFile) == 0)
                hBinary = SecurityHelper.digest(HashedFile.DIGEST_ALGO, absFile);
            else {
                sendError(String.format("Binary %s not found", path));
                return;
            }
        }

        if (hBinary.length != 0)
            service.sendObjectAndClose(
                    ctx, new FileHashRequestEvent(hBinary));
        else
            service.sendObjectAndClose(
                    ctx, new FileHashRequestEvent(dir));

    }
}
