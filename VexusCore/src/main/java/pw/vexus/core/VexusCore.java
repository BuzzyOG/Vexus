package pw.vexus.core;

import lombok.Getter;
import net.cogzmc.core.modular.ModularPlugin;
import net.cogzmc.core.modular.ModuleMeta;
import pw.vexus.core.commands.Confirmer;
import pw.vexus.core.commands.SpawnCommand;
import pw.vexus.core.commands.TpaCommand;
import pw.vexus.core.commands.TpahereCommand;
import pw.vexus.core.econ.EconomyManager;
import pw.vexus.core.econ.command.BalanceCommand;
import pw.vexus.core.econ.command.EconCommand;
import pw.vexus.core.econ.command.PayCommand;

@ModuleMeta(name = "VexusCore", description = "Vexus shiz")
public final class VexusCore extends ModularPlugin {
    @Getter private static VexusCore instance;

    @Getter private EconomyManager economyManager;
    @Getter private DataJSONFile dataJSONFile;

    @Override
    protected void onModuleEnable() throws Exception {
        instance = this;
        economyManager = new EconomyManager();

        registerCommand(new BalanceCommand());
        registerCommand(new PayCommand());
        registerCommand(new EconCommand());

        registerCommand(new SpawnCommand());
        registerCommand(new TpaCommand());
        registerCommand(new TpahereCommand());

        new Confirmer.ConfirmerDriver();
    }

    @Override
    protected void onModuleDisable() throws Exception {
        instance = null;
    }
}
