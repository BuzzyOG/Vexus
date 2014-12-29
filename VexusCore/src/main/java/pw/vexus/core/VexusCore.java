package pw.vexus.core;

import lombok.Getter;
import net.cogzmc.core.modular.ModularPlugin;
import net.cogzmc.core.modular.ModuleMeta;
import pw.vexus.core.commands.*;
import pw.vexus.core.econ.EconomyManager;
import pw.vexus.core.econ.command.BalanceCommand;
import pw.vexus.core.econ.command.EconCommand;
import pw.vexus.core.econ.command.PayCommand;
import pw.vexus.core.warp.DelWarpCommand;
import pw.vexus.core.warp.SetwarpCommand;
import pw.vexus.core.warp.WarpCommand;
import pw.vexus.core.warp.WarpManager;

import java.io.File;

@ModuleMeta(name = "VexusCore", description = "Vexus shiz")
public final class VexusCore extends ModularPlugin {
    @Getter private static VexusCore instance;

    @Getter private EconomyManager economyManager;
    @Getter private WarpManager warpManager;

    @Override
    protected void onModuleEnable() throws Exception {
        instance = this;
        economyManager = new EconomyManager();
        warpManager = new WarpManager(new File(getDataFolder(), "warps.json"));

        registerCommand(new BalanceCommand());
        registerCommand(new PayCommand());
        registerCommand(new EconCommand());

        registerCommand(new SpawnCommand());
        registerCommand(new SetspawnCommand());
        registerCommand(new TpaCommand());
        registerCommand(new TpahereCommand());
        registerCommand(new FlyCommand());
        registerCommand(new NearCommand());

        registerCommand(new GamemodeCommand());
        registerCommand(new HealCommand());
        registerCommand(new FeedCommand());
        registerCommand(new TpHereCommand());
        registerCommand(new TpCommand());

        registerCommand(new WarpCommand());
        registerCommand(new SetwarpCommand());
        registerCommand(new DelWarpCommand());

        new Confirmer.ConfirmerDriver();
    }

    @Override
    protected void onModuleDisable() throws Exception {
        warpManager.save();
        instance = null;
    }
}
