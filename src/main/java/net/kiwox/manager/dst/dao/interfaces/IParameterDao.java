package net.kiwox.manager.dst.dao.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.kiwox.manager.dst.domain.Parameter;

@Repository
public interface IParameterDao extends JpaRepository<Parameter, String> {

	@Query("SELECT p"
			+ " FROM Parameter p"
			+ " WHERE (p.dataType = 'STRING' AND p.name LIKE 'MAIL_SENDER_%')"
			+ " OR (p.dataType = 'INTEGER' AND p.name = 'MAIL_SENDER_PORT')")
	List<Parameter> getMailSenderParameters();
	
}
