package controllers;


import api.ReceiptResponse;
import dao.ReceiptDao;
import generated.tables.records.ReceiptsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags/{tag}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class tagsController {
    final ReceiptDao tags;

    public tagsController(ReceiptDao tags) {
        this.tags = tags;
    }

    @PUT
    public void toggleTag(@PathParam("tag") String tagName, @Valid @NotNull int id) {
        tags.insert(tagName, id);
    }

    @GET
    public List<ReceiptResponse> getReceipts(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> receiptRecords = tags.getReceiptsByTag(tagName);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }
}
