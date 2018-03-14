package com.demo.presistent.config;

public class HibernateConst {
	
	public enum Config {
		HIBERNATE_CURRENT_SESSION_CONTEXT_CLASS("hibernate.current_session_context_class"),
		HIBERNATE_QUERY_FACTORY_CLASS("hibernate.query.factory_class"),
		HIBERNATE_QUERY_SUBSTITUTIONS("hibernate.query.substitutions"),
		HIBERNATE_HBM2DDL_AUTO("hibernate.hbm2ddl.auto"),
		HIBERNATE_CGLIB_USE_REFLECTION_OPTIMIZER("hibernate.cglib.use_reflection_optimizer"),
		HIBERNATE_DIALECT("hibernate.dialect"),
		HIBERNATE_show_sql("hibernate.show_sql"),
		HIBERNATE_format_sql("hibernate.format_sql"),
		HIBERNATE_default_schema("hibernate.default_schema"),
		HIBERNATE_default_catalog("hibernate.default_catalog"),
		HIBERNATE_session_factory_name("hibernate.session_factory_name"),
		HIBERNATE_max_fetch_depth("hibernate.max_fetch_depth"),
		HIBERNATE_default_batch_fetch_size("hibernate.default_batch_fetch_size"),
		HIBERNATE_default_entity_mode("hibernate.default_entity_mode"),
		HIBERNATE_order_updates("hibernate.order_updates"),
		HIBERNATE_generate_statistics("hibernate.generate_statistics"),
		HIBERNATE_use_identifier_rollback("hibernate.use_identifier_rollback"),
		HIBERNATE_use_sql_comments("hibernate.use_sql_comments");
	
		private String value;
		Config(String aValue) {
	        value=aValue;
	    }

	    public String getValue() {
	        return value;
	    }
	}
	
	public enum ConnectProp {
		HIBERNATE_DIALECT("hibernate.jdbc.fetch_size"),
		HIBERNATE_JDBC_BATCH_SIZE("hibernate.jdbc.batch_size"),
		HIBERNATE_JDBC_BATCH_VERSIONED_DATA("hibernate.jdbc.batch_versioned_data"),
		HIBERNATE_JDBC_FACTORY_CLASS("hibernate.jdbc.factory_class"),
		HIBERNATE_JDBC_USE_SCROLLABLE_RESULTSET("hibernate.jdbc.use_scrollable_resultset"),
		HIBERNATE_JDBC_USE_STREAMS_FOR_BINARY("hibernate.jdbc.use_streams_for_binary"),
		HIBERNATE_JDBC_USE_GET_GENERATED_KEYS("hibernate.jdbc.use_get_generated_keys"),
		HIBERNATE_CONNECTION_PROVIDER_CLASS("hibernate.connection.provider_class"),
		HIBERNATE_CONNECTION_ISOLATION("hibernate.connection.isolation"),
		HIBERNATE_CONNECTION_AUTOCOMMIT("hibernate.connection.autocommit"),
		HIBERNATE_CONNECTION_RELEASE_MODE("hibernate.connection.release_mode");
		
		private String value;
		ConnectProp(String aValue) {
			value=aValue;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	public enum CacheProp {
		HIBERNATE_CACHE_PROVIDER_CLASS("hibernate.cache.provider_class"),
		HIBERNATE_CACHE_USE_MINIMAL_PUTS("hibernate.cache.use_minimal_puts"),
		HIBERNATE_CACHE_USE_QUERY_CACHE("hibernate.cache.use_query_cache"),
		HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE("hibernate.cache.use_second_level_cache"),
		HIBERNATE_CACHE_QUERY_CACHE_FACTORY("hibernate.cache.query_cache_factory"),
		HIBERNATE_CACHE_REGION_PREFIX("hibernate.cache.region_prefix"),
		HIBERNATE_CACHE_USE_STRUCTURED_ENTRIES("hibernate.cache.use_structured_entries");
		
		private String value;
		CacheProp(String aValue) {
			value=aValue;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	public enum MultTenantProp {
		HIBERNATE_MULTITENANCY("hibernate.multiTenancy"),
		HIBERNATE_TENANT_IDENTIFIER_RESOLVER("hibernate.tenant_identifier_resolver"),
		HIBERNATE_MULTI_TENANT_CONNECTION_PROVIDER("hibernate.multi_tenant_connection_provider");
		private String value;
		MultTenantProp(String aValue) {
			value=aValue;
		}
		
		public String getValue() {
			return value;
		}
	}
}
