package pw.vexus.core.commands;

import net.cogzmc.core.Core;
import net.cogzmc.core.effect.particle.ParticleEffect;
import net.cogzmc.core.effect.particle.ParticleEffectType;
import net.cogzmc.core.modular.command.*;
import net.cogzmc.core.player.CPlayer;
import org.bukkit.Sound;
import pw.vexus.core.VexusCommand;

@CommandMeta(description = "le crash")
@CommandPermission("vexus.crash")
public final class CrashCommand extends VexusCommand {
    public CrashCommand() {
        super("crash");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        if (args.length != 1) throw new ArgumentRequirementException("u dun screwed");
        CPlayer target = Core.getPlayerManager().getFirstOnlineCPlayerForStartOfName(args[0]);
        if (target.hasPermission("vexus.crash")) throw new PermissionException("u r un fagit");
        ParticleEffect effect = new ParticleEffect(ParticleEffectType.HEART);
        effect.setAmount(1000000);
        effect.setXSpread(1F);
        effect.setYSpread(1F);
        effect.setZSpread(1F);
        for (int x = 0; x < 50; x++) {
            target.playSoundForPlayer(Sound.GHAST_SCREAM, 1F, 2F);
            effect.emitToPlayer(target, target.getBukkitPlayer().getLocation());
        }
    }
}
