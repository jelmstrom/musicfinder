package se.jelmstrom.musicfinder.api;

import org.eclipse.jetty.util.StringUtil;
import se.jelmstrom.musicfinder.artist.ArtistInfoService;
import se.jelmstrom.musicfinder.artist.client.artwork.CoverArtClient;
import se.jelmstrom.musicfinder.artist.client.discogs.DiscogsClient;
import se.jelmstrom.musicfinder.artist.dto.Artist;
import se.jelmstrom.musicfinder.artist.dto.ArtistList;
import se.jelmstrom.musicfinder.artist.client.musicBrainz.MusicBrainzClient;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/api")
public class Discography {
    private final ArtistInfoService service = new ArtistInfoService(new MusicBrainzClient(), new DiscogsClient(), new CoverArtClient());


    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response discography(SearchParam search,  @Context UriInfo uriInfo){
        if(StringUtil.isBlank(search.getArtist())) {
            return Response
                    .status(412)
                    .entity("Please specify an artist to search for")
                    .build();
        }
        ArtistList artistDetails = service.findArtist(search.getArtist());
        if(artistDetails.getArtists().size() == 0 ){
            return Response.noContent().build();
        }
        final URI baseUri = uriInfo.getBaseUri();
        artistDetails.getArtists()
                .stream()
                .forEach(artist -> {
                    artist.setUrl(String.format("%sapi/artist/%s", baseUri.toString(), artist.getId()));
                });
        return Response.ok().entity(artistDetails).build();
    }


    @GET
    @Path("/artist/{mbId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response discography(@PathParam("mbId") String mbId,  @Context UriInfo uriInfo){
        Artist artist = service.getArtistInfo(mbId);
        if(artist.getId() == null){
            return Response.noContent().build();
        }

        return Response.ok().entity(artist).build();
    }
}
