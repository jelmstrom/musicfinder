package se.jelmstrom.musicfinder.artist;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private static int CACHE_DURATION = 120;
    static{
        if(System.getenv("CACHE_EXPIRY_SECONDS") != null ) {
            CACHE_DURATION = Integer.parseInt(System.getenv("CACHE_EXPIRY_SECONDS"));
        }
    }
    private final static Log LOG = LogFactory.getLog(Cache.class);
    private static final HashMap<String, CacheEntity<Artist>> CACHE = new HashMap<>();

    public static Artist put(Artist artist) {
        CacheEntity<Artist> previous = CACHE.put(artist.getId(), new CacheEntity<>(LocalDateTime.now(ZoneOffset.UTC).plusSeconds(CACHE_DURATION), artist));
        return previous == null? null : previous.artist;
    }

    public static Artist get(String mbId) {
        CacheEntity<Artist> artistCacheEntity = CACHE.get(mbId);
        boolean expired = (artistCacheEntity == null || artistCacheEntity.expires.isBefore(LocalDateTime.now(ZoneOffset.UTC)));
        if(expired){
            return null;
        }
        LOG.debug("Serving from cache");
        return artistCacheEntity.artist;
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
