package commands;

public class Command {
    private String command;
    private String username;
    private int timestamp;
    private String type;
    private int itemNumber;
    private FilterInput filters;
    private int playlistId;
    private String playlistName;
    private int seed;

    public Command() {
    }

    /**
     *
     * @return ce fel de comanda a fost data
     */
    public String getCommand() {
        return command;
    }

    /**
     *
     * @return userul care executa comanda
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return momentul de timp cand se executa comanda
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * o sa l folosesc in search
     * @return tipul fisierului pe care il cauta
     */
    public String getType() {
        return type;
    }

    /**
     * il folosesc in select
     * @return numarul fisieruluipe care trebuie sa l selectez
     */
    public int getItemNumber() {
        return itemNumber;
    }

    /**
     * il folosesc in search
     * @return filtrele dupa care caut un AudioFile
     */
    public FilterInput getFilters() {
        return filters;
    }

    /**
     *
     * @return indexul playlistului
     */
    public int getPlaylistId() {
        return playlistId;
    }

    /**
     * il folosesc in addInRemove
     * @return numele playlistului pe care vreau sa l adaug/sterg
     */
    public String getPlaylistName() {
        return playlistName;
    }

    /**
     * il folosesc in shuffle
     * @return seed ul dupa care fac shuffle ul
     */
    public int getSeed() {
        return seed;
    }
}
