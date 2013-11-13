package org.t4f.msc.managedbeans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;






public class ManagedBeanCommon {
	
	
	public void addMessage(String clientID, String mensaje) {
		
		FacesContext.getCurrentInstance().addMessage(clientID, new FacesMessage(mensaje));
	}
	
	public String getParameter(String parametro) {

		FacesContext context = FacesContext.getCurrentInstance();
		
		return (String) context.getExternalContext()
							.getRequestParameterMap().get(parametro);

	}
	
	public void addAttribute(String nombre, Object valor) {
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
		request.setAttribute(nombre, valor);		
		
	}

	
	public ManagedBeanCommon getManagedBean(String nombre) {
		
		FacesContext fc = FacesContext.getCurrentInstance();		
		ManagedBeanCommon mb = (ManagedBeanCommon) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, nombre);		   
		
		return mb;
	}
	
	public HttpServletRequest getRequest() {
		return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

	}
	
}
