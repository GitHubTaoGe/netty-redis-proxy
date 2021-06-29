package com.netty.command;

public enum Command {
    GET("get", 1),

    DEL("del", 1),

    SET("set", 2),

    SETEX("set", 3);


    Command(String command, int len) {
        this.command = command;
        this.len = len;
    }

    public static Command codeOf(String command) {
        for (Command e : Command.values()) {
            if (e.getCommand().equals(command)) {
                return e;
            }
        }
        return null;
    }

    private String command;

    private int len;

    public String getCommand() {
        return command;
    }

    public int getLen() {
        return len;
    }
}
