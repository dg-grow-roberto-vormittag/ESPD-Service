package eu.europa.ec.grow.espd.xml;

import eu.europa.ec.grow.espd.domain.EspdDocument;
import eu.europa.ec.grow.espd.xml.request.UblRequestToEspdDocumentTransformer;
import eu.europa.ec.grow.espd.xml.request.UblRequestTypeTransformer;
import eu.europa.ec.grow.espd.xml.response.UblResponseToEspdDocumentTransformer;
import eu.europa.ec.grow.espd.xml.response.UblResponseTypeTransformer;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * Class that can perform XML marshalling and unmarshalling from {@link ESPDRequestType} and {@link ESPDResponseType}
 * to a {@link EspdDocument}.
 * <p/>
 * Created by vigi on 11/11/15:3:22 PM.
 */
@Component
public class EspdExchangeMarshaller {

    private final Jaxb2Marshaller jaxb2Marshaller;
    private final UblRequestTypeTransformer toEspdRequestTransformer;
    private final UblRequestToEspdDocumentTransformer requestToEspdDocumentTransformer;
    private final UblResponseToEspdDocumentTransformer responseToEspdDocumentTransformer;
    private final UblResponseTypeTransformer toEspdResponseTransformer;
    private final grow.names.specification.ubl.schema.xsd.espdrequest_1.ObjectFactory espdRequestObjectFactory;
    private final grow.names.specification.ubl.schema.xsd.espdresponse_1.ObjectFactory espdResponseObjectFactory;

    @Autowired
    EspdExchangeMarshaller(Jaxb2Marshaller jaxb2Marshaller,
            UblRequestTypeTransformer toEspdRequestTransformer,
            UblRequestToEspdDocumentTransformer requestToEspdDocumentTransformer,
            UblResponseToEspdDocumentTransformer responseToEspdDocumentTransformer,
            UblResponseTypeTransformer toEspdResponseTransformer) {
        this.jaxb2Marshaller = jaxb2Marshaller;
        this.toEspdRequestTransformer = toEspdRequestTransformer;
        this.requestToEspdDocumentTransformer = requestToEspdDocumentTransformer;
        this.responseToEspdDocumentTransformer = responseToEspdDocumentTransformer;
        this.toEspdResponseTransformer = toEspdResponseTransformer;
        this.espdRequestObjectFactory = new grow.names.specification.ubl.schema.xsd.espdrequest_1.ObjectFactory();
        this.espdResponseObjectFactory = new grow.names.specification.ubl.schema.xsd.espdresponse_1.ObjectFactory();
    }

    /**
     * Create a {@link ESPDRequestType} from the provided {@link EspdDocument} and marshals it
     * to the output stream.
     *
     * @param espdDocument The ESPD document that will be written out
     * @param out          The place where the XML representation will be written out
     */
    public void generateEspdRequest(EspdDocument espdDocument, OutputStream out) {
        ESPDRequestType espdRequestType = toEspdRequestTransformer.apply(espdDocument);
        StreamResult result = new StreamResult(out);
        jaxb2Marshaller.marshal(espdRequestObjectFactory.createESPDRequest(espdRequestType), result);
    }

    /**
     * Create a {@link ESPDRequestType} from the provided {@link EspdDocument} and marshals it
     * as a {@link StringWriter}.
     *
     * @param espdDocument The ESPD document that will be written out
     * @param sw           The place where the XML representation will be written out
     */
    public void generateEspdRequest(EspdDocument espdDocument, StringWriter sw) {
        ESPDRequestType espdRequestType = toEspdRequestTransformer.apply(espdDocument);
        StreamResult result = new StreamResult(sw);

        jaxb2Marshaller.marshal(espdRequestObjectFactory.createESPDRequest(espdRequestType), result);
    }

    /**
     * Create a {@link ESPDResponseType} from the provided {@link EspdDocument} and marshals it
     * to the output stream.
     *
     * @param espdDocument The ESPD document that will be written out
     * @param out          The place where the XML representation will be written out
     */
    public void generateEspdResponse(EspdDocument espdDocument, OutputStream out) {
        ESPDResponseType espdResponseType = toEspdResponseTransformer.apply(espdDocument);
        StreamResult result = new StreamResult(out);
        jaxb2Marshaller.marshal(espdResponseObjectFactory.createESPDResponse(espdResponseType), result);
    }

    /**
     * Create a {@link ESPDResponseType} from the provided {@link EspdDocument} and marshals it
     * to the output stream.
     *
     * @param espdDocument The ESPD document that will be written out
     * @param sw           The place where the XML representation will be written out
     */
    public void generateEspdResponse(EspdDocument espdDocument, StringWriter sw) {
        ESPDResponseType espdResponseType = toEspdResponseTransformer.apply(espdDocument);
        StreamResult result = new StreamResult(sw);
        jaxb2Marshaller.marshal(espdResponseObjectFactory.createESPDResponse(espdResponseType), result);
    }

    /**
     * Convert a {@link ESPDRequestType} coming from an input stream into a {@link EspdDocument} object needed by
     * the web application user interface.
     *
     * @param espdRequestStream An input stream containing the ESPD Request
     *
     * @return The ESPD Document object coming out from the ESPD Request
     */
    @SuppressWarnings("unchecked")
    public EspdDocument importEspdRequest(InputStream espdRequestStream) {
        JAXBElement<ESPDRequestType> element = (JAXBElement<ESPDRequestType>) jaxb2Marshaller
                .unmarshal(new StreamSource(espdRequestStream));
        ESPDRequestType requestType = element.getValue();
        return requestToEspdDocumentTransformer.apply(requestType);
    }

    /**
     * Convert a {@link ESPDResponseType} coming from an input stream into a {@link EspdDocument} object needed by
     * the web application user interface.
     *
     * @param espdResponseStream An input stream containing the ESPD Response
     *
     * @return The ESPD Document object coming out from the ESPD Response
     */
    @SuppressWarnings("unchecked")
    public EspdDocument importEspdResponse(InputStream espdResponseStream) {
        JAXBElement<ESPDResponseType> element = (JAXBElement<ESPDResponseType>) jaxb2Marshaller
                .unmarshal(new StreamSource(espdResponseStream));
        ESPDResponseType responseType = element.getValue();
        return responseToEspdDocumentTransformer.apply(responseType);
    }
}