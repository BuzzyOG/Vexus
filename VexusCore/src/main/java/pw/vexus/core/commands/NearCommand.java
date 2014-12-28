package pw.vexus.core.commands;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Data;
import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.modular.command.CommandMeta;
import net.cogzmc.core.modular.command.CommandPermission;
import net.cogzmc.core.modular.command.ModuleCommand;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.util.Point;
import pw.vexus.core.VexusCore;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

@CommandPermission("vexus.near")
@CommandMeta(description = "Lists nearby players")
public final class NearCommand extends ModuleCommand {
    public NearCommand() {
        super("near");
    }

    @Override
    protected void handleCommand(CPlayer player, String[] args) throws CommandException {
        BiMap<CPlayer, Double> distances = HashBiMap.create();
        Point point = player.getPoint();
        for (CPlayer cPlayer : Core.getPlayerManager()) {
            if (cPlayer.equals(player)) continue;
            if (cPlayer.hasPermission("vexus.near.hide") && !player.hasPermission("vexus.near.showall")) continue;
            Double aDouble = cPlayer.getPoint().distanceSquared(point);
            if (aDouble < 2500) distances.put(cPlayer, Math.sqrt(aDouble));
        }

        player.sendMessage(VexusCore.getInstance().getFormat("near-head"));
        if (distances.size() == 0) {
            player.sendMessage(VexusCore.getInstance().getFormat("near-none"));
            return;
        }

        Set<CPlayer> cPlayers = distances.keySet();
        CPlayer[] cPlayers1 = cPlayers.toArray(new CPlayer[cPlayers.size()]);
        Arrays.sort(cPlayers1, new DistanceInvertedComparator(distances));

        for (CPlayer cPlayer : cPlayers1) {
            player.sendMessage(VexusCore.getInstance().getFormat("near-line", new String[]{"<distance>", String.format("%.02f", distances.get(cPlayer))}, new String[]{"<name>", cPlayer.getDisplayName()}));
        }

    }

    @Data
    private class DistanceInvertedComparator implements Comparator<CPlayer> {
        private final Map<CPlayer, Double> data;

        @Override
        public int compare(CPlayer o1, CPlayer o2) {
            return data.get(o2) > data.get(o1) ? 1 : -1;
        }
    }
}
