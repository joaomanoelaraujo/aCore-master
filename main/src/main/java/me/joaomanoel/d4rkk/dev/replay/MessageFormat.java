/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public class MessageFormat {
    private String syntaxMessage;
    private String overviewMessage;
    private String permissionMessage;
    private String consoleMessage;
    private String notFoundMessage;

    public MessageFormat syntax(String syntaxMessage) {
        this.syntaxMessage = syntaxMessage;
        return this;
    }

    public MessageFormat overview(String overviewMessage) {
        this.overviewMessage = overviewMessage;
        return this;
    }

    public MessageFormat permission(String permissionMessage) {
        this.permissionMessage = permissionMessage;
        return this;
    }

    public MessageFormat console(String consoleMessage) {
        this.consoleMessage = consoleMessage;
        return this;
    }

    public MessageFormat notFound(String notFoundMessage) {
        this.notFoundMessage = notFoundMessage;
        return this;
    }

    public String getSyntaxMessage(String command, String arg) {
        return new MessageBuilder(this.syntaxMessage).set("command", command).set("args", arg).build();
    }

    public String getOverviewMessage(String command, String arg, String desc) {
        return new MessageBuilder(this.overviewMessage).set("command", command).set("args", arg).set("desc", desc).build();
    }

    public String getConsoleMessage() {
        return this.consoleMessage != null ? this.consoleMessage : "You must be a player to execute this command.";
    }

    public String getPermissionMessage() {
        return this.permissionMessage;
    }

    public String getNotFoundMessage() {
        return this.notFoundMessage;
    }
}

