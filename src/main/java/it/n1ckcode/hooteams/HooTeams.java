package it.n1ckcode.hooteams;

import it.n1ckcode.hooteams.commands.MainCommand;
import it.n1ckcode.hooteams.commands.TeamCommand;
import it.n1ckcode.hooteams.config.FileManager;
import it.n1ckcode.hooteams.storage.DatabaseManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class HooTeams extends JavaPlugin {

    @Getter
    private static HooTeams instance;

    @Getter
    private static FileManager fileManager;

    private DatabaseManager databaseManager;



    @Override
    public void onEnable() {
        instance = this;
        HooTeams.fileManager = new FileManager(HooTeams.instance);
        loadCommands();

        databaseManager = new DatabaseManager();
        databaseManager.connect();
    }

    private void loadCommands() {
        getCommand("hooteams").setExecutor(new MainCommand());
        getCommand("team").setExecutor(new TeamCommand(databaseManager));
    }

    @Override
    public void onDisable() {
        databaseManager.closeConnection();
    }
}
