package CommandCode;

public enum CommandCode {
    CREATE(1),
    RETRIEVE(2),
    UPDATE(3),
    DELETE(4);

    private final int code;

    CommandCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CommandCode getByCode(int code) {
        for (CommandCode command : values()) {
            if (command.getCode() == code) {
                return command;
            }
        }
        return null; // Code not found
    }
}
