create table operation_type (
    id bigint not null auto_increment,
    description varchar(50) not null,

    primary key (id)
) engine=InnoDB default charset=utf8;

create table transaction (
    id bigint not null auto_increment,
    operation_type int not null,
    amount decimal(10,2) not null,
    created_at datetime not null,

    primary key (id)
) engine=InnoDB default charset=utf8;