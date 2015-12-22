package org.jstorni.lambdasupport.test.api.endpoints;

import org.jstorni.lambdasupport.endpoint.BaseRepositoryEndpoint;
import org.jstorni.lambdasupport.endpoint.Configuration;
import org.jstorni.lambdasupport.endpoint.annotations.apigateway.Resource;
import org.jstorni.lambdasupport.test.api.model.Merchant;
import org.jstorni.lorm.EntityManager;
import org.jstorni.lorm.Repository;
import org.jstorni.lorm.mapping.EntityToItemMapperImpl;
import org.jstorni.lorm.mapping.ItemToEntityMapperImpl;

@Resource(name="merchant")
public class MerchantEndpoint extends BaseRepositoryEndpoint<Merchant> {

	private final Repository<Merchant> merchantRepository;

	public MerchantEndpoint() {
		EntityManager entityManager = Configuration.getEntitymanager();
		entityManager.addEntity(Merchant.class,
				new EntityToItemMapperImpl<Merchant>(Merchant.class),
				new ItemToEntityMapperImpl<Merchant>(Merchant.class,
						entityManager), new EntityToItemMapperImpl<Merchant>(
						Merchant.class));

		merchantRepository = entityManager.getRepository(Merchant.class);
	}
	
	@Override
	protected Repository<Merchant> getRepository() {
		return merchantRepository;
	}

}
