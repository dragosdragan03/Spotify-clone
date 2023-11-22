package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import main.Output;

public class Next extends CommandExecute {

    private String message;

    public Next(Command command, LibraryInput library) {
        super(command, library);
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getTimeLoad() != 0) { // inseamna ca am incarcat ceva
            FindTrack findTrack = new FindTrack(user, getTimestamp());

        } else {
            this.message = "Please load a source before skipping to the next track.";
        }
    }

    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.outputMessage(this.message);
        return output;
    }
}
