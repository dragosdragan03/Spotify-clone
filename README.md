**Nume:** Dragan Dragos Ovidiu
**Grupă:** 323 CA

## Titlu temă
Proiect GlobalWaves - Etapa 1 - Audio Player
### Descriere:

* Scurtă descriere a funcționalității temei
* Eventuale explicații suplimentare pentru anumite părți din temă ce crezi că nu sunt suficient de clare doar urmărind codul

Pentru inceput am facut clasa Command care imi citeste comenzile din fisierele de input.

Clasa CommandExecute este o clasa parinte care va contine: comanda, timestampul, numele userului, o lista de playlisuri public si private ale tuturor userilor, precum si o lista de useri care retine istoricul acestora (cat timp a ascultat, tipul fisierelor incarcate(podcast/melodie/playlist), ce podcasturi a asculta, daca este pe Play/Pause, cand a fost incarcat fisierul etc). In metoda executeCommand de tip Output se va apela comanda respectiva si va returna mesajul generat/lista generata.

Majoritatea celorlalte clase vor mosteni clasa CommandExecute, deoarece ma voi folosi de parametrii respectivi.

Clasa FindTrack are scopul de a returna: cat timp a ascultat un utilizator, la ce episod/melodie este, numele melodiei/episodul, precum si alte detalii ce tin de AudioFile ul respectiv

Clasa AudioFile retine ce a incarcat un utilizator (un podcast, melodie sau playlist)

Am creat si o clasa noua numita PodcastInputModified pentru a putea retine cat timp a ascultat din podcastul respectiv, daca este pe repeat, de cine apartine podcastul etc.

Clasa SongInputModified a fost creata cu scopul de a putea insera un camp nou de tip repeat si pentru a vedea pe ce fel de repeat este melodia respectiva (no repeat/repeat once/ repeat infinite).

Clasa FilterInput are rolul de a pastra toate campurile citite din fisierele de input. Aceasta clasa va fi folostia in Clasa Search pentru a cauta melodia/podcastul/playlistul.

Clasa UserHistory are rolul de a retine toate statisticile unui utilizator (numele, cat timp a ascultat un AudioFile, daca este pe Play/Pause, melodiile la care a dat like, playlisturile pe care le detine si playlisturile la care a dat follow).

Clasa Playlist a fost creata pentru a retine o lista cu melodiile pe care un utilizator le adauga, numarul de followeri, tipul playlistului (public/privat), numele playlistului, daca este pus pe shuffle sau nu, precum si prorpietarul playlistului.

* Ce ai invățat din realizarea acestei teme?
M am familiarizat cu limbajul de programare java si am inteles rolul constructorilor si a metodelor. Am inteles conceptul de mostenire, clase interne, overriding, overloading, getter si setter.
