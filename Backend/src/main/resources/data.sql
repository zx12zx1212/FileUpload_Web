-- 創建部門表格
CREATE TABLE department (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- 插入部門測試數據
INSERT INTO department (id, name) VALUES 
('D001', '人力資源部'),
('D002', '市場部'),
('D003', '財務部'),
('D004', '技術部'),
('D005', '客戶服務部');

-- 創建員工帳號密碼表格
CREATE TABLE employee_account (
    account VARCHAR(10) PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);

-- 插入員工帳號密碼數據
INSERT INTO employee_account (account, password)
VALUES
('E00001', '12345'),
('E00002', '23456'),
('E00003', '34567'),
('E00004', '45678'),
('E00005', '56789'),
('E00006', '67890'),
('E00007', '78901'),
('E00008', '89012'),
('E00009', '90123'),
('E00010', '01234');

-- 創建員工表格
CREATE TABLE employee (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    department_id VARCHAR(50),
    email VARCHAR(50),
    birthday DATE,
    address VARCHAR(100)
);

-- 插入員工測試數據
INSERT INTO employee (id, name, department_id,email, birthday, address) VALUES
('E00001', '王小明', 'D001','E00001@gmail.com', '1990-01-01', '台北市中正區'),
('E00002', '陳小美', 'D002','E00002@gmail.com', '1991-02-02', '台北市大安區'),
('E00003', '張小華', 'D003','E00003@gmail.com', '1992-03-03', '新北市板橋區'),
('E00004', '林小強', 'D004','E00004@gmail.com', '1993-04-04', '台中市西屯區'),
('E00005', '李小花', 'D005','E00005@gmail.com', '1994-05-05', '高雄市鼓山區'),
('E00006', '吳小明', 'D001','E00006@gmail.com', '1995-06-06', '台北市信義區'),
('E00007', '許小華', 'D002','E00007@gmail.com', '1996-07-07', '新北市新莊區'),
('E00008', '周小強', 'D003','E00008@gmail.com', '1997-08-08', '台中市南屯區'),
('E00009', '劉小美', 'D004','E00009@gmail.com', '1998-09-09', '高雄市三民區'),
('E00010', '黃小花', 'D005','E00010@gmail.com', '1999-10-10', '台南市安南區');

-- 創建檔案路徑表格
CREATE TABLE files (
    id VARCHAR(255)PRIMARY KEY,
    folder VARCHAR(255),
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    create_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP NOT NULL
);

-- 創建檔案詳細資料表格
CREATE TABLE file_detail (
    id SERIAL PRIMARY KEY,
    file_number VARCHAR(255),
    creator_dep VARCHAR(255),
    creator VARCHAR(255),
    sharer_dep VARCHAR(255),
    sharer VARCHAR(255),
    status VARCHAR(255),
    modify_file BOOLEAN,
    modify_permission BOOLEAN,
    delete_permission BOOLEAN,
    create_date TIMESTAMP,
    update_date TIMESTAMP
);

-- 插入檔案路徑測試數據
INSERT INTO files (floder,file_name, file_path,modify_file,modify_permission,delete_permission, create_date) VALUES
('8c21d59c-64e7-03d5-e62d-6eeabb822ea5', 'file1', '/path/to/file1',TRUE, TRUE,TRUE, '2024-01-01'),
('d4c81cfd-35e9-9ce9-4d7d-3f8c3c9b4b4f', 'file2', '/path/to/file2',TRUE, TRUE,TRUE, '2024-01-02'),
('38c4b68f-2a8a-b06a-c38f-09bf0db93c5e', 'file3', '/path/to/file3',TRUE, FALSE,TRUE,  '2024-01-03'),
('bba713b4-93cf-723e-a54b-c71b7e0fa79f', 'file4', '/path/to/file4',TRUE, FALSE,TRUE,  '2024-01-04'),
('068f20a8-db17-5d6d-8a36-22997c29af90', 'file5', '/path/to/file5',TRUE, FALSE,TRUE,  '2024-01-05');

-- 插入檔案詳細資料測試數據
INSERT INTO file_detail (file_id,creator_dep, creator,sharer_dep,sharer,status, create_date,update_date) VALUES
('8c21d59c-64e7-03d5-e62d-6eeabb822ea5','D004','E00009','D004','E00001', 'A',  '2024-01-01','2024-01-01'),
('8c21d59c-64e7-03d5-e62d-6eeabb822ea5','D004','E00009','D002','E00002', 'A',  '2024-01-01','2024-01-02'),
('8c21d59c-64e7-03d5-e62d-6eeabb822ea5','D004','E00009','D003','E00003', 'A',  '2024-01-01','2024-01-03'),
('8c21d59c-64e7-03d5-e62d-6eeabb822ea5','D004','E00009','D004','E00004','A' , '2024-01-01','2024-01-04'),
('8c21d59c-64e7-03d5-e62d-6eeabb822ea5','D004','E00009','D005','E00005', 'A' , '2024-01-01','2024-01-05'),
('8c21d59c-64e7-03d5-e62d-6eeabb822ea5','D004','E00009','D001','E00006', 'A', '2024-01-01', '2024-01-06'),
('8c21d59c-64e7-03d5-e62d-6eeabb822ea5','D004','E00009','D002','E00007','A' , '2024-01-01','2024-01-07'),
('8c21d59c-64e7-03d5-e62d-6eeabb822ea5','D004','E00009','D003','E00008', 'A' , '2024-01-01','2024-01-08'),
('8c21d59c-64e7-03d5-e62d-6eeabb822ea5','D004','E00009','D004','E00009', 'A' , '2024-01-01','2024-01-09'),
('8c21d59c-64e7-03d5-e62d-6eeabb822ea5','D004','E00009','D004','E00009', 'A' , '2024-01-01','2024-01-10');
