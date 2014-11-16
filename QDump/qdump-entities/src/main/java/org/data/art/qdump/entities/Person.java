package org.data.art.qdump.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PERSONS")
@AttributeOverride(name="id", column=@Column(
		name="person_id", insertable=false, updatable=false))
public class Person extends BaseEntity{
		private String name;
		private String surname;
		private String email;
		private String login;
		private String pass;
		private PersonGroup group;
		
		public Person() {
			super();
		}
		
		public Person(String email, String pass){
			super();
			this.email = email;
			this.pass = pass;
		}

		@Override
		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		public int getId() {
			return id;
		}
		
		@Override
		public void setId(int id) {
			this.id = id;
		}
		
		@Column(name="name",length=32)
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		@Column(name="surname",length=32)
		public String getSurname() {
			return surname;
		}

		public void setSurname(String surname) {
			this.surname = surname;
		}

		@Column(name="email",length=20,nullable=false,unique=true)
		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		@Column(name="login",length=15,nullable=false,unique=true)
		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		@Column(name="pass",length=15,nullable=false)
		public String getPass() {
			return pass;
		}

		public void setPass(String pass) {
			this.pass = pass;
		}
		
		@Column(name="group")
		@Enumerated(EnumType.STRING)
		public PersonGroup getCategory() {
			return group;
		}
		
		public void setPersonGroup(PersonGroup group) {
			this.group = group;
		}

}