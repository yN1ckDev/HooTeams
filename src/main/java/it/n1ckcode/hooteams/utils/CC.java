package it.n1ckcode.hooteams.utils;

import it.n1ckcode.hooteams.HooTeams;
import org.bukkit.ChatColor;

public class CC {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getFormattedText(String path) {
        return CC.colorize(HooTeams.getFileManager().getMessages().getString(path));
    }
}
