package com.javatpoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		request.getRequestDispatcher("link.html").include(request, response);
		
		String name=request.getParameter("name");
		
		byte[] password=request.getParameter("password").getBytes();
		try {
			setPassword("admin123".getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(checkPassword(password)){
				out.print("You are successfully logged ina!");
				out.print("<br>Welcome, "+name);
				
				HttpSession session = request.getSession();
				session.invalidate();
				session = request.getSession(true);
				session.setMaxInactiveInterval(60*15);
				
				String newRandom = "AAAA";
				
				Cookie ck=new Cookie("rememberme" , name + ":" + newRandom);
				response.addCookie(ck);
			}else{
				out.print("sorry, username or password error!");
				request.getRequestDispatcher("login.html").include(request, response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		out.close();
	}

	

	void setPassword(byte[] pass) throws Exception{
	    SecureRandom random = new SecureRandom();
		byte[] salt = new byte[12];
		random.nextBytes(salt);
	
		byte[] input = appendArray(pass, salt);
		
		MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
		
		byte[] hashVal = msgDigest.digest(input);
		clearArray(pass);
		clearArray(input);
		
		
		File fileSalt = new File("c:\\tomcat\\salt.bin");
		fileSalt.createNewFile(); // if file already exists will do nothing 
		 FileOutputStream foss = new FileOutputStream(fileSalt, false);
		  foss.write(salt);
		  foss.close();
			File filePassword = new File("c:\\tomcat\\password.bin");
			filePassword.createNewFile(); // if file already exists will do nothing 
			 FileOutputStream fosp = new FileOutputStream(filePassword, false);
			  fosp.write(hashVal);
			  fosp.close();
		clearArray(salt);
		clearArray(hashVal);
	}
	
	boolean checkPassword(byte[] pass) throws Exception{	
		RandomAccessFile f = new RandomAccessFile("c:\\tomcat\\salt.bin", "r");
		byte[] salt = new byte[(int)f.length()];
		f.readFully(salt);
		f.close();
		
		byte[] input = appendArray(pass, salt);
		
		MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
		
		byte[] hashVal1 = msgDigest.digest(input);
		clearArray(pass);
		clearArray(input);
	
		RandomAccessFile g = new RandomAccessFile("c:\\tomcat\\password.bin", "r");
		byte[] hashVal2 = new byte[(int)g.length()];
		g.readFully(hashVal2);
		g.close();
		
		boolean arraysEqual = Arrays.equals(hashVal1,hashVal2);
		clearArray(hashVal1);
		clearArray(hashVal2);
		return arraysEqual;
	}
	
	
	void clearArray(byte[]a){
		for(int i=0;i<a.length;i++){
			a[i]=0;
		}
	}
	
	byte[] appendArray(byte[] pass, byte[] salt) {	
		byte[] input = new byte[pass.length + salt.length];
		System.arraycopy(pass, 0, input, 0, pass.length);
		System.arraycopy(salt, 0, input, pass.length, salt.length);
		return input;
	}



}
