package it.n1ckcode.hooteams.config;

import it.n1ckcode.hooteams.HooTeams;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class FileManager {
    @Getter
    private final FileConfiguration config;
    @Getter
    private final FileConfiguration messages;

    private Plugin instance;

    public static File configFile;

    public FileManager(Plugin plugin) {
        this.instance = plugin;
        this.config = this.saveConfig("config.yml");
        this.messages = this.saveConfig("messages.yml");
    }

    public void saveFile(FileConfiguration configuration, File file) {
        try {
            configuration.save(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    private FileConfiguration saveConfig(String configName) {
        File file = new File(HooTeams.getInstance().getDataFolder(), configName);
        if (!file.exists()) {
            this.instance.saveResource(configName, false);
        }
        return this.loadConfig(file);
    }

    public FileConfiguration loadConfig(File file) {
        YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        }
        catch (IOException | InvalidConfigurationException ex3) {
            Throwable ex2;
            Throwable ex = ex2 = null;
            ex.printStackTrace();
        }
        return configuration;
    }


}