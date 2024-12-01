CREATE TABLE security_rules (
    id INT PRIMARY KEY AUTO_INCREMENT,
    method_name VARCHAR(255) NOT NULL,
    allowed_roles VARCHAR(255) NOT NULL
)