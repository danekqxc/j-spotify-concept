package com.example.myapplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicData {
    private static List<Track> tracks = new ArrayList<>();
    private static List<Album> albums = new ArrayList<>();
    private static List<Artist> artists = new ArrayList<>();

    static {
        artists.add(new Artist("FRIENDLY THUG 52 NGG", R.drawable.thug));
        artists.add(new Artist("ALBLAK 52", R.drawable.alblak));
        artists.add(new Artist("Big Baby Tape", R.drawable.tape));
        artists.add(new Artist("shadowraze", android.R.drawable.ic_menu_gallery));
        artists.add(new Artist("kizaru", R.drawable.kizaru));
        artists.add(new Artist("Платина", R.drawable.platina));
        artists.add(new Artist("Markul", R.drawable.markul));
        artists.add(new Artist("SaintPrince 52", R.drawable.sp));
        artists.add(new Artist("FEDUK", R.drawable.feduk));

        String thugName = "FRIENDLY THUG 52 NGG";
        
        List<Track> cruiserTracks = new ArrayList<>();
        cruiserTracks.add(new Track("Two Thousand Nineteen", thugName, 0, R.drawable.cruiser));
        cruiserTracks.add(new Track("Heart On The Corner", thugName, 0, R.drawable.cruiser));
        cruiserTracks.add(new Track("Calmer", thugName, 0, R.drawable.cruiser));
        cruiserTracks.add(new Track("Total Control", thugName, 0, R.drawable.cruiser));
        cruiserTracks.add(new Track("Local Club Star", thugName, 0, R.drawable.cruiser));
        cruiserTracks.add(new Track("Loyalty Above Money", thugName, 0, R.drawable.cruiser));
        cruiserTracks.add(new Track("Victor", thugName, 0, R.drawable.cruiser));
        cruiserTracks.add(new Track("Cruiser Aurora", thugName, 0, R.drawable.cruiser));
        cruiserTracks.add(new Track("Earth Walker 100", thugName, 0, R.drawable.cruiser));

        Album cruiserAurora = new Album("Cruiser Aurora", thugName, cruiserTracks, R.drawable.cruiser);
        albums.add(cruiserAurora);
        tracks.addAll(cruiserTracks);

        List<Track> gmcTracks = new ArrayList<>();
        gmcTracks.add(new Track("Maestro", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("No Gletcher Gang 2", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("AIST", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("OCG", "FRIENDLY THUG 52 NGG & SaintPrince 52", 0, R.drawable.gmc));
        gmcTracks.add(new Track("After Kilian Flow", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("Graf Monte-Cristo", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("Sorry Mama Ama Still Sniper", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("Not My Type", "FRIENDLY THUG 52 NGG & kizaru", 0, R.drawable.gmc));
        gmcTracks.add(new Track("Most Valuable Pirate", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("No Coco Senior Please", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("Non Grata", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("Record Me / 4 Humble", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("Berliner", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("Skyscraper", "FRIENDLY THUG 52 NGG & Markul", 0, R.drawable.gmc));
        gmcTracks.add(new Track("2021", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("Eclipse", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("Junkie Kis2 3", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("From Day One / Miss You Again", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("Tom Sorry", thugName, 0, R.drawable.gmc));
        gmcTracks.add(new Track("Arrivederci 2", thugName, 0, R.drawable.gmc));

        Album gmcAlbum = new Album("Graf Monte-Cristo / Most Valuable Pirate", thugName, gmcTracks, R.drawable.gmc);
        albums.add(gmcAlbum);
        tracks.addAll(gmcTracks);

        String a52 = "ALBLAK 52";
        List<Track> echoTracks = new ArrayList<>();
        echoTracks.add(new Track("Intro. Здравствуйте!", a52, 0, R.drawable.echo));
        echoTracks.add(new Track("On The Line", a52, 0, R.drawable.echo));
        echoTracks.add(new Track("TOUGH GUY", a52, 0, R.drawable.echo));
        echoTracks.add(new Track("Where I came from", a52, 0, R.drawable.echo));
        echoTracks.add(new Track("Only 1 Heart", "ALBLAK 52 & kizaru", 0, R.drawable.echo));
        echoTracks.add(new Track("Fortune", a52, 0, R.drawable.echo));
        echoTracks.add(new Track("Hail", a52, 0, R.drawable.echo));
        echoTracks.add(new Track("INTERNATIONAL", a52, 0, R.drawable.echo));
        echoTracks.add(new Track("GO UP, EAGLE / YEEI 9", a52, 0, R.drawable.echo));
        echoTracks.add(new Track("Спокойной ночи", "ALBLAK 52, Скриптонит & FRIENDLY THUG 52 NGG", 0, R.drawable.echo));
        echoTracks.add(new Track("Заря", a52, 0, R.drawable.echo));
        echoTracks.add(new Track("Всегда", a52, 0, R.drawable.echo));
        echoTracks.add(new Track("Память", a52, 0, R.drawable.echo));
        echoTracks.add(new Track("Как и мы", a52, 0, R.drawable.echo));
        echoTracks.add(new Track("Записки (outro)", a52, 0, R.drawable.echo));

        Album streetsEcho = new Album("Streets Echo 2052", a52, echoTracks, R.drawable.echo);
        albums.add(streetsEcho);
        tracks.addAll(echoTracks);

        List<Track> quattroTracks = new ArrayList<>();
        quattroTracks.add(new Track("After Silence", a52, 0, R.drawable.quattro));
        quattroTracks.add(new Track("Show Must Go On", a52, 0, R.drawable.quattro));
        quattroTracks.add(new Track("F L Y", a52, 0, R.drawable.quattro));
        quattroTracks.add(new Track("OOOO", a52, R.raw.audi, R.drawable.quattro, "NAvVeZMCFT4"));
        quattroTracks.add(new Track("No Water", "ALBLAK 52 & Markul", 0, R.drawable.quattro));
        quattroTracks.add(new Track("Tier-1", a52, 0, R.drawable.quattro));
        quattroTracks.add(new Track("12 a.m", a52, 0, R.drawable.quattro));
        quattroTracks.add(new Track("Top Dawg", "ALBLAK 52 & ICEGERGERT", 0, R.drawable.quattro));
        quattroTracks.add(new Track("Who Wanna Be Millionaire?", a52, 0, R.drawable.quattro));
        quattroTracks.add(new Track("SS`25", a52, 0, R.drawable.quattro));
        quattroTracks.add(new Track("Smooth", "ALBLAK 52, FEDUK & Платина", 0, R.drawable.quattro));
        quattroTracks.add(new Track("Ace", a52, 0, R.drawable.quattro));
        quattroTracks.add(new Track("Waltz", "ALBLAK 52 & MACAN", 0, R.drawable.quattro));
        quattroTracks.add(new Track("Monologue", "ALBLAK 52 & FRIENDLY THUG 52 NGG", 0, R.drawable.quattro));
        quattroTracks.add(new Track("Tomorrow", a52, 0, R.drawable.quattro));
        quattroTracks.add(new Track("SELL YOUR 4SS", a52, 0, R.drawable.quattro));
        quattroTracks.add(new Track("Thank you", a52, 0, R.drawable.quattro));
        quattroTracks.add(new Track("TOUR (Бонус Трек)", a52, 0, R.drawable.quattro));

        Album quattroAlbum = new Album("QUATTRO: La Famiglia", a52, quattroTracks, R.drawable.quattro);
        albums.add(quattroAlbum);
        tracks.addAll(quattroTracks);

        String bbt = "Big Baby Tape";
        List<Track> varskvaTracks = new ArrayList<>();
        varskvaTracks.add(new Track("ROVLEN2U", bbt, 0, R.drawable.tape));
        varskvaTracks.add(new Track("Bandana", "Big Baby Tape & kizaru", 0, R.drawable.bandana));
        Album varskvaAlbum = new Album("VARSKVA", bbt, varskvaTracks, R.drawable.tape);
        albums.add(varskvaAlbum);
        tracks.addAll(varskvaTracks);

        List<Track> mixtapeTracks = new ArrayList<>();
        mixtapeTracks.add(new Track("HOOLIGAN", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("GELIK", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("TEAR DA CLUB", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("WATER", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("TORRENT", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("ICECREAMM", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("DRUNK", "Big Baby Tape, Scally Milano & BUSHIDO ZHO", 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("LOX", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("ALI", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("SDVG", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("TOPBOY", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("STOMP", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("SUPERVILLAIN", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("MF DOOM", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("LUNATIC", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("DUO", "Big Baby Tape & kizaru", 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("BRATAN", bbt, 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("COLLECTOR", "Big Baby Tape & John Garik", 0, R.drawable.mixtape));
        mixtapeTracks.add(new Track("RUN", bbt, 0, R.drawable.mixtape));

        Album mixtapeAlbum = new Album("MIXTAPE", bbt, mixtapeTracks, R.drawable.mixtape);
        albums.add(mixtapeAlbum);
        tracks.addAll(mixtapeTracks);

        String jointBBTKizaru = "Big Baby Tape & kizaru";
        List<Track> bandanaTracks = new ArrayList<>();
        bandanaTracks.add(new Track("99 Problems", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("So Icy Nihao", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("Big Tymers", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("Dirrt", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("Ladidadida", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("Million", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("5 Nights Crazy", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("Errbody Sleeping", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("Mama Makusa", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("Bandana", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("Slip & Slide", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("Andrew Story", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("Bon Voyage", jointBBTKizaru, 0, R.drawable.bandana));
        bandanaTracks.add(new Track("Ride Or Die", jointBBTKizaru, 0, R.drawable.bandana));

        Album bandanaAlbum = new Album("BANDANA I", jointBBTKizaru, bandanaTracks, R.drawable.bandana);
        albums.add(bandanaAlbum);
        tracks.addAll(bandanaTracks);

        String kizaruName = "kizaru";
        
        List<Track> fdoTracks = new ArrayList<>();
        fdoTracks.add(new Track("First Day Out", kizaruName, 0, R.drawable.kizaru));
        Album fdoAlbum = new Album("First Day Out", kizaruName, fdoTracks, R.drawable.kizaru);
        albums.add(fdoAlbum);
        tracks.addAll(fdoTracks);

        List<Track> simbaTracks = new ArrayList<>();
        simbaTracks.add(new Track("DON’T ASK", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("SIMBA", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("SQUADROBBER", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("I’M HIM", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("DEFILE", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("NOTHING PERSONAL", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("FULL MOON", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("GOLDEN VISA", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("LOADED", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("EU FLEX", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("MENACE", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("ON TIME", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("Presidential (Majestic)", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("RIVIERA", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("PURPLE DREAMS", kizaruName, 0, R.drawable.simba));
        simbaTracks.add(new Track("PROBLEM CHILD", kizaruName, 0, R.drawable.simba));

        Album simbaAlbum = new Album("SIMBA", kizaruName, simbaTracks, R.drawable.simba);
        albums.add(simbaAlbum);
        tracks.addAll(simbaTracks);

        tracks.add(new Track("Poke", thugName, 0, R.drawable.poke, "z2N2f_9Y-5E"));
        tracks.add(new Track("Cow", thugName, R.raw.cow, R.drawable.cow, "uS87_W-hUjE"));
    }

    public static List<Object> getAllForSearch() {
        List<Object> combined = new ArrayList<>();
        combined.addAll(artists);
        combined.addAll(albums);
        combined.addAll(tracks);
        return combined;
    }

    public static List<Artist> getAllArtists() { return new ArrayList<>(artists); }
    public static List<Album> getAllAlbums() { return new ArrayList<>(albums); }
    public static List<Track> getAllTracks() { return new ArrayList<>(tracks); }

    public static List<Track> getTracksByMood(String mood) {
        List<Track> all = new ArrayList<>(tracks);
        Collections.shuffle(all);
        int count = Math.min(10, all.size());
        return all.subList(0, count);
    }

    public static List<Track> getTracksByArtist(String artistName) {
        List<Track> result = new ArrayList<>();
        for (Track track : tracks) {
            if (track.getArtist().toUpperCase().contains(artistName.toUpperCase())) result.add(track);
        }
        return result;
    }

    public static List<Album> getAlbumsByArtist(String artistName) {
        List<Album> result = new ArrayList<>();
        for (Album album : albums) {
            if (album.getArtist().toUpperCase().contains(artistName.toUpperCase())) result.add(album);
        }
        return result;
    }

    public static Album getAlbumByTitle(String title) {
        for (Album album : albums) {
            if (album.getTitle().equalsIgnoreCase(title)) return album;
        }
        return null;
    }

    public static Artist getArtistByName(String name) {
        for (Artist artist : artists) {
            if (artist.getName().equalsIgnoreCase(name)) return artist;
        }
        return null;
    }
}