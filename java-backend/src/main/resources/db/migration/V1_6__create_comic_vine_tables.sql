-- create the table

CREATE TABLE comic_vine_volumes(
    id bigint generated by default as identity not null,
    cv_volume_id varchar(32) not null,
    content clob,
    primary key (id)
);

CREATE TABLE comic_vine_issues(
	id bigint generated by default as identity not null,
	cv_issue_id varchar(32) not null,
	content clob,
	primary key (id)
);

CREATE TABLE comic_vine_publishers(
    id bigint generated by default as identity not null,
    cv_publisher_id varchar(32) not null,
    content clob,
    primary key (id)
);

CREATE TABLE comic_vine_volume_query_cache(
    id bigint generated by default as identity not null,
    series_name varchar(256) not null,
    index integer not null,
    content clob,
    primary key (id)
);