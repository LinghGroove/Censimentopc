package it.contrader.hardwaretracking.service;

import it.contrader.hardwaretracking.DAO.UserRepository;
import it.contrader.hardwaretracking.entity.User;
import it.contrader.hardwaretracking.entity.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
	
	User user = new User();
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final LoginAttemptService loginAttemptService;
	private final EmailService emailService;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
			LoginAttemptService loginAttemptService, EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.loginAttemptService = loginAttemptService;
		this.emailService = emailService;
	}
	
	@Override
	public User findUserByUsername(String username) {
		System.out.println("Sono al service");
		return userRepository.findByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username);
		
		if(user == null) {
			System.out.println("User not found by username \"" + username + "\"");
			throw new UsernameNotFoundException("User not found by username \"" + username + "\"");
		}else {
			try {
				validateLoginAttempt(user);
			} catch (ExecutionException e) {
				e.printStackTrace();
			} 
			user.setLastLogin(new Date(System.currentTimeMillis() + 3600*1000)); //aggiorniamo l'ultimo accesso alla data corrente
			userRepository.save(user);
			UserPrincipal userPrincipal = new UserPrincipal(user);
			System.out.println("Returning found user by username \"" + username + "\"");
			return userPrincipal; //perchè UserPrincipal implementa  UserDetails
		}
	}

	@Override
	public User register(User user) throws MessagingException {
			
		final String[] authorities = {};
		
		String cryptedPassword = passwordEncoder.encode(user.getPassword());
		String notCryptedPassword = user.getPassword();

		user.setJoinDate(new Date(System.currentTimeMillis() + 3600*1000));
		user.setPassword(cryptedPassword);
		user.setActive(true);
		user.setNotLocked(true);
		user.setAuthorities(authorities);

		emailService.sendNewPasswordEmail(user.getFirstName(), notCryptedPassword, user.getEmail());
		userRepository.save(user);
		
		return user;
		
	}

	@Override
	public User update(User modifiedData) {
		System.out.println("Sono al service " + modifiedData.getUsername());
		User user = userRepository.findById(modifiedData.getId()).orElse(null);
		user.setUsername(modifiedData.getUsername());
		System.out.println(user);
		if(!modifiedData.getPassword().equals(""))
			user.setPassword(passwordEncoder.encode(modifiedData.getPassword()));
		userRepository.save(user);
		return user;
	}


	private void validateLoginAttempt(User user) throws ExecutionException {
		if(user.isNotLocked()) { //se non è bloccato
			if(loginAttemptService.hasExceededMaxAttempts(user.getUsername())){
				//se ha superato il numero massimo di tentativi...
				user.setNotLocked(false); //bloccalo
			}else 
				user.setNotLocked(true);
	} else {
		loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
	}
		
	}


}
