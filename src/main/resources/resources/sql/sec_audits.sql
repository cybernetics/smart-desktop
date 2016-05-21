SELECT
sec_audits.audit_id,
sec_users.user_id,
sec_audits.audit_date,
sec_audit_type.audit_type_name,
sec_modules.module_name,
sec_audits.record_id,
sec_audits.record_name,
sec_audits.description
FROM
sec_audits
LEFT Join sec_users ON sec_audits.user_record_id = sec_users.user_record_id
LEFT Join sec_audit_type ON sec_audits.audit_type_id = sec_audit_type.audit_type_id
LEFT Join sec_modules ON sec_audit_type.module_id = sec_modules.module_id
