package app.pageSystem;

import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.user.Artist;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class ArtistPage implements Page {

    public String printCurrentPage(User user) {
        String currentPage;
        List<String> albumsName = new ArrayList<>();
        List<String> merchandise = new ArrayList<>();
        List<String> event = new ArrayList<>();
        for (Album iterAlbum : user.getAlbumsOfAnArtist()) {
            albumsName.add(iterAlbum.getName());
        }

        for (Artist.Merch iterMerch : user.getMerchandiseOfAnArtist()) {
            merchandise.add(iterMerch.getName() + " - " + iterMerch.getPrice()
                    + ":\n\t" + iterMerch.getDescription());
        }

        for (Artist.Event iterEvent : user.getEventsOfAnArtist()) {
            event.add(iterEvent.getNameEvent() + " - " + iterEvent.getDate()
            + ":\n\t" + iterEvent.getDescription());
        }

        currentPage = "Albums:\n\t" + albumsName
                + "\n\nMerch:\n\t" + merchandise
        + "\n\nEvents:\n\t" + event;
        return currentPage;
    }

}
