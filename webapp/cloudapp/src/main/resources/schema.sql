CREATE Table user(
    id varchar(255) primary key,
    first_name  varchar(255) NOT NULL,
    last_name  varchar(255) NOT NULL,
    user_name  varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    account_created varchar(255) NOT NUll,
    account_updated varchar(255),
    is_valid boolean,
    UNIQUE KEY user_name_primary_key(user_name));

create table document(
                         doc_id varchar(255) primary key,
                         user_id  varchar(255) NOT NULL,
                         file_name  varchar(255) NOT NULL,
                         s3_bucket_path varchar(255) NOT NULL,
                         account_created varchar(255) NOT Null),
                          FOREIGN KEY (user_id) references user(id);