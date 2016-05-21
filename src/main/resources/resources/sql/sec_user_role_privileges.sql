SELECT DISTINCT
sec_roles.role_id,
sec_users.user_full_name,
sec_roles.role_name,
sec_privileges.privilege_desc
FROM
sec_user_roles
Inner Join sec_roles ON sec_user_roles.role_id = sec_roles.role_id
Inner Join sec_users ON sec_user_roles.user_id = sec_users.user_record_id
Inner Join sec_role_privileges ON sec_role_privileges.role_id = sec_roles.role_id
Inner Join sec_privileges ON sec_role_privileges.privilege_id = sec_privileges.privilege_id
WHERE sec_users.user_record_id= "?" 
ORDER BY
sec_users.user_full_name ASC,
sec_roles.role_name ASC
