DROP TABLE IF EXISTS site;
CREATE TABLE site (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name CHAR NOT NULL,
    address CHAR NOT NULL,
    city CHAR NOT NULL,
    state CHAR NOT NULL,
    zipcode CHAR NOT NULL
);

DROP TABLE IF EXISTS use_type;
CREATE TABLE use_type (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name CHAR NOT NULL
);

DROP TABLE IF EXISTS site_use;
CREATE TABLE site_use (
    id INT AUTO_INCREMENT PRIMARY KEY,
    description CHAR NOT NULL,
    size_sqft INT NOT NULL,
    site_id INT NOT NULL,
    use_type_id INT NOT NULL,
    FOREIGN KEY (site_id) REFERENCES site(id),
    FOREIGN KEY (use_type_id) REFERENCES use_type(id)
);
