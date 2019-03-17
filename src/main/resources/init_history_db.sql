
/*==============================================================*/
/* Table: message                                               */
/*==============================================================*/
create table if not exists message
(
   id                   int not null auto_increment,
   message_id           varchar(32) not null,
   prev_message_id      varchar(32) not null,
   label                varchar(255) not null,
   received             bool not null,
   sent_time            timestamp not null,
   received_time        timestamp,
   target_address       varchar(255),
   reply_address        varchar(255),
   headers              varchar(1024),
   body                 varchar(1024),
   primary key (id)
);

/*==============================================================*/
/* Index: messageIdIndex                                        */
/*==============================================================*/
create unique index if not exists messageIdIndex on message
(
   message_id
);

/*==============================================================*/
/* Index: prevMessageIdIndex                                    */
/*==============================================================*/
create index if not exists prevMessageIdIndex on message
(
   prev_message_id
);

/*==============================================================*/
/* Index: labelIndex                                            */
/*==============================================================*/
create index if not exists labelIndex on message
(
   label
);

/*==============================================================*/
/* Index: receivedIndex                                         */
/*==============================================================*/
create index if not exists receivedIndex on message
(
   received
);

/*==============================================================*/
/* Table: span                                                  */
/*==============================================================*/
create table if not exists span
(
   span_id              int not null auto_increment,
   first_message_label  varchar(255) not null,
   last_message_label   varchar(255) not null,
   start_time           timestamp not null,
   end_time             timestamp not null,
   primary key (span_id)
);

/*==============================================================*/
/* Table: trace                                                 */
/*==============================================================*/
create table if not exists trace
(
   trace_id             int not null auto_increment,
   trace_pattern_id     int,
   primary key (trace_id)
);

/*==============================================================*/
/* Table: trace_part                                            */
/*==============================================================*/
create table if not exists trace_part
(
   trace_part_id        int not null auto_increment,
   trace_id             int not null,
   span_id              int not null,
   order_number         int not null,
   primary key (trace_part_id)
);

/*==============================================================*/
/* Index: traceIdIndex                                        */
/*==============================================================*/
create index if not exists traceIdIndex on trace_part
(
   trace_id
);

/*==============================================================*/
/* Index: spanIdIndex                                        */
/*==============================================================*/
create index if not exists spanIdIndex on trace_part
(
   span_id
);

/*==============================================================*/
/* Table: trace_unit                                            */
/*==============================================================*/
create table if not exists trace_unit
(
   message_id           int not null,
   trace_unit_id        int not null auto_increment,
   span_id              int not null,
   order_num            int not null,
   primary key (trace_unit_id)
);

/*==============================================================*/
/* Index: trace_unit_messageIdIndex                                        */
/*==============================================================*/
create unique index if not exists trace_unit_messageIdIndex on trace_unit
(
   message_id
);

/*==============================================================*/
/* Index: trace_unit_spanIdIndex                                        */
/*==============================================================*/
create index if not exists trace_unit_spanIdIndex on trace_unit
(
   span_id
);


