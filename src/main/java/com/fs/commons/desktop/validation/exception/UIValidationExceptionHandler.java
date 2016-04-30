package com.fs.commons.desktop.validation.exception;

import javax.swing.JComponent;

import com.fs.commons.application.exceptions.util.DefaultExceptionHandler;
import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Severity;
import com.fs.commons.desktop.validation.ui.ComponentDecorator;


public class UIValidationExceptionHandler extends DefaultExceptionHandler<UIValidationException> {
	
	@Override
	public void handleException(UIValidationException e) {
		Problems problems = e.getProblems();
		if(e.getComponent()!=null){
			ComponentDecorator d=new ComponentDecorator();
			JComponent  component = (JComponent) e.getComponent();
			component.setBorder(d.createProblemBorder(component, component.getBorder(), Severity.FATAL));
			component.requestFocus();
			if(problems!=null){
				component.setToolTipText(problems.getLeadProblem().getMessage());
			}
		}else{
			super.handleException(e);
		}
	}
}
