package org.metricminer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.metricminer.infra.encryptor.Encryptor;

@Entity
public class User implements Serializable {
	@Id @GeneratedValue
	private Long id;
	
	@NotEmpty(message="Name should not be empty")
	private String name;
	
	@Column(unique = true) 
	@NotEmpty(message="Email should not be empty")
	@Email(message="Email should be valid")
	private String email;
	
	@NotEmpty(message="Password should not be empty")
	@Length(min=5, message="Password length should be at least 5")
	private String password;
	
	@Enumerated(EnumType.STRING)
	private UserRole role = UserRole.RESEARCHER;
	
	private String university;
	private String cvUrl;
	private String twitter;
	
	@Transient
	private static final long serialVersionUID = 1L;
	@Transient
	private String passwordConfirmation;

	public User() {
	}

	public boolean isValid() {
		return password.equals(passwordConfirmation);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getUniversity() {
		return university;
	}

	public String getCvUrl() {
		return cvUrl;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email
				+ ", password=" + password + ", university=" + university
				+ ", cvUrl=" + cvUrl + ", twitter=" + twitter
				+ ", passwordConfirmation=" + passwordConfirmation + "]";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCvUrl(String cvUrl) {
		this.cvUrl = cvUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public void setUniversity(String university) {
		this.university = university;
	}
	
	public void encryptPassword(Encryptor encryptor) {
		this.password = encryptor.encrypt(password);
	}
	
	public UserRole getRole() {
		return role;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (this.getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!this.getId().equals(other.getId()))
            return false;
        return true;
    }
	
}
