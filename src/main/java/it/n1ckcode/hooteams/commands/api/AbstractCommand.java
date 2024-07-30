package it.n1ckcode.hooteams.commands.api;

import it.n1ckcode.hooteams.HooTeams;
import it.n1ckcode.hooteams.utils.CC;
import org.bukkit.command.*;
import org.bukkit.*;


public abstract class AbstractCommand implements CommandExecutor
{
    String command;
    String permissions;
    boolean useConsole;

    public AbstractCommand(final String command, final String permissions, final boolean useConsole) {
        this.command = command;
        this.permissions = permissions;
        this.useConsole = useConsole;
        HooTeams.getInstance().getCommand(command).setExecutor((CommandExecutor)this);
    }

    public abstract void execute(final CommandSender sender, final String[] args);

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!this.useConsole && sender instanceof ConsoleCommandSender) {
            Bukkit.getLogger().warning("Non puoi eseguire questo comando dalla Console!");
            return true;
        }
        if (this.permissions == null) {
            this.execute(sender, args);
            return true;
        }
        if (!sender.hasPermission(this.permissions)) {
            sender.sendMessage(CC.getFormattedText("errors.no-permission"));
            return true;
        }
        this.execute(sender, args);
        return true;
    }
}