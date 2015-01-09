package pw.vexus.core;

import com.google.gson.*;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import lombok.Getter;
import net.cogzmc.core.Core;
import net.cogzmc.core.modular.ModularPlugin;
import net.cogzmc.core.modular.ModuleMeta;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import pw.vexus.core.announcer.AnnouncerManager;
import pw.vexus.core.announcer.cmd.AnnouncerCommand;
import pw.vexus.core.chat.ChatFeaturesListener;
import pw.vexus.core.chat.ChatListener;
import pw.vexus.core.commands.*;
import pw.vexus.core.econ.EconomyManager;
import pw.vexus.core.econ.command.BalanceCommand;
import pw.vexus.core.econ.command.EconCommand;
import pw.vexus.core.econ.command.PayCommand;
import pw.vexus.core.home.DelHomeCommand;
import pw.vexus.core.home.HomeCommand;
import pw.vexus.core.home.HomeManager;
import pw.vexus.core.home.SetHomeCommand;
import pw.vexus.core.pvp.EngageEnderBar;
import pw.vexus.core.pvp.FoodCorrection;
import pw.vexus.core.pvp.PvPListener;
import pw.vexus.core.pvp.PvPTagManager;
import pw.vexus.core.shop.ShopManager;
import pw.vexus.core.specials.EnderBarManager;
import pw.vexus.core.specials.FactionsFeature;
import pw.vexus.core.warp.DelWarpCommand;
import pw.vexus.core.warp.SetwarpCommand;
import pw.vexus.core.warp.WarpCommand;
import pw.vexus.core.warp.WarpManager;

import java.io.File;

@ModuleMeta(name = "VexusCore", description = "Vexus shiz")
public final class VexusCore extends ModularPlugin {
    private static final String X_KEY = "x", Y_KEY = "y", Z_KEY = "z", PITCH_KEY = "p", YAW_KEY = "ya", WORLD_KEY = "world";

    @Getter private static VexusCore instance;

    @Getter private EconomyManager economyManager;
    @Getter private WarpManager warpManager;
    @Getter private HomeManager homeManager;
    @Getter private ShopManager shopManager;
    @Getter private AnnouncerManager announcerManager;
    @Getter private PvPTagManager pvpTagManager;
    @Getter private WorldGuardPlugin wgPlugin;

    @Getter private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Location.class, (JsonSerializer<Location>) (location, type, jsonSerializationContext) -> {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(X_KEY, jsonSerializationContext.serialize(location.getX()));
        jsonObject.add(Y_KEY, jsonSerializationContext.serialize(location.getY()));
        jsonObject.add(Z_KEY, jsonSerializationContext.serialize(location.getZ()));
        jsonObject.add(PITCH_KEY, jsonSerializationContext.serialize(location.getYaw()));
        jsonObject.add(YAW_KEY, jsonSerializationContext.serialize(location.getPitch()));
        jsonObject.add(WORLD_KEY, jsonSerializationContext.serialize(location.getWorld().getName()));
        return jsonObject;
    }).registerTypeAdapter(Location.class, (JsonDeserializer<Location>) (jsonElement, type, jsonDeserializationContext) -> {
        JsonObject asJsonObject = jsonElement.getAsJsonObject();
        return new Location(
                Bukkit.getWorld(asJsonObject.get(WORLD_KEY).getAsString()),
                asJsonObject.get(X_KEY).getAsDouble(),
                asJsonObject.get(Y_KEY).getAsDouble(),
                asJsonObject.get(Z_KEY).getAsDouble(),
                asJsonObject.get(YAW_KEY).getAsFloat(),
                asJsonObject.get(PITCH_KEY).getAsFloat()
        );
    }).create();

    @Override
    protected void onModuleEnable() throws Exception {
        instance = this;

        Core.getPlayerManager().registerCPlayerConnectionListener(new EnderBarManager.EnderBarLoginObserver());

        economyManager = new EconomyManager();
        warpManager = new WarpManager(new File(getDataFolder(), "warps.json"));
        homeManager = new HomeManager();
        shopManager = new ShopManager(new File(getDataFolder(), "shop.csv"));
        wgPlugin = (WorldGuardPlugin) VexusCore.getProvidingPlugin(WorldGuardPlugin.class);
        announcerManager = new AnnouncerManager(new File(getDataFolder(), "announcements.json"));
        pvpTagManager = new PvPTagManager();

        registerCommand(new RulesCommand(new File(getDataFolder(), "rule.txt")));

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
        registerCommand(new SuicideCommand());
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

        registerCommand(new AnnouncerCommand());

        registerListener(new ChatListener());
        registerListener(new MessageModifier());
        registerListener(new ChatFeaturesListener());
        registerListener(new PvPListener());
        registerListener(new FoodCorrection());
        registerListener(new EngageEnderBar());
        registerListener(new FactionsFeature());

        new Confirmer.ConfirmerDriver();
    }

    @Override
    protected void onModuleDisable() throws Exception {
        warpManager.save();
        homeManager.save();
        announcerManager.save();
        instance = null;
    }
}
