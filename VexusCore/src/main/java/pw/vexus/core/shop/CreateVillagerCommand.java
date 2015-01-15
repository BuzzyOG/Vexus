package pw.vexus.core.shop;

import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Sound;
import pw.vexus.core.VexusCommand;
import pw.vexus.core.VexusCore;

@CommandMeta(aliases = {"addvillager", "avil"}, description = "Adds a villager where you stand, exactly. Configure in the json file.")
@CommandPermission("vexus.shop.admin")
public final class CreateVillagerCommand extends VexusCommand {
    public CreateVillagerCommand() {
        super("createvillager");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        VexusCore.getInstance().getShopVillagerManager().addVillager(player.getBukkitPlayer().getLocation());
        player.sendMessage(VexusCore.getInstance().getFormat("shop-created-villager"));
        player.playSoundForPlayer(Sound.LEVEL_UP);
    }
}
