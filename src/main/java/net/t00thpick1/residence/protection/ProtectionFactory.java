package net.t00thpick1.residence.protection;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.t00thpick1.residence.Residence;
import net.t00thpick1.residence.api.EconomyManager;
import net.t00thpick1.residence.api.ResidenceManager;
import net.t00thpick1.residence.api.UsernameUUIDCache;
import net.t00thpick1.residence.api.WorldManager;
import net.t00thpick1.residence.api.areas.CuboidArea;
import net.t00thpick1.residence.protection.yaml.YAMLResidenceManager;
import net.t00thpick1.residence.protection.yaml.YAMLUsernameUUIDCache;
import net.t00thpick1.residence.protection.yaml.YAMLWorldManager;

public class ProtectionFactory {
    private static UsernameUUIDCache usernameUUIDCache;
    private static ResidenceManager residenceManager;
    private static CuboidAreaFactory cuboidAreaFactory;
    private static WorldManager worldManager;
    private static EconomyManager economyManager;
    private static SimpleGroupManager groupManager;

    public static void init(Residence residence) throws Exception {
        switch (residence.getBackend()) {
            case MYSQL_LIVE:
                throw new UnsupportedOperationException();
            case MYSQL_MEMORY:
                throw new UnsupportedOperationException();
            case WORLDGUARD:
                throw new UnsupportedOperationException();
            case YAML:
                File dataFolder = residence.getDataFolder();
                economyManager = new MemoryEconomyManager();
                usernameUUIDCache = new YAMLUsernameUUIDCache(new File(dataFolder, "UsernameUUIDCache.yml"));
                residenceManager = new YAMLResidenceManager(dataFolder);

                File groupsFile = new File(dataFolder, "groups.yml");
                if (!groupsFile.isFile()) {
                    groupsFile.createNewFile();
                    FileConfiguration internalConfig = YamlConfiguration.loadConfiguration(residence.getResource("groups.yml"));
                    internalConfig.save(groupsFile);
                }

                groupManager = new SimpleGroupManager(YamlConfiguration.loadConfiguration(groupsFile));

                File worldFolder = new File(dataFolder, "WorldConfigurations");
                if (!worldFolder.isDirectory()) {
                    worldFolder.mkdirs();
                }
                worldManager = new YAMLWorldManager(worldFolder);
                cuboidAreaFactory = new MemoryCuboidArea.MemoryCuboidAreaFactory();
        }
    }

    public static CuboidArea createNewCuboidArea(Location loc1, Location loc2) {
        return cuboidAreaFactory.createArea(loc1, loc2);
    }

    public static ResidenceManager getResidenceManager() {
        return residenceManager;
    }

    public static WorldManager getWorldManager() {
        return worldManager;
    }

    public static EconomyManager getEconomyManager() {
        return economyManager;
    }

    public static UsernameUUIDCache getUsernameUUIDCache() {
        return usernameUUIDCache;
    }

    public static SimpleGroupManager getGroupManager() {
        return groupManager;
    }

    public static void save() {
        try {
            residenceManager.save();
            usernameUUIDCache.save();
        } catch (IOException e) {
            Residence.getInstance().getLogger().log(Level.SEVERE, "SEVERE SAVE ERROR", e);
        }
    }
}
