package ru.gravit.launcher.addhash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import ru.gravit.launchserver.auth.AuthException;
import ru.gravit.launchserver.auth.ClientPermissions;
import ru.gravit.launchserver.auth.MySQLSourceConfig;
import ru.gravit.launchserver.auth.provider.AuthProvider;
import ru.gravit.launchserver.auth.provider.AuthProviderResult;
import ru.gravit.utils.helper.CommonHelper;
import ru.gravit.utils.helper.SecurityHelper;

public final class MySQLBcryptAuthProvider extends AuthProvider {
    private MySQLSourceConfig mySQLHolder;
    private String query;
    private String message;
    private String[] queryParams;
    private boolean usePermission;

    @Override
    public AuthProviderResult auth(String login, String password, String ip) throws SQLException, AuthException {
        Connection c = mySQLHolder.getConnection();
        PreparedStatement s = c.prepareStatement(query);
        String[] replaceParams = {"login", login, "password", password, "ip", ip};
        for (int i = 0; i < queryParams.length; i++)
            s.setString(i + 1, CommonHelper.replace(queryParams[i], replaceParams));

        // Execute SQL query
        s.setQueryTimeout(MySQLSourceConfig.TIMEOUT);
        try (ResultSet set = s.executeQuery()) {
            return set.next() ? BCrypt.checkpw(password, set.getString(1)) ? new AuthProviderResult(set.getString(2), SecurityHelper.randomStringToken(), usePermission ? new ClientPermissions(set.getLong(3)) : new ClientPermissions()) : authError(message) : authError(message);
        }
    }

    @Override
    public void close() {
        // Do nothing
    }
}