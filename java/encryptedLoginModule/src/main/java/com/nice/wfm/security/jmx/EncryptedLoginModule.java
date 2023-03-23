package com.nice.wfm.security.jmx;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import javax.management.remote.JMXPrincipal;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.wfm.security.util.EncryptedString;


//import com.sun.jmx.remote.security.FileLoginModule;

public class EncryptedLoginModule implements LoginModule {

	private static final Logger logger = LoggerFactory.getLogger(EncryptedLoginModule.class);
	
    // Location of the default password file
    private static final String PASSWORD_FILE_KEY = "passwordFile";

    private static final String DEFAULT_PASSWORD_FILE_NAME = "/totalview/inst/tv4/config/tvconfig/jmxremote.password";
    
    
    // Key to retrieve the stored username
    private static final String USERNAME_KEY =
        "javax.security.auth.login.name";

    // Key to retrieve the stored password
    private static final String PASSWORD_KEY =
        "javax.security.auth.login.password";
    
    // Configurable options
    private boolean useFirstPass = false;
    private boolean tryFirstPass = false;
    private boolean storePass = false;
    private boolean clearPass = false;
    
    // Authentication status
    private boolean succeeded = false;
    private boolean commitSucceeded = false;
    
    // Supplied username and password
    private String username;
    private char[] password;
    private JMXPrincipal user;
    
	// Initial state
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, Object> sharedState;
    private Map<String, ?> options;
    private String passwordFile;
    private String passwordFileDisplayName;
    private boolean hasJavaHomePermission;
    private Properties userCredentials;
    private boolean userSuppliedPasswordFile;

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
			Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = cast(sharedState);
        this.options = options;
        
        // initialize any configured options
        tryFirstPass =
                "true".equalsIgnoreCase((String)options.get("tryFirstPass"));
        useFirstPass =
                "true".equalsIgnoreCase((String)options.get("useFirstPass"));
        storePass =
                "true".equalsIgnoreCase((String)options.get("storePass"));
        clearPass =
                "true".equalsIgnoreCase((String)options.get("clearPass"));

        passwordFile = (String)options.get("passwordFile");
        passwordFileDisplayName = passwordFile;
        userSuppliedPasswordFile = true;
        
