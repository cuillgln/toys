
package io.cuillgln.toys.infrastructure.jdbc;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * https://github.com/brettwooldridge/HikariCP
 * https://javadoc.io/doc/com.zaxxer/HikariCP/3.2.0/com/zaxxer/hikari/HikariDataSource.html
 *
 */
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class HikariCPProperties {

	private String jdbcUrl;
	private String username;
	private String password;
	private boolean autoCommit = true;

	private int idleTimeout = 600000;
	private int maxLifeTime = 1800000;
	private int minimumIdle = 10;
	private int maximumPoolSize = 10;
	private int validationTimeout = 5000;
	private Properties dataSource;

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public int getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public int getMaxLifeTime() {
		return maxLifeTime;
	}

	public void setMaxLifeTime(int maxLifeTime) {
		this.maxLifeTime = maxLifeTime;
	}

	public int getMinimumIdle() {
		return minimumIdle;
	}

	public void setMinimumIdle(int minimumIdle) {
		this.minimumIdle = minimumIdle;
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public int getValidationTimeout() {
		return validationTimeout;
	}

	public void setValidationTimeout(int validationTimeout) {
		this.validationTimeout = validationTimeout;
	}

	public Properties getDataSource() {
		return dataSource;
	}

	public void setDataSource(Properties dataSource) {
		this.dataSource = dataSource;
	}

	public static class MySQLDataSource {

		/* MySQL */
		private boolean cachePrepStmts = true;
		private int prepStmtCacheSize = 250;
		private int prepStmtCacheSqlLimit = 2048;
		private boolean useServerPrepStmts = true;
		private boolean useLocalSessionState = true;
		private boolean rewriteBatchedStatements = true;
		private boolean cacheResultSetMetadata = true;
		private boolean cacheServerConfiguration = true;
		private boolean elideSetAutoCommits = true;
		private boolean maintainTimeStats = false;

		public boolean isCachePrepStmts() {
			return cachePrepStmts;
		}

		public void setCachePrepStmts(boolean cachePrepStmts) {
			this.cachePrepStmts = cachePrepStmts;
		}

		public int getPrepStmtCacheSize() {
			return prepStmtCacheSize;
		}

		public void setPrepStmtCacheSize(int prepStmtCacheSize) {
			this.prepStmtCacheSize = prepStmtCacheSize;
		}

		public int getPrepStmtCacheSqlLimit() {
			return prepStmtCacheSqlLimit;
		}

		public void setPrepStmtCacheSqlLimit(int prepStmtCacheSqlLimit) {
			this.prepStmtCacheSqlLimit = prepStmtCacheSqlLimit;
		}

		public boolean isUseServerPrepStmts() {
			return useServerPrepStmts;
		}

		public void setUseServerPrepStmts(boolean useServerPrepStmts) {
			this.useServerPrepStmts = useServerPrepStmts;
		}

		public boolean isUseLocalSessionState() {
			return useLocalSessionState;
		}

		public void setUseLocalSessionState(boolean useLocalSessionState) {
			this.useLocalSessionState = useLocalSessionState;
		}

		public boolean isRewriteBatchedStatements() {
			return rewriteBatchedStatements;
		}

		public void setRewriteBatchedStatements(boolean rewriteBatchedStatements) {
			this.rewriteBatchedStatements = rewriteBatchedStatements;
		}

		public boolean isCacheResultSetMetadata() {
			return cacheResultSetMetadata;
		}

		public void setCacheResultSetMetadata(boolean cacheResultSetMetadata) {
			this.cacheResultSetMetadata = cacheResultSetMetadata;
		}

		public boolean isCacheServerConfiguration() {
			return cacheServerConfiguration;
		}

		public void setCacheServerConfiguration(boolean cacheServerConfiguration) {
			this.cacheServerConfiguration = cacheServerConfiguration;
		}

		public boolean isElideSetAutoCommits() {
			return elideSetAutoCommits;
		}

		public void setElideSetAutoCommits(boolean elideSetAutoCommits) {
			this.elideSetAutoCommits = elideSetAutoCommits;
		}

		public boolean isMaintainTimeStats() {
			return maintainTimeStats;
		}

		public void setMaintainTimeStats(boolean maintainTimeStats) {
			this.maintainTimeStats = maintainTimeStats;
		}

	}
}
