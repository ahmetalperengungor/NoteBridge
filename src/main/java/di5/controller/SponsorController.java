package di5.controller;

import di5.data.dto.CreateSponsorDTO;
import di5.data.dto.GetSponsorDTO;
import di5.services.SponsorService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/sponsors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SponsorController {

    private SponsorService sponsorService;

    public SponsorController() throws Exception {
        this.sponsorService = new SponsorService();
    }

    @POST
    public Response createSponsor(CreateSponsorDTO createSponsorDTO) {
        sponsorService.createSponsor(createSponsorDTO);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public List<GetSponsorDTO> getAllSponsors() {
        return sponsorService.getAllSponsors();
    }

    @GET
    @Path("/{id}")
    public Response getSponsorById(@PathParam("id") String id) {
        GetSponsorDTO sponsor = sponsorService.getSponsorById(id);
        if (sponsor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(sponsor).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSponsor(@PathParam("id") String id) {
        sponsorService.deleteSponsor(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
