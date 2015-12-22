package org.jstorni.lambdasupport.api.endpoint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.jstorni.lambdasupport.endpoint.Configuration;
import org.jstorni.lambdasupport.endpoint.Endpoint;
import org.jstorni.lambdasupport.endpoint.GenericHandler;
import org.jstorni.lambdasupport.test.api.endpoints.MerchantEndpoint;
import org.jstorni.lambdasupport.test.api.model.Merchant;
import org.jstorni.lorm.EntityManager;
import org.jstorni.lorm.Repository;
import org.jstorni.lorm.mapping.EntityToItemMapperImpl;
import org.jstorni.lorm.mapping.ItemToEntityMapperImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IntegrationTest {

	private Repository<Merchant> merchantRepository;

	@Before
	public void setup() {
		EntityManager entityManager = Configuration.getEntitymanager();
		entityManager.addEntity(Merchant.class,
				new EntityToItemMapperImpl<Merchant>(Merchant.class),
				new ItemToEntityMapperImpl<Merchant>(Merchant.class,
						entityManager), new EntityToItemMapperImpl<Merchant>(
						Merchant.class));

		merchantRepository = entityManager.getRepository(Merchant.class);

	}

	@Test
	public void findByIdTest() throws JsonParseException, JsonMappingException,
			IOException {
		Merchant merchant = new Merchant();
		merchant.setName("sample for test");
		Merchant updatedMerchant = merchantRepository.save(merchant);

		String request = "{\"action\": \"merchant.findById\", \"payload\": { \"id\": \""
				+ updatedMerchant.getId() + "\" }}";

		String response = invoke(request);

		Assert.assertNotNull(response);
		Assert.assertTrue(response.length() > 0);

		ObjectMapper mapper = new ObjectMapper();
		Merchant foundMerchant = mapper.readValue(response, Merchant.class);

		Assert.assertNotNull(foundMerchant);
		Assert.assertEquals(updatedMerchant.getId(), foundMerchant.getId());

	}

	@Test
	public void findAll() throws JsonParseException, JsonMappingException,
			IOException {
		Merchant merchant = new Merchant();
		merchant.setName("sample for test");
		merchantRepository.save(merchant);

		String request = "{\"action\": \"merchant.findAll\"}}";
		String response = invoke(request);

		Assert.assertNotNull(response);
		Assert.assertTrue(response.length() > 0);

		ObjectMapper mapper = new ObjectMapper();
		List<Merchant> foundMerchants = mapper.readValue(response, List.class);

		Assert.assertNotNull(foundMerchants);
		Assert.assertTrue(foundMerchants.size() > 1);
	}

	@Test
	public void deleteById() throws JsonParseException, JsonMappingException,
			IOException {
		Merchant merchant = new Merchant();
		merchant.setName("sample for test");
		Merchant updatedMerchant = merchantRepository.save(merchant);

		String request = "{\"action\": \"merchant.deleteById\", \"payload\": { \"id\": \""
				+ updatedMerchant.getId() + "\" }}";

		Assert.assertNull(invoke(request));

		Assert.assertNull(merchantRepository.findOne(updatedMerchant.getId()));

	}

	@Test
	public void save() throws JsonParseException, JsonMappingException,
			IOException {
		// add
		String request = "{\"action\": \"merchant.create\", \"payload\": { \"name\": \""
				+ "armando barrera" + "\" }}";
		String response = invoke(request);

		Assert.assertNotNull(response);
		Assert.assertTrue(response.length() > 0);

		ObjectMapper mapper = new ObjectMapper();
		Merchant foundMerchant = mapper.readValue(response, Merchant.class);

		Assert.assertNotNull(foundMerchant);
		Assert.assertEquals("armando barrera", foundMerchant.getName());

		// update
		request = "{\"action\": \"merchant.update\", \"payload\": { \"name\": \""
				+ "armando barrera 2"
				+ "\", \"id\": \""
				+ foundMerchant.getId() + "\" }}";
		String updateResponse = invoke(request);

		Assert.assertNotNull(updateResponse);
		Assert.assertTrue(updateResponse.length() > 0);

		Merchant updatedMerchant = mapper.readValue(updateResponse,
				Merchant.class);

		Assert.assertNotNull(updatedMerchant);
		Assert.assertEquals("armando barrera 2", updatedMerchant.getName());
		Assert.assertEquals(foundMerchant.getId(), updatedMerchant.getId());

	}

	private String invoke(String payload) throws JsonParseException,
			JsonMappingException, IOException {
		InputStream in = new ByteArrayInputStream(payload.getBytes());
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GenericHandler handler = new GenericHandler() {

			@Override
			protected List<Endpoint> getEndpoints() {
				return Arrays.asList(new Endpoint[] { new MerchantEndpoint() });
			}
		};
		
		handler.handle(in, out, null);

		byte[] response = out.toByteArray();

		return response != null && response.length > 0 ? new String(response)
				: null;
	}
}
