package elevator.server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/")
public class WebResource {

    private final StartedElevatorServer server;

    public WebResource(StartedElevatorServer server) {
        this.server = server;
    }

    @POST
    @Path("/player/register")
    public void newParticipant(@QueryParam("email") String email, @QueryParam("pseudo") String pseudo,
                               @QueryParam("serverURL") String serverURL) throws MalformedURLException {
        try {
            server.addElevatorGame(new Player(email, pseudo), new URL(serverURL));
        } catch (IllegalStateException | MalformedURLException e) {
            throw new WebApplicationException(e, Response.status(FORBIDDEN).entity(e.getMessage()).build());
        }
    }

    @POST
    @Path("/player/unregister")
    public void unregisterParticipant(@QueryParam("pseudo") String pseudo) {
        server.removeElevatorGame(pseudo);
    }

    @GET
    @Path("/players")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Player> players() {
        return server.players();
    }

    @GET
    @Path("/player/info/")
    @Produces(MediaType.APPLICATION_JSON)
    public PlayerInfo infoForPlayer(@QueryParam("pseudo") String pseudo) {
        return server.getPlayerInfo(pseudo);
    }

    @GET
    @Path("/leaderboard")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<PlayerInfo> leaderboard() {
        Collection<PlayerInfo> players = new ArrayList<>();
        for (ElevatorGame game : server.getUnmodifiableElevatorGames()) {
            players.add(game.getPlayerInfo());
        }
        return players;
    }

    @GET
    @Path("/admin/maxNumberOfUsers")
    public String getMaxNumberOfUsers() {
        return String.valueOf(server.getMaxNumberOfUsers());
    }

    @GET
    @Path("/admin/increaseMaxNumberOfUsers")
    public String increaseMaxNumberOfUsers() {
        return String.valueOf(server.increaseMaxNumberOfUsers());
    }

    @GET
    @Path("/admin/decreaseMaxNumberOfUsers")
    public String decreaseMaxNumberOfUsers() {
        return String.valueOf(server.decreaseMaxNumberOfUsers());
    }

}
