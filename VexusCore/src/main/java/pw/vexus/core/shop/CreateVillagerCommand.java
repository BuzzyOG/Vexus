package pw.vexus.core.shop;

import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import pw.vexus.core.VexusCommand;

@CommandMeta(aliases = {"addvillager", "avil"}, description = "Adds a villager where you stand, exactly. Configure in the json file.")
@CommandPermission("vexus.shop.admin")
public final class CreateVillagerCommand extends VexusCommand {
    public CreateVillagerCommand() {
        super("createvillager");
    }
}
