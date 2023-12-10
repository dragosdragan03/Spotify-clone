package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Artist extends User {

    private List<Album> albums; // fac o lista de albume ale unui user
    private List<Event> events;
    private List<Merch> merchandise;

    public Artist( String username, int age, String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merchandise = new ArrayList<>();
    }

    public final class Event {
        @Getter
        private String nameEvent;
        @Getter
        private String description;
        @Getter
        private String date;

        public Event(String nameEvent, String description, String date) {
            this.nameEvent = nameEvent;
            this.description = description;
            this.date = date;
        }
    }

    public final class Merch {
        @Getter
        private String name;
        @Getter
        private String description;
        @Getter
        private int price;

        public Merch(String name, String description, int price) {
            this.name = name;
            this.description = description;
            this.price = price;
        }
    }
    @Override
    public List<Merch> getMerchandiseOfAnArtist() {
        return merchandise;
    }
    @Override
    public List<Album> getAlbumsOfAnArtist() {
        return albums;
    }
    @Override
    public List<Event> getEventsOfAnArtist() {
        return events;
    }
    @Override
    public boolean isArtist() {
        return true;
    }

    private Song copySongInputToSong(final SongInput songInput) {
        Song song = new Song(songInput.getName(),
                songInput.getDuration(),
                songInput.getAlbum(),
                songInput.getTags(),
                songInput.getLyrics(),
                songInput.getGenre(),
                songInput.getReleaseYear(),
                songInput.getArtist());
        return song;
    }

    /**
     * metoda care verifica daca exista duplicate in lista de melodii
     * @param songs lista de melodii
     * @return true or false
     */
    private boolean isDuplicated(final List<SongInput> songs) {
        Set<String> uniqueItems = new HashSet<>();
        for (SongInput iterSongInput : songs) {
            if (!uniqueItems.add(iterSongInput.getName())) {
                return true; // inseamna ca este duplicat
            }
        }
        return false; // inseamna ca nu este duplicat
    }

    /**
     *
     * @param songs lista de melodii pe care un artist vrea sa le adauge in album
     * @param name numele albumului
     * @param releaseYear anul de lansare al albumului
     * @param description descrierea albumului
     * @return mesajul in functie daca s a putut adauga albumul sau nu
     */
    @Override
    public String addAlbum(final List<SongInput> songs, final String name, final int releaseYear, final String description) {

        for (Album iter : albums) {
            if (iter.getName().equals(name)) { // sa vad
                return getUsername() + " has another album with the same name.";
            }
        }

        if (isDuplicated(songs)) {
            return getUsername() + " has the same song at least twice in this album.";
        }

        Album newAlbum = new Album(name, getUsername(), releaseYear, description);
        for (SongInput iterSong : songs) {
            Song song = copySongInputToSong(iterSong);
            if (!Admin.getSongs().contains(song)) {
                Admin.getSongs().add(song);
            }
            newAlbum.addSong(song);
        }
        albums.add(newAlbum);

        return getUsername() + " has added new album successfully.";
    }

    /**
     *
     * @param date data evenimentului
     * @return daca data este una valida sau nu
     */
    private boolean isDateCorrect(final String date) {
        String[] dateParts = date.split("-");

        // extrag ziua, luna si anul
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        if (month == 6 && day > 28) {
            return false;
        }

        if (day > 31) {
            return false;
        }

        if (month > 12) {
            return false;
        }

        if (year < 1900 || year > 2023) {
            return false;
        }

        return true;
    }

    /**
     * metoda care adauga in lista un eveniment
     * @param name numele evenimentului
     * @param date data evenimentului
     * @param description descrierea evenimentului
     * @return mesajul daca a putut fi adaugat evenimentul sau nu
     */
    @Override
    public String addEvent(final String name, final String date, final String description) {
        for (Event iterEvents : events) {
            if (iterEvents.getNameEvent().equals(name)) {
                return getUsername() + " has another event with the same name.";
            }
        }
        // inseama ca nu mai exista eventul respectiv
        if (isDateCorrect(date)) {
            Event newEvent = new Event(name, description, date); // creez un nou eveniment
            events.add(newEvent);
            return getUsername() + " has added new event successfully.";
        } else {
            return "Event for " + getUsername() + " does not have a valid date.";
        }
    }

    @Override
    public String addMerch(final String name, final String description, final int price) {
        for (Merch iterMerch : merchandise) {
            if (iterMerch.getName().equals(name)) {
                return getUsername() + " has merchandise with the same name.";
            }
        }
        final int minPrice = 0;
        if (price < minPrice) {
            return "Price for merchandise can not be negative.";
        }

        Merch newMerch = new Merch(name, description, price);
        merchandise.add(newMerch);
        return getUsername() + " has added new merchandise successfully.";
    }

    @Override
    public void removeSongs() {
        for (Album iterAlbum : albums) { // parcurg fiecare album
            for (Song iterSong : iterAlbum.getSongs()) { // parcurg lista de melodii din albume si le scot din lista mare
                Admin.getSongs().remove(iterSong);
                for (User iterUsers : Admin.getUsers()) {
                    if (iterUsers.getLikedSongs().contains(iterSong)) {
                        iterUsers.getLikedSongs().remove(iterSong);
                    }
                }
            }
        }
    }
}
