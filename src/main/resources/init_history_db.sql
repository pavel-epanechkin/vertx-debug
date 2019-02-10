/*==============================================================*/
/* Table: messages                                              */
/*==============================================================*/
create table messages
(
   id                   int not null,
   messageId            varchar(16) not null,
   prevMessageId        varchar(16) not null,
   label                varchar(255) not null,
   received             bool not null,
   sentTime             bigint not null,
   receivedTime         bigint,
   targetAddress        varchar(255),
   replyAddress         varchar(255),
   headers              varchar(1024),
   body                 varchar(1024),
   primary key (id)
);

/*==============================================================*/
/* Index: messageIdIndex                                        */
/*==============================================================*/
create unique index messageIdIndex on messages
(
   messageId
);

/*==============================================================*/
/* Index: prevMessageIdIndex                                    */
/*==============================================================*/
create index prevMessageIdIndex on messages
(
   prevMessageId
);

/*==============================================================*/
/* Index: labelIndex                                            */
/*==============================================================*/
create index labelIndex on messages
(
   label
);

/*==============================================================*/
/* Index: receivedIndex                                         */
/*==============================================================*/
create index receivedIndex on messages
(
   received
);

