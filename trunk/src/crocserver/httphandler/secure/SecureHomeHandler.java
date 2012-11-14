/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.secure;

import crocserver.storage.servicerecord.ServiceRecord;
import crocserver.app.CrocApp;
import crocserver.httphandler.common.AbstractPageHandler;
import crocserver.storage.adminuser.AdminUser;
import crocserver.storage.common.CrocStorage;
import java.util.Collection;
import java.util.Iterator;
import vellum.datatype.Millis;
import vellum.html.HtmlPrinter;
import vellum.logr.LogrFactory;
import vellum.logr.LogrRecord;
import vellum.format.ListFormats;
import crocserver.storage.org.Org;
import crocserver.storage.servicecert.ClientService;
import vellum.format.CalendarFormats;
import vellum.logr.LogrLevel;

/**
 *
 * @author evans
 */
public class SecureHomeHandler extends AbstractPageHandler {

    CrocApp app;
    CrocStorage storage;
    
    public SecureHomeHandler(CrocApp app) {
        super();
        this.app = app;
        this.storage = app.getStorage();
    }

    @Override
    protected void handle() throws Exception {
        h.div("menuBarDiv");
        h.a_("/", "Home");
        h._div();
        printOrgs("orgs", storage.getOrgStorage().getList());
        printUsers("admin users", storage.getUserStorage().getList());
        printCerts("certs", storage.getClientCertStorage().getList());
        printSeviceRecords("service records", storage.getServiceRecordStorage().getList());
        if (LogrFactory.getDefaultLevel().ordinal() < LogrLevel.INFO.ordinal()) {
            printLog("log", LogrFactory.getDequerProvider().getDequerHandler().getDequer().tailDescending(100));
        }
    }

    private void printOrgs(String label, Collection<Org> orgs) {
        h.h(3, label);
        h.tableDiv("resultSet");
        h.trh("id", "org name", "display name", "url", "updated");
        for (Org org : orgs) {
            h.trd(
                    String.format("<a href='/view/org/%d'>%d</a>", org.getId(), org.getId()),
                    org.getName(),
                    org.getDisplayName(),
                    org.getUrl(),
                    CalendarFormats.timestampFormat.format(org.getUpdated()));
        }
        h._table();
        h._div();
    }

    private void printUsers(String label, Collection<AdminUser> users) {
        h.h(3, label);
        h.tableDiv("resultSet");
        h.trh("id", "username", "display name", "email", "updated");
        for (AdminUser user : users) {
            h.trd(
                    String.format("<a href='/view/user/%s'>%s</a>", user.getId(), user.getId()),
                    user.getUserName(),
                    user.getDisplayName(),
                    user.getEmail(),
                    CalendarFormats.timestampFormat.format(user.getUpdated()));
        }
        h._table();
        h._div();
    }

    private void printCerts(String label, Collection<ClientService> certs) {
        h.h(3, label);
        h.tableDiv("resultSet");
        h.trh("id", "org", "host", "client", "updated", "updated by");
        for (ClientService cert : certs) {
            h.trd(
                    String.format("<a href='/view/cert/%s'>%s</a>", cert.getId(), cert.getId()),
                    cert.getOrgId(),
                    cert.getHostName(),
                    cert.getServiceName(),
                    CalendarFormats.timestampFormat.format(cert.getUpdated()),
                    cert.getUpdatedBy());
        }
        h._table();
        h._div();
    }

    private void printSeviceRecords(String label, Collection<ServiceRecord> serviceRecords) {
        h.h(3, label);
        h.tableDiv("resultSet");
        for (ServiceRecord serviceRecord : serviceRecords) {
            h.trd(
                    String.format("<a href='/view/serviceRecord/%d'>%d</a>", serviceRecord.getId(), serviceRecord.getId()),
                    Millis.format(serviceRecord.getTimestamp()),
                    serviceRecord.getHostName(),
                    serviceRecord.getServiceName(),
                    serviceRecord.getServiceStatus());
        }
        h._tableDiv();
    }

    private void printLog(String label, Collection<LogrRecord> records) {
        printLog(label, records.iterator());
    }

    private void printLog(String label, Iterator<LogrRecord> iterator) {
        HtmlPrinter p = new HtmlPrinter(out);
        p.h(3, label);
        p.tableDiv("resultSet");
        p.thead();
        p._thead();
        p.tbody();
        while (iterator.hasNext()) {
            LogrRecord record = iterator.next();
            p.trd(Millis.formatAsSeconds(record.getTimestamp()),
                    record.getContext().getName(),
                    record.getLevel(), record.getMessage(),
                    ListFormats.displayFormatter.formatArray(record.getArgs()));
        }
        p._tbody();
        p._tableDiv();
    }
}
