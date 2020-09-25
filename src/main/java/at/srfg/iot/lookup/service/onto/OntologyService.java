package at.srfg.iot.lookup.service.onto;

import java.util.List;

public interface OntologyService {


	/**
	 * Upload a ontology (provided as string)
	 * @param mimeType
	 * @param nameSpaces The list of namespaces to include in the upload
	 * @param onto
	 */
    public void upload(String mimeType, String onto, List<String> namespaces);

	boolean deleteNamespace(String namespace);
	


}