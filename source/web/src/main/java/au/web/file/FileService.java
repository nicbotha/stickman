package au.web.file;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileService {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response);
	
	public void doGet(HttpServletRequest request, HttpServletResponse response);
	
	public String create(byte[] content) throws NamingException;
	
	public void delete(String identifier) throws NamingException;
}
