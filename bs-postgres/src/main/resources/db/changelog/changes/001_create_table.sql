CREATE TABLE IF NOT EXISTS "event" (
    id bigserial not null,
    client_Id varchar(255),
    account_Id varchar(255),
    ap_Instance_Id varchar(255),
    calling_Number varchar(255),
    called_Number varchar(255),
    calling_Prefix varchar(255),
    called_Prefix varchar(255),
    event_Begin_Date varchar(255),
    event_End_Date varchar(255),
    product_Id varchar(255),
    root_Product_Id varchar(255),
    int_Property1 bigint,
    int_Property2 bigint,
    int_Property3 bigint,
    int_Property4 bigint,
    int_Property5 bigint,
    string_Property1 varchar(255),
    string_Property2 varchar(255),
    string_Property3 varchar(255),
    string_Property4 varchar(255),
    string_Property5 varchar(255),
    boolean_Property1 boolean,
    boolean_Property2 boolean,
    boolean_Property3 boolean,
    boolean_Property4 boolean,
    boolean_Property5 boolean,
    quantity bigint,
    billing_Cycle_Def_Id varchar(255),
    billing_Cycle_Instance_Id varchar(255),
    unit varchar(255),
    billing_Provider_Id varchar(255),
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS client_idx ON event (client_Id);
CREATE INDEX IF NOT EXISTS account_idx ON event (account_Id);

CREATE TABLE IF NOT EXISTS "account" (
    id bigserial not null,
    client_Id varchar(255) not null,
    account_Id varchar(255) not null,
    PRIMARY KEY (id)
    );

CREATE INDEX IF NOT EXISTS account_client_idx ON account (client_Id);
CREATE INDEX IF NOT EXISTS account_account_idx ON account (account_Id);