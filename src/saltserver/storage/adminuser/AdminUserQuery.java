/*
 *    https://code.google.com/p/vellum - Contributed by Evan Summers
 * 
 */
package saltserver.storage.adminuser;

/**
 *
 * @author evan.summers
 */
public enum AdminUserQuery {
    validate,
    insert,
    update_subject,
    exists_username,
    exists_email,
    exists_subject,
    delete_username,
    find_username,
    find_email,
    find_subject,
    list,
}