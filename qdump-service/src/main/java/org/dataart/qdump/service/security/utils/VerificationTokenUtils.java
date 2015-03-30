package org.dataart.qdump.service.security.utils;

import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.security.VerificationTokenEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.time.LocalDateTime;
import java.util.StringTokenizer;

/**
 * Created by artemvlasov on 06/02/15.
 */
public class VerificationTokenUtils {
    private static CryptoUtils cryptoUtils = new CryptoUtils();

    public static String beanToToken(VerificationTokenEntity verificationToken) {
        if(verificationToken == null) {
            throw new RuntimeException("VerificationToken cannot be null");
        }
        PersonEntity personEntity = verificationToken.getPersonEntity();
        if(personEntity == null) {
            throw new RuntimeException("PersonEntity applied to token cannot be null");
        } else if(personEntity.getId() == 0) {
            throw new RuntimeException("PersonEntity id cannot be null");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id=" + personEntity.getId());
        stringBuilder.append("\nemail=" + personEntity.getEmail());
        stringBuilder.append("\nexpire_date=" + verificationToken.getExpireDate());
        stringBuilder.append("\nsalt=" + BCrypt.gensalt());
        return cryptoUtils.encrypt(stringBuilder.toString());
    }

    public static VerificationTokenEntity tokenToBean(String token) {
        if(token == null) {
            throw new RuntimeException("Token cannot be null");
        }
        String decryptedToken = cryptoUtils.decrypt(token);
        StringTokenizer stringTokenizer = new StringTokenizer(decryptedToken, "\n", false);
        VerificationTokenEntity verificationToken = new VerificationTokenEntity();
        PersonEntity entity = new PersonEntity();
        while(stringTokenizer.hasMoreTokens()) {
            String[] splitToken = stringTokenizer.nextToken().split("=");
            switch (splitToken[0]) {
                case "id" : entity.setId(Integer.parseInt(splitToken[1]));
                    break;
                case "email" : entity.setEmail(splitToken[1]);
                    break;
                case "expire_date" : verificationToken.setExpireDate(LocalDateTime.parse(splitToken[1]));
                    break;
                default: break;
            }
        }
        verificationToken.setPersonEntity(entity);
        return verificationToken;
    }
}
