CREATE TABLE applicants (
    id INTEGER PRIMARY KEY auto_increment,

    -- Foreign key to companies table
    company_id INTEGER NOT NULL,

    -- Name fields
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    middle_initial TEXT,

    -- Personal info
    date_of_birth TEXT,
    age INTEGER,
    gender TEXT,
    contact TEXT,
    email TEXT,
    address TEXT,
    nationality TEXT,
    occupation TEXT,
    religion TEXT,

    -- Uploaded documents (paths)
    picture_path TEXT,
    resume_path TEXT,
    philhealth_path TEXT,

    -- Applied position
    position TEXT,
    branch TEXT,
    location TEXT,

    FOREIGN KEY (company_id) REFERENCES companies(id)
);

select * from applicants

