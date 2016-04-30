package  com.fs.security;

import com.fs.commons.application.AbstractModule;
import com.fs.commons.application.exceptions.ModuleException;
import com.fs.commons.security.SecurityManager;

public class SecurityModule extends AbstractModule{
	@Override
	public void init() throws ModuleException {
		SecurityImpl securityImpl = new SecurityImpl();
		SecurityManager.setAuthenticaor(securityImpl);
		SecurityManager.setAuthorizer(securityImpl);
		super.init();
	}
}
