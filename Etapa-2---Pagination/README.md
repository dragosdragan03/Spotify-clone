Nume: DRAGAN DRAGOS-OVIDIU
Grupă: 323CA

Titlu temă: "Proiect GlobalWaves - Etapa 2 - Pagination"

### Descriere:

* Scurtă descriere a funcționalității temei
* Eventuale explicații suplimentare pentru anumite părți din temă ce crezi că nu sunt suficient de clare doar urmărind codul

-Am folosit scheletul etapei 2 + modficari in CommandInput si FiltersInput-

Pentru inceput am decis sa fac clasa Admin de tip singleton pentru a ma folosi de aceasta in celelalte clase + am inlocuit metodele de tip static din clasa Admin.

Pentru a crea un user de tip host/artist/user normal am implementat factory design pattern. In functie de ce tip este parametrul "type", UserFactory va crea un User instantiat de tip Artist/Host/User, iar la final, in clasa Admin, il va adauga in lista de users.

M-am gandit ca pachetul "user" sa contina clasele User, Host, Artist si UserFactory. Host si Artist sunt 2 clase care mostenesc clasa User si au in plus campurile pt podcasturi si anunturi, respectiv albume, merchandise si evenimente. In afara de clasa Album care mosteneste AudioCollection si am inclus-o in pachetul collection, celelalte clase (ex: Event, Merch, Announcement) sunt clase interne formate in interiorul mostenitorilor.

Pe parcursul acestui proiect m-am folosit foarte mult de suprascrierea metodelor. In cazul in care este un user normal, acesta sa execute o metoda, iar in cazul in care este un artist/host sa suprascrie metoda respectiva.

Pentru printarea paginilor am creat un pachet numit "pageSystem" care contine interfata Page si clasele "ArtistPage", "HostPage", "LikedContentPage" si " HomePage" care executa metoda printCurrentPage.

In searchBar am adaugat cazurile de cautare a unui host, artist, album.
