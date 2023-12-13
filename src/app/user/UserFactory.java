package app.user;

import app.pageSystem.ArtistPage;
import app.pageSystem.HostPage;

public final class UserFactory {

    /**
     * metoda care creeaza un host/artist/user normal
     * @param userType tipul userului artist/host/user normal
     * @param username numele userului
     * @param age varsta userului
     * @param city orasul de provenienta al userului
     * @return un user creat in functie de parametrii dati
     */
    public User createUser(final String userType, final String username, final int age,
                           final String city) {
        switch (userType) {
            case "user": // creez un user normal
                return new User(username, age, city);
            case "artist":
                User artist = new Artist(username, age, city);
                artist.setCurrentPage(new ArtistPage());
                artist.setOnline(false);
                return artist;
            case "host":
                User host = new Host(username, age, city);
                host.setCurrentPage(new HostPage());
                host.setOnline(false);
                return host;
            default:
                return null;
        }
    }
}
