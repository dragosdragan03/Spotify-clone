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

public final class Artist extends User {

    private List<Album> albums; // fac o lista de albume ale unui user
    private List<Event> events;
    private List<Merch> merchandise;

    public Artist(final String username, final int age, final String city) {
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

        public Event(final String nameEvent, final String description, final String date) {
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

        public Merch(final String name, final String description, final int price) {
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
     *
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
     * @param songs       lista de melodii pe care un artist vrea sa le adauge in album
     * @param name        numele albumului
     * @param releaseYear anul de lansare al albumului
     * @param description descrierea albumului
     * @return mesajul in functie daca s a putut adauga albumul sau nu
     */
    @Override
    public String addAlbum(final List<SongInput> songs, final String name,
                           final int releaseYear, final String description) {

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
            if (!Admin.getInstance().getSongs().contains(song)) {
                Admin.getInstance().getSongs().add(song);
            }
            newAlbum.addSong(song);
        }
        albums.add(newAlbum);

        return getUsername() + " has added new album successfully.";
    }

    /**
     * @param date data evenimentului
     * @return daca data este una valida sau nu
     */
    private boolean isDateCorrect(final String date) {
        final int februaryMonth = 2;
        final int februaryMaxDay = 28;
        final int maxDay = 31;
        final int maxMonth = 12;
        final int minYear = 1900;
        final int maxYear = 2023;
        String[] dateParts = date.split("-");

        // extrag ziua, luna si anul
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        if (month == februaryMonth && day > februaryMaxDay) {
            return false;
        }

        if (day > maxDay) {
            return false;
        }

        if (month > maxMonth) {
            return false;
        }

        if (year < minYear || year > maxYear) {
            return false;
        }

        return true;
    }

    /**
     * metoda care adauga in lista un eveniment
     *
     * @param name        numele evenimentului
     * @param date        data evenimentului
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
    public void removeReference() {
        for (Album iterAlbum : albums) { // parcurg fiecare album
            // parcurg lista de melodii din album si le scot din lista mare
            for (Song iterSong : iterAlbum.getSongs()) {
                Admin.getInstance().getSongs().remove(iterSong);
                for (User iterUsers : Admin.getInstance().getUsers()) {
                    if (iterUsers.getLikedSongs().contains(iterSong)) {
                        iterUsers.getLikedSongs().remove(iterSong);
                    }
                }
            }
        }
    }

    private void removeSongsOfASingleAlbum(final Album album) {
        for (Song iterSong : album.getSongs()) {
            Admin.getInstance().getSongs().remove(iterSong);
            for (User iterUsers : Admin.getInstance().getUsers()) {
                if (iterUsers.getLikedSongs().contains(iterSong)) {
                    iterUsers.getLikedSongs().remove(iterSong);
                }
            }
        }
    }

    private int indexAlbum(final String nameAlbum) {
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getName().equals(nameAlbum)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String removeAlbum(final String name) {
        final int indexAlbum = indexAlbum(name);
        if (indexAlbum == -1) { // inseamna ca nu exista albumul
            return getUsername() + " doesn't have an album with the given name.";
        }
        // parcurg toata lista de useri sa vad daca cineva a incarcat albumul respectiv
        for (User iterUser : Admin.getInstance().getUsers()) {
            if (!iterUser.equals(getUsername())) { // sa fie diferit de artistul respectiv
                if (iterUser.isListening(getUsername())
                        || iterUser.getUserPage().equals(getUsername())) {
                    return getUsername() + " can't delete this album.";
                }
            }
        }

        // inseamna ca niciun user nu asculta albumul pe care vreau sa l sterg
        // sau o melodie din acesta
        // am sters melodiile din albumul respectiv, din toate sursele
        removeSongsOfASingleAlbum(albums.get(indexAlbum));
        albums.remove(indexAlbum);
        return getUsername() + " deleted the album successfully.";

    }

    @Override
    public String removeEvent(final String name) {
        for (Event iterEvent : events) {
            if (iterEvent.getNameEvent().equals(name)) {
                events.remove(iterEvent);
                return getUsername() + " deleted the event successfully.";
            }
        }
        return getUsername() + " doesn't have an event with the given name.";
    }

    @Override
    public int numberLikesAlbums() {
        int sum = 0;
        for (Album iterAlbum : albums) {
            sum += iterAlbum.getNumberLikesAlbum();
        }
        return sum;
    }
}
