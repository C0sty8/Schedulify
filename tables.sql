CREATE TABLE IF NOT EXISTS users_data (
    user_id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(45) NOT NULL UNIQUE,
    user_name VARCHAR(45) NOT NULL,
    password VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS calendars_data (
    calendar_id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cal_name VARCHAR(45) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    edinv VARCHAR(8) NOT NULL,
    norminv VARCHAR(8) NOT NULL,
    location VARCHAR(45) NOT NULL
);
CREATE TABLE IF NOT EXISTS calendar_user (
    user_id INT UNSIGNED NOT NULL,
    calendar_id INT UNSIGNED NOT NULL,
    perm TINYINT UNSIGNED NOT NULL,
    join_date DATETIME NOT NULL,
    PRIMARY KEY (user_id, calendar_id),
    FOREIGN KEY (user_id) REFERENCES users_data(user_id) ON DELETE CASCADE,
    FOREIGN KEY (calendar_id) REFERENCES calendars_data(calendar_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS schedules_events (
    event_id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    calendar_id INT UNSIGNED NOT NULL,
    ename VARCHAR(45) NOT NULL,
    edate DATE NOT NULL,
    start_hour TIME NOT NULL,
    end_hour TIME NOT NULL,
    location VARCHAR(45) NOT NULL,
    FOREIGN KEY (calendar_id) REFERENCES calendars_data(calendar_id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS def_schedules (
    activity_id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    calendar_id INT UNSIGNED NOT NULL,
    aname VARCHAR(45) NOT NULL,
    astart TIME NOT NULL,
    aend TIME NOT NULL,
    aday ENUM('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday') NOT NULL,
    FOREIGN KEY (calendar_id) REFERENCES calendars_data(calendar_id) ON DELETE CASCADE
);