package com.edgit.server.jsf;

import static com.edgit.server.services.AuthenticationType.LDAP;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.NamingException;

import com.edgit.server.domain.User;
import com.edgit.server.filesystem.EdGitRepositoryManager;
import com.edgit.server.jpa.GitFile;
import com.edgit.server.jsf.handlers.ServerRepositoryHandler;
import com.edgit.server.jsf.util.Authentication;
import com.edgit.server.services.AuthenticationService;

@ApplicationScoped
public class UserServiceImpl implements UserService {

	@Inject
	private RepositoryServiceImpl repositoryService;

	@Inject
	@Authentication(LDAP)
	private AuthenticationService authenticationService;

//	@PostConstruct
//	private void init() {
//		authenticationService = AuthenticationServiceFactory.getAuthenticationService(AuthenticationType.LDAP);
//	}

	public User authenticateUser(String identification, String password) {
		return authenticationService.authenticate(identification, password);
	}

	@Override
	public void saveUser(User user) throws NamingException {
		// Creates the root folder that will serve to contain all the projects
		// of this user.
		GitFile root = repositoryService.createRoot(user.getUsername());
		// When the transaction ends, the flush will happen, and the entity
		// outside of the transaction will appear with the generated ID.
		user.setRootRepository(root);
		// save user
		authenticationService.save(user);
		// Create the remote repository for this user
		EdGitRepositoryManager.makeDirectory(
				new File(ServerRepositoryHandler.REPOSITORY_ROOT_LOCATION + File.separator + user.getUsername()));
	}
}

