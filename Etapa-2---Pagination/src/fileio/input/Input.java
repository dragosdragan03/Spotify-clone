package fileio.input;

import java.util.ArrayList;

public final class Input {
    private LibraryInput library;
    private ArrayList<UserInput> users;
    private ArrayList<CommandInput> commands;

    public Input() {
    }

    public LibraryInput getLibrary() {
        return library;
    }

    public void setLibrary(final LibraryInput library) {
        this.library = library;
    }

    public ArrayList<UserInput> getUsers() {
        return users;
    }

    public void setUsers(final ArrayList<UserInput> users) {
        this.users = users;
    }

    public ArrayList<CommandInput> getCommands() {
        return commands;
    }

    public void setCommands(final ArrayList<CommandInput> commands) {
        this.commands = commands;
    }

    @Override
    public String toString() {
        return "AppInput{"
                + "library=" + library
                + ", users=" + users
                + ", commands=" + commands
                + '}';
    }
}
