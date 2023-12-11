package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.Podcast;
import app.audio.Collections.Playlist;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.pageSystem.HomePage;
import app.pageSystem.LikedContentPage;
import app.pageSystem.Page;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type User.
 */
public class User {
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    @Getter
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    @Getter
    @Setter
    private boolean online = true;
    @Setter
    @Getter
    private Page currentPage = new HomePage();
    @Getter
    @Setter
    private String userPage;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.userPage = username;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        List<LibraryEntry> libraryEntries = searchBar.search(filters, type);

        for (LibraryEntry libraryEntry : libraryEntries) {
            results.add(libraryEntry.getName());
        }
        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        LibraryEntry selected = searchBar.select(itemNumber);

        if (selected == null) {
            return "The selected ID is too high.";
        }

        if (searchBar.getLastSearchType().equals("artist")
                || searchBar.getLastSearchType().equals("host")) {
            this.currentPage = Objects.requireNonNull(Admin.getUser(selected.getName()))
                    .getCurrentPage();
            this.userPage = selected.getName();
            return "Successfully selected %s's page.".formatted(selected.getName());
        }

        return "Successfully selected %s.".formatted(selected.getName());
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (!online) {
            return username + " is offline.";
        }

        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
                && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * schimba statusul unui user din online in ofline si invers
     *
     * @return daca s a putut realiza schimbarea
     */
    public String switchConnectionStatus() {
        if (!isHost() && !isArtist()) {
            online = !online;
            return username + " has changed status successfully.";
        } else {
            return username + " is not a normal user.";
        }
    }

    /**
     * adauga un album in lista de albumuri a artistului
     *
     * @param songs       lista de melodii care trebuie adauga in album
     * @param name        numele albumului
     * @param releaseYear anul lansarii albumului
     * @param description descrierea albumului
     * @return mesajul daca s a putuu adauga un album
     */
    public String addAlbum(final List<SongInput> songs, final String name, final int releaseYear,
                           final String description) {
        return username + " is not an artist.";
    }

    /**
     * sterge un album din lista
     *
     * @param name numele albumului
     * @return mesajul daca a putut fi sters sau nu
     */
    public String removeAlbum(final String name) {
        return username + " is not an artist.";
    }

    /**
     * @return pagina userului
     */
    public String printCurrentPage() {
        if (online) {
            return currentPage.printCurrentPage(Admin.getUser(userPage));
        } else {
            return username + " is offline.";
        }
    }

    /**
     * @param nameEvent   numele evenimentului
     * @param date        data evenimentului
     * @param description descrierea evenimentului
     * @return mesajul daca s a putut adauga evenimentul
     */
    public String addEvent(final String nameEvent, final String date, final String description) {
        return username + " is not an artist.";
    }

    /**
     * sterge un eveniment din lista de evenimente a artistului
     *
     * @param nameEvent numele evenimentului
     * @return daca s a putut sterge sau nu evenimentul
     */
    public String removeEvent(final String nameEvent) {
        return username + " is not an artist.";
    }

    /**
     * @return lista de evenimente a unui artist
     */
    public List<Artist.Event> getEventsOfAnArtist() {
        return null;
    }

    /**
     * @param name        numele merchului
     * @param description descrierea
     * @param price       pretul
     * @return mesajul daca s a putut adauga in lista, merchul
     */
    public String addMerch(final String name, final String description, final int price) {
        return username + " is not an artist.";
    }

    /**
     * @return lista de merchuri a unui artist
     */
    public List<Artist.Merch> getMerchandiseOfAnArtist() {
        return null;
    }

    /**
     * adauga un podcast in lista de podcasturi a hostului
     *
     * @param name     numele podcastului
     * @param episodes lista de episoade
     * @return mesajul daca s a putut adauga un podcast
     */
    public String addPodcast(final String name, final List<EpisodeInput> episodes) {
        return username + " is not a host.";
    }

    /**
     * sterge u podcast din lista hostului
     *
     * @param name numele podcastului pe care vreau sa l sterg
     * @return mesajul daca s a putut sterge podcastul
     */
    public String removePodcast(final String name) {
        return username + " is not a host.";
    }

    /**
     * @return lista de podcasturi a unui host
     */
    public List<Podcast> getPodcastsHost() {
        return null;
    }

    /**
     * adauga un anunt in lista de anunturi a hostului
     *
     * @param name        numele anuntului
     * @param description descrierea anuntului
     * @return mesajul daca s a putut adauga anuntul
     */
    public String addAnnouncement(final String name, final String description) {
        return username + " is not a host.";
    }

    /**
     * sterge un anunt din lista a unui host
     *
     * @param name numele anuntului
     * @return mesajul daca s a putut sterge anuntul
     */
    public String removeAnnouncement(final String name) {
        return username + " is not a host.";
    }

    /**
     * @return lista cu toate anunturile unui host
     */
    public List<Host.Announcement> getAnnouncementsHost() {
        return null;
    }

    /**
     * @param nextPage pagina pe care vrea sa ajunga
     * @return daca s a putut muta pe pagina respectiva sau nu
     */
    public String changePage(final String nextPage) {
        if (nextPage.equals("Home")) {
            currentPage = new HomePage();
            userPage = username;
            return username + " accessed " + nextPage + " successfully.";
        } else if (nextPage.equals("LikedContent")) {
            currentPage = new LikedContentPage();
            userPage = username;
            return username + " accessed " + nextPage + " successfully.";
        } else {
            return username + " is trying to access a non-existent page.";
        }
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        if (online) { // doar daca este online verific
            player.simulatePlayer(time);
        }
    }

    /**
     * @return lista de albume a unui artist
     */
    public List<Album> getAlbumsOfAnArtist() {
        return null;
    }

    /**
     * @return daca este artist sau nu
     */
    public boolean isArtist() {
        return false;
    }

    /**
     * @return daca este host sau nu
     */
    public boolean isHost() {
        return false;
    }

    /**
     * o metoda care sterge toate referintele a tot ce a creat un user
     * (playlisturile si melodiile din el)
     */
    public void removeReference() {
        // parcurg playlisturile unui user care trebuie sters
        for (Playlist iterPlaylists : playlists) {
            for (User iterUsers : Admin.getUsers()) {
                // sa nu fie userul pe care vreau sa l sterg
                if (!(iterUsers.getUsername().equals(username))) {
                    // sterg playlistul din lista de playlisturi unui user
                    iterUsers.getPlaylists().remove(iterPlaylists);
                    iterUsers.getFollowedPlaylists().remove(iterPlaylists);
                }
            }
        }
        // trebuie sa vad cui a dat follow la playlist
        // playlisturile la care a dat follow userul pe care vreau sa l sterg
        for (Playlist iterPlaylist : followedPlaylists) {
            iterPlaylist.decreaseFollowers();
        }

    }

    /**
     * @param name numele userului pe care vreau sa l sterg
     * @return daca un user asculta in acest moment ceva din ce detine user ul cu numele "name"
     */
    public boolean isListening(final String name) {
        // verific daca asculta ceva acum
        if (player.getSource() != null && player.getSource().getAudioCollection() != null) {
            // inseamna ca asculta un podcast/playlist/album
            if (player.getSource().getAudioCollection().matchesOwner(name)) {
                return true;
            }
        }
        // inseamna ca asculta o melodie
        if (player.getSource() != null && player.getSource().getAudioFile() != null) {
            if (player.getSource().getAudioFile().matchesArtist(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return numarul de likeuri a tuturor albumelor unui artist
     */
    public int numberLikesAlbums() {
        return 0;
    }

}
