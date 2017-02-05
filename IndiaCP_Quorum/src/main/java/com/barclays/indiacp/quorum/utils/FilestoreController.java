package com.barclays.indiacp.quorum.utils;

/**
 * Created by surajman
 */
/*
@Path("filestore")
public class FilestoreController {

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("metadata") FormDataContentDisposition fileDetail) {
        String dochash = CakeshopUtils.uploadDocumentToIPFS(uploadedInputStream);
        return Response.status(Response.Status.OK).entity(dochash).build();
    }

    @POST
    @Path("download/{docHash}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response downloadFile(@PathParam("docHash") String docHash, @FormParam("filePath") String filePath) {
        try {
            CakeshopUtils.downloadDocumentFromIPFS(docHash, filePath);
            return Response.status(Response.Status.OK).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
*/