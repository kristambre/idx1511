package controller;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import dao.EntityDAO;
import entity.subject.Person;

@ManagedBean
@SessionScoped
public class SessionController {

	private static Person loggedInUser;

	public String login() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String[] names = params.get("username").split(" ");
		String firstName = names[0];
		String lastName = names[1];
		String password = params.get("password");

		Person p = null;
		EntityDAO dao = new EntityDAO();
		p = dao.findEmployeeByName(firstName, lastName);
		if (p != null) {
			loggedInUser = p;
			return "/index?faces-redirect=true";
		}
		return null;
	}

	public void logout() {
		loggedInUser = null;
	}

	public Person getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(Person loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public static Person loggedInUser() {
		return loggedInUser;
	}

}
