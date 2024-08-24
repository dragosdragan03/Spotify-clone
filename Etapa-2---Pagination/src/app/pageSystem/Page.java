package app.pageSystem;

import app.user.User;

public interface Page {

    /**
     *
     * @param user cel caruia i se va afisa pagina
     * @return
     */
    String printCurrentPage(User user);
}
