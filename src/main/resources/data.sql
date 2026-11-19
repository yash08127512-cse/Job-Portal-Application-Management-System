INSERT INTO jobs (title, company, location, job_type, salary, description, posted_date)
SELECT 'Java Developer Intern', 'CodeCraft Technologies', 'Noida, Uttar Pradesh', 'INTERNSHIP', 15000,
       'Assist the engineering team with Java development, debugging and REST API tasks.', CURRENT_DATE
WHERE NOT EXISTS (SELECT 1 FROM jobs WHERE title='Java Developer Intern' AND company='CodeCraft Technologies');

INSERT INTO jobs (title, company, location, job_type, salary, description, posted_date)
SELECT 'Junior Backend Developer', 'BrightByte Solutions', 'Gurugram, Haryana', 'FULL_TIME', 360000,
       'Work on backend features, SQL queries and application maintenance with senior developers.', CURRENT_DATE
WHERE NOT EXISTS (SELECT 1 FROM jobs WHERE title='Junior Backend Developer' AND company='BrightByte Solutions');

INSERT INTO jobs (title, company, location, job_type, salary, description, posted_date)
SELECT 'Graduate Software Trainee', 'Nexa Systems', 'Faridabad, Haryana', 'FULL_TIME', 300000,
       'Entry-level role for graduates interested in programming, databases and web applications.', CURRENT_DATE
WHERE NOT EXISTS (SELECT 1 FROM jobs WHERE title='Graduate Software Trainee' AND company='Nexa Systems');
