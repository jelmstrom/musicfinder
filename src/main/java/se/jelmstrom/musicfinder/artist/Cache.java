package se.jelmstrom.musicfinder.artist;

import se.jelmstrom.musicfinder.artist.dto.Artist;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;

/**
 *
 * Poormans cache
 *  Wrapper to enable proper caching
 *
 */
public class Cache {
    private static int cacheDuration = 120;
    static{
        if(System.getenv("CACHE_EXPIRY_SECONDS") != null ) {
            cacheDuration = Integer.parseInt(System.getenv("CACHE_EXPIRY_SECONDS"));
        }
    }
    private static final HashMap<String, CacheEntity<Artist>> cache = new HashMap<>();

    public static Artist put(Artist artist) {
        CacheEntity<Artist> previous = cache.put(artist.getId(), new CacheEntity<>(LocalDateTime.now(ZoneOffset.UTC).plusSeconds(cacheDuration), artist));
        return previous == null? null : previous.artist;
    }

    public static Artist get(String mbId) {
        CacheEntity<Artist> artistCacheEntity = cache.get(mbId);
        return artistCacheEntity == null || artistCacheEntity.expires.isBefore(LocalDateTime.now(ZoneOffset.UTC))? null : artistCacheEntity.artist;
    }

    private static class CacheEntity<Artist> {
        private final LocalDateTime expires;
        private final Artist artist;

        public CacheEntity(LocalDateTime expires, Artist artist) {
            this.expires = expires;
            this.artist = artist;
        }
    }
}
