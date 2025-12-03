CREATE TABLE positions (
    id INTEGER PRIMARY KEY auto_increment,
    company_id INTEGER NOT NULL,
    position_name TEXT NOT NULL,
    FOREIGN KEY (company_id) REFERENCES companies(id)
);

select * from positions
