
/*==============================================================*/
/* Table: message                                               */
/*==============================================================*/
create table if not exists message
(
   id                   int not null auto_increment,
   message_id           varchar(32) not null,
   prev_message_id      varchar(32) not null,
   label                varchar(255) not null,
   label_id             int not null,
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
/* Index: label_IdIndex                                            */
/*==============================================================*/
create index if not exists label_IdIndex on message
(
   label_id
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
   span_pattern_Id      int,
   first_message_label  varchar(255) not null,
   last_message_label   varchar(255) not null,
   start_time           timestamp not null,
   end_time             timestamp not null,
   primary key (span_id)
);

/*==============================================================*/
/* Index: span_spanPatternIdIndex                                         */
/*==============================================================*/
create index if not exists span_spanPatternIdIndex on span
(
   span_pattern_Id
);

/*==============================================================*/
/* Table: trace_label                                                  */
/*==============================================================*/
create table if not exists trace_label
(
   label_id           int not null auto_increment,
   occurrence_count      int,
   primary key (label_id)
);

/*==============================================================*/
/* Table: span_pattern                                                  */
/*==============================================================*/
create table if not exists span_pattern
(
   pattern_id           int not null auto_increment,
   hash                 varchar(255) not null,
   occurrence_count      int,
   primary key (pattern_id)
);

/*==============================================================*/
/* Index: span_pattern_hashIndex                                         */
/*==============================================================*/
create unique index if not exists span_pattern_hashIndex on span_pattern
(
   hash
);

/*==============================================================*/
/* Table: trace_pattern                                                  */
/*==============================================================*/
create table if not exists trace_pattern
(
   pattern_id           int not null auto_increment,
   hash                 varchar(255) not null,
   occurrence_count      int,
   primary key (pattern_id)
);

/*==============================================================*/
/* Index: trace_pattern_hashIndex                                         */
/*==============================================================*/
create unique index if not exists trace_pattern_hashIndex on trace_pattern
(
   hash
);

/*==============================================================*/
/* Table: graph_pattern                                                  */
/*==============================================================*/
create table if not exists graph_pattern
(
   pattern_id           int not null auto_increment,
   hash                 varchar(255) not null,
   occurrence_count      int,
   primary key (pattern_id)
);

/*==============================================================*/
/* Index: graph_pattern_hashIndex                                         */
/*==============================================================*/
create unique index if not exists graph_pattern_hashIndex on graph_pattern
(
   hash
);

/*==============================================================*/
/* Table: graph                                                  */
/*==============================================================*/
create table if not exists graph
(
   graph_id             int not null auto_increment,
   graph_pattern_id     int,
   first_span_id        int,
   primary key (graph_id)
);

/*==============================================================*/
/* Index: graph_graph_pattern_idIndex                                         */
/*==============================================================*/
create index if not exists graph_graph_pattern_idIndex on graph
(
   graph_pattern_id
);

/*==============================================================*/
/* Index: graph_first_span_idIndex                                         */
/*==============================================================*/
create index if not exists graph_first_span_idIndex on graph
(
   first_span_id
);


/*==============================================================*/
/* Table: trace                                                 */
/*==============================================================*/
create table if not exists trace
(
   trace_id             int not null auto_increment,
   trace_pattern_id     int,
   graph_id             int,
   primary key (trace_id)
);

/*==============================================================*/
/* Index: trace_trace_pattern_idIndex                                         */
/*==============================================================*/
create index if not exists trace_trace_pattern_idIndex on trace
(
   trace_pattern_id
);

/*==============================================================*/
/* Index: trace_trace_pattern_idIndex                                         */
/*==============================================================*/
create index if not exists trace_trace_graph_idIndex on trace
(
   graph_id
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
/* Index: trace_part_traceIdIndex                                        */
/*==============================================================*/
create index if not exists trace_part_traceIdIndex on trace_part
(
   trace_id
);

/*==============================================================*/
/* Index: trace_part_spanIdIndex                                        */
/*==============================================================*/
create index if not exists trace_part_spanIdIndex on trace_part
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

/*==============================================================*/
/* Table: graph_pattern_structure_anomaly                                            */
/*==============================================================*/
create table if not exists graph_pattern_structure_anomaly
(
   anomaly_id                 int not null auto_increment,
   anomaly_graph_pattern_id   int not null,
   related_graph_pattern_id   int not null,
   primary key (anomaly_id)
);

/*==============================================================*/
/* Index: graph_pattern_structure_anomaly_anomaly_graph_pattern_idIndex                                        */
/*==============================================================*/
create unique index if not exists graph_pattern_structure_anomaly_anomaly_graph_pattern_idIndex on graph_pattern_structure_anomaly
(
   anomaly_graph_pattern_id
);

/*==============================================================*/
/* Index: graph_pattern_structure_anomaly_related_graph_pattern_idIndex                                        */
/*==============================================================*/
create unique index if not exists graph_pattern_structure_anomaly_related_graph_pattern_idIndex on graph_pattern_structure_anomaly
(
   related_graph_pattern_id
);


