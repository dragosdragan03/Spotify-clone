package app;

import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.pageSystem.ArtistPage;
import app.pageSystem.HomePage;
import app.pageSystem.HostPage;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Admin.
 */
public final class Admin {
    @Getter
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static int timestamp = 0;
    private static final int LIMIT = 5;

    private Admin() {
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }


    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return songs;
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return podcasts;
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * @return albumele tuturor artistilor
     */
    public static List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
        for (User iterUser : users) {
            if (iterUser.isArtist()) {
                if (iterUser.getAlbumsOfAnArtist().size() != 0) {
                    albums.addAll(iterUser.getAlbumsOfAnArtist());
                }
            }
        }
        return albums;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * @return lista cu toti artistii
     */
    public static List<User> getArtists() {
        List<User> listArtists = new ArrayList<>();
        for (User user : users) {
            if (user.isArtist()) {
                listArtists.add(user);
            }
        }
        return listArtists;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    public static List<String> getOnlineUsers() {
        List<String> onlineUsers; // fac un string pentru a vedea userii activi
        onlineUsers = users.stream()
                .filter(User::isOnline)
                .map(User::getUsername)
                .collect(Collectors.toList());

        return onlineUsers;
    }

    /**
     * @param username numle unui user
     * @param age      varsta userului
     * @param city     orasul
     * @param type     artist/user normal/host
     * @return adauga in lista de users un artis/host/user normal
     */
    public static String addUser(final String username, final int age, final String city, final String type) {

        for (User iter : users) { // verific daca mai exista userul respectiv
            if (iter.getUsername().equals(username)) {
                return "The username " + username + " is already taken.";
            }
        }
        // inseamna ca nu a mai fost gasit un user cu acelasi nume
        if (type.equals("user")) { // inseamna ca este admin
            User user = new User(username, age, city); // creez un user normal
            users.add(user);
        } else if (type.equals("artist")) {
            User artist = new Artist(username, age, city);
            artist.setCurrentPage(new ArtistPage());
            users.add(artist);
            artist.setOnline(false);
        } else if (type.equals("host")) {
            User host = new Host(username, age, city);
            host.setCurrentPage(new HostPage());
            users.add(host);
            host.setOnline(false);
        }
        return "The username " + username + " has been added successfully.";
    }

    /**
     * @return lista cu numele tuturor userilor normali, artisti si host
     */
    public static List<String> getAllUsers() {
        List<String> usersName = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (User iterUser : users) {
                if (i == 0 && iterUser.isHost() == false && iterUser.isArtist() == false) {
                    usersName.add(iterUser.getUsername());
                }
                if (i == 1 && iterUser.isArtist()) {
                    usersName.add(iterUser.getUsername());
                }
                if (i == 2 && iterUser.isHost()) {
                    usersName.add(iterUser.getUsername());
                }
            }
        }
        return usersName;
    }

    /**
     * @param username numele userului pe care vreau sa l sterg
     * @return mesajul daca a putut fi sters userul sau nu
     */
    public static String deleteUser(final String username) {
        User user = getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }

        for (User userIter : users) {
            if (!userIter.equals(user)) { // sa fie diferit de userul pe care vreau sa l sterg
                if (userIter.getPlayer().getSource() != null && userIter.getPlayer().getSource().getAudioCollection() != null) { // verific daca asculta ceva acum
                    if (userIter.getPlayer().getSource().getAudioCollection().matchesOwner(username)) { // inseamna ca asculta un podcast/playlist/album
                        return username + " can't be deleted.";
                    }
                }
                if (userIter.getPlayer().getSource() != null && userIter.getPlayer().getSource().getAudioFile() != null) { // inseamna ca asculta o melodie (nu podcast/album/playlist
                    if (userIter.getPlayer().getSource().getAudioFile().matchesArtist(username)) { // asta inseamna ca asculta o melodie
                        return username + " can't be deleted.";
                    }

                }
            }
        }
        user.removeSongs(); // asta daca userul meu este artist
        users.remove(getUser(username));
        return username + " was successfully deleted.";
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
