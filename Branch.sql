
-- 1. BRANCH TABLE
CREATE TABLE branch (
    branch_id INT AUTO_INCREMENT PRIMARY KEY,
    branch_name VARCHAR(100) NOT NULL,
    company_id INT NOT NULL
    
    );



-- 2. LOCATION TABLE (DEPENDS ON BRANCH)
CREATE TABLE location (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    branch_id INT NOT NULL,
    location_name VARCHAR(100) NOT NULL,
	company_id INT NOT NULL,


    FOREIGN KEY (branch_id) REFERENCES branch(branch_id)
        ON DELETE CASCADE
);

-- 3. POSITION TABLE (DEPENDS ON BRANCH + LOCATION)
CREATE TABLE position (
    position_id INT AUTO_INCREMENT PRIMARY KEY,
    branch_id INT NOT NULL,
    location_id INT NOT NULL,
    position_name VARCHAR(100) NOT NULL,
    company_id INT NOT NULL,


    FOREIGN KEY (branch_id) REFERENCES branch(branch_id)
        ON DELETE CASCADE,

    FOREIGN KEY (location_id) REFERENCES location(location_id)
        ON DELETE CASCADE
);
