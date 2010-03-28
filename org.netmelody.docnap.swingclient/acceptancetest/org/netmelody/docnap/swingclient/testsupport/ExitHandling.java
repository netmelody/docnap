package org.netmelody.docnap.swingclient.testsupport;

import java.security.Permission;


public class ExitHandling {
    
    private static SecurityManager securityManager;

    public static void overrideSecurityManager() {
        securityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());
    }

    public static void restoreDefaultSecurityManager() {
        System.setSecurityManager(securityManager);
    }

    
    public static class ExitException extends SecurityException {
        private static final long serialVersionUID = -1982617086752946683L;
        public final int status;

        public ExitException(int status) {
            super("There is no escape!");
            this.status = status;
        }
    }

    private static class NoExitSecurityManager extends SecurityManager {
        @Override
        public void checkPermission(Permission perm) {
            // allow anything
        }

        @Override
        public void checkPermission(Permission perm, Object context) {
            // allow anything.
        }

        @Override
        public void checkExit(int status) {
            super.checkExit(status);
            throw new ExitException(status);
        }
    }

    

}
