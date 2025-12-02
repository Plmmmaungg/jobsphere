CREATE TABLE recently_applied (
    id INT AUTO_INCREMENT PRIMARY KEY,

    -- Link to the original applicant
    applicant_id INT,

    -- Display fields for admin dashboard
    name VARCHAR(255),
    company VARCHAR(255),
    date VARCHAR(50),
    status VARCHAR(50),

    FOREIGN KEY (applicant_id) REFERENCES applicants(id)
);

select * from recently_applied