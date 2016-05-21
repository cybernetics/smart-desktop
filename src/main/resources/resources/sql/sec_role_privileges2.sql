SELECT
sec_roles.role_id,
sec_roles.role_name,
sec_privileges.privilege_desc
FROM
sec_privileges
Inner Join sec_role_privileges ON sec_role_privileges.privilege_id = sec_privileges.privilege_id
Inner Join sec_roles ON sec_role_privileges.role_id = sec_roles.role_id
where sec_roles.role_id > 0
ORDER BY
sec_roles.role_name ASC
