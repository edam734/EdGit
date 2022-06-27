package com.edgit.server.jpa;

import org.hibernate.dialect.InnoDBStorageEngine;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.MySQLStorageEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MySQL5Dialect still uses the default MyISAM storage engine instead of InnDB.
 * I want to use InnDB to support transactions.
 * <p>
 * There are other ways to achieve this, such as using MySQL55Dialect, or a
 * dialect older than MySQL55Dialect along with the
 * hibernate.dialect.storage_engine=innodb property
 * 
 * @author Eduardo Amorim
 *
 */
public class EdGitMySQL5Dialect extends MySQL5Dialect {

	private static final Logger LOG = LoggerFactory.getLogger(EdGitMySQL5Dialect.class);

	@Override
	protected MySQLStorageEngine getDefaultMySQLStorageEngine() {
		LOG.info("Set storage engine to InnoDB of MySQL.");
		return InnoDBStorageEngine.INSTANCE;
	}
}
