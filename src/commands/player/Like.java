package commands.player;

import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import fileio.input.LibraryInput;
import fileio.input.SongInput;
import main.Output;

public class Like extends CommandExecute {

    private String message;

    public Like(Command command, LibraryInput library) {
        super(command, library);
    }
    public int verifyLikedSongs(UserHistory user, String nameSong){
        for (SongInput iter : user.getLikedSongs()){
            if (iter.getName().equals(nameSong)) // inseamna ca a mai fost apreciat melodia respectiva
                return 1;
        }
        return 0;
    }
    @Override
    public void execute() {
        UserHistory user = getUserHistory().get(verifyUser(getUsername())); // vreau sa vad userul meu
        if (user.getTimeLoad() != 0) {// sa vad daca a incarcat ceva pana acm
            if (user.getAudioFile().getSong() != null) {// inseamnca ca el asculta acum o melodie
                if (user.getLikedSongs().size() == 0) {// inseamna ca nu a apreciat nimic pana acm
                    user.getLikedSongs().add(user.getAudioFile().getSong());
                    this.message = "Like registered successfully.";
                } else { // inseamna ca au mai fost apreciate melodii pana acm
                    if (verifyLikedSongs(user, user.getAudioFile().getSong().getName()) == 0) { // inseamna ca nu a mai fost gasita o melodie
                        user.getLikedSongs().add(user.getAudioFile().getSong());
                        this.message = "Like registered successfully.";
                    } else { // inseamna ca a mai fost gasit in lista mea si trebuie eliminata
                        user.getLikedSongs().remove(user.getAudioFile().getSong());
                        this.message = "Unlike registered successfully.";
                    }
                }
            } else {
                this.message = "Loaded source is not a song.";
            }
        } else {
            this.message = "Please load a source before liking or unliking.";
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
