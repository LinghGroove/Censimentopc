package it.contrader.hardwaretracking.service;

import it.contrader.hardwaretracking.entity.User;

import javax.mail.MessagingException;

public interface UserService {
	
	User findUserByUsername(String username);

	User register(User user) throws MessagingException;

	User update(User user);

}
