package commands.player;

import commands.Command;
import commands.CommandExecute;
import fileio.input.LibraryInput;
import main.Output;

public class Load extends CommandExecute {

    private String message;
    public Load(Command command, LibraryInput library) {
        super(command, library);
    }

    @Override
    public void execute() {
        if (getUserHistory().get(verifyUser(getUsername())).getAudioFile() != null) { // asta inseamna ca s a selectat ceva deja
            this.message = "Playback loaded successfully."; // retin mesajul si l afisez
            // chiar daca am fct load ul vreau sa pastrez valoarea selectata ca sa o pot prelucra
            getUserHistory().get(verifyUser(getUsername())).setTimeLoad(getTimestamp()); // retin cand a fost incarcat fisierul
            getUserHistory().get(verifyUser(getUsername())).setPlayPauseResult(true); // inseamna ca i am dat play
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
