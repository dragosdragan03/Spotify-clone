package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;
import app.user.UserFactory;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Admin.
 */
public final class Admin {
    @Getter
    private List<User> users = new ArrayList<>();
    /**
     * -- GETTER --
     * Gets songs.
     *
     * @return the songs
     */
    @Getter
    private List<Song> songs = new ArrayList<>();
    /**
     * -- GETTER --
     * Gets podcasts.
     *
     * @return the podcasts
     */
    @Getter
    private List<Podcast> podcasts = new ArrayList<>();
    private int timestamp = 0;
    private final int limit = 5;

    private static Admin instance = new Admin();

    public static Admin getInstance() {
        return instance;
    }

    private Admin() {
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public void setUsers(final List<UserInput> userInputList) {
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
    public void setSongs(final List<SongInput> songInputList) {
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
    public void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(), episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * @return albumele tuturor artistilor
     */
    public List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
        for (User iterUser : users) {
            if (iterUser.isArtist()) {
                if (!iterUser.getAlbumsOfAnArtist().isEmpty()) {
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
    public User getUser(final String username) {
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
    public List<User> getArtists() {
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
    public void updateTimestamp(final int newTimestamp) {
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
    public List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= limit) {
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
    public List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers).reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= limit) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * @return primele 5 cele mai apreciate albume
     */
    public List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(getAlbums());
        sortedAlbums.sort(Comparator.comparing(Album::getName));
        sortedAlbums.sort(Comparator.comparingInt(Album::getNumberLikesAlbum).reversed());
        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= limit) {
                break;
            }
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }

    /**
     * @return primii 5 artisti care au cele mai multe likeuri la meldoii
     */
    public List<String> getTop5Artists() {
        List<User> artists = new ArrayList<>();
        for (User iterUser : users) {
            if (iterUser.isArtist()) {
                artists.add(iterUser);
            }
        }
        artists.sort(Comparator.comparing(User::getUsername));
        artists.sort(Comparator.comparingInt(User::numberLikesAlbums).reversed());
        List<String> topArtists = new ArrayList<>();
        int count = 0;
        for (User iterArtist : artists) {
            if (count >= limit) {
                break;
            }
            topArtists.add(iterArtist.getUsername());
            count++;
        }
        return topArtists;
    }

    /**
     * @return lista de useri online
     */
    public List<String> getOnlineUsers() {
        List<String> onlineUsers; // fac un string pentru a vedea userii activi
        onlineUsers = users.stream().filter(User::isOnline).map(User::getUsername)
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
    public String addUser(final String username, final int age, final String city,
                          final String type) {

        // verific sa nu mai fie un user cu acelasi nume
        for (User iter : users) { // verific daca mai exista userul respectiv
            if (iter.getUsername().equals(username)) {
                return "The username " + username + " is already taken.";
            }
        }

        // am fct un factory pattern pentru a crea un user/host/artist
        UserFactory factory = new UserFactory();
        users.add(factory.createUser(type, username, age, city));
        return "The username " + username + " has been added successfully.";
    }

    /**
     * @return lista cu numele tuturor userilor normali, artisti si host
     */
    public List<String> getAllUsers() {
        List<String> usersName = new ArrayList<>();
        final int orderUsers = 3;
        for (int i = 0; i < orderUsers; i++) {
            for (User iterUser : users) {
                if (i == 0 && !iterUser.isHost() && !iterUser.isArtist()) {
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
    public String deleteUser(final String username) {
        User user = getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }

        for (User userIter : users) {
            if (!userIter.equals(user)) { // sa fie diferit de userul pe care vreau sa l sterg
                if (userIter.isListening(username) || userIter.getUserPage().equals(username)) {
                    return username + " can't be deleted.";
                }
            }
        }

        user.removeReference(); // sterg toate referintele userului

        users.remove(user);
        return username + " was successfully deleted.";
    }

    /**
     * Reset.
     */
    public void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
