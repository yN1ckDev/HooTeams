package it.n1ckcode.hooteams.commands;

import it.n1ckcode.hooteams.commands.api.AbstractCommand;
import it.n1ckcode.hooteams.storage.DatabaseManager;
import it.n1ckcode.hooteams.utils.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand extends AbstractCommand {

    private DatabaseManager databaseManager;

    public TeamCommand(DatabaseManager databaseManager) {
        super("team", "hooteams.commands.team", false);
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0) {
           player.sendMessage(CC.colorize("&cSyntax error: /team create <name>"));
        } else if (args[0].equalsIgnoreCase("create")) {
            if(!databaseManager.canCreateClan(player)) {
                player.sendMessage(CC.getFormattedText("errors.cannot-create-team"));
            }
            if (args.length == 3) {
                String name = args[1];
                databaseManager.createClan(player.getName(), name);
                player.sendMessage(CC.getFormattedText("teams.created"));
            } else {
                player.sendMessage(CC.colorize("&cSyntax error: /team create <name>"));
            }
        } else if (args[0].equalsIgnoreCase("rename")) {
            if (args.length == 3) {
                String oldName = args[1];
                String newName = args[2];
                databaseManager.renameClan(oldName, newName);
                player.sendMessage(CC.getFormattedText("teams.renamed"));
            } else {
                player.sendMessage(CC.colorize("&cSyntax error: /team rename <old-name> <new-name>"));
            }

        } else if (args[0].equalsIgnoreCase("disband")) {
            if(!databaseManager.hasClan(String.valueOf(player))) {
                player.sendMessage(CC.getFormattedText("errors.cannot-disband-team"));
            }
            if (args.length == 2) {
                String name = args[1];
                databaseManager.disbandClan(name);
                player.sendMessage(CC.getFormattedText("teams.deleted"));
            } else {
                player.sendMessage(CC.colorize("&cSyntax error: /team delete <name>"));
            }

        }
    }
}
