package org.dataart.qdump.persistence.security;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.persistence.repository.PersonCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by artemvlasov on 27/01/15.
 */
@Component
public class QdumpRealm extends AuthorizingRealm {

    @Autowired
    private PersonCrudRepository personCrudRepository;

    public QdumpRealm() {
        setName("QdumpRealm");
        setCredentialsMatcher(new BcryptCredentialsMatcher());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Long personId = (Long) principals.fromRealm(getName()).iterator().next();
        PersonEntity person = personCrudRepository.findOne(personId);
        if(person != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for(PersonGroupEnums group : PersonGroupEnums.values()) {
                if(person.getPersonGroup() == group) {
                    info.addRole(person.getPersonGroup().name());
                }
            }
            return info;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        PersonEntity person = null;
        String tokenUsername = token.getUsername();
        if(EmailValidator.getInstance().isValid(tokenUsername)) {
            person = personCrudRepository.findByEmail(tokenUsername);
        } else {
            person = personCrudRepository.findByLogin(token.getUsername());
        }
        if(person != null) {
            return new SimpleAuthenticationInfo(person.getId(), person.getPassword(), getName());
        } else {
            return null;
        }
    }
}
