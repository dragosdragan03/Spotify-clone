package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.SongInput;
import main.Output;

import java.util.ArrayList;

public class ShowPreferredSongs extends CommandExecute {

    private ArrayList<String> result = new ArrayList<>();

    public ShowPreferredSongs(final Command command, final LibraryInput library) {
        super(command, library);
    }

    /**
     * adauga in arraylist melodiile placute de utilizator
     */
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getLikedSongs().size() != 0) { // sa vad daca a fost ceva adaugat pana acm
            for (SongInput iter : user.getLikedSongs()) {
                this.result.add(iter.getName());
            }
        }
    }

    /**
     *
     * @return arraylist ul generat in metoda execute cu numele melodiilor
     */
    @Override
    public Output generateOutput() {
        execute();
        Output output = new Output(getCommand(), getUsername(), getTimestamp());
        output.setResult();
        for (String iter : this.result) {
            output.getResult().add(iter);
        }
        return output;
    }
}
