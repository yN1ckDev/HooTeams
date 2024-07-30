package it.n1ckcode.hooteams.commands;

import it.n1ckcode.hooteams.commands.api.AbstractCommand;
import it.n1ckcode.hooteams.utils.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand extends AbstractCommand {
    public MainCommand() {
        super("hooteams", null, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        player.sendMessage(CC.colorize("&6&l========================================"));
        player.sendMessage(CC.colorize("&7This server is running &eHooTeams &7v1.0"));
        player.sendMessage(CC.colorize("&7Plugin completely developed by &eN1ckCode"));
        player.sendMessage(CC.colorize("&6&l========================================"));
    }
}
