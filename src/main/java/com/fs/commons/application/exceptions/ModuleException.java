package com.fs.commons.application.exceptions;

import com.fs.commons.application.Module;

public class ModuleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Module module;

	public ModuleException(Module module) {
		super();
		this.module = module;
	}

	public ModuleException(Module moduele, String message, Throwable cause) {
		super(message, cause);
		module = moduele;
	}

	public ModuleException(Module module, String message) {
		super(message);
		this.module = module;
	}

	public ModuleException(Module module, Throwable cause) {
		super(cause);
		this.module = module;
	}

	public Module getModule() {
		return module;
	}

}
