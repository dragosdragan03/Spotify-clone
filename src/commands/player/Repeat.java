package commands.player;

import commands.Command;
import commands.CommandExecute;
import fileio.input.LibraryInput;

public class Repeat extends CommandExecute {

    private String message;

    public Repeat(Command command, LibraryInput library) {
        super(command, library);
    }

    @Override
    public void execute() {

    }
}
