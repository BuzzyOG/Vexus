package pw.vexus.core.econ;

import net.cogzmc.core.player.COfflinePlayer;
import net.cogzmc.core.player.DatabaseConnectException;
import pw.vexus.core.VexusCore;

/**
 * Manages the economy of the server
 */
public final class EconomyManager {
    private final double defaultBalance = VexusCore.getInstance().getConfig().getDouble("default-econ-balance");

    /**
     * Gets the balance of a player
     * @param player The player whom we are getting the balance of
     * @return The exact balance of the player
     */
    public double getBalance(COfflinePlayer player) {
        return player.containsSetting("econ-balance") ? player.getSettingValue("econ-balance", Double.class) : defaultBalance;
    }

    /**
     * This method will add or remove (based on positive or negative value of {@code amount}) balance from a player's account
     * @param player The player whom we are affecting
     * @param amount The balance we are affecting
     */
    public void modifyBalance(COfflinePlayer player, double amount) throws DatabaseConnectException {
        player.storeSettingValue("econ-balance", getBalance(player)+amount);
        player.saveIntoDatabase();
    }

    /**
     * Resets the balance of the player by purging the data from the database
     * @param player The player whom we are resetting the balance of.
     * @throws DatabaseConnectException
     */
    public void resetBalance(COfflinePlayer player) throws DatabaseConnectException {
        player.removeSettingValue("econ-balance");
        player.saveIntoDatabase();
    }

    public static String format(Double amount) {
        return String.format("$%.02f", amount);
    }
}
