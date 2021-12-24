package com.ketul.securityConf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
		
	/* 
	 * 1. This is the default schema for h2 database but this is not a good approach
		
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.withDefaultSchema()
			.withUser(
					User.withUsername("user")
						.password("pass")
						.roles("USER")
			).withUser(
					User.withUsername("admin")
						.password("pass")
						.roles("ADMIN")
			);
	*/	
		
	/*
	 * 2. This is also not recommended	
	 * -> 1) In this we create a own table and data, we create 2 files: 
	 * 			1. schema.sql
	 * 				<
	 * 					create table users (
							username varchar_ignorecase(50) not null primary key,
							password varchar_ignorecase(50) not null,
							enabled boolean not null
						);

						create table authorities (
							username varchar_ignorecase(50) not null,
							authority varchar_ignorecase(50) not null,
							constraint fk_authorities_users foreign key(username) references users(username)
						);

						create unique index ix_auth_username on authorities (username, authority);
					>
					
	*			2. data.sql
	*				<
						insert into users(username, password, enabled) values('user', 'pass', true);

						insert into users(username, password, enabled) values('admin', 'pass', true);

						insert into authorities (username, authority) values ('user', 'ROLE_USER');

						insert into authorities (username, authority) values ('admin', 'ROLE_ADMIN');
					>
		
		auth.jdbcAuthentication()
			.dataSource(dataSource);
	*/
		
		/* 
		 * 3. in this we can customize our tables and columns
		 * 
		 * */
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery("select username, password, enabled "
					+ "from users "
					+ "where username = ?")
			.authoritiesByUsernameQuery("select username, authority "
					+ "from authorities "
					+ "where username = ?");
	}
	
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/user").hasAnyRole("USER", "ADMIN")
			.antMatchers("/").permitAll()
			.and().formLogin();
	}

	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
