package pw.vexus.core;

import lombok.Getter;
import net.cogzmc.core.modular.ModularPlugin;
import net.cogzmc.core.modular.ModuleMeta;
import pw.vexus.core.commands.*;
import pw.vexus.core.econ.EconomyManager;
import pw.vexus.core.econ.command.BalanceCommand;
import pw.vexus.core.econ.command.EconCommand;
import pw.vexus.core.econ.command.PayCommand;
import pw.vexus.core.home.DelHomeCommand;
import pw.vexus.core.home.HomeCommand;
import pw.vexus.core.home.HomeManager;
import pw.vexus.core.home.SetHomeCommand;
import pw.vexus.core.shop.ShopManager;
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
    @Getter private HomeManager homeManager;
    @Getter private ShopManager shopManager;

    @Override
    protected void onModuleEnable() throws Exception {
        instance = this;
        economyManager = new EconomyManager();
        warpManager = new WarpManager(new File(getDataFolder(), "warps.json"));
        homeManager = new HomeManager();
        shopManager = new ShopManager(new File(getDataFolder(), "shop.csv"));

        registerCommand(new BalanceCommand());
        registerCommand(new PayCommand());
        registerCommand(new EconCommand());

        registerCommand(new FlyCommand());
        registerCommand(new NearCommand());
        registerCommand(new GamemodeCommand());
        registerCommand(new HealCommand());
        registerCommand(new FeedCommand());
        registerCommand(new PlayerTimeCommand());
        registerCommand(new VanishCommand());
        registerCommand(new CrashCommand());

        registerCommand(new TpHereCommand());
        registerCommand(new TpCommand());
        registerCommand(new TpaCommand());
        registerCommand(new TpahereCommand());

        registerCommand(new ClearInvCommand());
        registerCommand(new ClearChatCommand());
        registerCommand(new ListCommand());

        registerCommand(new WarpCommand());
        registerCommand(new SetwarpCommand());
        registerCommand(new DelWarpCommand());
        registerCommand(new SpawnCommand());
        registerCommand(new SetspawnCommand());

        registerCommand(new HomeCommand());
        registerCommand(new SetHomeCommand());
        registerCommand(new DelHomeCommand());

        getServer().getPluginManager().registerEvents(new MessageModifier(), this);

        new Confirmer.ConfirmerDriver();
    }

    @Override
    protected void onModuleDisable() throws Exception {
        warpManager.save();
        instance = null;
    }
}
