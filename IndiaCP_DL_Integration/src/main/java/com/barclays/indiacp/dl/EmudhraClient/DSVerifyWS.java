/**
 * DSVerifyWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.barclays.indiacp.dl.EmudhraClient;

public interface DSVerifyWS extends java.rmi.Remote {
    public String signPdf(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) throws java.rmi.RemoteException;
    public String decryptionData(String arg0, String arg1) throws java.rmi.RemoteException;
    public String protectedPdf(String arg0, String arg1) throws java.rmi.RemoteException;
    public String encryptionData(String arg0, String arg1) throws java.rmi.RemoteException;
    public String initialization() throws java.rmi.RemoteException;
}
