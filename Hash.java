import java.security.NoSuchAlgorithmException; //Imports exception for handling just in case the algorithm does not exist



/**
 * @author ritvikverma
 * Hash interface for defining a blueprint of the hashing function
 */


interface Hash {
	String hashPassword(String password) throws NoSuchAlgorithmException;
}
