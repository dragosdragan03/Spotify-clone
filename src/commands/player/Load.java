package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import main.Output;

public class Load extends CommandExecute {

    private String message;
    public Load(Command command, LibraryInput library) {
        super(command, library);
    }

    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername()));
        if (user.getAudioFile() != null) { // asta inseamna ca s a selectat ceva deja
            if (user.getAudioFile().getPlaylist() != null && user.getAudioFile().getPlaylist().getListSongs().size() != 0) {
                this.message = "Playback loaded successfully."; // retin mesajul si l afisez
                // chiar daca am fct load ul vreau sa pastrez valoarea selectata ca sa o pot prelucra
                user.setTimeLoad(getTimestamp()); // retin cand a fost incarcat fisierul
                user.setPlayPauseResult(true); // inseamna ca i am dat play
                return;
            }
            if (user.getAudioFile().getPlaylist() != null && user.getAudioFile().getPlaylist().getListSongs().size() == 0){
                this.message = "You can't load an empty audio collection!";
                return;
            }
            this.message = "Playback loaded successfully."; // retin mesajul si l afisez
            // chiar daca am fct load ul vreau sa pastrez valoarea selectata ca sa o pot prelucra
            user.setTimeLoad(getTimestamp()); // retin cand a fost incarcat fisierul
            user.setPlayPauseResult(true); // inseamna ca i am dat play
        }
        else
        {
            this.message = "Please select a source before attempting to load.";
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
