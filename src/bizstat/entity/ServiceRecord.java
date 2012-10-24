/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.entity;

import vellum.util.Args;
import bizstat.enumtype.ServiceStatus;
import java.util.List;
import vellum.datatype.Millis;
import vellum.entity.LongIdEntity;
import vellum.datatype.Timestamped;
import vellum.util.Strings;

/**
 *
 * @author evan
 */
public class ServiceRecord extends LongIdEntity implements Timestamped {

    String[] args;
    String outText;
    String errText;
    int exitCode;
    long dispatchedMillis;
    long receivedMillis;
    long notifiedMillis;
    long timestampMillis;
    transient ServiceStatus serviceStatus;
    transient Throwable exception;
    transient Host host;
    transient Service service;
    transient List<String> outList;
    
    public ServiceRecord() {
    }

    public ServiceRecord(Host host, Service service) {
        this.host = host;
        this.service = service;
    }
    
    public ServiceRecord(Host host, Service service, long dispatchedMillis) {
        this(host, service);
        this.dispatchedMillis = dispatchedMillis;
    }
    public ServiceRecord(Host host, Service service, ServiceStatus serviceStatus, long timestampMillis, String outText) {
        this(host, service);
        this.serviceStatus = serviceStatus;
        this.timestampMillis = timestampMillis;
        this.outText = outText;        
    }
        
    public HostServiceKey getKey() {
        return new HostServiceKey(host, service);
    }

    public Host getHost() {
        return getKey().getHost();
    }
    
    public Service getService() {
        return getKey().getService();
    }
    
    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getErrText() {
        return errText;
    }

    public void setErrText(String errText) {
        this.errText = errText;
    }

    public void setOutList(List<String> outList) {
        this.outList = outList;
        this.outText = Strings.joinLines(outList);
    }

    public List<String> getOutList() {
        return outList;
    }
    
    public String getOutText() {
        return outText;
    }

    public void setOutText(String outText) {
        this.outText = outText;
    }

    public String getMessage() {
        if (outText == null) {
            return null;
        }
        String text = outText.trim();
        int index = text.lastIndexOf("\n");
        if (index > 0) {
            return text.substring(index + 1);
        }
        return text;
    }

    public void setTimestampMillis(long timestampMillis) {
        this.timestampMillis = timestampMillis;
    }
    
    @Override
    public long getTimestamp() {
        return timestampMillis;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
        if (exitCode == 127) {
            this.serviceStatus = ServiceStatus.ERROR;
        } else if (exitCode == 255) {
            this.serviceStatus = ServiceStatus.ERROR;
        } else if (exitCode < ServiceStatus.NONZERO.ordinal()) {
            this.serviceStatus = ServiceStatus.find(exitCode);
        } else {
            this.serviceStatus = ServiceStatus.NONZERO;
        }
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }

    public long getNotifiedMillis() {
        return notifiedMillis;
    }

    public void setNotifiedMillis(long notifiedMillis) {
        this.notifiedMillis = notifiedMillis;
    }

    public long getReceivedMillis() {
        return receivedMillis;
    }

    public void setReceivedMillis(long receivedMillis) {
        this.receivedMillis = receivedMillis;
    }

    public long getDispatchedMillis() {
        return dispatchedMillis;
    }

    public void setDispatchedMillis(long dispatchedMillis) {
        this.dispatchedMillis = dispatchedMillis;
    }

    public boolean isKnown() {
        return serviceStatus != null && serviceStatus.isKnown();
    }

    @Override
    public String toString() {
        return Args.format(host, service, serviceStatus, Millis.formatTime(dispatchedMillis));
    }    
}