        // set the location of the password file
        if (passwordFile == null) {
            passwordFile = DEFAULT_PASSWORD_FILE_NAME;
            userSuppliedPasswordFile = false;
            try {
                System.getProperty("java.home");
                hasJavaHomePermission = true;
                passwordFileDisplayName = passwordFile;
            } catch (SecurityException e) {
                hasJavaHomePermission = false;
                passwordFileDisplayName = PASSWORD_FILE_KEY;
            }
        }
	}
	
	@Override
    public boolean login() throws LoginException {

        try {
            loadPasswordFile();
        } catch (IOException ioe) {
            LoginException le = new LoginException(
                    "Error: unable to load the password file: " +
                    passwordFileDisplayName);
            le.initCause(ioe);
            throw le;
        }

        if (userCredentials == null) {
            throw new LoginException
                ("Error: unable to locate the users' credentials.");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("login",
                    "Using password file: " + passwordFileDisplayName);
        }

        // attempt the authentication
        if (tryFirstPass) {

            try {
                // attempt the authentication by getting the
                // username and password from shared state
                attemptAuthentication(true);

                // authentication succeeded
                succeeded = true;
                if (logger.isDebugEnabled()) {
                    logger.debug("login",
                        "Authentication using cached password has succeeded");
                }
                return true;

            } catch (LoginException le) {
                // authentication failed -- try again below by prompting
                cleanState();
                logger.debug("login",
                    "Authentication using cached password has failed");
            }

        } else if (useFirstPass) {

            try {
                // attempt the authentication by getting the
                // username and password from shared state
                attemptAuthentication(true);

                // authentication succeeded
                succeeded = true;
                if (logger.isDebugEnabled()) {
                    logger.debug("login",
                        "Authentication using cached password has succeeded");
                }
                return true;

            } catch (LoginException le) {
                // authentication failed
                cleanState();
                logger.debug("login",
                    "Authentication using cached password has failed");

                throw le;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("login", "Acquiring password");
        }

        // attempt the authentication using the supplied username and password
        try {
            attemptAuthentication(false);

            // authentication succeeded
            succeeded = true;
            if (logger.isDebugEnabled()) {
                logger.debug("login", "Authentication has succeeded");
            }
            return true;

        } catch (LoginException le) {
            cleanState();
            logger.debug("login", "Authentication has failed");

            throw le;
        }
    }

	@Override
    public boolean commit() throws LoginException {

        if (succeeded == false) {
            return false;
        } else {
            if (subject.isReadOnly()) {
                cleanState();
                throw new LoginException("Subject is read-only");
            }
            // add Principals to the Subject
            if (!subject.getPrincipals().contains(user)) {
                subject.getPrincipals().add(user);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("commit",
                    "Authentication has completed successfully");
            }
        }
        // in any case, clean out state
        cleanState();
        commitSucceeded = true;
        return true;
    }

	@Override
	public boolean abort() throws LoginException {
       if (logger.isDebugEnabled()) {
            logger.debug("abort",
                "Authentication has not completed successfully");
        }

        if (succeeded == false) {
            return false;
        } else if (succeeded == true && commitSucceeded == false) {

            // Clean out state
            succeeded = false;
            cleanState();
            user = null;
        } else {
            // overall authentication succeeded and commit succeeded,
            // but someone else's commit failed
            logout();
        }
        return true;
	}

	@Override
    public boolean logout() throws LoginException {
        if (subject.isReadOnly()) {
            cleanState();
            throw new LoginException ("Subject is read-only");
        }
        subject.getPrincipals().remove(user);

        // clean out state
        cleanState();
        succeeded = false;
        commitSucceeded = false;
        user = null;

        if (logger.isDebugEnabled()) {
            logger.debug("logout", "Subject is being logged out");
        }

        return true;
    }
	
    @SuppressWarnings("unchecked")  // sharedState used as Map<String,Object>
    private void attemptAuthentication(boolean usePasswdFromSharedState)
        throws LoginException {

        // get the username and password
        getUsernamePassword(usePasswdFromSharedState);

        String localPassword = userCredentials.getProperty(username);
        localPassword = this.decode(localPassword);
        
        // userCredentials is initialized in login()
        if ((localPassword == null) || (!localPassword.equals(new String(password)))) {
            if (logger.isDebugEnabled()) {
                logger.debug("login", "Invalid username or password");
            }
            throw new FailedLoginException("Invalid username or password");
        }

        // Save the username and password in the shared state
        // only if authentication succeeded
        if (storePass &&
            !sharedState.containsKey(USERNAME_KEY) &&
            !sharedState.containsKey(PASSWORD_KEY)) {
            sharedState.put(USERNAME_KEY, username);
            sharedState.put(PASSWORD_KEY, password);
        }

        // Create a new user principal
        user = new JMXPrincipal(username);

        if (logger.isDebugEnabled()) {
            logger.debug("login",
                "User '" + username + "' successfully validated");
        }
    }
    
    private void getUsernamePassword(boolean usePasswdFromSharedState)
            throws LoginException {

            if (usePasswdFromSharedState) {
                // use the password saved by the first module in the stack
                username = (String)sharedState.get(USERNAME_KEY);
                password = (char[])sharedState.get(PASSWORD_KEY);
                return;
            }

            // acquire username and password
            if (callbackHandler == null)
                throw new LoginException("Error: no CallbackHandler available " +
                    "to garner authentication information from the user");

            Callback[] callbacks = new Callback[2];
            callbacks[0] = new NameCallback("username");
            callbacks[1] = new PasswordCallback("password", false);

            try {
                callbackHandler.handle(callbacks);
                username = ((NameCallback)callbacks[0]).getName();
                char[] tmpPassword = ((PasswordCallback)callbacks[1]).getPassword();
                password = new char[tmpPassword.length];
                System.arraycopy(tmpPassword, 0,
                                    password, 0, tmpPassword.length);
                ((PasswordCallback)callbacks[1]).clearPassword();

            } catch (IOException ioe) {
                LoginException le = new LoginException(ioe.toString());
                le.initCause(ioe);
                throw le;
            } catch (UnsupportedCallbackException uce) {
                LoginException le = new LoginException(
                                        "Error: " + uce.getCallback().toString() +
                                        " not available to garner authentication " +
                                        "information from the user");
                le.initCause(uce);
                throw le;
            }
        }
    
    private void loadPasswordFile() throws IOException {
        FileInputStream fis;
        try {
            fis = new FileInputStream(passwordFile);
        } catch (SecurityException e) {
            if (userSuppliedPasswordFile || hasJavaHomePermission) {
                throw e;
            } else {
                final FilePermission fp =
                        new FilePermission(passwordFileDisplayName, "read");
                AccessControlException ace = new AccessControlException(
                        "access denied " + fp.toString());
                ace.setStackTrace(e.getStackTrace());
                throw ace;
            }
        }
        
        try {
            final BufferedInputStream bis = new BufferedInputStream(fis);
            try {
                userCredentials = new Properties();
                userCredentials.load(bis);
            } finally {
                bis.close();
            }
        } finally {
            fis.close();
        }
    }
    
    /**
     * Clean out state because of a failed authentication attempt
     */
    private void cleanState() {
        username = null;
        if (password != null) {
            Arrays.fill(password, ' ');
            password = null;
        }

        if (clearPass) {
            sharedState.remove(USERNAME_KEY);
            sharedState.remove(PASSWORD_KEY);
        }
    }
    
	private static <T> T cast(Object x) {
        return (T) x;
    }
	
	private String decode(String value) {
		if(value != null && Base64.isBase64(value.getBytes())) {
			EncryptedString encryptedString = EncryptedString.createFromCryptext(value);
			return encryptedString.getPlaintext();
		} else {
			return value;
		}
	}
	
	public static void main(String[] argv) {
		System.out.println();
	}
}
